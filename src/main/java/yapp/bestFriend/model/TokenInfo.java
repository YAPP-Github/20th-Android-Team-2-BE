package yapp.bestFriend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class TokenInfo {
    private String userId;
    private String nickName;
    private String fcmToken;
}