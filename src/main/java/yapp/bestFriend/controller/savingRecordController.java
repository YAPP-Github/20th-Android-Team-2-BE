package yapp.bestFriend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CheckProductRequest;
import yapp.bestFriend.service.SavingRecordService;

import javax.validation.Valid;

@Api(tags = {"절약 완료 여부 관련API"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class savingRecordController {

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

}
