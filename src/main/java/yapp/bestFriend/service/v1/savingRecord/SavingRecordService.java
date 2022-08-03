package yapp.bestFriend.service.v1.savingRecord;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CheckProductRequest;
import yapp.bestFriend.model.dto.res.SavingRecordDto;
import yapp.bestFriend.model.dto.res.SavingRecordSummaryDto;
import yapp.bestFriend.model.dto.res.SavingRecordSummaryInterface;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.SavingRecordRepository;
import yapp.bestFriend.repository.SavingRecordRepositoryCustom;
import yapp.bestFriend.repository.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavingRecordService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final SavingRecordRepositoryCustom savingRecordRepositoryCustom;

    private final SavingRecordRepository savingRecordRepository;

    public DefaultRes checkProduct(CheckProductRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());

        if(user.isPresent()){
            Optional<Product> product = productRepository.findById(request.getProductId());
            User existingUser = user.get();

            if(!product.isPresent()){
                return DefaultRes.response(HttpStatus.OK.value(), "체크 실패(절약 정보 없음)");
            }
            Product existingProduct = product.get();
            LocalDate requestDate = request.getToday();

            boolean result = savingRecordRepositoryCustom.isChecked(existingUser, requestDate, existingProduct);

            // 이미 체크 표시가 되어 있는 경우
            if(result){
                return savingRecordSoftDelete(existingUser, existingProduct);
            }
            else{
                List<SavingRecord> deletedList = savingRecordRepository.findSavingRecordsByRecordYmdAndProductIdAndUserIdAndDeletedYn
                        (requestDate, existingProduct.getId(), existingUser.getId(), true);

                //product, 유저, 날짜로 이미 삭제했던 데이터가 있는 경우 true로 업데이트하여 복원
                if(!deletedList.isEmpty()){
                    return restoreSavingRecord(deletedList);
                }

                //없는 경우 신규 생성
                SavingRecord savingRecord = SavingRecord.builder()
                        .product(existingProduct)
                        .user(existingUser)
                        .recordYmd(requestDate)
                        .build();

                savingRecordRepository.save(savingRecord);

                return DefaultRes.response(HttpStatus.OK.value(), "체크 성공");
            }

        }

        else return DefaultRes.response(HttpStatus.OK.value(), "체크 실패(사용자 정보 없음)");
    }

    private DefaultRes<Object> restoreSavingRecord(List<SavingRecord> deletedList) {
        for(SavingRecord sr: deletedList){
            sr.restore();
            savingRecordRepository.save(sr);
        }
        return DefaultRes.response(HttpStatus.OK.value(), "체크 성공");
    }

    private DefaultRes<Object> savingRecordSoftDelete(User existingUser, Product existingProduct) {
        // 해당날짜, 상품id, 유저id, 삭제여부로 조회
        List<SavingRecord> savingRecordList = savingRecordRepository.findSavingRecordsByRecordYmdAndProductIdAndUserIdAndDeletedYn(LocalDate.now(), existingProduct.getId(), existingUser.getId(), false);
        for(SavingRecord sr: savingRecordList){
            sr.delete();
            savingRecordRepository.save(sr);
        }
        return DefaultRes.response(HttpStatus.OK.value(), "체크 해제 성공");
    }

    public DefaultRes getSavingList(Long userId, String recordYmd) {
        Optional<User> user = userRepository.findById(userId);

        if(!checkDate(recordYmd)){
            return DefaultRes.response(HttpStatus.OK.value(), "조회실패(기록일자 파라미터 오류)");
        }

        //해당 userId로 가입된 사용자가 존재하는 경우
        if(user.isPresent()){
            User existingUser = user.get();
            List<SavingRecord> savingRecordList = existingUser.getSavingList();

            if(savingRecordList.isEmpty()){
                return DefaultRes.response(HttpStatus.OK.value(), "데이터 없음");
            }else{
                List<SavingRecordDto> savingRecordVoList = savingRecordRepositoryCustom.findByUserId(existingUser, recordYmd);
                return DefaultRes.response(HttpStatus.OK.value(), "조회성공", savingRecordVoList);
            }
        }
        // 해당 userId로 가입된 사용자가 존재하지 않는 경우
        else return DefaultRes.response(HttpStatus.OK.value(), "조회 실패(사용자 정보 없음)");
    }

    private static boolean checkDate(String checkDate) {
        try {
            SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMM"); //검증할 날짜 포맷 설정
            dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
            dateFormatParser.parse(checkDate); //대상 값 포맷에 적용되는지 확인
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public DefaultRes getSavingSummary(long userId, String recordMM) {
        Optional<User> user = userRepository.findById(userId);

        if(!checkDate(recordMM)){
            return DefaultRes.response(HttpStatus.OK.value(), "조회실패(기록일자 파라미터 오류)");
        }

        //해당 userId로 가입된 사용자가 존재하는 경우
        if(user.isPresent()){
            //한달 전
            String prevMM = AddDate(recordMM+"01", 0, -1, 0);
            String fromDate = prevMM.substring(0,4)+"-"+prevMM.substring(4,6);
            String toDate = recordMM.substring(0,4)+"-"+recordMM.substring(4,6);
            LocalDate from = LocalDate.parse(fromDate+"-01");

            //말일 구하기 - 월 부분은 -1을 해주어야 합니다 ( 0이 1월로 잡힌다. )
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(recordMM.substring(0,4)),Integer.parseInt(recordMM.substring(4,6))-1,1);
            LocalDate to = LocalDate.parse(toDate+"-"+cal.getActualMaximum(Calendar.DAY_OF_MONTH));

            // 각 절약 항목별로 직전월 대비 횟수를 보여준다.
            List<SavingRecordSummaryInterface> monthlySummary = savingRecordRepository.selectSummary(fromDate, toDate, from, to, userId);
            List<SavingRecordSummaryDto> savingRecordSummaryList =
                    monthlySummary.stream().filter(s -> s.getRecordMm().equals(toDate))
                            .map(record -> new SavingRecordSummaryDto(record.getRecordMm(), record.getProductName(), record.getBaseTimes(),
                                            monthlySummary.stream()
                                                    .filter(s -> s.getRecordMm().equals(fromDate) &&
                                                            s.getProductName().equals(record.getProductName()))
                                                    .findFirst().map(SavingRecordSummaryInterface::getPrevTimes).orElse(0),
                                            record.getBaseTimes() - monthlySummary.stream()
                                                    .filter(s -> s.getRecordMm().equals(fromDate) &&
                                                            s.getProductName().equals(record.getProductName()))
                                                    .findFirst().map(SavingRecordSummaryInterface::getPrevTimes).orElse(0)
                                    )
                            )
                            .map(SavingRecordSummaryDto::new)
                            .collect(Collectors.toList());

            if(savingRecordSummaryList.isEmpty()){
                return DefaultRes.response(HttpStatus.OK.value(), "데이터 없음");
            }else{
                return DefaultRes.response(HttpStatus.OK.value(), "조회성공", savingRecordSummaryList);
            }
        }
        // 해당 userId로 가입된 사용자가 존재하지 않는 경우
        else return DefaultRes.response(HttpStatus.OK.value(), "조회 실패(사용자 정보 없음)");
    }

    private static String AddDate(String strDate, int year, int month, int day) {
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");

        Calendar cal = Calendar.getInstance();
        try{
            Date dt = dtFormat.parse(strDate);
            cal.setTime(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        cal.add(Calendar.YEAR,  year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE,  day);

        return dtFormat.format(cal.getTime());
    }

    /************************ version 1.1 *************************/

}
