package com.carlkuesters.fifachampions.game.math;

import java.util.HashMap;

public class Parabole {

    public Parabole(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    private float a;
    private float b;
    private float c;
    private HashMap<String, Float> calculatedValues = new HashMap<>();

    public float getValue(float x) {
        return (a * x * x) + (b * x) + c;
    }

    public float getFirstDerivative(float x) {
        return (2 * a * x) + b;
    }

    public float getSecondDerivative() {
        return 2 * a;
    }

    public void setCalculatedValue(String name, float value) {
        calculatedValues.put(name, value);
    }

    public float getCalculatedValue(String name) {
        return calculatedValues.get(name);
    }
}
