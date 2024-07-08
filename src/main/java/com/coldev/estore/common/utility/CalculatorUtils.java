package com.coldev.estore.common.utility;

import java.math.BigDecimal;
import java.util.List;

public class CalculatorUtils {

    public static BigDecimal sum(List<BigDecimal> bigDecimals) {
        BigDecimal sum = BigDecimal.ZERO;

        for (BigDecimal bigDecimal : bigDecimals) {
            sum = sum.add(bigDecimal);
        }
        return sum;
    }

}
