package yapp.bestFriend.service.v1pt1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.res.SimpleProductResponse;
import yapp.bestFriend.model.dvo.SavingRecordWithProductInterface;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.utils.LocalDateUtil;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.SavingRecordRepository;
import yapp.bestFriend.repository.SavingRecordRepositoryCustom;
import yapp.bestFriend.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceV1pt1 {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SavingRecordRepository savingRecordRepository;
    private final SavingRecordRepositoryCustom savingRecordRepositoryCustom;

    public DefaultRes<List<yapp.bestFriend.model.dto.res.v1pt.SimpleProductResponse>> getProductListV1pt1(Long userId, String recordYmd) {
        Optional<User> user = userRepository.findById(userId);

        // 해당 userId로 가입된 사용자가 존재하는 경우
        if(user.isPresent()){
            try {
                return getListDefaultRes(user.get(), recordYmd);
            }catch (Exception e){
                return DefaultRes.response(HttpStatus.OK.value(), "등록 실패(날짜 형식 오류)");
            }
        }
        // 해당 userId로 가입된 사용자가 존재하지 않는 경우
        else return DefaultRes.response(HttpStatus.OK.value(), "조회 실패(사용자 정보 없음)");
    }

    private DefaultRes<List<yapp.bestFriend.model.dto.res.v1pt.SimpleProductResponse>> getListDefaultRes(User existingUser, String recordYmd) {
        List<Product> productList = productRepository.findProductList(recordYmd, existingUser.getId(), LocalDateUtil.getDayOfWeek(recordYmd));

        if(productList.isEmpty()){
            return DefaultRes.response(HttpStatus.OK.value(), "데이터 없음");
        }
        else{
            List<yapp.bestFriend.model.dto.res.v1pt.SimpleProductResponse> SimpleProductResponseList = new ArrayList<>();
            for(Product p:productList){
                List<String> intervalList = Arrays.asList(p.getFreqInterval().split(","));
                SavingRecordWithProductInterface product = savingRecordRepository.searchProductListWithSavingRecord(existingUser.getId(), LocalDateUtil.convertToLocalDate(recordYmd).toString(), p.getId(), intervalList);
                SimpleProductResponseList.add(new yapp.bestFriend.model.dto.res.v1pt.SimpleProductResponse(
                        product.getProductId(),
                        product.getName(),
                        product.getPrice(),
                        Integer.parseInt(product.getPrice())>0?true:false,
                        LocalDate.now(),
                        product.getAccmTimes(),
                        product.getRemainingTimes()));
            }

            return DefaultRes.response(HttpStatus.OK.value(), "조회성공", SimpleProductResponseList);
        }
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
