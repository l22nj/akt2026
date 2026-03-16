package week4.baselangs.expr.ast;

public record ExprNeg(ExprNode expr) implements ExprNode {

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int eval() {
        return 0 - expr.eval();
    }
}
