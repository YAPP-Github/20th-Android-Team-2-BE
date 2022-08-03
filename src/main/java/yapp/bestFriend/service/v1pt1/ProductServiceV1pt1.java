package yapp.bestFriend.service.v1pt1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.res.SimpleProductResponse;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.SavingRecordRepositoryCustom;
import yapp.bestFriend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceV1pt1 {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final SavingRecordRepositoryCustom savingRecordRepositoryCustom;

    public DefaultRes<List<SimpleProductResponse>> getProductListV1pt1(Long userId, String recordYmd) {
        return null;
    }

    public DefaultRes<List<SimpleProductResponse>> createProductV1pt1(Long userId, yapp.bestFriend.model.dto.request.v1pt1.product.CreateProductRequest request) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()){
            user.get().getProductList().add(productRepository.save(request.toEntity(user.get())));

            return DefaultRes.response(HttpStatus.OK.value(), "등록 성공");
        }
        else return DefaultRes.response(HttpStatus.OK.value(), "등록 실패(사용자 정보 없음)");
    }
}
