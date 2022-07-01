package yapp.bestFriend.model.dto.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {

    @NotNull
    @ApiModelProperty(value = "현재 로그인한 사용자의 user_id")
    private Long userId;

    @NotBlank
    @ApiModelProperty(value = "절약 상품의 이름")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "1회당 지출 금액")
    private String price;

    public Product toEntity(User user) {
        return Product.builder()
                .user(user)
                .name(name)
                .price(price)
                .build();
    }
}
