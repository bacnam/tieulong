package javolution.util.function;

public class MultiVariable<L, R> {
    private final L left;
    private final R right;

    public MultiVariable(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return this.left;
    }

    public R getRight() {
        return this.right;
    }
}

