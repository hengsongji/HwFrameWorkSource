package com.android.org.bouncycastle.math.raw;

import com.android.org.bouncycastle.util.Pack;
import java.math.BigInteger;

public abstract class Nat192 {
    private static final long M = 4294967295L;

    public static int add(int[] x, int[] y, int[] z) {
        long c = 0 + ((((long) x[0]) & M) + (((long) y[0]) & M));
        z[0] = (int) c;
        c = (c >>> 32) + ((((long) x[1]) & M) + (((long) y[1]) & M));
        z[1] = (int) c;
        c = (c >>> 32) + ((((long) x[2]) & M) + (((long) y[2]) & M));
        z[2] = (int) c;
        c = (c >>> 32) + ((((long) x[3]) & M) + (((long) y[3]) & M));
        z[3] = (int) c;
        c = (c >>> 32) + ((((long) x[4]) & M) + (((long) y[4]) & M));
        z[4] = (int) c;
        c = (c >>> 32) + ((((long) x[5]) & M) + (((long) y[5]) & M));
        z[5] = (int) c;
        return (int) (c >>> 32);
    }

    public static int addBothTo(int[] x, int[] y, int[] z) {
        long c = 0 + (((((long) x[0]) & M) + (((long) y[0]) & M)) + (((long) z[0]) & M));
        z[0] = (int) c;
        c = (c >>> 32) + (((((long) x[1]) & M) + (((long) y[1]) & M)) + (((long) z[1]) & M));
        z[1] = (int) c;
        c = (c >>> 32) + (((((long) x[2]) & M) + (((long) y[2]) & M)) + (((long) z[2]) & M));
        z[2] = (int) c;
        c = (c >>> 32) + (((((long) x[3]) & M) + (((long) y[3]) & M)) + (((long) z[3]) & M));
        z[3] = (int) c;
        c = (c >>> 32) + (((((long) x[4]) & M) + (((long) y[4]) & M)) + (((long) z[4]) & M));
        z[4] = (int) c;
        c = (c >>> 32) + (((((long) x[5]) & M) + (((long) y[5]) & M)) + (((long) z[5]) & M));
        z[5] = (int) c;
        return (int) (c >>> 32);
    }

    public static int addTo(int[] x, int[] z) {
        long c = 0 + ((((long) x[0]) & M) + (((long) z[0]) & M));
        z[0] = (int) c;
        c = (c >>> 32) + ((((long) x[1]) & M) + (((long) z[1]) & M));
        z[1] = (int) c;
        c = (c >>> 32) + ((((long) x[2]) & M) + (((long) z[2]) & M));
        z[2] = (int) c;
        c = (c >>> 32) + ((((long) x[3]) & M) + (((long) z[3]) & M));
        z[3] = (int) c;
        c = (c >>> 32) + ((((long) x[4]) & M) + (((long) z[4]) & M));
        z[4] = (int) c;
        c = (c >>> 32) + ((((long) x[5]) & M) + (((long) z[5]) & M));
        z[5] = (int) c;
        return (int) (c >>> 32);
    }

    public static int addTo(int[] x, int xOff, int[] z, int zOff, int cIn) {
        long c = (((long) cIn) & M) + ((((long) x[xOff + 0]) & M) + (((long) z[zOff + 0]) & M));
        z[zOff + 0] = (int) c;
        c = (c >>> 32) + ((((long) x[xOff + 1]) & M) + (((long) z[zOff + 1]) & M));
        z[zOff + 1] = (int) c;
        c = (c >>> 32) + ((((long) x[xOff + 2]) & M) + (((long) z[zOff + 2]) & M));
        z[zOff + 2] = (int) c;
        c = (c >>> 32) + ((((long) x[xOff + 3]) & M) + (((long) z[zOff + 3]) & M));
        z[zOff + 3] = (int) c;
        c = (c >>> 32) + ((((long) x[xOff + 4]) & M) + (((long) z[zOff + 4]) & M));
        z[zOff + 4] = (int) c;
        c = (c >>> 32) + ((((long) x[xOff + 5]) & M) + (M & ((long) z[zOff + 5])));
        z[zOff + 5] = (int) c;
        return (int) (c >>> 32);
    }

    public static int addToEachOther(int[] u, int uOff, int[] v, int vOff) {
        long c = 0 + ((((long) u[uOff + 0]) & M) + (((long) v[vOff + 0]) & M));
        u[uOff + 0] = (int) c;
        v[vOff + 0] = (int) c;
        c = (c >>> 32) + ((((long) u[uOff + 1]) & M) + (((long) v[vOff + 1]) & M));
        u[uOff + 1] = (int) c;
        v[vOff + 1] = (int) c;
        c = (c >>> 32) + ((((long) u[uOff + 2]) & M) + (((long) v[vOff + 2]) & M));
        u[uOff + 2] = (int) c;
        v[vOff + 2] = (int) c;
        c = (c >>> 32) + ((((long) u[uOff + 3]) & M) + (((long) v[vOff + 3]) & M));
        u[uOff + 3] = (int) c;
        v[vOff + 3] = (int) c;
        c = (c >>> 32) + ((((long) u[uOff + 4]) & M) + (((long) v[vOff + 4]) & M));
        u[uOff + 4] = (int) c;
        v[vOff + 4] = (int) c;
        c = (c >>> 32) + ((((long) u[uOff + 5]) & M) + (((long) v[vOff + 5]) & M));
        u[uOff + 5] = (int) c;
        v[vOff + 5] = (int) c;
        return (int) (c >>> 32);
    }

