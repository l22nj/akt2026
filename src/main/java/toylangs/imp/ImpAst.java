package toylangs.imp;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.imp.ast.ImpAssign;
import toylangs.imp.ast.ImpNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import static toylangs.imp.ast.ImpNode.*;

public class ImpAst {

    public static ImpNode makeImpAst(String input) {
        ImpLexer lexer = new ImpLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        ImpParser parser = new ImpParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    private static ImpNode parseTreeToAst(ParseTree tree) {
        var visitor = new ImpBaseVisitor<ImpNode>() {
            @Override
            public ImpNode visitInit(ImpParser.InitContext ctx) {
                return super.visitInit(ctx);
            }

            @Override
            public ImpNode visitProg(ImpParser.ProgContext ctx) {
                var assigns = new ArrayList<ImpAssign>();
                for (var assign : ctx.assign()) {
                    assigns.add(visitAssign(assign));
                }
                return ImpNode.prog(visit(ctx.plusExpr()), assigns);
            }

            @Override
            public ImpAssign visitAssign(ImpParser.AssignContext ctx) {
                return ImpNode.assign(ctx.Variable().getText().charAt(0),
                            visit(ctx.plusExpr()));
            }

            @Override
            public ImpNode visitSimplePlus(ImpParser.SimplePlusContext ctx) {
                return super.visitSimplePlus(ctx);
            }

            @Override
            public ImpNode visitBinaryPlus(ImpParser.BinaryPlusContext ctx) {
                return super.visitBinaryPlus(ctx);
            }

            @Override
            public ImpNode visitBinaryDiv(ImpParser.BinaryDivContext ctx) {
                return super.visitBinaryDiv(ctx);
            }

            @Override
            public ImpNode visitSimpleDiv(ImpParser.SimpleDivContext ctx) {
                return super.visitSimpleDiv(ctx);
            }

            @Override
            public ImpNode visitUnaryMinus(ImpParser.UnaryMinusContext ctx) {
                return super.visitUnaryMinus(ctx);
            }

            @Override
            public ImpNode visitSimpleMinus(ImpParser.SimpleMinusContext ctx) {
                return super.visitSimpleMinus(ctx);
            }

            @Override
            public ImpNode visitParen(ImpParser.ParenContext ctx) {
                return super.visitParen(ctx);
            }

            @Override
            public ImpNode visitLiteral(ImpParser.LiteralContext ctx) {
                return super.visitLiteral(ctx);
            }

            @Override
            public ImpNode visitVariable(ImpParser.VariableContext ctx) {
                return super.visitVariable(ctx);
            }
        };
        return null;
    }

    static void main() throws IOException {
        ImpNode ast = makeImpAst("x = 5, x + 1");
        ast.renderPngFile(Paths.get("graphs", "imp.png"));
        System.out.println(ast);
    }
}
