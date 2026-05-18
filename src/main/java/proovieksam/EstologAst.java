package proovieksam;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import proovieksam.ast.EstologDef;
import proovieksam.ast.EstologNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import static proovieksam.EstologParser.*;
import static proovieksam.ast.EstologNode.*;

public class EstologAst {

    static void main() throws IOException {
        EstologNode ast = makeEstologAst("""
                x := 0;
                y := 1;
                a := (x JA y);
                b := (x VOI y);

                (KUI (x = y) SIIS a MUIDU b)""");
        System.out.println(ast);
        ast.renderPngFile(Paths.get("graphs", "estolog.png"));
    }

    public static EstologNode makeEstologAst(String input) {
        EstologLexer lexer = new EstologLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        EstologParser parser = new EstologParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    // Implementeeri see meetod.
    private static EstologNode parseTreeToAst(ParseTree tree) {
        EstologBaseVisitor<EstologNode> visitor = new EstologBaseVisitor<>() {
            @Override
            public EstologNode visitInit(InitContext ctx) {
                return visit(ctx.prog());
            }

            @Override
            public EstologNode visitProg(ProgContext ctx) {
                var definitsioonid = new ArrayList<EstologDef>();
                for (var definitsioon : ctx.definitsioon()) {
                    definitsioonid.add(visitDefinitsioon(definitsioon));
                }
                var avaldis = visit(ctx.avaldis());
                return EstologNode.prog(avaldis, definitsioonid);
            }

            @Override
            public EstologDef visitDefinitsioon(DefinitsioonContext ctx) {
                return EstologNode.def(ctx.Muutuja().getText(), visit(ctx.avaldis()));
            }

            @Override
            public EstologNode visitLiteraal(LiteraalContext ctx) {
                var väärtus = ctx.Arv().getText().equals("1");
                return EstologNode.lit(väärtus);
            }

            @Override
            public EstologNode visitMuutuja(MuutujaContext ctx) {
                return EstologNode.var(ctx.Muutuja().getText());
            }

            @Override
            public EstologNode visitSulud(SuludContext ctx) {
                return visit(ctx.avaldis());
            }

            @Override
            public EstologNode visitBinOp(BinOpContext ctx) {
                var v = visit(ctx.left);
                String op = ctx.op.getText();
                var p = visit(ctx.right);

                switch (op) {
                    case ("JA"):
                        return EstologNode.ja(v, p);

                    case ("VOI"):
                        return EstologNode.voi(v, p);

                    case ("NING"):
                        return EstologNode.ja(v, p);

                    default:
                        throw new UnsupportedOperationException("midagi valesti läinud:///");
                }
            }

            @Override
            public EstologNode visitBinaryOp(BinaryOpContext ctx) {
                return super.visitBinaryOp(ctx);
            }
            //            @Override
//            public EstologNode visitInit(InitContext ctx) {
//                return visit(ctx.prog());
//            }
//
//            @Override
//            public EstologNode visitProg(ProgContext ctx) {
//                return EstologNode.prog(ctx.avaldis(),);
//            }
//
//            @Override
//            public EstologNode visitAvaldis(AvaldisContext ctx) {
//                String arvText = ctx.Literaal().getText();
//                boolean arvValue = arvText.equals("1"); // grammatika lubab ainult 1 ja 0
//                return EstologNode.lit(arvValue);
//            }
        };
        return visitor.visit(tree);
    }
}
