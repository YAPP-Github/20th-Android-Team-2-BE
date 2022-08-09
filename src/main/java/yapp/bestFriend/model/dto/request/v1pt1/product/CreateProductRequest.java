package yapp.bestFriend.model.dto.request.v1pt1.product;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.User;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {

    @NotBlank
    @ApiModelProperty(value = "절약 상품의 이름")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "반복 유형 ex)1(once), 2(Daily), 3(Weekly)")
    private String freqType;

    @NotBlank
    @ApiModelProperty(value = "반복 일시 ex)1(월), 2(화), 3(수), 4(목), 5(금), 6(토), 7(일)")
    private String freqInterval;

    @NotBlank
    @ApiModelProperty(value = "시작 일시")
    private String startYmd;

    @NotBlank
    @ApiModelProperty(value = "종료 일시")
    private String endYmd;

    public Product toEntity(User user) {
        return Product.builder()
                .user(user)
                .name(name)
                .freqType(freqType)
                .freqInterval(freqInterval)
                .startYmd(startYmd)
                .endYmd(endYmd)
                .build();
    }
}
