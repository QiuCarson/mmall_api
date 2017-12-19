package com.carson.mmall.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalUtil {
    public static BigDecimal priceFormat(BigDecimal price){
        DecimalFormat df1 = new DecimalFormat("0.00");
        String str = df1.format(price);
        return new BigDecimal(str);
    }


}
