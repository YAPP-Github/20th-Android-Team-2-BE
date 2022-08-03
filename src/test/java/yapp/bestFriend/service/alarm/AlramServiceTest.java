package yapp.bestFriend.service.alarm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import yapp.bestFriend.service.v1.alarm.AlramService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class AlramServiceTest {

    @InjectMocks
    private AlramService alramService;

    @Test
    @DisplayName("10분 전")
    void calculateElapsedTimeMinutesAgo(){
        //given
        LocalDateTime now = LocalDateTime.of(2022,7,1,12,21);
        LocalDateTime past = LocalDateTime.of(2022,7,1,12,11);

        //then
        assertThat(alramService.CallCalculateElapsedTime(past, now), is("10분 전"));
    }

    @Test
    @DisplayName("어제")
    void calculateElapsedTimeYesterday(){
        //given
        LocalDateTime now = LocalDateTime.of(2022,7,1,12,21);
        LocalDateTime past = LocalDateTime.of(2022,6,30,10,21);

        //then
        assertThat(alramService.CallCalculateElapsedTime(past, now), is("어제"));
    }

    @Test
    @DisplayName("5일전")
    void calculateElapsedTimeDaysAgo(){
        //given
        LocalDateTime now = LocalDateTime.of(2022,7,1,12,21);
        LocalDateTime past = LocalDateTime.of(2022,6,26,10,21);
        //then
        assertThat(alramService.CallCalculateElapsedTime(past, now), is("5일 전"));
    }

    @Test
    @DisplayName("3개월 전")
    void calculateElapsedTimeWhenMonthAgo(){
        //given
        LocalDateTime now = LocalDateTime.of(2022,7,1,12,21);
        LocalDateTime past = LocalDateTime.of(2022,3,13,12,21);

        //then
        assertThat(alramService.CallCalculateElapsedTime(past, now), is("3개월 전"));
    }

    @Test
    @DisplayName("2년 전")
    void calculateElapsedTimeWhenYearAgo(){
        //given
        LocalDateTime now = LocalDateTime.of(2022,7,1,12,21);
        LocalDateTime past = LocalDateTime.of(2019,12,13,12,21);

        //then
        assertThat(alramService.CallCalculateElapsedTime(past, now), is("2년 전"));
    }

}