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
public class SimpleAlarmOnlyCreateAtDto {

    @ApiModelProperty(value = "마지막으로 발송한 알림 시간")
    private LocalDateTime createAt;
}
