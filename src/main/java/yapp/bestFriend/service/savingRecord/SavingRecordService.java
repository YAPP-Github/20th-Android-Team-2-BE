package yapp.bestFriend.service.savingRecord;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CheckProductRequest;
import yapp.bestFriend.model.dto.res.SavingRecordDto;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.SavingRecordRepository;
import yapp.bestFriend.repository.SavingRecordRepositoryCustom;
import yapp.bestFriend.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

            if(product.isPresent()){
                Product existingProduct = product.get();

                boolean result = savingRecordRepositoryCustom.isChecked(existingUser, LocalDate.now(),existingProduct);

                // 이미 체크 표시가 되어 있는 경우
                if(result){
                    // soft-delete로 리팩토링 해야 함
                    return null;
                }
                else{
                    SavingRecord savingRecord = SavingRecord.builder()
                            .product(existingProduct)
                            .user(existingUser)
                            .build();

                    savingRecordRepository.save(savingRecord);

                    return DefaultRes.response(HttpStatus.OK.value(), "체크 성공");
                }
            }

            else return DefaultRes.response(HttpStatus.OK.value(), "체크 실패(절약 정보 없음)");
        }

        else return DefaultRes.response(HttpStatus.OK.value(), "체크 실패(사용자 정보 없음)");
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
}
