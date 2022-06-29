package yapp.bestFriend.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestPushMessage {

    ADD_SAVING_LIST("절약과 친해지는 중!", "절약 리스트를 추가해보세요!"),
    REPORT_WAITING("절약과 친해지는 중!", "m월 리포트가 u님을 기다려요!"),
    WEEKEND_CHEER_UP("절약 어렵지않죠?","약속 많은 이번 주말도 절약과 친해져요!")
    ;

    final String title;
    final String body;
}
