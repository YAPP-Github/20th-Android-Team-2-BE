package yapp.bestFriend.model.dto.res.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignInResponseDto {

    private String accessToken;

    private String refreshToken;

    private Long userId;

    private String nickName;

    public UserSignInResponseDto(String accessToken, String refreshToken,
                                 Long userId, String nickName) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.nickName = nickName;
    }
}
