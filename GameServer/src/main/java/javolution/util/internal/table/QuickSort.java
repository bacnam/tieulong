package javolution.util.internal.table;

import javolution.util.service.TableService;

import java.util.Comparator;

public class QuickSort<E> {
    private final Comparator<? super E> comparator;
    private final TableService<E> table;

    public QuickSort(TableService<E> table, Comparator<? super E> comparator) {
        this.table = table;
        this.comparator = comparator;
    }

    public void sort() {
        int size = this.table.size();
        if (size > 0) quicksort(0, this.table.size() - 1);
    }

    public void sort(int first, int last) {
        if (first < last) {
            int pivIndex = partition(first, last);
            sort(first, pivIndex - 1);
            sort(pivIndex + 1, last);
        }
    }

    void quicksort(int first, int last) {
        int pivIndex = 0;
        if (first < last) {
            pivIndex = partition(first, last);
            quicksort(first, pivIndex - 1);
            quicksort(pivIndex + 1, last);
        }
    }

    private int partition(int f, int l) {
        E piv = (E) this.table.get(f);
        int up = f;
        int down = l;
        while (true) {
            if (this.comparator.compare((E) this.table.get(up), piv) <= 0 && up < l) {
                up++;
                continue;
            }
            while (this.comparator.compare((E) this.table.get(down), piv) > 0 && down > f) {
                down--;
            }
            if (up < down) {
                E temp = (E) this.table.get(up);
                this.table.set(up, this.table.get(down));
                this.table.set(down, temp);
            }
            if (down <= up) {
                this.table.set(f, this.table.get(down));
                this.table.set(down, piv);
                return down;
            }
        }
    }
}

