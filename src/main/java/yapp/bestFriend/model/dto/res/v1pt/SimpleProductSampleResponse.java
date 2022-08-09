package yapp.bestFriend.model.dto.res.v1pt;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleProductSampleResponse {

    @ApiModelProperty(value = "샘플 절약 상품의 이름")
    private String name;
}
