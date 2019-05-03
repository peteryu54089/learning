package model;

import java.util.Collections;
import java.util.List;

public class ModelList<T> {
    private List<T> list;
    private int total;

    public ModelList(List<T> list, int total) {
        this.list = Collections.unmodifiableList(list);
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public int getTotal() {
        return total;
    }
}
