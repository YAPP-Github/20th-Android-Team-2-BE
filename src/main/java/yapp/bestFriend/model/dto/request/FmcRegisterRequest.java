package yapp.bestFriend.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data //Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode를 한번에
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FmcRegisterRequest {
    @NotNull
    @ApiModelProperty(value = "Fcm Token")
    private String fcmToken;
}
