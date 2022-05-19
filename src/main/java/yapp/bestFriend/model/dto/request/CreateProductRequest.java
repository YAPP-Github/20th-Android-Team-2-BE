package yapp.bestFriend.model.dto.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.User;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank
    @ApiModelProperty(value = "현재 로그인한 사용자의 user_id")
    private Long userId;

    @NotBlank
    @ApiModelProperty(value = "절약 상품의 이름")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "1회당 지출 금액")
    private String price;

    @NotBlank
    @ApiModelProperty(value = "다짐")
    private String resolution;

    public Product toEntity(User user) {
        return Product.builder()
                .user(user)
                .name(name)
                .price(price)
                .resolution(resolution)
                .build();
    }
}
