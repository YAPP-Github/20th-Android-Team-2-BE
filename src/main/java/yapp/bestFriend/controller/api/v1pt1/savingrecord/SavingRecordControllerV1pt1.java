package yapp.bestFriend.controller.api.v1pt1.savingrecord;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.v1pt1.product.CheckProductRequest;
import yapp.bestFriend.model.dto.request.v1pt1.product.UpdateSavingRecordsRequest;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.service.v1pt1.savingRecord.SavingRecordServiceV1pt1;

import javax.validation.Valid;

@Api(tags = {"절약 기록 API(Ver 1.1)"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SavingRecordControllerV1pt1 {

    private final SavingRecordServiceV1pt1 savingRecordService;

    @ApiOperation(value = "당일 절약 완료 등록 API", notes = "절약 리스트에 있는 절약 중 하나를 완료하여 체크 버튼을 누를 때 사용되는 API입니다.")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 등록 성공 \t\n 2. 등록 실패(사용자 정보 없음) \t 3. 등록 실패(절약 정보 없음) \t\n"),
    })
    @PostMapping(value = "/savingRecords", headers="X-API-VERSION=1.1")
    public ResponseEntity<DefaultRes> checkProduct(@Valid @RequestBody CheckProductRequest request){
         return new ResponseEntity<>(savingRecordService.checkProduct(UserUtil.getId(), request), HttpStatus.OK);
    }

    @ApiOperation(value = "절약 금액 수정 API", notes = "절약 금액을 수정할 때 사용되는 API입니다")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 수정 성공 \t\n 2. 수정 실패(사용자 정보 없음) \t\n 3. 수정 실패(절약 정보 없음) \t\n 4. 수정 실패(기록 정보 없음) \t\n" +
                    "5. 체크 해제 성공"),
    })
    @PatchMapping(value = "/savingRecords", headers="X-API-VERSION=1.1")
    public ResponseEntity<DefaultRes> updateSavingRecord(@Valid @RequestBody UpdateSavingRecordsRequest request){
        return new ResponseEntity<>(savingRecordService.updateSavingRecord(UserUtil.getId(), request), HttpStatus.OK);
    }

}
