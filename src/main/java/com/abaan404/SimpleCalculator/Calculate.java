package com.abaan404.SimpleCalculator;

import org.mariuszgromada.math.mxparser.Expression;

public class Calculate {
    public static String eval(String Expression) {
        Expression exp = new Expression(Expression);
        return Double.toString(exp.calculate());
    }
}
