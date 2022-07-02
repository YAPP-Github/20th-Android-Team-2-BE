package yapp.bestFriend.model.dto.res.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInResponseDto {

    private String accessToken;

    private String refreshToken;

    private Long userId;

    private String nickName;

    private String email;

    private LocalDateTime createAt;

}
