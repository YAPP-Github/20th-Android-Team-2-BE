package yapp.bestFriend.model.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleAlarmDto {

    @ApiModelProperty(value = "push 메시지의 title 영역")
    private String title;

    @ApiModelProperty(value = "push 메시지의 body 영역")
    private String body;

    @ApiModelProperty(value = "알림 발송 후 경과 시간")
    private String elapsedTime;

    @ApiModelProperty(value = "알림 발송 시간")
    private LocalDateTime createAt;
}
