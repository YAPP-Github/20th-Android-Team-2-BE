package yapp.bestFriend.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yapp.bestFriend.model.enumClass.SocialLoginType;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLoginRequest {

    @NotNull
    @ApiModelProperty(value = "사용자의 SNS TYPE", example = "KAKAO")
    private SocialLoginType provider;

    @NotNull
    @ApiModelProperty(value = "사용자의 SNS 고유 ID", example = "2242469369")
    private String providerId;

    @NotNull
    @ApiModelProperty(value = "사용자의 SNS email 주소", example = "test@naver.com")
    private String email;

    @NotNull
    @ApiModelProperty(value = "사용자의 SNS 닉네임", example = "best friend")
    private String nickName;
}
