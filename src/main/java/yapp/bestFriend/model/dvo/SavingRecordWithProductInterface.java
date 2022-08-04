package yapp.bestFriend.model.dvo;

import io.swagger.annotations.ApiModelProperty;

public interface SavingRecordWithProductInterface {

    Long getProductId();

    @ApiModelProperty(value = "절약 상품의 이름")
    String getName();

    @ApiModelProperty(value = "recordYmd 기준 지출 금액")
    String getPrice();

    @ApiModelProperty(value = "누적 절약 횟수")
    Integer getAccmTimes();

    @ApiModelProperty(value = "잔여 절약 횟수")
    Integer getRemainingTimes();
}
