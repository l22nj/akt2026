package toylangs.imp;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaStack;
import toylangs.imp.ast.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static toylangs.imp.ast.ImpNode.*;

public class ImpCompiler {
    private final CMaProgramWriter pw = new CMaProgramWriter();

    public static CMaProgram compile(ImpProg prog) {
        ImpCompiler impCompiler = new ImpCompiler();
        impCompiler.compileNode(prog);
        return impCompiler.pw.toProgram();
    }

    private final List<Character> vars = new ArrayList<>();

    private void compileNode(ImpNode node) {
        switch (node) {
            // Iga case lükkab CMa masina magasinile väärtuse
            case ImpNum impNum -> {
                pw.visit(LOADC, impNum.value());
            }
            case ImpVar impVar -> {
                char name = impVar.name();
                var index = vars.indexOf(name);
                if (index == -1) throw new NoSuchElementException();
                pw.visit(LOADA, index);
            }
            case ImpAdd impAdd -> {
                compileNode(impAdd.left());
                compileNode(impAdd.right());
                pw.visit(ADD);
            }
            case ImpDiv impDiv -> {
                compileNode(impDiv.numerator());
                compileNode(impDiv.denominator());
                pw.visit(DIV);
            }
            case ImpAssign impAssign -> {
                compileNode(impAssign.expr());
                if (vars.contains(impAssign.name())) {
                    var index = vars.indexOf(impAssign.name());
                    pw.visit(STOREA, index);
                    pw.visit(POP);
                } else {
                    vars.add(impAssign.name());
                    // Siia rohkem midagi ei tule - miks?
                }

            }
            case ImpNeg impNeg -> {
                compileNode(impNeg.expr());
                pw.visit(NEG);
            }
            case ImpProg impProg -> {
                for (var assign : impProg.assigns()) {
                    compileNode(assign);
                }
                compileNode(impProg.expr());
            }
        }
    }

    static void main() throws IOException {
        ImpProg prog = prog(
                var('x'),

                assign('x', num(5)),
                assign('y', add(var('x'), num(1))),
                assign('x', add(var('y'), num(1)))
        );

        // väärtustame otse
        System.out.printf("eval: %d%n", ImpEvaluator.eval(prog));

        // kompileeri avaldist arvutav CMa programm
        CMaProgram program = compile(prog);

        // kirjuta programm faili, mida saab Vam-iga vaadata
        CMaStack initialStack = new CMaStack();
        program.toFile(Paths.get("cmas", "imp.cma"), initialStack);

        // interpreteeri CMa programm
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);
        System.out.printf("compiled: %d%n", finalStack.peek());
        System.out.printf("finalStack: %s%n", finalStack);
    }
}