    public static void copy(int[] x, int[] z) {
        z[0] = x[0];
        z[1] = x[1];
        z[2] = x[2];
        z[3] = x[3];
        z[4] = x[4];
        z[5] = x[5];
    }

    public static void copy64(long[] x, long[] z) {
        z[0] = x[0];
        z[1] = x[1];
        z[2] = x[2];
    }

    public static int[] create() {
        return new int[6];
    }

    public static long[] create64() {
        return new long[3];
    }

    public static int[] createExt() {
        return new int[12];
    }

    public static long[] createExt64() {
        return new long[6];
    }

    public static boolean diff(int[] x, int xOff, int[] y, int yOff, int[] z, int zOff) {
        boolean pos = gte(x, xOff, y, yOff);
        if (pos) {
            sub(x, xOff, y, yOff, z, zOff);
        } else {
            sub(y, yOff, x, xOff, z, zOff);
        }
        return pos;
    }

    public static boolean eq(int[] x, int[] y) {
        for (int i = 5; i >= 0; i--) {
            if (x[i] != y[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean eq64(long[] x, long[] y) {
        for (int i = 2; i >= 0; i--) {
            if (x[i] != y[i]) {
                return false;
            }
        }
        return true;
    }

    public static int[] fromBigInteger(BigInteger x) {
        if (x.signum() < 0 || x.bitLength() > 192) {
            throw new IllegalArgumentException();
        }
        int[] z = create();
        int i = 0;
        while (x.signum() != 0) {
            int i2 = i + 1;
            z[i] = x.intValue();
            x = x.shiftRight(32);
            i = i2;
        }
        return z;
    }

    public static long[] fromBigInteger64(BigInteger x) {
        if (x.signum() < 0 || x.bitLength() > 192) {
            throw new IllegalArgumentException();
        }
        long[] z = create64();
        int i = 0;
        while (x.signum() != 0) {
            int i2 = i + 1;
            z[i] = x.longValue();
            x = x.shiftRight(64);
            i = i2;
        }
        return z;
    }

    public static int getBit(int[] x, int bit) {
        if (bit == 0) {
            return x[0] & 1;
        }
        int w = bit >> 5;
        if (w < 0 || w >= 6) {
            return 0;
        }
        return (x[w] >>> (bit & 31)) & 1;
    }

    public static boolean gte(int[] x, int[] y) {
        for (int i = 5; i >= 0; i--) {
            int x_i = x[i] ^ Integer.MIN_VALUE;
            int y_i = Integer.MIN_VALUE ^ y[i];
            if (x_i < y_i) {
                return false;
            }
            if (x_i > y_i) {
                return true;
            }
        }
        return true;
    }

    public static boolean gte(int[] x, int xOff, int[] y, int yOff) {
        for (int i = 5; i >= 0; i--) {
            int x_i = x[xOff + i] ^ Integer.MIN_VALUE;
            int y_i = Integer.MIN_VALUE ^ y[yOff + i];
            if (x_i < y_i) {
                return false;
            }
            if (x_i > y_i) {
                return true;
            }
        }
        return true;
    }

    public static boolean isOne(int[] x) {
        if (x[0] != 1) {
            return false;
        }
        for (int i = 1; i < 6; i++) {
            if (x[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isOne64(long[] x) {
        if (x[0] != 1) {
            return false;
        }
        for (int i = 1; i < 3; i++) {
            if (x[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isZero(int[] x) {
        for (int i = 0; i < 6; i++) {
            if (x[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isZero64(long[] x) {
        for (int i = 0; i < 3; i++) {
            if (x[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static void mul(int[] x, int[] y, int[] zz) {
        long y_2 = ((long) y[2]) & M;
        long y_3 = ((long) y[3]) & M;
        long y_1 = ((long) y[1]) & M;
        long y_4 = ((long) y[4]) & M;
        long y_5 = ((long) y[5]) & M;
        long y_0 = ((long) y[0]) & M;
        long x_0 = ((long) x[0]) & M;
        long c = 0 + (x_0 * y_0);
        zz[0] = (int) c;
        long c2 = (c >>> 32) + (x_0 * y_1);
        int i = 1;
        zz[1] = (int) c2;
        c2 = (c2 >>> 32) + (x_0 * y_2);
        zz[2] = (int) c2;
        c2 = (c2 >>> 32) + (x_0 * y_3);
        zz[3] = (int) c2;
        c2 = (c2 >>> 32) + (x_0 * y_4);
        zz[4] = (int) c2;
        c2 = (c2 >>> 32) + (x_0 * y_5);
        zz[5] = (int) c2;
        int i2 = 6;
        zz[6] = (int) (c2 >>> 32);
        while (true) {
            int i3 = i;
            if (i3 < i2) {
                c = ((long) x[i3]) & M;
                long c3 = 0 + ((c * y_0) + (((long) zz[i3 + 0]) & M));
                zz[i3 + 0] = (int) c3;
                long y_52 = y_5;
                c3 = (c3 >>> 32) + ((c * y_1) + (((long) zz[i3 + 1]) & M));
                zz[i3 + 1] = (int) c3;
                c3 = (c3 >>> 32) + ((c * y_2) + (((long) zz[i3 + 2]) & M));
                zz[i3 + 2] = (int) c3;
                c3 = (c3 >>> 32) + ((c * y_3) + (((long) zz[i3 + 3]) & M));
                zz[i3 + 3] = (int) c3;
                c3 = (c3 >>> 32) + ((c * y_4) + (((long) zz[i3 + 4]) & M));
                zz[i3 + 4] = (int) c3;
                c3 = (c3 >>> 32) + ((c * y_52) + (((long) zz[i3 + 5]) & M));
                zz[i3 + 5] = (int) c3;
                zz[i3 + 6] = (int) (c3 >>> 32);
                i = i3 + 1;
                y_5 = y_52;
                i2 = 6;
            } else {
                return;
            }
        }
    }

    public static void mul(int[] x, int xOff, int[] y, int yOff, int[] zz, int zzOff) {
        long y_0 = ((long) y[yOff + 0]) & M;
        long y_1 = ((long) y[yOff + 1]) & M;
        long y_2 = ((long) y[yOff + 2]) & M;
        long y_3 = ((long) y[yOff + 3]) & M;
        long y_4 = ((long) y[yOff + 4]) & M;
        long y_5 = ((long) y[yOff + 5]) & M;
        long x_0 = ((long) x[xOff + 0]) & M;
        long c = 0 + (x_0 * y_0);
        long y_02 = y_0;
        zz[zzOff + 0] = (int) c;
        long c2 = (c >>> 32) + (x_0 * y_1);
        zz[zzOff + 1] = (int) c2;
        c2 = (c2 >>> 32) + (x_0 * y_2);
        zz[zzOff + 2] = (int) c2;
        c2 = (c2 >>> 32) + (x_0 * y_3);
        zz[zzOff + 3] = (int) c2;
        c2 = (c2 >>> 32) + (x_0 * y_4);
        zz[zzOff + 4] = (int) c2;
        c2 = (c2 >>> 32) + (x_0 * y_5);
        zz[zzOff + 5] = (int) c2;
        zz[zzOff + 6] = (int) (c2 >>> 32);
        int i = 1;
        int zzOff2 = zzOff;
        while (i < 6) {
            zzOff2++;
            c = ((long) x[xOff + i]) & M;
            int i2 = i;
            y_0 = 0 + ((c * y_02) + (((long) zz[zzOff2 + 0]) & M));
            zz[zzOff2 + 0] = (int) y_0;
            long y_12 = y_1;
            y_0 = (y_0 >>> 32) + ((c * y_1) + (((long) zz[zzOff2 + 1]) & M));
            zz[zzOff2 + 1] = (int) y_0;
            y_0 = (y_0 >>> 32) + ((c * y_2) + (((long) zz[zzOff2 + 2]) & M));
            zz[zzOff2 + 2] = (int) y_0;
            y_0 = (y_0 >>> 32) + ((c * y_3) + (((long) zz[zzOff2 + 3]) & M));
            zz[zzOff2 + 3] = (int) y_0;
            y_0 = (y_0 >>> 32) + ((c * y_4) + (((long) zz[zzOff2 + 4]) & M));
            zz[zzOff2 + 4] = (int) y_0;
            y_0 = (y_0 >>> 32) + ((c * y_5) + (((long) zz[zzOff2 + 5]) & M));
            zz[zzOff2 + 5] = (int) y_0;
            zz[zzOff2 + 6] = (int) (y_0 >>> 32);
            i = i2 + 1;
            y_1 = y_12;
        }
    }

    public static int mulAddTo(int[] x, int[] y, int[] zz) {
        long y_1 = ((long) y[1]) & M;
        long y_2 = ((long) y[2]) & M;
        long y_3 = ((long) y[3]) & M;
        long y_4 = ((long) y[4]) & M;
        long y_0 = ((long) y[0]) & M;
        long y_5 = ((long) y[5]) & M;
        long zc = 0;
        int i = 0;
        while (true) {
            int i2 = i;
            long y_42;
            long y_12;
            long y_22;
            if (i2 < 6) {
                long y_52 = y_5;
                y_5 = ((long) x[i2]) & M;
                y_42 = y_4;
                y_4 = 0 + ((y_5 * y_0) + (((long) zz[i2 + 0]) & M));
                zz[i2 + 0] = (int) y_4;
                y_12 = y_1;
                y_4 = (y_4 >>> 32) + ((y_5 * y_1) + (((long) zz[i2 + 1]) & M));
                zz[i2 + 1] = (int) y_4;
                y_22 = y_2;
                y_1 = (y_4 >>> 32) + ((y_5 * y_2) + (((long) zz[i2 + 2]) & M));
                zz[i2 + 2] = (int) y_1;
                y_1 = (y_1 >>> 32) + ((y_5 * y_3) + (((long) zz[i2 + 3]) & M));
                zz[i2 + 3] = (int) y_1;
                y_1 = (y_1 >>> 32) + ((y_5 * y_42) + (((long) zz[i2 + 4]) & M));
                zz[i2 + 4] = (int) y_1;
                y_1 = (y_1 >>> 32) + ((y_5 * y_52) + (((long) zz[i2 + 5]) & M));
                zz[i2 + 5] = (int) y_1;
                y_1 = (y_1 >>> 32) + (zc + (((long) zz[i2 + 6]) & M));
                zz[i2 + 6] = (int) y_1;
                zc = y_1 >>> 32;
                i = i2 + 1;
                y_5 = y_52;
                y_4 = y_42;
                y_1 = y_12;
                y_2 = y_22;
            } else {
                y_12 = y_1;
                y_22 = y_2;
                y_42 = y_4;
                return (int) zc;
            }
        }
    }

    public static int mulAddTo(int[] x, int xOff, int[] y, int yOff, int[] zz, int zzOff) {
        long y_0;
        long y_1;
        long y_2;
        long y_02 = ((long) y[yOff + 0]) & M;
        long y_12 = ((long) y[yOff + 1]) & M;
        long y_22 = ((long) y[yOff + 2]) & M;
        long y_3 = ((long) y[yOff + 3]) & M;
        long y_4 = ((long) y[yOff + 4]) & M;
        long y_5 = ((long) y[yOff + 5]) & M;
        int i = 0;
        long zc = 0;
        int zzOff2 = zzOff;
        while (i < 6) {
            int i2 = i;
            long y_52 = y_5;
            y_5 = ((long) x[xOff + i]) & M;
            y_0 = y_02;
            long c = 0 + ((y_5 * y_02) + (((long) zz[zzOff2 + 0]) & M));
            zz[zzOff2 + 0] = (int) c;
            long c2 = (c >>> 32) + ((y_5 * y_12) + (((long) zz[zzOff2 + 1]) & M));
            zz[zzOff2 + 1] = (int) c2;
            y_1 = y_12;
            c2 = (c2 >>> 32) + ((y_5 * y_22) + (((long) zz[zzOff2 + 2]) & M));
            zz[zzOff2 + 2] = (int) c2;
            y_2 = y_22;
            c2 = (c2 >>> 32) + ((y_5 * y_3) + (((long) zz[zzOff2 + 3]) & M));
            zz[zzOff2 + 3] = (int) c2;
            c2 = (c2 >>> 32) + ((y_5 * y_4) + (((long) zz[zzOff2 + 4]) & M));
            zz[zzOff2 + 4] = (int) c2;
            c2 = (c2 >>> 32) + ((y_5 * y_52) + (((long) zz[zzOff2 + 5]) & M));
            zz[zzOff2 + 5] = (int) c2;
            c2 = (c2 >>> 32) + (zc + (((long) zz[zzOff2 + 6]) & M));
            zz[zzOff2 + 6] = (int) c2;
            zc = c2 >>> 32;
            zzOff2++;
            i = i2 + 1;
            y_5 = y_52;
            y_02 = y_0;
            y_12 = y_1;
            y_22 = y_2;
        }
        y_0 = y_02;
        y_1 = y_12;
        y_2 = y_22;
        return (int) zc;
    }

    public static long mul33Add(int w, int[] x, int xOff, int[] y, int yOff, int[] z, int zOff) {
        long wVal = ((long) w) & M;
        long x0 = ((long) x[xOff + 0]) & M;
        long c = 0 + ((wVal * x0) + (((long) y[yOff + 0]) & M));
        z[zOff + 0] = (int) c;
        long x1 = ((long) x[xOff + 1]) & M;
        c = (c >>> 32) + (((wVal * x1) + x0) + (((long) y[yOff + 1]) & M));
        z[zOff + 1] = (int) c;
        x0 = ((long) x[xOff + 2]) & M;
        c = (c >>> 32) + (((wVal * x0) + x1) + (((long) y[yOff + 2]) & M));
        z[zOff + 2] = (int) c;
        x1 = ((long) x[xOff + 3]) & M;
        c = (c >>> 32) + (((wVal * x1) + x0) + (((long) y[yOff + 3]) & M));
        z[zOff + 3] = (int) c;
        x0 = ((long) x[xOff + 4]) & M;
        c = (c >>> 32) + (((wVal * x0) + x1) + (((long) y[yOff + 4]) & M));
        z[zOff + 4] = (int) c;
        x1 = ((long) x[xOff + 5]) & M;
        c = (c >>> 32) + (((wVal * x1) + x0) + (((long) y[yOff + 5]) & M));
        z[zOff + 5] = (int) c;
        return (c >>> 32) + x1;
    }

    public static int mulWordAddExt(int x, int[] yy, int yyOff, int[] zz, int zzOff) {
        long xVal = ((long) x) & M;
        long c = 0 + (((((long) yy[yyOff + 0]) & M) * xVal) + (((long) zz[zzOff + 0]) & M));
        zz[zzOff + 0] = (int) c;
        c = (c >>> 32) + (((((long) yy[yyOff + 1]) & M) * xVal) + (((long) zz[zzOff + 1]) & M));
        zz[zzOff + 1] = (int) c;
        c = (c >>> 32) + (((((long) yy[yyOff + 2]) & M) * xVal) + (((long) zz[zzOff + 2]) & M));
        zz[zzOff + 2] = (int) c;
        c = (c >>> 32) + (((((long) yy[yyOff + 3]) & M) * xVal) + (((long) zz[zzOff + 3]) & M));
        zz[zzOff + 3] = (int) c;
        c = (c >>> 32) + (((((long) yy[yyOff + 4]) & M) * xVal) + (((long) zz[zzOff + 4]) & M));
        zz[zzOff + 4] = (int) c;
        c = (c >>> 32) + (((((long) yy[yyOff + 5]) & M) * xVal) + (M & ((long) zz[zzOff + 5])));
        zz[zzOff + 5] = (int) c;
        return (int) (c >>> 32);
    }

    public static int mul33DWordAdd(int x, long y, int[] z, int zOff) {
        int[] iArr = z;
        int i = zOff;
        long xVal = ((long) x) & M;
        long y00 = y & M;
        long y002 = y00;
        long c = 0 + ((xVal * y00) + (((long) iArr[i + 0]) & M));
        iArr[i + 0] = (int) c;
        long y01 = y >>> 32;
        c = (c >>> 32) + (((xVal * y01) + y002) + (((long) iArr[i + 1]) & M));
        iArr[i + 1] = (int) c;
        c = (c >>> 32) + ((((long) iArr[i + 2]) & M) + y01);
        iArr[i + 2] = (int) c;
        c = (c >>> 32) + (((long) iArr[i + 3]) & M);
        iArr[i + 3] = (int) c;
        return (c >>> 32) == 0 ? 0 : Nat.incAt(6, iArr, i, 4);
    }

    public static int mul33WordAdd(int x, int y, int[] z, int zOff) {
        long yVal = ((long) y) & M;
        long c = 0 + ((yVal * (((long) x) & M)) + (((long) z[zOff + 0]) & M));
        z[zOff + 0] = (int) c;
        c = (c >>> 32) + ((((long) z[zOff + 1]) & M) + yVal);
        z[zOff + 1] = (int) c;
        c = (c >>> 32) + (M & ((long) z[zOff + 2]));
        z[zOff + 2] = (int) c;
        return (c >>> 32) == 0 ? 0 : Nat.incAt(6, z, zOff, 3);
    }

    public static int mulWordDwordAdd(int x, long y, int[] z, int zOff) {
        long xVal = ((long) x) & M;
        long c = 0 + (((y & M) * xVal) + (((long) z[zOff + 0]) & M));
        z[zOff + 0] = (int) c;
        c = (c >>> 32) + (((y >>> 32) * xVal) + (((long) z[zOff + 1]) & M));
        z[zOff + 1] = (int) c;
        c = (c >>> 32) + (M & ((long) z[zOff + 2]));
        z[zOff + 2] = (int) c;
        return (c >>> 32) == 0 ? 0 : Nat.incAt(6, z, zOff, 3);
    }

    public static int mulWord(int x, int[] y, int[] z, int zOff) {
        long c = 0;
        long xVal = ((long) x) & M;
        int i = 0;
        do {
            c += (((long) y[i]) & M) * xVal;
            z[zOff + i] = (int) c;
            c >>>= 32;
            i++;
        } while (i < 6);
        return (int) c;
    }

    public static void square(int[] x, int[] zz) {
        long x_0 = (long) x[0];
        long j = M;
        x_0 &= M;
        int c = 0;
        int i = 5;
        int j2 = 12;
        while (true) {
            int i2 = i - 1;
            long xVal = ((long) x[i]) & j;
            long p = xVal * xVal;
            j2--;
            zz[j2] = ((int) (p >>> 33)) | (c << 31);
            j2--;
            long x_02 = x_0;
            zz[j2] = (int) (p >>> 1);
            c = (int) p;
            if (i2 <= 0) {
                x_0 = x_02 * x_02;
                xVal = (((long) (c << 31)) & M) | (x_0 >>> 33);
                zz[0] = (int) x_0;
                long x_1 = ((long) x[1]) & M;
                p = ((long) zz[2]) & M;
                xVal += x_1 * x_02;
                i = (int) xVal;
                zz[1] = (i << 1) | (((int) (x_0 >>> 32)) & 1);
                j2 = ((long) x[2]) & -1;
                long x_12 = x_1;
                long zz_3 = ((long) zz[3]) & M;
                long zz_4 = ((long) zz[4]) & M;
                p = (p + (xVal >>> 32)) + (j2 * x_02);
                int w = (int) p;
                zz[2] = (w << 1) | (i >>> 31);
                long zz_32 = zz_3 + ((p >>> 32) + (j2 * x_12));
                zz_4 += zz_32 >>> 32;
                xVal = ((long) x[3]) & M;
                long zz_5 = (((long) zz[5]) & M) + (zz_4 >>> 32);
                zz_4 &= M;
                p = (((long) zz[6]) & M) + (zz_5 >>> 32);
                zz_5 &= M;
                c = (zz_32 & M) + (xVal * x_02);
                w = (int) c;
                zz[3] = (w << 1) | (w >>> 31);
                int c2 = w >>> 31;
                zz_4 += (c >>> 32) + (xVal * x_12);
                zz_5 += (zz_4 >>> 32) + (xVal * j2);
                p += zz_5 >>> 32;
                zz_5 &= M;
                long zz_33 = c;
                x_1 = ((long) x[4]) & M;
                long x_2 = j2;
                long zz_7 = (((long) zz[7]) & M) + (p >>> 32);
                p &= M;
                long x_3 = xVal;
                long zz_8 = (((long) zz[8]) & M) + (zz_7 >>> 32);
                zz_7 &= M;
                zz_4 = (zz_4 & M) + (x_1 * x_02);
                int w2 = (int) zz_4;
                zz[4] = (w2 << 1) | c2;
                int c3 = w2 >>> 31;
                zz_5 += (zz_4 >>> 32) + (x_1 * x_12);
                p += (zz_5 >>> 32) + (x_1 * x_2);
                zz_7 += (p >>> 32) + (x_1 * x_3);
                p &= M;
                zz_8 += zz_7 >>> 32;
                zz_7 &= M;
                zz_4 = ((long) x[5]) & M;
                long zz_9 = (((long) zz[9]) & M) + (zz_8 >>> 32);
                i2 = zz_8 & -1;
                long x_4 = x_1;
                x_1 = (((long) zz[10]) & M) + (zz_9 >>> 32);
                zz_9 &= M;
                zz_5 = (zz_5 & M) + (zz_4 * x_02);
                c2 = (int) zz_5;
                zz[5] = (c2 << 1) | c3;
                c3 = c2 >>> 31;
                p += (zz_5 >>> 32) + (zz_4 * x_12);
                zz_7 += (p >>> 32) + (zz_4 * x_2);
                i2 += (zz_7 >>> 32) + (zz_4 * x_3);
                zz_9 += (i2 >>> 32) + (zz_4 * x_4);
                x_1 += zz_9 >>> 32;
                c2 = (int) p;
                zz[6] = (c2 << 1) | c3;
                c3 = c2 >>> 31;
                c2 = (int) zz_7;
                zz[7] = (c2 << 1) | c3;
                c3 = c2 >>> 31;
                c2 = (int) i2;
                zz[8] = (c2 << 1) | c3;
                c3 = c2 >>> 31;
                c2 = (int) zz_9;
                zz[9] = (c2 << 1) | c3;
                c3 = c2 >>> 31;
                c2 = (int) x_1;
                zz[10] = (c2 << 1) | c3;
                zz[11] = ((zz[11] + ((int) (x_1 >>> 32))) << 1) | (c2 >>> 31);
                return;
            }
            i = i2;
            j = M;
            x_0 = x_02;
        }
    }

    public static void square(int[] x, int xOff, int[] zz, int zzOff) {
        long x_0 = (long) x[xOff + 0];
        long j = M;
        x_0 &= M;
        int c = 0;
        int i = 5;
        int j2 = 12;
        while (true) {
            int i2 = i - 1;
            long xVal = ((long) x[xOff + i]) & j;
            long p = xVal * xVal;
            j2--;
            zz[zzOff + j2] = ((int) (p >>> 33)) | (c << 31);
            j2--;
            zz[zzOff + j2] = (int) (p >>> 1);
            c = (int) p;
            if (i2 <= 0) {
                xVal = x_0 * x_0;
                p = (((long) (c << 31)) & M) | (xVal >>> 33);
                zz[zzOff + 0] = (int) xVal;
                int c2 = 1 & ((int) (xVal >>> 32));
                c = ((long) x[xOff + 1]) & -1;
                xVal = ((long) zz[zzOff + 2]) & M;
                p += c * x_0;
                j2 = (int) p;
                zz[zzOff + 1] = (j2 << 1) | c2;
                xVal += p >>> 32;
                j = ((long) x[xOff + 2]) & M;
                long zz_3 = ((long) zz[zzOff + 3]) & M;
                p = ((long) zz[zzOff + 4]) & M;
                xVal += j * x_0;
                long x_02 = x_0;
                int w = (int) xVal;
                zz[zzOff + 2] = (w << 1) | (j2 >>> 31);
                int c3 = w >>> 31;
                zz_3 += (xVal >>> 32) + (j * c);
                p += zz_3 >>> 32;
                xVal = ((long) x[xOff + 3]) & M;
                long zz_5 = (((long) zz[zzOff + 5]) & M) + (p >>> 32);
                p &= M;
                long x_2 = j;
                j = (((long) zz[zzOff + 6]) & M) + (zz_5 >>> 32);
                zz_5 &= M;
                zz_3 = (zz_3 & M) + (xVal * x_02);
                w = (int) zz_3;
                zz[zzOff + 3] = (w << 1) | c3;
                c3 = w >>> 31;
                p += (zz_3 >>> 32) + (xVal * c);
                zz_5 += (p >>> 32) + (xVal * x_2);
                j += zz_5 >>> 32;
                zz_5 &= M;
                zz_3 = ((long) x[xOff + 4]) & M;
                long x_3 = xVal;
                xVal = (((long) zz[zzOff + 7]) & M) + (j >>> 32);
                long zz_6 = j & M;
                j = (((long) zz[zzOff + 8]) & M) + (xVal >>> 32);
                xVal &= M;
                p = (p & M) + (zz_3 * x_02);
                w = (int) p;
                zz[zzOff + 4] = (w << 1) | c3;
                c3 = w >>> 31;
                zz_5 += (p >>> 32) + (zz_3 * c);
                long zz_62 = zz_6 + ((zz_5 >>> 32) + (zz_3 * x_2));
                xVal += (zz_62 >>> 32) + (zz_3 * x_3);
                long zz_63 = zz_62 & M;
                j += xVal >>> 32;
                xVal &= M;
                p = ((long) x[xOff + 5]) & M;
                long x_4 = zz_3;
                zz_3 = (((long) zz[zzOff + 9]) & M) + (j >>> 32);
                long zz_8 = j & M;
                j = (((long) zz[zzOff + 10]) & M) + (zz_3 >>> 32);
                j2 = zz_3 & -1;
                zz_5 = (zz_5 & M) + (p * x_02);
                w = (int) zz_5;
                zz[zzOff + 5] = (w << 1) | c3;
                c3 = w >>> 31;
                zz_5 = zz_63 + ((zz_5 >>> 32) + (p * c));
                xVal += (zz_5 >>> 32) + (p * x_2);
                long x_1 = c;
                c = zz_8 + ((xVal >>> 32) + (p * x_3));
                j2 += (c >>> 32) + (p * x_4);
                j += j2 >>> 32;
                w = (int) zz_5;
                zz[zzOff + 6] = (w << 1) | c3;
                c3 = w >>> 31;
                w = (int) xVal;
                zz[zzOff + 7] = (w << 1) | c3;
                c3 = w >>> 31;
                w = (int) c;
                zz[zzOff + 8] = (w << 1) | c3;
                c3 = w >>> 31;
                w = (int) j2;
                zz[zzOff + 9] = (w << 1) | c3;
                c3 = w >>> 31;
                x_0 = (int) j;
                zz[zzOff + 10] = (x_0 << 1) | c3;
                zz[zzOff + 11] = ((zz[zzOff + 11] + ((int) (j >>> 32))) << 1) | (x_0 >>> 31);
                return;
            }
            i = i2;
            j = M;
        }
    }

    public static int sub(int[] x, int[] y, int[] z) {
        long c = 0 + ((((long) x[0]) & M) - (((long) y[0]) & M));
        z[0] = (int) c;
        c = (c >> 32) + ((((long) x[1]) & M) - (((long) y[1]) & M));
        z[1] = (int) c;
        c = (c >> 32) + ((((long) x[2]) & M) - (((long) y[2]) & M));
        z[2] = (int) c;
        c = (c >> 32) + ((((long) x[3]) & M) - (((long) y[3]) & M));
        z[3] = (int) c;
        c = (c >> 32) + ((((long) x[4]) & M) - (((long) y[4]) & M));
        z[4] = (int) c;
        c = (c >> 32) + ((((long) x[5]) & M) - (((long) y[5]) & M));
        z[5] = (int) c;
        return (int) (c >> 32);
    }

    public static int sub(int[] x, int xOff, int[] y, int yOff, int[] z, int zOff) {
        long c = 0 + ((((long) x[xOff + 0]) & M) - (((long) y[yOff + 0]) & M));
        z[zOff + 0] = (int) c;
        c = (c >> 32) + ((((long) x[xOff + 1]) & M) - (((long) y[yOff + 1]) & M));
        z[zOff + 1] = (int) c;
        c = (c >> 32) + ((((long) x[xOff + 2]) & M) - (((long) y[yOff + 2]) & M));
        z[zOff + 2] = (int) c;
        c = (c >> 32) + ((((long) x[xOff + 3]) & M) - (((long) y[yOff + 3]) & M));
        z[zOff + 3] = (int) c;
        c = (c >> 32) + ((((long) x[xOff + 4]) & M) - (((long) y[yOff + 4]) & M));
        z[zOff + 4] = (int) c;
        c = (c >> 32) + ((((long) x[xOff + 5]) & M) - (((long) y[yOff + 5]) & M));
        z[zOff + 5] = (int) c;
        return (int) (c >> 32);
    }

    public static int subBothFrom(int[] x, int[] y, int[] z) {
        long c = 0 + (((((long) z[0]) & M) - (((long) x[0]) & M)) - (((long) y[0]) & M));
        z[0] = (int) c;
        c = (c >> 32) + (((((long) z[1]) & M) - (((long) x[1]) & M)) - (((long) y[1]) & M));
        z[1] = (int) c;
        c = (c >> 32) + (((((long) z[2]) & M) - (((long) x[2]) & M)) - (((long) y[2]) & M));
        z[2] = (int) c;
        c = (c >> 32) + (((((long) z[3]) & M) - (((long) x[3]) & M)) - (((long) y[3]) & M));
        z[3] = (int) c;
        c = (c >> 32) + (((((long) z[4]) & M) - (((long) x[4]) & M)) - (((long) y[4]) & M));
        z[4] = (int) c;
        c = (c >> 32) + (((((long) z[5]) & M) - (((long) x[5]) & M)) - (((long) y[5]) & M));
        z[5] = (int) c;
        return (int) (c >> 32);
    }

    public static int subFrom(int[] x, int[] z) {
        long c = 0 + ((((long) z[0]) & M) - (((long) x[0]) & M));
        z[0] = (int) c;
        c = (c >> 32) + ((((long) z[1]) & M) - (((long) x[1]) & M));
        z[1] = (int) c;
        c = (c >> 32) + ((((long) z[2]) & M) - (((long) x[2]) & M));
        z[2] = (int) c;
        c = (c >> 32) + ((((long) z[3]) & M) - (((long) x[3]) & M));
        z[3] = (int) c;
        c = (c >> 32) + ((((long) z[4]) & M) - (((long) x[4]) & M));
        z[4] = (int) c;
        c = (c >> 32) + ((((long) z[5]) & M) - (((long) x[5]) & M));
        z[5] = (int) c;
        return (int) (c >> 32);
    }

    public static int subFrom(int[] x, int xOff, int[] z, int zOff) {
        long c = 0 + ((((long) z[zOff + 0]) & M) - (((long) x[xOff + 0]) & M));
        z[zOff + 0] = (int) c;
        c = (c >> 32) + ((((long) z[zOff + 1]) & M) - (((long) x[xOff + 1]) & M));
        z[zOff + 1] = (int) c;
        c = (c >> 32) + ((((long) z[zOff + 2]) & M) - (((long) x[xOff + 2]) & M));
        z[zOff + 2] = (int) c;
        c = (c >> 32) + ((((long) z[zOff + 3]) & M) - (((long) x[xOff + 3]) & M));
        z[zOff + 3] = (int) c;
        c = (c >> 32) + ((((long) z[zOff + 4]) & M) - (((long) x[xOff + 4]) & M));
        z[zOff + 4] = (int) c;
        c = (c >> 32) + ((((long) z[zOff + 5]) & M) - (((long) x[xOff + 5]) & M));
        z[zOff + 5] = (int) c;
        return (int) (c >> 32);
    }

    public static BigInteger toBigInteger(int[] x) {
        byte[] bs = new byte[24];
        for (int i = 0; i < 6; i++) {
            int x_i = x[i];
            if (x_i != 0) {
                Pack.intToBigEndian(x_i, bs, (5 - i) << 2);
            }
        }
        return new BigInteger(1, bs);
    }

    public static BigInteger toBigInteger64(long[] x) {
        byte[] bs = new byte[24];
        for (int i = 0; i < 3; i++) {
            long x_i = x[i];
            if (x_i != 0) {
                Pack.longToBigEndian(x_i, bs, (2 - i) << 3);
            }
        }
        return new BigInteger(1, bs);
    }

    public static void zero(int[] z) {
        z[0] = 0;
        z[1] = 0;
        z[2] = 0;
        z[3] = 0;
        z[4] = 0;
        z[5] = 0;
    }
}
