package week1.soojendus;

/**
 * Klass on mõeldud kahendpuu tipu esitamiseks. Tüübiparameeter T määrab, millist
 * tüüpi väärtust saab tipus hoida. Väärtuse küsimiseks on meetod getValue.
 */
public class Node<T> {
    private final Node<T> left;
    private final Node<T> right;
    private final T value;

    public Node(T value, Node<T> leftChild, Node<T> rightChild) {
        this.value = value;
        this.left = leftChild;
        this.right = rightChild;
    }

    public T getValue() {
        return value;
    }

    /**
     * Tagastab vasaku alampuu. Selle puudumisel tagastab null-i.
     */
    public Node<T> getLeftChild() {
        return left;
    }

    public Node<T> getRightChild() {
        return right;
    }

    /**
     * Tagastab true tippude korral, millel pole ühtegi alampuud.
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }


    /**
     * Meetod peab tagastama true või false vastavalt sellele, kas antud (alam)puus
     * leidub näidatud väärtus. Võrdlemine toimub meetodi equals alusel.
     */
    public boolean contains(T value) {
        return this.value.equals(value) ||
                left != null && left.contains(value) ||
                right != null && right.contains(value);
    }


    @Override
    public String toString() {
        return "(%s, %s, %s)".formatted(
                value,
                left == null ? "_" : left,
                right == null ? "_" : right);
    }
}
