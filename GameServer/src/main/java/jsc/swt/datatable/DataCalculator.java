package jsc.swt.datatable;

import java.awt.*;

public abstract class DataCalculator {
    protected DataTable dataTable;
    protected Component parentComponent;

    public DataCalculator(Component paramComponent, DataTable paramDataTable) {
        this.dataTable = paramDataTable;
        this.parentComponent = paramComponent;
    }

    public abstract void show();

    public abstract void updateNames();
}

