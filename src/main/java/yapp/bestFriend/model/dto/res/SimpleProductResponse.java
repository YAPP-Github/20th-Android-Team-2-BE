package yapp.bestFriend.model.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleProductResponse {

    private Long productId;

    @ApiModelProperty(value = "절약 상품의 이름")
    private String name;

    @ApiModelProperty(value = "1회당 지출 금액")
    private String price;

    @ApiModelProperty(value = "당일 절약 완료 여부")
    private boolean isChecked;

    @ApiModelProperty(value = "Product GET API가 호출되었을때의 서버 시간")
    private LocalDate today;
}
