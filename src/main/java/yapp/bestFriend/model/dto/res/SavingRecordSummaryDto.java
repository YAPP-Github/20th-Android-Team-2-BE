package yapp.bestFriend.model.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingRecordSummaryDto {

    @ApiModelProperty(value = "기록 기준월", example = "202205")
    private String recordMM;

    @ApiModelProperty(value = "절약 상품의 이름", example = "빙수")
    private String name;

    @ApiModelProperty(value = "이번달 절약 횟수", example = "2")
    private Integer baseTimes;

    @ApiModelProperty(value = "지난달 절약 횟수", example = "1")
    private Integer prevTimes;

    @ApiModelProperty(value = "직전월 대비 횟수", example = "1")
    private Integer timesComparedToPrev;

    public SavingRecordSummaryDto(SavingRecordSummaryInterface input) {
        this.recordMM = input.getRecordMm();
        this.name = input.getProductName();
        this.baseTimes = input.getBaseTimes();
        this.prevTimes = input.getPrevTimes();
        this.timesComparedToPrev = input.getTimesComparedToPrev();
    }

    public SavingRecordSummaryDto(SavingRecordSummaryDto input) {
        this.recordMM = input.getRecordMM();
        this.name = input.getName();
        this.baseTimes = input.getBaseTimes();
        this.prevTimes = input.getPrevTimes();
        this.timesComparedToPrev = input.getTimesComparedToPrev();
    }
}
