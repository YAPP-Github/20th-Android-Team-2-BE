package yapp.bestFriend.model.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingRecordDto {

    @ApiModelProperty(value = "기록일자", example = "20220531")
    private LocalDate recordYmd;

    private Long productId;

    @ApiModelProperty(value = "절약 상품의 이름", example = "빙수")
    private String name;

    @ApiModelProperty(value = "1회당 지출 금액", example = "8000")
    private String price;

    @ApiModelProperty(value = "다짐")
    private String resolution;

}
