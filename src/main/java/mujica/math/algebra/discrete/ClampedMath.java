package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/2/27")
public class ClampedMath extends IntegralMath {

    public static final ClampedMath INSTANCE = new ClampedMath();

    public int cast2i(long value) {
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) value;
    }

    @Override
    public int abs(int a) {
        if (a < 0) {
            if (a == Integer.MIN_VALUE) {
                return Integer.MAX_VALUE;
            }
            return -a;
        } else {
            return a;
        }
    }

    @Override
    public long abs(long a) {
        if (a < 0) {
            if (a == Long.MIN_VALUE) {
                return Long.MAX_VALUE;
            }
            return -a;
        } else {
            return a;
        }
    }

    @Override
    public int increment(int a) {
        if (a == Integer.MAX_VALUE) {
            return a;
        } else {
            return a + 1;
        }
    }

    @Override
    public long increment(long a) {
        if (a == Long.MAX_VALUE) {
            return a;
        } else {
            return a + 1L;
        }
    }

    @Override
    public int decrement(int a) {
        if (a == Integer.MIN_VALUE) {
            return a;
        } else {
            return a - 1;
        }
    }

    @Override
    public long decrement(long a) {
        if (a == Long.MIN_VALUE) {
            return a;
        } else {
            return a - 1L;
        }
    }

    @Override
    public int add(int x, int y) {
        final int s = x + y;
        if (((x ^ s) & (y ^ s)) < 0) {
            if (s < 0) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }
        return s;
    }

    @Override
    public long add(long x, long y) {
        final long s = x + y;
        if (((x ^ s) & (y ^ s)) < 0L) {
            if (s < 0L) {
                return Long.MAX_VALUE;
            } else {
                return Long.MIN_VALUE;
            }
        }
        return s;
    }

    @Override
    public int subtract(int x, int y) {
        final int d = x - y;
        if (((x ^ y) & (x ^ d)) < 0) {
            if (x < 0) {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        }
        return d;
    }

    @Override
    public long subtract(long x, long y) {
        final long d = x - y;
        if (((x ^ y) & (x ^ d)) < 0L) {
            if (x < 0L) {
                return Long.MIN_VALUE;
            } else {
                return Long.MAX_VALUE;
            }
        }
        return d;
    }

    @Override
    public int multiply(int x, int y) {
        final long p = ((long) x) * y;
        final int ip = (int) p;
        if (p != ip) {
            if (p < 0L) {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        }
        return ip;
    }

    @Override
    public long multiply(long x, long y) {
        final long r = x * y;
        final long ax = Math.abs(x);
        final long ay = Math.abs(y);
        if (((ax | ay) >>> 31 != 0L)) {
            if (((y != 0L) && (r / y != x)) || (x == Long.MIN_VALUE && y == -1L)) {
                if ((x ^ y) < 0) {
                    return Long.MIN_VALUE;
                } else {
                    return Long.MAX_VALUE;
                }
            }
        }
        return r;
    }

    @Override
    public int divideExact(int x, int y) {
        if (x % y != 0) {
            throw new ArithmeticException("Can not divide exact");
        }
        if (y == -1 && x == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }
        return x / y;
    }

    @Override
    public long divideExact(long x, long y) {
        if (x % y != 0L) {
            throw new ArithmeticException("Can not divide exact");
        }
        if (y == -1L && x == Long.MIN_VALUE) {
            return Long.MAX_VALUE;
        }
        return x / y;
    }

    @Override
    public int power(int base, int exponent) {
        if (exponent <= 0) {
            if (exponent == 0) {
                if (base == 0) {
                    throw new ArithmeticException("power zero");
                }
                return 1;
            }
            throw new ArithmeticException("power negative");
        }
        int product = base;
        for (int i = Integer.SIZE - 2 - Integer.numberOfLeadingZeros(exponent); i >= 0; i--) {
            product = multiply(product, product);
            if (product == Integer.MAX_VALUE) { // do not need to check for min value
                break;
            }
            if ((exponent & (1 << i)) != 0) {
                product = multiply(product, base);
            }
        }
        return product;
    }

    @Override
    public long power(long base, long exponent) {
        if (exponent <= 0L) {
            if (exponent == 0L) {
                if (base == 0L) {
                    throw new ArithmeticException("power zero");
                }
                return 1L;
            }
            throw new ArithmeticException("power negative");
        }
        long product = base;
        for (int i = Long.SIZE - 2 - Long.numberOfLeadingZeros(exponent); i >= 0; i--) {
            product = multiply(product, product);
            if (product == Long.MAX_VALUE) { // do not need to check for min value
                break;
            }
            if ((exponent & (1L << i)) != 0) {
                product = multiply(product, base);
            }
        }
        return product;
    }

    @Override
    public int factorial(int a) {
        if (a < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        if (a > 12) {
            return Integer.MAX_VALUE;
        }
        int f = 1;
        for (int i = 2; i <= a; i++) {
            f *= i;
        }
        return f;
    }

    @Override
    public long factorial(long a) {
        if (a < 0L) {
            throw new ArithmeticException("Factorial negative");
        }
        if (a > 20L) {
            return Long.MAX_VALUE;
        }
        long f = 1;
        for (long i = 2; i <= a; i++) {
            f *= i;
        }
        return f;
    }

    public int arrangement(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        int product = 1;
        for (int i = n - m + 1; i <= n; i++) {
            product = multiply(product, i);
            if (product == Integer.MAX_VALUE) {
                break;
            }
        }
        return product;
    }
}
