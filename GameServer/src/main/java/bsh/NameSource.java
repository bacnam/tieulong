package bsh;

public interface NameSource {
    String[] getAllNames();

    void addNameSourceListener(Listener paramListener);

    public static interface Listener {
        void nameSourceChanged(NameSource param1NameSource);
    }
}

