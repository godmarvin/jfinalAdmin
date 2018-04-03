package com.ware.commonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {


    /**
     * /**
     * SimpleDateFormat函数语法：
     * <p>
     * G 年代标志符
     * y 年
     * M 月
     * d 日
     * h 时 在上午或下午 (1~12)
     * H 时 在一天中 (0~23)
     * m 分
     * s 秒
     * S 毫秒
     * E 星期
     * D 一年中的第几天
     * F 一月中第几个星期几
     * w 一年中第几个星期
     * W 一月中第几个星期
     * a 上午 / 下午 标记符
     * k 时 在一天中 (1~24)
     * K 时 在上午或下午 (0~11)
     * z 时区
     *
     * @return
     */
    public static String shortDate() {

        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date()).toString();

    }

    public static String shortDate(Date date) {

        return new SimpleDateFormat("yyyy年MM月dd日").format(date).toString();

    }

    public static String shortDate(String format) {

        return new SimpleDateFormat(format).format(new Date()).toString();

    }

    public static String shortDate(Date date, String format) {

        return new SimpleDateFormat(format).format(date).toString();

    }

    public static Date normalDate() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        try {
            return simpleDateFormat.parse(simpleDateFormat.format(new Date()).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String normalDate(Date date) {

        return new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒").format(date).toString();

    }

    public static String normalDate(String format) {

        return new SimpleDateFormat(format).format(new Date()).toString();

    }

    public static String normalDate(String format, Date date) {

        return new SimpleDateFormat(format).format(date).toString();

    }


}
