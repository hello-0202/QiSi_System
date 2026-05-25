package com.sc.qisi_system.common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class DateUtils {

    /**
     * 获取两个日期之间的所有日期（返回 MM-dd 格式）
     */
    public static List<String> getBetweenDays(String beginTime, String endTime) {
        List<String> dateList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate begin = LocalDate.parse(beginTime, formatter);
        LocalDate end = LocalDate.parse(endTime, formatter);

        while (!begin.isAfter(end)) {
            dateList.add(begin.format(DateTimeFormatter.ofPattern("MM-dd")));
            begin = begin.plusDays(1);
        }
        return dateList;
    }
}