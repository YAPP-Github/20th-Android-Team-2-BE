package yapp.bestFriend.controller.api.v1.alarm;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.service.v1.alarm.AlramService;

@Api(tags = {"알람 관련 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AlarmController {

    private final AlramService alramService;

    @ApiOperation(value = "알람 조회 API",notes = "사용자가 수신 받은 push 알람 리스트를 조회합니다.")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 조회 성공 \t\n 2. 조회 실패(사용자 정보 없음)"),
    })
    @GetMapping("/alarm")
    public ResponseEntity<DefaultRes> inquiryAlarm(){
        DefaultRes defaultRes = alramService.inquiryAlarm(UserUtil.getId());

        if (defaultRes.getStatusCode() == HttpStatus.UNAUTHORIZED.value()){
            return new ResponseEntity<>(defaultRes, HttpStatus.UNAUTHORIZED);
        }else{
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "마지막 알람 일시 API",notes = "사용자가 마지막으로 수신 받은 push 알람 일시를 조회합니다.")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "1. 조회 성공 \t\n 2. 조회 실패(사용자 정보 없음)"),
    })
    @GetMapping("/alarm/recent-created")
    public ResponseEntity<DefaultRes> inquiryLastAlarmDt(){
        DefaultRes defaultRes = alramService.inquiryLastAlarmDt(UserUtil.getId());

        if (defaultRes.getStatusCode() == HttpStatus.UNAUTHORIZED.value()){
            return new ResponseEntity<>(defaultRes, HttpStatus.UNAUTHORIZED);
        }else{
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        }
    }

}
