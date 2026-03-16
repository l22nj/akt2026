package week4.baselangs.expr.ast;

public record ExprAdd(ExprNode left, ExprNode right) implements ExprNode {

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int eval() {
        return left.eval() + right.eval();
    }
}
