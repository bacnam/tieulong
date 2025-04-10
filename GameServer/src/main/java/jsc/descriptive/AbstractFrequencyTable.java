package jsc.descriptive;

public abstract class AbstractFrequencyTable
{
int n;
int numberOfBins;
int[] freq;
private String name;

public AbstractFrequencyTable(String paramString) {
this.n = 0; this.numberOfBins = 0; this.name = paramString;
}

public void clearData() {
this.n = 0;
for (byte b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }

}

public int getCumulativeFrequency(int paramInt) {
int i = 0;
for (byte b = 0; b <= paramInt; ) { i += this.freq[b]; b++; }
return i;
}

public double getCumulativePercentage(int paramInt) {
return 100.0D * getCumulativeProportion(paramInt);
}

public double getCumulativeProportion(int paramInt) {
return (this.n > 0) ? (getCumulativeFrequency(paramInt) / this.n) : 0.0D;
}

public int[] getFrequencies() {
return this.freq;
}

public int getFrequency(int paramInt) {
return this.freq[paramInt];
}

public int getMaximumFreq() {
int i = 0;
for (byte b = 0; b < this.numberOfBins; b++) {
if (this.freq[b] > i) i = this.freq[b]; 
}  return i;
}

public double getMaximumProportion() {
return (this.n > 0) ? (getMaximumFreq() / this.n) : 0.0D;
}

public int getNumberOfBins() {
return this.numberOfBins;
}

public int getN() {
return this.n;
}

public String getName() {
return this.name;
}

public double getPercentage(int paramInt) {
return 100.0D * getProportion(paramInt);
}

public double getProportion(int paramInt) {
return (this.n > 0) ? (this.freq[paramInt] / this.n) : 0.0D;
}

public void setName(String paramString) {
this.name = paramString;
}

public String toString() {
StringBuffer stringBuffer = new StringBuffer("AbstractFrequencyTable: " + this.name + " from " + this.n + " observations, " + this.numberOfBins + " bins with frequencies:");

for (byte b = 0; b < this.numberOfBins; ) { stringBuffer.append(" "); stringBuffer.append(this.freq[b]); b++; }
return stringBuffer.toString();
}
}

