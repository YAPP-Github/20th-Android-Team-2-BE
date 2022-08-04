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
public class UpdateSavingRecordsRequest {

    @NotBlank
    @ApiModelProperty(value = "1회당 지출 금액")
    private String price;

    @NotNull
    @ApiModelProperty(value = "수정하려는 절약(Product)의 product_id")
    private Long productId;

    @NotNull
    @ApiModelProperty(value = "기준 일시")
    private LocalDate requestYmd;
}
