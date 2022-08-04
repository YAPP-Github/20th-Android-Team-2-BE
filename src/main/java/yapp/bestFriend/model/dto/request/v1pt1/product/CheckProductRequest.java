package yapp.bestFriend.model.dto.request.v1pt1.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckProductRequest {

    @NotNull
    @ApiModelProperty(value = "절약 항목 ID")
    private Long productId;

    @NotNull
    @ApiModelProperty(value = "기준 일시")
    private LocalDate requestYmd;

    @NotBlank
    @ApiModelProperty(value = "절약 금액")
    private String savings;
}
