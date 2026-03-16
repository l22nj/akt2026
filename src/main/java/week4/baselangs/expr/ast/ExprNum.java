package week4.baselangs.expr.ast;

// See klass on kirje (record), mistõttu konstruktor, get meetodid (ilma get eesliiteta), equals, hashCode ja toString genereeritakse automaatselt kompilaatori poolt.
// See on võimalik alates Java 16-st.
public record ExprNum(int value) implements ExprNode {

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int eval() {
        return value();
    }
}
