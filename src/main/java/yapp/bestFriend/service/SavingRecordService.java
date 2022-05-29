package yapp.bestFriend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CheckProductRequest;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.SavingRecordRepository;
import yapp.bestFriend.repository.SavingRecordRepositoryCustom;
import yapp.bestFriend.repository.UserRepository;

import java.time.LocalDate;
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
}
