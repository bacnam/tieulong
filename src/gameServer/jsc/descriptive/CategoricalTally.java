package jsc.descriptive;

import java.util.Vector;
import jsc.util.CaseInsensitiveVector;
import jsc.util.Sort;

public class CategoricalTally
extends AbstractFrequencyTable
implements Cloneable
{
public static final String[] LETTER_LABELS = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

protected Vector labels = new Vector();

public CategoricalTally(String paramString, String[] paramArrayOfString) {
super(paramString);

this.labels = Sort.getLabels(paramArrayOfString);
this.numberOfBins = this.labels.size();

this.n = 0;
this.freq = new int[this.numberOfBins]; byte b;
for (b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }
for (b = 0; b < paramArrayOfString.length; ) { addValue(paramArrayOfString[b]); b++; }

}

public CategoricalTally(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) {
super(paramString);

this.numberOfBins = paramArrayOfString1.length;
this.labels = (Vector)new CaseInsensitiveVector(this.numberOfBins); byte b;
for (b = 0; b < this.numberOfBins; ) { this.labels.add(paramArrayOfString1[b]); b++; }

this.n = 0;
this.freq = new int[this.numberOfBins];
for (b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }
for (b = 0; b < paramArrayOfString2.length; ) { addValue(paramArrayOfString2[b]); b++; }

}

public CategoricalTally(String paramString, String[] paramArrayOfString, int[] paramArrayOfint) {
super(paramString);

this.numberOfBins = paramArrayOfString.length;
if (this.numberOfBins != paramArrayOfint.length)
throw new IllegalArgumentException("Arrays not same length."); 
this.labels = (Vector)new CaseInsensitiveVector(this.numberOfBins); byte b;
for (b = 0; b < this.numberOfBins; ) { this.labels.add(paramArrayOfString[b]); b++; }

this.n = 0;
this.freq = new int[this.numberOfBins];
for (b = 0; b < this.numberOfBins; ) { this.freq[b] = paramArrayOfint[b]; this.n += paramArrayOfint[b]; b++; }

}

public boolean addValue(String paramString) {
int i = this.labels.indexOf(paramString);
if (i < 0) return false; 
this.n++;
this.freq[i] = this.freq[i] + 1;
return true;
}

public Object clone() {
Object object = null; try {
object = super.clone();
} catch (CloneNotSupportedException cloneNotSupportedException) {
System.out.println("CategoricalTally can't clone");
}  return object;
}

public String getLabel(int paramInt) {
return this.labels.elementAt(paramInt);
}

public String[] getLabels() {
String[] arrayOfString = new String[this.labels.size()];
return (String[])this.labels.toArray((Object[])arrayOfString);
}

public int indexOf(String paramString) {
return this.labels.indexOf(paramString);
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
String[] arrayOfString = { "D", "B", "C", "D", "A", "e", "B", "a", "c", "A", "B", "E", "A", "b", "C", "a", "E", "c", "E" };

CategoricalTally categoricalTally = new CategoricalTally("Test", arrayOfString);

System.out.println("Tally " + categoricalTally.getN() + " values");
for (byte b = 0; b < categoricalTally.getNumberOfBins(); b++)
System.out.println(categoricalTally.getLabel(b) + ", Freq = " + categoricalTally.getFrequency(b) + ", % = " + categoricalTally.getPercentage(b)); 
}
}
}

