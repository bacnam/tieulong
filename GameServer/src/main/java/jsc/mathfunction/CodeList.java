package jsc.mathfunction;

public class CodeList
        implements Cloneable {
    int codeListSize = 0;
    CodeListLine[] codeList;
    private int nMax;

    public CodeList(int paramInt) {
        this.nMax = paramInt;

        this.codeList = new CodeListLine[this.nMax];

        for (byte b = 0; b < this.nMax; ) {
            this.codeList[b] = new CodeListLine();
            b++;
        }

    }

    public static int getLabelCode(int paramInt) {
        return -paramInt;
    }

    public Object clone() {
        return copy();
    }

    public CodeList copy() {
        CodeList codeList = new CodeList(this.nMax);
        codeList.codeListSize = this.codeListSize;
        for (byte b = 0; b < this.nMax; b++) {

            (codeList.codeList[b]).f = (this.codeList[b]).f;
            (codeList.codeList[b]).op = (this.codeList[b]).op;
            (codeList.codeList[b]).type = (this.codeList[b]).type;
            (codeList.codeList[b]).left = (this.codeList[b]).left;
            (codeList.codeList[b]).right = (this.codeList[b]).right;
        }
        return codeList;
    }

    public int decrementSize() {
        return --this.codeListSize;
    }

    public boolean equals(CodeList paramCodeList) {
        if (paramCodeList.size() != this.codeListSize) return false;
        for (byte b = 1; b <= this.codeListSize; b++) {

            if ((paramCodeList.codeList[b]).f != (this.codeList[b]).f) return false;
            if ((paramCodeList.codeList[b]).op != (this.codeList[b]).op) return false;
            if ((paramCodeList.codeList[b]).type != (this.codeList[b]).type) return false;
            if ((paramCodeList.codeList[b]).left != (this.codeList[b]).left) return false;
            if ((paramCodeList.codeList[b]).right != (this.codeList[b]).right) return false;
        }
        return true;
    }

    public int getCode(int paramInt) {
        return (this.codeList[paramInt]).op;
    }

    public int getLabelLine(int paramInt) {
        return (paramInt < 0) ? -paramInt : paramInt;
    }

    public int getLeft(int paramInt) {
        return (this.codeList[paramInt]).left;
    }

    public int getLeftCode(int paramInt) {
        return (this.codeList[getLabelLine((this.codeList[paramInt]).left)]).op;
    }

    public int getLeftType(int paramInt) {
        return (this.codeList[getLabelLine((this.codeList[paramInt]).left)]).type;
    }

    public double getLeftValue(int paramInt) {
        return (this.codeList[getLabelLine((this.codeList[paramInt]).left)]).f;
    }

    public int getRight(int paramInt) {
        return (this.codeList[paramInt]).right;
    }

    public int getRightCode(int paramInt) {
        return (this.codeList[getLabelLine((this.codeList[paramInt]).right)]).op;
    }

    public int getRightType(int paramInt) {
        return (this.codeList[getLabelLine((this.codeList[paramInt]).right)]).type;
    }

    public double getRightValue(int paramInt) {
        return (this.codeList[getLabelLine((this.codeList[paramInt]).right)]).f;
    }

    public int getType(int paramInt) {
        return (this.codeList[paramInt]).type;
    }

    public double getValue() {
        return (this.codeList[this.codeListSize]).f;
    }

    public double getValue(int paramInt) {
        return (this.codeList[paramInt]).f;
    }

    public int incrementSize() {
        return ++this.codeListSize;
    }

    public void setCode(int paramInt1, int paramInt2) {
        (this.codeList[paramInt2]).op = paramInt1;
    }

    public void setLeft(int paramInt1, int paramInt2) {
        (this.codeList[paramInt2]).left = paramInt1;
    }

    public void setRight(int paramInt1, int paramInt2) {
        (this.codeList[paramInt2]).right = paramInt1;
    }

    public void setType(int paramInt1, int paramInt2) {
        (this.codeList[paramInt2]).type = paramInt1;
    }

    public void setSize(int paramInt) {
        this.codeListSize = paramInt;
    }

    public void setValue(double paramDouble, int paramInt) {
        (this.codeList[paramInt]).f = paramDouble;
    }

    public int size() {
        return this.codeListSize;
    }

    private class CodeListLine {
        private final CodeList this$0;
        double f;
        int op;
        int type;
        int left;
        int right;
        private CodeListLine(CodeList this$0) {
            CodeList.this = CodeList.this;
        }
    }

}

