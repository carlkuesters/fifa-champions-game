package com.carlkuesters.fifachampions.game.math;

import com.jme3.math.FastMath;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ParaboleUtil {

    public static Parabole getParabole_X1_Y1_X2_Y2_A(float x1, float y1, float x2, float y2, float a) {
        // f(x) = ax² + bx + c
        // f'(x) = 2ax + b
        // f''(x) = 2a
        // ----------
        // f(x1) = y1
        // f(x2) = y2
        // ----------
        // ax1² + bx1 + c = y1
        // ax2² + bx2 + c = y2
        // =>
        // c = y1 - ax1² - bx1
        // c = y2 - ax2² - bx2
        // =>
        // y1 - ax1² - bx1 = y2 - ax2² - bx2
        // bx1 - bx2 = y1 - y2 + ax2² - ax1²
        // b(x1 - x2) = y1 - y2 + a(x2²-x1²)
        // b = (y1 - y2 + a(x2²-x1²)) / (x1 - x2)
        // ----------
        float b = (y1 - y2 + (a * ((x2 * x2) - (x1 * x1)))) / (x1 - x2);
        float c = (y1 - (a * x1 * x1) - (b * x1));

        return new Parabole(a, b, c);
    }

    public static Parabole getParabole_X1_Y1_X2_Y2_FDX1(float x1, float y1, float x2, float y2, float firstDerivativeX1) {
        // f(x) = ax² + bx + c
        // f'(x) = 2ax + b
        // f''(x) = 2a
        // ----------
        // f(x1) = y1
        // f(x2) = y2
        // ----------
        // ax1² + bx1 + c = y1
        // ax2² + bx2 + c = y2
        // =>
        // c = y1 - ax1² - bx1
        // c = y2 - ax2² - bx2
        // =>
        // y1 - ax1² - bx1 = y2 - ax2² - bx2
        // bx1 - bx2 = y1 - y2 + ax2² - ax1²
        // b(x1 - x2) = y1 - y2 + a(x2²-x1²)
        // b = (y1 - y2 + a(x2²-x1²)) / (x1 - x2)
        // ----------
        // f'(x1) = firstDerivativeX1
        // 2ax1 + b = firstDerivativeX1
        // b = firstDerivativeX1 - 2ax1
        // ----------
        // (y1 - y2 + a(x2²-x1²)) / (x1 - x2) = firstDerivativeX1 - 2ax1
        // y1 - y2 + a(x2²-x1²) = (firstDerivativeX1 - 2ax1) * (x1 - x2)
        // y1 - y2 + a(x2²-x1²) = firstDerivativeX1*x1 - 2ax1² - firstDerivativeX1*x2 + 2ax1x2
        // a(x2²-x1²) + 2ax1² - 2ax1x2 = firstDerivativeX1*x1 - firstDerivativeX1*x2 - y1 + y2
        // a(x2² - x1² + 2x1² - 2x1x2) = firstDerivativeX1*x1 - firstDerivativeX1*x2 - y1 + y2
        // a(x1² + x2² - 2x1x2) = firstDerivativeX1*x1 - firstDerivativeX1*x2 - y1 + y2
        // a = (firstDerivativeX1*x1 - firstDerivativeX1*x2 - y1 + y2) / (x1² + x2² - 2x1x2)
        // ----------
        float a = ((firstDerivativeX1 * x1) - (firstDerivativeX1 * x2) - y1 + y2) / ((x1 * x1) + (x2 * x2) - (2 * x1 * x2));

        return getParabole_X1_Y1_X2_Y2_A(x1, y1, x2, y2, a);
    }

    public static Parabole getParabole_X1_Y1_X2_Y2_FDX2(float x1, float y1, float x2, float y2, float firstDerivativeX2) {
        // f(x) = ax² + bx + c
        // f'(x) = 2ax + b
        // f''(x) = 2a
        // ----------
        // f(x1) = y1
        // f(x2) = y2
        // ----------
        // ax1² + bx1 + c = y1
        // ax2² + bx2 + c = y2
        // =>
        // c = y1 - ax1² - bx1
        // c = y2 - ax2² - bx2
        // =>
        // y1 - ax1² - bx1 = y2 - ax2² - bx2
        // bx1 - bx2 = y1 - y2 + ax2² - ax1²
        // b(x1 - x2) = y1 - y2 + a(x2²-x1²)
        // b = (y1 - y2 + a(x2²-x1²)) / (x1 - x2)
        // ----------
        // f'(x2) = firstDerivativeX2
        // 2ax2 + b = firstDerivativeX2
        // b = firstDerivativeX2 - 2ax2
        // ----------
        // (y1 - y2 + a(x2²-x1²)) / (x1 - x2) = firstDerivativeX2 - 2ax2
        // y1 - y2 + a(x2²-x1²) = (firstDerivativeX2 - 2ax2) * (x1 - x2)
        // y1 - y2 + a(x2²-x1²) = firstDerivativeX2*x1 - 2ax1x2 - firstDerivativeX2*x2 + 2ax2²
        // a(x2²-x1²) + 2ax1x2 - 2ax2² = firstDerivativeX2*x1 - firstDerivativeX2*x2 - y1 + y2
        // a(x2² - x1² + 2x1x2 - 2x2²) = firstDerivativeX2*x1 - firstDerivativeX2*x2 - y1 + y2
        // a(-x1² - x2² + 2x1x2) = firstDerivativeX2*x1 - firstDerivativeX2*x2 - y1 + y2
        // a = (firstDerivativeX2*x1 - firstDerivativeX2*x2 - y1 + y2) / (-x1² - x2² + 2x1x2)
        // ----------
        float a = ((firstDerivativeX2 * x1) - (firstDerivativeX2 * x2) - y1 + y2) / ((-1 * x1 * x1) - (x2 * x2) + (2 * x1 * x2));

        return getParabole_X1_Y1_X2_Y2_A(x1, y1, x2, y2, a);
    }

    public static List<Parabole> getParaboles_X1_Y1_Y2_FDX2_A(float x1, float y1, float y2, float firstDerivativeX2, float a) {
        // f(x) = ax² + bx + c
        // f'(x) = 2ax + b
        // f''(x) = 2a
        // ----------
        // f(x1) = y1
        // f(x2) = y2
        // ----------
        // ax1² + bx1 + c = y1
        // ax2² + bx2 + c = y2
        // =>
        // c = y1 - ax1² - bx1
        // c = y2 - ax2² - bx2
        // =>
        // y1 - ax1² - bx1 = y2 - ax2² - bx2
        // bx1 - bx2 = y1 - y2 + ax2² - ax1²
        // b(x1 - x2) = y1 - y2 + ax2² - ax1²
        // b = (y1 - y2 + ax2² - ax1²) / (x1 - x2)
        // ----------
        // f'(x2) = firstDerivativeX2
        // 2ax2 + b = firstDerivativeX2
        // b = firstDerivativeX2 - 2ax2
        // ----------
        // (y1 - y2 + ax2² - ax1²) / (x1 - x2) = firstDerivativeX2 - 2ax2
        // y1 - y2 + ax2² - ax1² = (firstDerivativeX2 - 2ax2) * (x1 - x2)
        // y1 - y2 + ax2² - ax1² = firstDerivativeX2*x1 - 2ax1x2 - firstDerivativeX2*x2 + 2ax2²
        // y1 - y2 + ax2² - ax1² - firstDerivativeX2*x1 + 2ax1x2 + firstDerivativeX2*x2 - 2ax2² = 0
        // y1 - y2 + ax2² - ax1² - firstDerivativeX2*x1 + 2ax1x2 + firstDerivativeX2*x2 - 2ax2² = 0
        // (a - 2a)x2² + (2ax1 + firstDerivativeX2)x2 + (y1 - y2 - ax1² - firstDerivativeX2*x1) = 0
        // -ax2² + (2ax1 + firstDerivativeX2)x2 + (y1 - y2 - ax1² - firstDerivativeX2*x1) = 0
        // x2 = "Mitternachtsformel" with
        // mA = -a
        // mB = 2ax1 + firstDerivativeX2
        // mC = y1 - y2 - ax1² - firstDerivativeX2*x1
        // ----------
        float mA = -1 * a;
        float mB = ((2 * a * x1) + firstDerivativeX2);
        float mC = (y1 - y2 - (a * x1 * x1) - (firstDerivativeX2 * x1));
        float termUnderRoot = ((mB * mB) - (4 * mA * mC));
        LinkedList<Float> termUnderRootSolutions = new LinkedList<>();
        if (termUnderRoot > 0) {
            float termUnderRootRoot = FastMath.sqrt(termUnderRoot);
            termUnderRootSolutions.add(-1 * termUnderRootRoot);
            termUnderRootSolutions.add(termUnderRootRoot);
        } else  if (termUnderRoot == 0) {
            termUnderRootSolutions.add(0f);
        }
        return termUnderRootSolutions.stream()
                .map(termUnderRootRoot -> {
                    float x2 = ((termUnderRootRoot - mB) / (2 * mA));

                    float b = (firstDerivativeX2 - (2 * a * x2));
                    float c = (y1 - (a * x1 * x1) - (b * x1));

                    Parabole parabole = new Parabole(a, b, c);
                    parabole.setCalculatedValue("x2", x2);
                    return parabole;
                })
                .collect(Collectors.toList());
    }

    public static float getA(float deltaDx) {
        // f(x) = ax² + bx + c
        // f'(x) = 2ax + b
        // ----------
        // f'(x) + deltaDx = 2a(x+1) + b
        // f'(x) = 2a(x+1) + b - deltaDx
        // ----------
        // 2ax + b = 2a(x+1) + b - deltaDx
        // 2ax + b = 2ax + 2a + b - deltaDx
        // b = 2a + b - deltaDx
        // 0 = 2a - deltaDx
        // -2a = -deltaDx
        // a = deltaDx / 2
        // ----------
        return deltaDx / 2;
    }
}
