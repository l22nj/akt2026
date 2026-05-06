package week7;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;
import week7.AktkParser.*;
import week7.ast.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class AktkAst {

    // Ise testimiseks soovitame kasutada selline meetod: inputs/sample.aktk failis sisend muuta.
    // Kui testid sinna kopeerida, siis äkki võtab IDE escape sümbolid ära ja on selgem,
    // milline see kood tegelikult välja näeb.
    static void main() throws IOException {
        String program = Files.readString(Paths.get("inputs", "sample.aktk"));
        AstNode ast = createAst(program);
        System.out.println(ast);
    }

    // Automaattestide jaoks vajalik meetod.
    public static Statement createAst(String program) {
        AktkLexer lexer = new AktkLexer(CharStreams.fromString(program));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        AktkParser parser = new AktkParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.program();
        //System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    // Põhimeetod, mida tuleks implementeerida:
    private static Statement parseTreeToAst(ParseTree tree) {
        var visitor = new StatementVisitor().visit(tree);
        return visitor;
    }

    static class StatementVisitor extends AktkBaseVisitor<Statement> {
        private final ExpressionVisitor exprVisitor = new ExpressionVisitor();

        @Override
        public Statement visitProgram(ProgramContext ctx) {
            return visit(ctx.statements());
        }

        @Override
        public Statement visitStatements(StatementsContext ctx) {
            var statements = new ArrayList<Statement>();
            for (var statement : ctx.statement()) {
                statements.add(visit(statement));
            }
            return new Block(statements);
        }

        @Override
        public Statement visitBlock(BlockContext ctx) {
            return visit(ctx.statements());
        }

        @Override
        public Statement visitExprStatement(ExprStatementContext ctx) {
            return new ExpressionStatement(exprVisitor.visit(ctx.expr()));
        }
    }

    static class ExpressionVisitor extends AktkBaseVisitor<Expression> {
        @Override
        public Expression visitExpr(ExprContext ctx) {
            return super.visitExpr(ctx);
        }

        @Override
        public Expression visitIntLiteral(IntLiteralContext ctx) {
            var value = Integer.parseInt(ctx.IntLiteral().getText());
            return new IntegerLiteral(value);
        }

        @Override
        public Expression visitStrLiteral(StrLiteralContext ctx) {
            var value = ctx.StrLiteral().getText();
            return new StringLiteral(value.substring(1, value.length() - 1));
        }

        @Override
        public Expression visitVariable(VariableContext ctx) {
            var name = ctx.Variable().getText();
            return new Variable(name);
        }
    }
}
