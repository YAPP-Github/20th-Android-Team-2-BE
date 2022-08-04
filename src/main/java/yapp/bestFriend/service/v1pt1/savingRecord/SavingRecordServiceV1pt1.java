package yapp.bestFriend.service.v1pt1.savingRecord;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.v1pt1.product.CheckProductRequest;
import yapp.bestFriend.model.dto.request.v1pt1.product.UpdateSavingRecordsRequest;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.SavingRecordRepository;
import yapp.bestFriend.repository.SavingRecordRepositoryCustom;
import yapp.bestFriend.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavingRecordServiceV1pt1 {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final SavingRecordRepositoryCustom savingRecordRepositoryCustom;

    private final SavingRecordRepository savingRecordRepository;

    public DefaultRes checkProduct(Long userId, CheckProductRequest request) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()){
            Optional<Product> product = productRepository.findById(request.getProductId());
            User existingUser = user.get();

            if(!product.isPresent()){
                return DefaultRes.response(HttpStatus.OK.value(), "등록 실패(절약 정보 없음)");
            }
            Product existingProduct = product.get();
            LocalDate requestDate = request.getRequestYmd();
            String savings = request.getSavings();

            boolean result = savingRecordRepositoryCustom.isChecked(existingUser, requestDate, existingProduct);

            // 이미 체크 표시가 되어 있는 경우
            if(result){
                return deleteSavingRecords(existingUser, existingProduct, requestDate);
            }
            else{
                //없는 경우 신규 생성
                SavingRecord savingRecord = SavingRecord.builder()
                        .product(existingProduct)
                        .user(existingUser)
                        .recordYmd(requestDate)
                        .savings(savings)
                        .build();

                savingRecordRepository.save(savingRecord);

                return DefaultRes.response(HttpStatus.OK.value(), "등록 성공");
            }

        }

        else return DefaultRes.response(HttpStatus.OK.value(), "등록 실패(사용자 정보 없음)");
    }

    private DefaultRes<Object> deleteSavingRecords(User existingUser, Product existingProduct, LocalDate requestDate) {
        // 해당날짜, 상품id, 유저id, 삭제여부로 조회
        List<SavingRecord> savingRecordList = savingRecordRepository.findSavingRecordsByRecordYmdAndProductIdAndUserIdAndDeletedYn(requestDate, existingProduct.getId(), existingUser.getId(), false);
        for(SavingRecord sr: savingRecordList){
            savingRecordRepository.deleteById(sr.getId());
        }
        return DefaultRes.response(HttpStatus.OK.value(), "체크 해제 성공");
    }

    public DefaultRes updateSavingRecord(Long userId, UpdateSavingRecordsRequest request) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()){
            Optional<Product> product = productRepository.findById(request.getProductId());

            if(product.isPresent()){
                Optional<SavingRecord> savingRecord = savingRecordRepository.findByUserIdAndRecordYmdAndProductId(userId, request.getRequestYmd(), request.getProductId());;

                if(savingRecord.isPresent()){
                    if(request.getPrice().trim().equals("0")){//0원이면 삭제 처리함
                        return deleteSavingRecords(user.get(), product.get(), request.getRequestYmd());
                    }

                    SavingRecord updateSavingRecord = savingRecord.get().updateSavings(request);
                    savingRecordRepository.save(updateSavingRecord);

                    return DefaultRes.response(HttpStatus.OK.value(), "수정 성공");
                }
                else return DefaultRes.response(HttpStatus.OK.value(), "수정 실패(기록 정보 없음)");
            }
            else return DefaultRes.response(HttpStatus.OK.value(), "수정 실패(절약 정보 없음)");
        }
        else return DefaultRes.response(HttpStatus.OK.value(), "수정 실패(사용자 정보 없음)");
    }
}
