package yapp.bestFriend.service.v1.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.res.SimpleAlarmDto;
import yapp.bestFriend.model.dto.res.SimpleAlarmOnlyCreateAtDto;
import yapp.bestFriend.model.entity.PushNotiHistory;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.PushNotiHistoryRepository;
import yapp.bestFriend.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlramService {
    private final PushNotiHistoryRepository pushNotiHistoryRepository;
    private final UserRepository userRepository;

    private static final String FEW_MINUTES_AGO = "분 전";
    private static final String FEW_DAYS_AGO = "일 전";
    private static final String YESTERDAY = "어제";
    private static final String MONTH_AGO = "개월 전";
    private static final String YEAR_ELAPSED = "년 전";

    public DefaultRes inquiryAlarm(long userId) {
        Optional<User> user = userRepository.findById(userId);
        LocalDateTime now = LocalDateTime.now(); // 현재시간

        // 해당 userId로 가입된 사용자가 존재하는 경우
        if(user.isPresent()){
            List<PushNotiHistory> pushNotiHistory = pushNotiHistoryRepository.findByUserIdAndDeletedYnOrderByCreatedAtDesc(userId, false);

            List<SimpleAlarmDto> pushAlamList = new ArrayList<>();
            for(PushNotiHistory pushNoti: pushNotiHistory){
                pushAlamList.add(new SimpleAlarmDto(pushNoti.getTitle(), pushNoti.getBody(), calculateElapsedTime(pushNoti.getCreatedAt(), now), pushNoti.getCreatedAt()));
            }

            return DefaultRes.response(HttpStatus.OK.value(), "조회성공", pushAlamList);
        }
        // 해당 userId로 가입된 사용자가 존재하지 않는 경우
        else return DefaultRes.response(HttpStatus.OK.value(), "조회 실패(사용자 정보 없음)");
    }

    /**
     * Search date and time of last sent push notification
     * @param userId
     * @return
     */
    public DefaultRes inquiryLastAlarmDt(long userId) {
        Optional<User> user = userRepository.findById(userId);
        LocalDateTime now = LocalDateTime.now(); // 현재시간

        // 해당 userId로 가입된 사용자가 존재하는 경우
        if(user.isPresent()){
            LocalDateTime maxCreatedAtByUserId = pushNotiHistoryRepository.findMaxCreatedAtByUserId(userId);
            if(maxCreatedAtByUserId!=null){
                return DefaultRes.response(HttpStatus.OK.value(), "조회성공", new SimpleAlarmOnlyCreateAtDto(maxCreatedAtByUserId));
            }
            return DefaultRes.response(HttpStatus.OK.value(), "조회성공", null);
        }
        // 해당 userId로 가입된 사용자가 존재하지 않는 경우
        else return DefaultRes.response(HttpStatus.OK.value(), "조회 실패(사용자 정보 없음)");
    }

    private String calculateElapsedTime(LocalDateTime pushTime, LocalDateTime now) {
        long minutes = ChronoUnit.MINUTES.between(pushTime, now); // 일수 계산
        if (minutes < 60) {//n분 전
            return minutes + FEW_MINUTES_AGO;
        }

        long hours = ChronoUnit.HOURS.between(pushTime, now);
        if (hours >= 24 && hours < 48) {//어제
            return YESTERDAY;
        }

        long days = ChronoUnit.DAYS.between(pushTime, now);
        if (days <= 30) {//최근 1달 이내
            return days + FEW_DAYS_AGO;
        }

        long months = ChronoUnit.MONTHS.between(pushTime, now);
        if (months <= 12) {//최근 1년 이내
            return months + MONTH_AGO;
        }

        //count how many years have passed
        long years = ChronoUnit.YEARS.between(pushTime, now);
        return years+ YEAR_ELAPSED;
    }

    public String CallCalculateElapsedTime(LocalDateTime pushTime, LocalDateTime now) {
        return calculateElapsedTime(pushTime, now);
    }
}
