package yapp.bestFriend.model.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateUtil {

    /**
     * @param recordYmd 오늘 날짜
     * @return LocalDate 타입의 오늘 날짜를 반환한다.
     */
    public static LocalDate convertToLocalDate(String recordYmd) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(recordYmd, inputFormat);
    }

    /**
     * @param recordYmd 오늘 날짜
     * @return 요일 번호를 리턴한다. (1:월요일 ~ 7:일요일)
     */
    public static String getDayOfWeek(String recordYmd) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate recordDate = LocalDate.parse(recordYmd, inputFormat);

        DayOfWeek dayOfWeek = recordDate.getDayOfWeek();

        return String.valueOf(dayOfWeek.getValue());
    }
}
