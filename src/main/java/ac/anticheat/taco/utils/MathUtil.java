package ac.anticheat.taco.utils;

public class MathUtil {
    public static float gcd(float a, float b) {
        while (b > 0.0001) {
            float temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static boolean isRounded(float value) {
        return value % 1 == 0;
    }
}
