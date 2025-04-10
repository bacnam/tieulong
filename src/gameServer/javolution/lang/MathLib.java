package javolution.lang;

@Realtime
public final class MathLib
{
public static int bitLength(int i) {
if (i < 0)
i = -++i; 
return (i < 65536) ? ((i < 256) ? BIT_LENGTH[i] : (BIT_LENGTH[i >>> 8] + 8)) : ((i < 16777216) ? (BIT_LENGTH[i >>> 16] + 16) : (BIT_LENGTH[i >>> 24] + 24));
}

private static final byte[] BIT_LENGTH = new byte[] { 0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 };

private static final long MASK_63 = 9223372036854775807L;

private static final long MASK_32 = 4294967295L;

public static int bitLength(long l) {
int i = (int)(l >> 32L);
if (i > 0) {
return (i < 65536) ? ((i < 256) ? (BIT_LENGTH[i] + 32) : (BIT_LENGTH[i >>> 8] + 40)) : ((i < 16777216) ? (BIT_LENGTH[i >>> 16] + 48) : (BIT_LENGTH[i >>> 24] + 56));
}

if (i < 0)
return bitLength(-++l); 
i = (int)l;
return (i < 0) ? 32 : ((i < 65536) ? ((i < 256) ? BIT_LENGTH[i] : (BIT_LENGTH[i >>> 8] + 8)) : ((i < 16777216) ? (BIT_LENGTH[i >>> 16] + 16) : (BIT_LENGTH[i >>> 24] + 24)));
}

public static int bitCount(long longValue) {
longValue -= longValue >>> 1L & 0x5555555555555555L;
longValue = (longValue & 0x3333333333333333L) + (longValue >>> 2L & 0x3333333333333333L);

longValue = longValue + (longValue >>> 4L) & 0xF0F0F0F0F0F0F0FL;
longValue += longValue >>> 8L;
longValue += longValue >>> 16L;
longValue += longValue >>> 32L;
return (int)longValue & 0x7F;
}

public static int numberOfLeadingZeros(long longValue) {
if (longValue == 0L)
return 64; 
int n = 1;
int x = (int)(longValue >>> 32L);
if (x == 0) { n += 32; x = (int)longValue; }
if (x >>> 16 == 0) { n += 16; x <<= 16; }
if (x >>> 24 == 0) { n += 8; x <<= 8; }
if (x >>> 28 == 0) { n += 4; x <<= 4; }
if (x >>> 30 == 0) { n += 2; x <<= 2; }
n -= x >>> 31;
return n;
}

public static int numberOfTrailingZeros(long longValue) {
int x;
if (longValue == 0L) return 64; 
int n = 63;
int y = (int)longValue; if (y != 0) { n -= 32; x = y; } else { x = (int)(longValue >>> 32L); }
y = x << 16; if (y != 0) { n -= 16; x = y; }
y = x << 8; if (y != 0) { n -= 8; x = y; }
y = x << 4; if (y != 0) { n -= 4; x = y; }
y = x << 2; if (y != 0) { n -= 2; x = y; }
return n - (x << 1 >>> 31);
}

public static int digitLength(int i) {
if (i >= 0) {
return (i >= 100000) ? ((i >= 10000000) ? ((i >= 1000000000) ? 10 : ((i >= 100000000) ? 9 : 8)) : ((i >= 1000000) ? 7 : 6)) : ((i >= 100) ? ((i >= 10000) ? 5 : ((i >= 1000) ? 4 : 3)) : ((i >= 10) ? 2 : 1));
}

if (i == Integer.MIN_VALUE)
return 10; 
return digitLength(-i);
}

public static int digitLength(long l) {
if (l >= 0L) {
return (l <= 2147483647L) ? digitLength((int)l) : ((l >= 100000000000000L) ? ((l >= 10000000000000000L) ? ((l >= 1000000000000000000L) ? 19 : ((l >= 100000000000000000L) ? 18 : 17)) : ((l >= 1000000000000000L) ? 16 : 15)) : ((l >= 100000000000L) ? ((l >= 10000000000000L) ? 14 : ((l >= 1000000000000L) ? 13 : 12)) : ((l >= 10000000000L) ? 11 : 10)));
}

if (l == Long.MIN_VALUE)
return 19; 
return digitLength(-l);
}

public static double toDoublePow2(long m, int n) {
if (m == 0L)
return 0.0D; 
if (m == Long.MIN_VALUE)
return toDoublePow2(-4611686018427387904L, n + 1); 
if (m < 0L)
return -toDoublePow2(-m, n); 
int bitLength = bitLength(m);
int shift = bitLength - 53;
long exp = 1075L + n + shift;
if (exp >= 2047L)
return Double.POSITIVE_INFINITY; 
if (exp <= 0L) {
if (exp <= -54L)
return 0.0D; 
return toDoublePow2(m, n + 54) / 1.8014398509481984E16D;
} 

long bits = (shift > 0) ? ((m >> shift) + (m >> shift - 1 & 0x1L)) : (m << -shift);

if (bits >> 52L != 1L && ++exp >= 2047L)
return Double.POSITIVE_INFINITY; 
bits &= 0xFFFFFFFFFFFFFL;
bits |= exp << 52L;
return Double.longBitsToDouble(bits);
}

public static double toDoublePow10(long m, int n) {
if (m == 0L)
return 0.0D; 
if (m == Long.MIN_VALUE)
return toDoublePow10(-922337203685477580L, n + 1); 
if (m < 0L)
return -toDoublePow10(-m, n); 
if (n >= 0) {
if (n > 308) {
return Double.POSITIVE_INFINITY;
}
long l1 = 0L;
long l2 = 0L;
long x2 = m & 0xFFFFFFFFL;
long x3 = m >>> 32L;
int i = 0;
while (n != 0) {
int j = (n >= POW5_INT.length) ? (POW5_INT.length - 1) : n;
int coef = POW5_INT[j];

if ((int)l1 != 0)
l1 *= coef; 
if ((int)l2 != 0)
l2 *= coef; 
x2 *= coef;
x3 *= coef;

l2 += l1 >>> 32L;
l1 &= 0xFFFFFFFFL;

x2 += l2 >>> 32L;
l2 &= 0xFFFFFFFFL;

x3 += x2 >>> 32L;
x2 &= 0xFFFFFFFFL;

i += j;
n -= j;

long carry = x3 >>> 32L;
if (carry != 0L) {
l1 = l2;
l2 = x2;
x2 = x3 & 0xFFFFFFFFL;
x3 = carry;
i += 32;
} 
} 

int shift = 31 - bitLength(x3);
i -= shift;
long mantissa = (shift < 0) ? (x3 << 31L | x2 >>> 1L) : ((x3 << 32L | x2) << shift | l2 >>> 32 - shift);

return toDoublePow2(mantissa, i);
} 

if (n < -344) {
return 0.0D;
}

long x1 = m;
long x0 = 0L;
int pow2 = 0;

while (true) {
int shift = 63 - bitLength(x1);
x1 <<= shift;
x1 |= x0 >>> 63 - shift;
x0 = x0 << shift & Long.MAX_VALUE;
pow2 -= shift;

if (n == 0) {
break;
}

int i = (-n >= POW5_INT.length) ? (POW5_INT.length - 1) : -n;
int divisor = POW5_INT[i];

long wh = x1 >>> 32L;
long qh = wh / divisor;
long r = wh - qh * divisor;
long wl = r << 32L | x1 & 0xFFFFFFFFL;
long ql = wl / divisor;
r = wl - ql * divisor;
x1 = qh << 32L | ql;

wh = r << 31L | x0 >>> 32L;
qh = wh / divisor;
r = wh - qh * divisor;
wl = r << 32L | x0 & 0xFFFFFFFFL;
ql = wl / divisor;
x0 = qh << 32L | ql;

n += i;
pow2 -= i;
} 
return toDoublePow2(x1, pow2);
}

private static final int[] POW5_INT = new int[] { 1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625, 1220703125 };

private static final double LOG2_DIV_LOG10 = 0.3010299956639812D;
public static final double E = 2.718281828459045D;
public static final double PI = 3.141592653589793D;
public static final double HALF_PI = 1.5707963267948966D;
public static final double TWO_PI = 6.283185307179586D;
public static final double FOUR_PI = 12.566370614359172D;
public static final double PI_SQUARE = 9.869604401089358D;
public static final double LOG2 = 0.6931471805599453D;
public static final double LOG10 = 2.302585092994046D;
public static final double SQRT2 = 1.4142135623730951D;
public static final double NaN = NaND;
public static final double Infinity = InfinityD;

public static long toLongPow2(double d, int n) {
long bits = Double.doubleToLongBits(d);
boolean isNegative = (bits >> 63L != 0L);
int exp = (int)(bits >> 52L) & 0x7FF;
long m = bits & 0xFFFFFFFFFFFFFL;
if (exp == 2047) {
throw new ArithmeticException("Cannot convert to long (Infinity or NaN)");
}
if (exp == 0) {
if (m == 0L)
return 0L; 
return toLongPow2(d * 1.8014398509481984E16D, n - 54);
} 
m |= 0x10000000000000L;
long shift = exp - 1023L - 52L + n;
if (shift <= -64L)
return 0L; 
if (shift >= 11L)
throw new ArithmeticException("Cannot convert to long (overflow)"); 
m = (shift >= 0L) ? (m << (int)shift) : ((m >> (int)-shift) + (m >> (int)-(shift + 1L) & 0x1L));

return isNegative ? -m : m;
}

public static long toLongPow10(double d, int n) {
long bits = Double.doubleToLongBits(d);
boolean isNegative = (bits >> 63L != 0L);
int exp = (int)(bits >> 52L) & 0x7FF;
long m = bits & 0xFFFFFFFFFFFFFL;
if (exp == 2047) {
throw new ArithmeticException("Cannot convert to long (Infinity or NaN)");
}
if (exp == 0) {
if (m == 0L)
return 0L; 
return toLongPow10(d * 1.0E16D, n - 16);
} 
m |= 0x10000000000000L;
int pow2 = exp - 1023 - 52;

if (n >= 0) {

long x0 = 0L;
long x1 = 0L;
long x2 = m & 0xFFFFFFFFL;
long x3 = m >>> 32L;
while (n != 0) {
int i = (n >= POW5_INT.length) ? (POW5_INT.length - 1) : n;
int coef = POW5_INT[i];

if ((int)x0 != 0)
x0 *= coef; 
if ((int)x1 != 0)
x1 *= coef; 
x2 *= coef;
x3 *= coef;

x1 += x0 >>> 32L;
x0 &= 0xFFFFFFFFL;

x2 += x1 >>> 32L;
x1 &= 0xFFFFFFFFL;

x3 += x2 >>> 32L;
x2 &= 0xFFFFFFFFL;

pow2 += i;
n -= i;

long carry = x3 >>> 32L;
if (carry != 0L) {
x0 = x1;
x1 = x2;
x2 = x3 & 0xFFFFFFFFL;
x3 = carry;
pow2 += 32;
} 
} 

int shift = 31 - bitLength(x3);
pow2 -= shift;
m = (shift < 0) ? (x3 << 31L | x2 >>> 1L) : ((x3 << 32L | x2) << shift | x1 >>> 32 - shift);

}
else {

long x1 = m;
long x0 = 0L;

while (true) {
int shift = 63 - bitLength(x1);
x1 <<= shift;
x1 |= x0 >>> 63 - shift;
x0 = x0 << shift & Long.MAX_VALUE;
pow2 -= shift;

if (n == 0) {
break;
}

int i = (-n >= POW5_INT.length) ? (POW5_INT.length - 1) : -n;
int divisor = POW5_INT[i];

long wh = x1 >>> 32L;
long qh = wh / divisor;
long r = wh - qh * divisor;
long wl = r << 32L | x1 & 0xFFFFFFFFL;
long ql = wl / divisor;
r = wl - ql * divisor;
x1 = qh << 32L | ql;

wh = r << 31L | x0 >>> 32L;
qh = wh / divisor;
r = wh - qh * divisor;
wl = r << 32L | x0 & 0xFFFFFFFFL;
ql = wl / divisor;
x0 = qh << 32L | ql;

n += i;
pow2 -= i;
} 
m = x1;
} 
if (pow2 > 0)
throw new ArithmeticException("Overflow"); 
if (pow2 < -63)
return 0L; 
m = (m >> -pow2) + (m >> -(pow2 + 1) & 0x1L);
return isNegative ? -m : m;
}

public static int floorLog2(double d) {
if (d <= 0.0D)
throw new ArithmeticException("Negative number or zero"); 
long bits = Double.doubleToLongBits(d);
int exp = (int)(bits >> 52L) & 0x7FF;
if (exp == 2047)
throw new ArithmeticException("Infinity or NaN"); 
if (exp == 0)
return floorLog2(d * 1.8014398509481984E16D) - 54; 
return exp - 1023;
}

public static int floorLog10(double d) {
int guess = (int)(0.3010299956639812D * floorLog2(d));
double pow10 = toDoublePow10(1L, guess);
if (pow10 <= d && pow10 * 10.0D > d)
return guess; 
if (pow10 > d)
return guess - 1; 
return guess + 1;
}

public static double toRadians(double degrees) {
return degrees * 0.017453292519943295D;
}

public static double toDegrees(double radians) {
return radians * 57.29577951308232D;
}

public static double sqrt(double x) {
return Math.sqrt(x);
}

public static double rem(double x, double y) {
double tmp = x / y;
if (abs(tmp) <= 9.223372036854776E18D) {
return x - round(tmp) * y;
}
return Double.NaN;
}

public static double ceil(double x) {
return Math.ceil(x);
}

public static double floor(double x) {
return Math.floor(x);
}

public static double sin(double radians) {
return Math.sin(radians);
}

public static double cos(double radians) {
return Math.cos(radians);
}

public static double tan(double radians) {
return Math.tan(radians);
}

public static double asin(double x) {
if (x < -1.0D || x > 1.0D)
return Double.NaN; 
if (x == -1.0D)
return -1.5707963267948966D; 
if (x == 1.0D)
return 1.5707963267948966D; 
return atan(x / sqrt(1.0D - x * x));
}

public static double acos(double x) {
return 1.5707963267948966D - asin(x);
}

public static double atan(double x) {
return _atan(x);
}

public static double atan2(double y, double x) {
if (x > 0.0D) return atan(y / x); 
if (y >= 0.0D && x < 0.0D) return atan(y / x) + Math.PI; 
if (y < 0.0D && x < 0.0D) return atan(y / x) - Math.PI; 
if (y > 0.0D && x == 0.0D) return 1.5707963267948966D; 
if (y < 0.0D && x == 0.0D) return -1.5707963267948966D; 
return Double.NaN;
}

public static double sinh(double x) {
return (exp(x) - exp(-x)) * 0.5D;
}

public static double cosh(double x) {
return (exp(x) + exp(-x)) * 0.5D;
}

public static double tanh(double x) {
return (exp(2.0D * x) - 1.0D) / (exp(2.0D * x) + 1.0D);
}

public static double exp(double x) {
return _ieee754_exp(x);
}

public static double log(double x) {
return _ieee754_log(x);
}

public static double log10(double x) {
return log(x) * INV_LOG10;
}

private static double INV_LOG10 = 0.4342944819032518D;

public static double pow(double x, double y) {
if (x < 0.0D && y == (int)y)
return (((int)y & 0x1) == 0) ? pow(-x, y) : -pow(-x, y); 
return exp(y * log(x));
}

public static int round(float f) {
return (int)floor((f + 0.5F));
}

public static long round(double d) {
return (long)floor(d + 0.5D);
}

public static int abs(int i) {
return (i < 0) ? -i : i;
}

public static long abs(long l) {
return (l < 0L) ? -l : l;
}

public static float abs(float f) {
return (f < 0.0F) ? -f : f;
}

public static double abs(double d) {
return (d < 0.0D) ? -d : d;
}

public static int max(int x, int y) {
return (x >= y) ? x : y;
}

public static long max(long x, long y) {
return (x >= y) ? x : y;
}

public static float max(float x, float y) {
return (x >= y) ? x : y;
}

public static double max(double x, double y) {
return (x >= y) ? x : y;
}

public static int min(int x, int y) {
return (x < y) ? x : y;
}

public static long min(long x, long y) {
return (x < y) ? x : y;
}

public static float min(float x, float y) {
return (x < y) ? x : y;
}

public static double min(double x, double y) {
return (x < y) ? x : y;
}

static final double[] atanhi = new double[] { 0.4636476090008061D, 0.7853981633974483D, 0.982793723247329D, 1.5707963267948966D };

static final double[] atanlo = new double[] { 2.2698777452961687E-17D, 3.061616997868383E-17D, 1.3903311031230998E-17D, 6.123233995736766E-17D };

static final double[] aT = new double[] { 0.3333333333333293D, -0.19999999999876483D, 0.14285714272503466D, -0.11111110405462356D, 0.09090887133436507D, -0.0769187620504483D, 0.06661073137387531D, -0.058335701337905735D, 0.049768779946159324D, -0.036531572744216916D, 0.016285820115365782D };

static final double one = 1.0D;
static final double huge = 1.0E300D;
static final double ln2_hi = 0.6931471803691238D;
static final double ln2_lo = 1.9082149292705877E-10D;
static final double two54 = 1.8014398509481984E16D;
static final double Lg1 = 0.6666666666666735D;
static final double Lg2 = 0.3999999999940942D;
static final double Lg3 = 0.2857142874366239D;
static final double Lg4 = 0.22222198432149784D;
static final double Lg5 = 0.1818357216161805D;
static final double Lg6 = 0.15313837699209373D;
static final double Lg7 = 0.14798198605116586D;
static final double zero = 0.0D;

static double _atan(double x) {
int id;
long xBits = Double.doubleToLongBits(x);
int __HIx = (int)(xBits >> 32L);
int __LOx = (int)xBits;

int hx = __HIx;
int ix = hx & Integer.MAX_VALUE;
if (ix >= 1141899264) {
if (ix > 2146435072 || (ix == 2146435072 && __LOx != 0))
return x + x; 
if (hx > 0) {
return atanhi[3] + atanlo[3];
}
return -atanhi[3] - atanlo[3];
} 
if (ix < 1071382528) {
if (ix < 1042284544 && 
1.0E300D + x > 1.0D)
return x; 
id = -1;
} else {
x = abs(x);
if (ix < 1072889856) {
if (ix < 1072037888) {
id = 0;
x = (2.0D * x - 1.0D) / (2.0D + x);
} else {
id = 1;
x = (x - 1.0D) / (x + 1.0D);
} 
} else if (ix < 1073971200) {
id = 2;
x = (x - 1.5D) / (1.0D + 1.5D * x);
} else {
id = 3;
x = -1.0D / x;
} 
} 

double z = x * x;
double w = z * z;

double s1 = z * (aT[0] + w * (aT[2] + w * (aT[4] + w * (aT[6] + w * (aT[8] + w * aT[10])))));

double s2 = w * (aT[1] + w * (aT[3] + w * (aT[5] + w * (aT[7] + w * aT[9]))));
if (id < 0) {
return x - x * (s1 + s2);
}
z = atanhi[id] - x * (s1 + s2) - atanlo[id] - x;
return (hx < 0) ? -z : z;
}

static double _ieee754_log(double x) {
long xBits = Double.doubleToLongBits(x);
int hx = (int)(xBits >> 32L);
int lx = (int)xBits;

int k = 0;
if (hx < 1048576) {
if ((hx & Integer.MAX_VALUE | lx) == 0)
return Double.NEGATIVE_INFINITY; 
if (hx < 0)
return (x - x) / 0.0D; 
k -= 54;
x *= 1.8014398509481984E16D;
xBits = Double.doubleToLongBits(x);
hx = (int)(xBits >> 32L);
} 
if (hx >= 2146435072)
return x + x; 
k += (hx >> 20) - 1023;
hx &= 0xFFFFF;
int i = hx + 614244 & 0x100000;
xBits = Double.doubleToLongBits(x);
int HIx = hx | i ^ 0x3FF00000;
xBits = (HIx & 0xFFFFFFFFL) << 32L | xBits & 0xFFFFFFFFL;
x = Double.longBitsToDouble(xBits);
k += i >> 20;
double f = x - 1.0D;
if ((0xFFFFF & 2 + hx) < 3) {
if (f == 0.0D) {
if (k == 0) {
return 0.0D;
}
double d = k;
return d * 0.6931471803691238D + d * 1.9082149292705877E-10D;
} 
double d1 = f * f * (0.5D - 0.3333333333333333D * f);
if (k == 0) {
return f - d1;
}
double d2 = k;
return d2 * 0.6931471803691238D - d1 - d2 * 1.9082149292705877E-10D - f;
} 

double s = f / (2.0D + f);
double dk = k;
double z = s * s;
i = hx - 398458;
double w = z * z;
int j = 440401 - hx;
double t1 = w * (0.3999999999940942D + w * (0.22222198432149784D + w * 0.15313837699209373D));
double t2 = z * (0.6666666666666735D + w * (0.2857142874366239D + w * (0.1818357216161805D + w * 0.14798198605116586D)));
i |= j;
double R = t2 + t1;
if (i > 0) {
double hfsq = 0.5D * f * f;
if (k == 0) {
return f - hfsq - s * (hfsq + R);
}
return dk * 0.6931471803691238D - hfsq - s * (hfsq + R) + dk * 1.9082149292705877E-10D - f;
} 
if (k == 0) {
return f - s * (f - R);
}
return dk * 0.6931471803691238D - s * (f - R) - dk * 1.9082149292705877E-10D - f;
}

static final double[] halF = new double[] { 0.5D, -0.5D };
static final double twom1000 = 9.332636185032189E-302D;
static final double o_threshold = 709.782712893384D;
static final double u_threshold = -745.1332191019411D;
static final double[] ln2HI = new double[] { 0.6931471803691238D, -0.6931471803691238D };

static final double[] ln2LO = new double[] { 1.9082149292705877E-10D, -1.9082149292705877E-10D };

static final double invln2 = 1.4426950408889634D;

static final double P1 = 0.16666666666666602D;
static final double P2 = -0.0027777777777015593D;
static final double P3 = 6.613756321437934E-5D;
static final double P4 = -1.6533902205465252E-6D;
static final double P5 = 4.1381367970572385E-8D;

static double _ieee754_exp(double x) {
double hi = 0.0D, lo = 0.0D;
int k = 0;

long xBits = Double.doubleToLongBits(x);
int __HIx = (int)(xBits >> 32L);
int __LOx = (int)xBits;

int hx = __HIx;
int xsb = hx >> 31 & 0x1;
hx &= Integer.MAX_VALUE;

if (hx >= 1082535490) {
if (hx >= 2146435072) {
if ((hx & 0xFFFFF | __LOx) != 0) {
return x + x;
}
return (xsb == 0) ? x : 0.0D;
}  if (x > 709.782712893384D)
return Double.POSITIVE_INFINITY; 
if (x < -745.1332191019411D) {
return 0.0D;
}
} 

if (hx > 1071001154) {
if (hx < 1072734898) {
hi = x - ln2HI[xsb];
lo = ln2LO[xsb];
k = 1 - xsb - xsb;
} else {
k = (int)(1.4426950408889634D * x + halF[xsb]);
double d = k;
hi = x - d * ln2HI[0];
lo = d * ln2LO[0];
} 
x = hi - lo;
} else if (hx < 1043333120) {
if (1.0E300D + x > 1.0D)
return 1.0D + x; 
} else {
k = 0;
} 

double t = x * x;
double c = x - t * (0.16666666666666602D + t * (-0.0027777777777015593D + t * (6.613756321437934E-5D + t * (-1.6533902205465252E-6D + t * 4.1381367970572385E-8D))));
if (k == 0) {
return 1.0D - x * c / (c - 2.0D) - x;
}
double y = 1.0D - lo - x * c / (2.0D - c) - hi;
long yBits = Double.doubleToLongBits(y);
int __HIy = (int)(yBits >> 32L);
if (k >= -1021) {
__HIy += k << 20;
yBits = (__HIy & 0xFFFFFFFFL) << 32L | yBits & 0xFFFFFFFFL;
y = Double.longBitsToDouble(yBits);
return y;
} 
__HIy += k + 1000 << 20;
yBits = (__HIy & 0xFFFFFFFFL) << 32L | yBits & 0xFFFFFFFFL;
y = Double.longBitsToDouble(yBits);
return y * 9.332636185032189E-302D;
}
}

