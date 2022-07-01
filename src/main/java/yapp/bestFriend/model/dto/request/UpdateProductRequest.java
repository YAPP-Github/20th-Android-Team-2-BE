package yapp.bestFriend.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateProductRequest {

    @NotNull
    @ApiModelProperty(value = "현재 로그인한 사용자의 user_id")
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "수정하려는 절약(Product)의 product_id")
    private Long productId;

    @NotBlank
    @ApiModelProperty(value = "절약 상품의 이름")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "1회당 지출 금액")
    private String price;
}
