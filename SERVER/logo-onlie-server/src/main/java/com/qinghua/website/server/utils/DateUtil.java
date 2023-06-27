package com.qinghua.website.server.utils;

import com.google.common.base.Preconditions;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author: 佰川
 * @date: 2020/6/11 16:28
 * @apiNote: JDK 8  新日期类 格式化与字符串转换 工具类
 */
@Slf4j
public class DateUtil {

    public static final DateTimeFormatter DFY_MD_HMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DFYMDHMS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter DFY_MD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HHmmss");
    public static final SimpleDateFormat SIMPLE_DATE = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat SIMPLE_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * LocalDateTime 转时间戳
     *
     * @param localDateTime /
     * @return /
     */
    public static Long getTimeStamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timeStamp /
     * @return /
     */
    public static LocalDateTime fromTimeStamp(Long timeStamp) {
        return LocalDateTime.ofEpochSecond(timeStamp, 0, OffsetDateTime.now().getOffset());
    }

    /**
     * 日期格式化 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static Date formatDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = null;
        try {
            curDate = df.parse(date.toString());
        } catch (ParseException e) {
            throw new BizException(SysConstant.ERROR_DATE_FORMAT);
        }
        return curDate;
    }

    /**
     * LocalDateTime 转 Date
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param localDateTime /
     * @return /
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转 LocalDateTime
     * Jdk8 后 不推荐使用 {@link Date} Date
     *
     * @param date /
     * @return /
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 日期 格式化
     *
     * @param localDateTime /
     * @param patten        /
     * @return /
     */
    public static String localDateTimeFormat(LocalDateTime localDateTime, String patten) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(patten);
        return df.format(localDateTime);
    }

    /**
     * 日期 格式化
     *
     * @param localDateTime /
     * @param df            /
     * @return /
     */
    public static String localDateTimeFormat(LocalDateTime localDateTime, DateTimeFormatter df) {
        return df.format(localDateTime);
    }

    /**
     * 日期格式化 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime /
     * @return /
     */
    public static String localDateTimeFormatyMdHms(LocalDateTime localDateTime) {
        return DFY_MD_HMS.format(localDateTime);
    }

    /**
     * 日期格式化 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public String localDateTimeFormatyMd(LocalDateTime localDateTime) {
        return DFY_MD.format(localDateTime);
    }

    /**
     * 获取前n天的日期 yyyyMMdd
     *
     * @return
     */
    public static Integer getDateBefore(int beforeDay) {
        return Integer.valueOf(DATE.format(LocalDateTime.now().plusDays(-beforeDay)));
    }

    /**
     * 获取之后开始时间日期 yyyyMMdd
     *
     * @return
     */
    public static String getDateLaterBegin(int laterBegin) {
        return DATE.format(LocalDate.now().plusDays(laterBegin));
    }

    /**
     * 获取两个日期间隔的所有日期
     *
     * @param start 格式必须为'20180125'
     * @param end   格式必须为'20180125'
     * @return
     */
    public static List<String> getBetweenDate(String start, String end) {
        List<String> list = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(start, DATE);
        LocalDate endDate = LocalDate.parse(end, DATE);

        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> list.add(DATE.format(f)));
        return list;
    }

    /**
     * 获取某段时间区间内的日期 yyyyMMdd
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return
     * @desc 包含开始日期
     */
    public static List<Long> getDateFromStartToEnd(Long start, Long end) {
        //例如传入：
        // 25   29
        //查询日期 25 26 27 28  返回此部分数据
        //统计日期 26 27 28 29
        long res = DateUtil.getValidDays(String.valueOf(start), String.valueOf(end));
        List<Long> dateList = new ArrayList<>();
        for (int i = Integer.parseInt(String.valueOf(res)); i >= 1; i--) {
            dateList.add(Long.valueOf(DateUtil.getChooseDateBefore(end.toString(), i)));
        }
        return dateList;
    }

    /**
     * 获取指定日期之前的n天 yyyyMMdd
     *
     * @param chooseDate 指定日期
     * @param beforeDay  前n天
     * @return
     */
    public static Integer getChooseDateBefore(String chooseDate, int beforeDay) {
        LocalDate date = LocalDate.parse(chooseDate, DATE);
        return Integer.valueOf(DATE.format(date.plusDays(-beforeDay)));
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static Integer getDate() {
        return Integer.valueOf(SIMPLE_DATE.format(new Date()));
    }

    /**
     * 获取当前日期时间 HHmmss
     *
     * @return
     */
    public static Integer getTime() {
        return Integer.valueOf(TIME.format(LocalTime.now()));
    }

    /**
     * 获取当前时间 HHmm
     *
     * @return
     */
    public static Integer getPlanTime() {
        return getTime() / 100;
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormat(String localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormat(String localDateTime, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormatyMdHms(String localDateTime) {
        return LocalDateTime.from(DFY_MD_HMS.parse(localDateTime));
    }

    /**
     * 判断 选择日期不能小于当前日期
     *
     * @param maxDate
     */
    public static boolean notLessThanCurrentDate(String maxDate) {

        if (maxDate.compareTo(getDate().toString()) < 0) {
            return true;
        }
        return false;
    }

    /**
     * 将指定的日期字符串转换成日期
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 日期对象
     */
    public static Date parseDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("日期转化错误");
        }
        return date;
    }

    /**
     * 计算两个日期相差的天数
     *
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static long getValidDays(String beforeDate, String afterDate) {
        Preconditions.checkArgument(beforeDate.length() == 8, "日期格式错误");
        Preconditions.checkArgument(afterDate.length() == 8, "日期格式错误");
        LocalDate start = LocalDate.of(Integer.parseInt(beforeDate.substring(0, 4)),
                Integer.parseInt(beforeDate.substring(4, 6)), Integer.parseInt(beforeDate.substring(6, 8)));
        LocalDate end = LocalDate.of(Integer.parseInt(afterDate.substring(0, 4)),
                Integer.parseInt(afterDate.substring(4, 6)), Integer.parseInt(afterDate.substring(6, 8)));
        return end.toEpochDay() - start.toEpochDay();
    }

    /**
     * 检查日期 YYYYMMDD
     */
    public static boolean isValidDate(String date) {
        String regex = "20[\\d]{2}[01][0-9][0123][0-9]";
        return Pattern.matches(regex, date);
    }

    /**
     * 检查时间 hhmmss
     */
    public static boolean isValidTime(String time) {
        String regex = "(([01]\\d)|(2[0-3]))[0-5]\\d([0-5]\\d)?";
        return Pattern.matches(regex, time);
    }

    /**
     * 验证开始时间结束时间格式是否正确
     *
     * @param startDate
     * @param startTime
     * @param endDate
     * @param endTime
     */
    public static void isValidDateStartAndEndFormat(Integer startDate, Integer startTime, Integer endDate, Integer endTime) {
        //判断日期参数有效
        if (startDate != null) {
            if (!isValidDate(String.valueOf(startDate))) {
                throw new BizException(SysConstant.ERROR_CHECK_START_DATE);
            }
        } else if (endDate != null) {
            if (!isValidDate(String.valueOf(endDate))) {
                throw new BizException(SysConstant.ERROR_CHECK_END_DATE);
            }
        } else if (startTime != null) {
            if (!isValidTime(String.valueOf(startTime))) {
                throw new BizException(SysConstant.ERROR_CHECK_START_TIME);
            }
        } else if (endTime != null) {
            if (!isValidTime(String.valueOf(endTime))) {
                throw new BizException(SysConstant.ERROR_CHECK_END_TIME);
            }
        }

        //如果时间参数传入参数，则日期参数也需要传入参数
        if (startTime != null && startDate == null) {
            Preconditions.checkNotNull(startDate, "开始时间选择后,开始日期不能为空");
        } else if (endTime != null && endDate == null) {
            Preconditions.checkNotNull(endDate, "结束时间选择后,结束日期不能为空");
        }
        //校验日期先后顺序，开始日期不能晚于结束日期，开始时间不能晚于结束时间
        if (startDate != null && endDate != null && (startDate > endDate)) {
            throw new BizException(SysConstant.ERROR_CHECK_DATE_INTERVAL);
        }
    }

    /**
     * 判断开始时间结束时间格式是否正确
     *
     * @param startDate
     * @param endDate
     */
    public static void isValidDateStartAndEndFormat(Integer startDate, Integer endDate) {
        //判断日期参数有效
        if (startDate != null) {
            if (!isValidDate(String.valueOf(startDate))) {
                throw new BizException(SysConstant.ERROR_CHECK_START_DATE);
            }
        } else if (endDate != null) {
            if (!isValidDate(String.valueOf(endDate))) {
                throw new BizException(SysConstant.ERROR_CHECK_END_DATE);
            }
        }
        //校验日期先后顺序，开始日期不能晚于结束日期，开始时间不能晚于结束时间
        if (startDate != null && endDate != null && (startDate > endDate)) {
            throw new BizException(SysConstant.ERROR_CHECK_DATE_INTERVAL);
        }
    }

    /**
     * 根据传入的时间区间，判断该时间区间格式是否合法
     *
     * @param startDate 起始始日期
     * @param startTime 起始时间
     * @param endDate   结束日期
     * @param endTime   结束时间
     * @return
     */
    public static boolean checkTimeInterval(int startDate, int startTime, int endDate, int endTime) {
        return startDate <= endDate &&
                (startDate != endDate || startTime <= endTime);
    }

    /**
     * 校验历史时间是否符合
     *
     * @param currentUpdateDate 当前更新日期
     * @param currentUpdateTime 当前更新时间
     * @param oldUpdateDate     历史更新日期
     * @param oldUpdateTime     历史更新时间
     * @return
     */
    public static boolean checkDataChange(Integer currentUpdateDate, Integer currentUpdateTime, Integer oldUpdateDate,
                                          Integer oldUpdateTime) {
        if (oldUpdateDate != null && currentUpdateDate != null) {
            return checkTimeInterval(oldUpdateDate, oldUpdateTime, currentUpdateDate, currentUpdateTime);
        } else {
            return currentUpdateDate != null;
        }
    }

    /**
     * 根据传入的日期区间，判断该时间区间格式是否合法
     *
     * @param startDate 起始始日期
     * @param endDate   结束日期
     * @return
     */
    public static boolean checkDateInterval(int startDate, int endDate) {
        return startDate <= endDate;
    }

    /**
     * 获取本周的开始时间
     *
     * @return
     */
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayOfWeek);

        return getDayStartTime(cal.getTime());
    }

    // 获取本周结束时间
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    // 获取本月开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    // 获取本月结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    // 获取今年是哪一年
    public static Integer getNowYear() {
        return LocalDateTime.now().getYear();
    }

    // 获取本月是哪一月
    public static int getNowMonth() {
        return LocalDateTime.now().getMonth().getValue();
    }

    // 获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    // 获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 根据指定日期的后N天
     *
     * @param specifiedDay 指定日期
     * @return 延后N天
     */
    public static String getSpecifiedDayAfter(String specifiedDay, long days) {
        LocalDate localDate = LocalDate.parse(specifiedDay, DATE);
        localDate = localDate.with(temporal -> temporal.plus(days, ChronoUnit.DAYS));
        return DATE.format(localDate);
    }

    /**
     * 时间戳转换成日期格式
     *
     * @param seconds 精确到秒
     * @return
     */
    public static String timeStampDate(String seconds) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        return SIMPLE_HMS.format(new Date(Long.parseLong(seconds)));
    }

    /**
     * 计算时间差 小时
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Long getDifferenceHours(String startDate, String startTime, String endDate, String endTime) {

        String startStr=startDate+startTime;
        String endStr=endDate+endTime;
        if(startTime.length()==5){
            startStr=startDate+"0"+startTime;
        }
        if(endTime.length()==5){
            endStr=endDate+"0"+endTime;
        }

        LocalDateTime startDateTime = LocalDateTime.parse(startStr, DFYMDHMS);

        LocalDateTime endDateTime = LocalDateTime.parse(endStr, DFYMDHMS);

        Duration duration = Duration.between(startDateTime, endDateTime);
        return duration.toHours();
    }

    public static void main(String[] args) {

//        long day1 = DateUtil.getValidDays("20201102", "20201120");
//        long day2 = DateUtil.getValidDays("20201102", "20201020");
//        long day3 = DateUtil.getValidDays("20201102", "20201102");
//        System.out.println(day1);
//        System.out.println(day2);
//        System.out.println(day3);
//        System.out.println(getPlanTime());
        System.out.println(getBetweenDate("20210121", "20210208"));
    }
}
