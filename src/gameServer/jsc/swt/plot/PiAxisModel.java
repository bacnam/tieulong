package jsc.swt.plot;

public class PiAxisModel
implements AxisModel
{
String label;
int minNumerator;
int maxNumerator;
int denominator;

public PiAxisModel(String paramString, int paramInt1, int paramInt2, int paramInt3) {
if (paramInt3 < 1)
throw new IllegalArgumentException("Denominator must be > 0."); 
if (paramInt1 >= paramInt2)
throw new IllegalArgumentException("minNumerator must be < maxNumerator."); 
this.label = paramString;
this.minNumerator = paramInt1;
this.maxNumerator = paramInt2;
this.denominator = paramInt3;
}

public PiAxisModel() {
this("", -1, 1, 1);
}
public Object clone() {
return copy();
}

public PiAxisModel copy() {
return new PiAxisModel(this.label, this.minNumerator, this.maxNumerator, this.denominator);
}

public double getLength() {
return (this.maxNumerator - this.minNumerator) * Math.PI / this.denominator;
} public double getMin() { return this.minNumerator * Math.PI / this.denominator; }
public double getMax() { return this.maxNumerator * Math.PI / this.denominator; }
public int getTickCount() { return 1 + this.maxNumerator - this.minNumerator; }
public double getFirstTickValue() { return getMin(); } public double getLastTickValue() {
return getMax();
}

public String getTickLabel(int paramInt) {
int i = this.minNumerator + paramInt;
if (i == 0) return "0"; 
if (this.denominator == 1) return (i == 1) ? "π" : (i + "π");

if (i % this.denominator == 0) {

i /= this.denominator;
if (i == 1) return "π"; 
if (i == -1) return "-π"; 
return i + "π";
} 

if (i == 1) return "π/" + this.denominator; 
if (i == -1) return "-π/" + this.denominator; 
return i + "π" + "/" + this.denominator;
}
public double getTickValue(int paramInt) {
return (this.minNumerator + paramInt) * Math.PI / this.denominator;
}
public String getLabel() { return this.label; } public void setLabel(String paramString) {
this.label = paramString;
}
}

