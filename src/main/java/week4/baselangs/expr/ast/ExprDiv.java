package week4.baselangs.expr.ast;

public record ExprDiv(ExprNode numerator, ExprNode denominator) implements ExprNode {

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int eval() {
        return numerator.eval() / denominator.eval();
    }
}
