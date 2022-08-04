package yapp.bestFriend.model.dto.res.v1pt;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleProductResponse {

    private Long productId;

    @ApiModelProperty(value = "절약 상품의 이름")
    private String name;

    @ApiModelProperty(value = "recordYmd 기준 지출 금액")
    private String price;

    @ApiModelProperty(value = "당일 절약 완료 여부")
    private boolean isChecked;

    @ApiModelProperty(value = "Product GET API가 호출되었을때의 서버 시간")
    private LocalDate today;

    @ApiModelProperty(value = "누적 절약 횟수")
    private Integer accmTimes;

    @ApiModelProperty(value = "잔여 절약 횟수")
    private Integer remainingTimes;
}
