package com.carlkuesters.fifachampions.game;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class MathUtil {

    public static final float EPSILON = 0.00001f;
    public static final float EPSILON_SQUARED = (EPSILON * EPSILON);
    public static final Vector2f UNIT_2D_Y = new Vector2f(0, 1);

    public static boolean equalsWithEpsilon(Vector2f vector1, Vector2f vector2) {
        return (vector1.distanceSquared(vector2) < EPSILON_SQUARED);
    }

    public static boolean equalsWithEpsilon(Vector3f vector1, Vector3f vector2) {
        return (vector1.distanceSquared(vector2) < EPSILON_SQUARED);
    }

    public static float getDistance_Line_Point(Vector2f line1, Vector2f line2, Vector2f point) {
        return FastMath.abs(((line2.y - line1.y) * point.x) - ((line2.x - line1.x) * point.y) + (line2.x * line1.y) - (line2.y * line1.x))
                / FastMath.sqrt(FastMath.sqr(line2.y - line1.y) + FastMath.sqr(line2.x - line1.x));
    }

    public static float getSmallestAngleBetween(Vector2f vector1, Vector2f vector2) {
        float angle = vector1.angleBetween(vector2);
        if (angle < -1 * FastMath.PI) {
            angle += FastMath.TWO_PI;
        } else if (angle > FastMath.PI) {
            angle -= FastMath.TWO_PI;
        }
        return angle;
    }

    public static Vector2f convertTo2D_XZ(Vector3f vector) {
        return new Vector2f(vector.x, vector.z);
    }

    public static Vector3f convertTo3D_XZ(Vector2f vector) {
        return new Vector3f(vector.x, 0, vector.y);
    }
}
