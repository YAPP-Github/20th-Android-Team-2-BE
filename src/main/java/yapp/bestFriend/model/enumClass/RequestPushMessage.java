package yapp.bestFriend.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestPushMessage {

    ADD_SAVING_LIST("절약과 친해지는 중!", "절약 리스트를 추가해보세요!"),
//    ADD_SAVING_LIST("절약과 친해지는 중!", "절약 리스트를 추가해보세요!")
    ;

    String title;
    String body;
}
