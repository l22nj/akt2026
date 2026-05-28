package proovieksam;

import cma.*;
import proovieksam.ast.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static proovieksam.ast.EstologNode.*;

public class EstologCompiler {
    private final CMaProgramWriter pw = new CMaProgramWriter();

    private final List<String> vars = new ArrayList<>();

    public static CMaProgram compile(EstologProg prog) {
        EstologCompiler estologCompiler = new EstologCompiler();
        estologCompiler.compileNode(prog);
        return estologCompiler.pw.toProgram();
    }

    private void compileNode(EstologNode node) {
        switch (node) {
            case EstologLiteraal estologLiteraal -> {
                boolean value = estologLiteraal.value();
                pw.visit(LOADC, CMaUtils.bool2int(value));
            }
            case EstologMuutuja estologMuutuja -> {
                String muutujaNimi = estologMuutuja.nimi();
                int index = vars.indexOf(muutujaNimi);
                if (index == -1) {
                    throw new NoSuchElementException("defineeriimata muutuja :DDD");
                }
                pw.visit(LOADA, index);
            }
            case EstologDef estologDef -> {
                String muutujaNimi = estologDef.nimi();
                int index = vars.indexOf(muutujaNimi);
                if (index == -1) {
                    vars.addLast(muutujaNimi);
                    compileNode(estologDef.avaldis());
                } else {
                    compileNode(estologDef.avaldis());
                    pw.visit(STOREA, index);
                    pw.visit(POP);
                }
            }
            case EstologBinOp estologBinOp -> {
                switch (estologBinOp) {
                    case EstologJa estologJa -> {
                        compileNode(estologJa.left());
                        compileNode(estologJa.right());
                        pw.visit(AND);
                    }
                    case EstologVoi estologVoi -> {
                        compileNode(estologVoi.left());
                        compileNode(estologVoi.right());
                        pw.visit(OR);
                    }
                    case EstologVordus estologVordus -> {
                        compileNode(estologVordus.left());
                        compileNode(estologVordus.right());
                        pw.visit(EQ);
                    }
                }
            }
            case EstologKui estologKui -> {
                // annaks teha ka labelitega
                // (kui keel, kus ka muud väärtused peale tõeväärtuste, siis oleks vaja)
                // CMaLabel elseLabel = new CMaLabel();
                // CMaLabel endLabel = new CMaLabel();
                // ja neid saab pw.visit()-iga lisada
                compileNode(estologKui.kui());
                compileNode(estologKui.siis());
                pw.visit(AND);

                compileNode(estologKui.kui());
                pw.visit(LOADC, 0);
                pw.visit(EQ);
                if (estologKui.muidu() == null) {
                    pw.visit(LOADC,1);
                } else {
                    compileNode(estologKui.muidu());
                }
                pw.visit(AND);

                pw.visit(OR);
            }
            case EstologProg estologProg -> {
                for (var def : estologProg.defs()) {
                    compileNode(def);
                }
                compileNode(estologProg.avaldis());
            }
        }
    }

    static void main() throws IOException {
        EstologProg prog = prog(
                kui(vordus(var("x"), var("y")), var("a"), var("b")),

                def("x", lit(false)),
                def("y", lit(true)),
                def("a", ja(var("x"), var("y"))),
                def("b", voi(var("x"), var("y")))
        );

        // väärtustame otse
        System.out.printf("eval: %b%n", EstologEvaluator.eval(prog));

        // kompileeri avaldist arvutav CMa programm
        CMaProgram program = compile(prog);

        // kirjuta programm faili, mida saab Vam-iga vaadata
        CMaStack initialStack = new CMaStack();
        program.toFile(Paths.get("cmas", "estolog.cma"), initialStack);

        // interpreteeri CMa programm
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);
        System.out.printf("compiled: %d%n", finalStack.peek());
        System.out.printf("finalStack: %s%n", finalStack);
    }
}
