package yapp.bestFriend.controller.api.savingrecord;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CheckProductRequest;
import yapp.bestFriend.model.dto.res.SavingRecordDto;
import yapp.bestFriend.model.dto.res.SavingRecordSummaryDto;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.service.savingRecord.SavingRecordService;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"절약 기록 API"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SavingRecordController {

    private final SavingRecordService savingRecordService;

    // product 완료 여부 API 테스트 하기 위해 작성,
    // basetime 엔티티에 삭제 여부를 나타내는 칼럼이 추가된 후 이미 체크가 되어 있는 절약을 또 체크할 시에 완료 목록(SavingRecord 테이블)에서 삭제(soft_delete) 하도록 하는 부분 작업 예정
    @ApiOperation(value = "당일 절약 완료 등록 API", notes = "절약 리스트에 있는 절약 중 하나를 완료하여 체크 버튼을 누를 때 사용되는 API입니다.")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 등록 성공 \t\n 2. 등록 실패(사용자 정보 없음) \t\n"),
    })
    @PostMapping(value = "/savingRecords")
    public ResponseEntity<DefaultRes> checkProduct(@Valid @RequestBody CheckProductRequest request){
        return new ResponseEntity<>(savingRecordService.checkProduct(request), HttpStatus.OK);
    }

    @ApiOperation(value = "절약 기록 조회 API", notes = "사용자가 월별 절약 기록을 조회 시 사용되는 API 입니다.")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 조회 성공 \t\n 2. 조회 실패(사용자 정보 없음) \t\n 3.조회실패(기록일자 파라미터 오류) \t\n 4. 데이터 없음 \t\n")
    })
    @GetMapping("/savingRecords")
    @ApiImplicitParam(name = "recordMM", value = "기록 일자(YYYYMM) ex)202206", required = true, paramType = "query", defaultValue = "")
    public ResponseEntity<DefaultRes<List<SavingRecordDto>>> getSavingList(@RequestParam("recordMM") String recordMM){
        return new ResponseEntity<>(savingRecordService.getSavingList(UserUtil.getId(), recordMM), HttpStatus.OK);
    }

    @ApiOperation(value = "절약 기록 Summary API", notes = "해당월의 전월대비 절약 기록 Summary를 제공해주는 API 입니다.")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 조회 성공 \t\n 2. 조회 실패(사용자 정보 없음) \t\n 3.조회실패(기록일자 파라미터 오류) \t\n 4. 데이터 없음 \t\n")
    })
    @GetMapping("/savingRecords/summary")
    @ApiImplicitParam(name = "recordMM", value = "기록 일자(YYYYMM) ex)202206", required = true, paramType = "query", defaultValue = "")
    public ResponseEntity<DefaultRes<List<SavingRecordSummaryDto>>> getSavingSummary(@RequestParam("recordMM") String recordMM){
        return new ResponseEntity<>(savingRecordService.getSavingSummary(UserUtil.getId(), recordMM), HttpStatus.OK);
    }

}
