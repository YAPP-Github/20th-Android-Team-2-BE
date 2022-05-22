package yapp.bestFriend.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleProductResponse {

    private Long productId;

    @ApiModelProperty(value = "절약 상품의 이름")
    private String name;

    @ApiModelProperty(value = "1회당 지출 금액")
    private String price;

    @ApiModelProperty(value = "다짐")
    private String resolution;
}
