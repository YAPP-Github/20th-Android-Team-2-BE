package yapp.bestFriend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CreateProductRequest;
import yapp.bestFriend.model.dto.request.UpdateProductRequest;
import yapp.bestFriend.model.dto.response.SimpleProductResponse;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.SavingRecordRepositoryCustom;
import yapp.bestFriend.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final SavingRecordRepositoryCustom savingRecordRepositoryCustom;

    public DefaultRes createProduct(CreateProductRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());

        if(user.isPresent()){
            user.get().getProductList().add(productRepository.save(request.toEntity(user.get())));

            return DefaultRes.response(HttpStatus.OK.value(), "등록 성공");
        }
        else return DefaultRes.response(HttpStatus.OK.value(), "등록 실패(사용자 정보 없음)");
    }

    public DefaultRes<List<SimpleProductResponse>> getProductList(Long userId, String recordYmd) {
        Optional<User> user = userRepository.findById(userId);

        // 해당 userId로 가입된 사용자가 존재하는 경우
        if(user.isPresent()){
            String nowYmd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            //오늘인 경우
            if(recordYmd.equals(nowYmd)){
                return getListDefaultRes(user);
            }

            List<SimpleProductResponse> savingRecordVoList = savingRecordRepositoryCustom.findByUserIdByYmd(user.get(), recordYmd);
            return DefaultRes.response(HttpStatus.OK.value(), "조회성공", savingRecordVoList);
        }
        // 해당 userId로 가입된 사용자가 존재하지 않는 경우
        else return DefaultRes.response(HttpStatus.OK.value(), "조회 실패(사용자 정보 없음)");
    }

    private DefaultRes<List<SimpleProductResponse>> getListDefaultRes(Optional<User> user) {
        User existingUser = user.get();
        List<Product> productList = productRepository.findByUserIdAndDeletedYn(existingUser.getId(), false);

        if(productList.isEmpty()){
            return DefaultRes.response(HttpStatus.OK.value(), "데이터 없음");
        }
        else{
            List<SimpleProductResponse> SimpleProductResponseList = productList.stream()
                    .map(product -> new SimpleProductResponse(product.getId(), product.getName(), product.getPrice(), product.getResolution(), savingRecordRepositoryCustom.isChecked(existingUser,LocalDate.now(),product), LocalDate.now()))
                    .collect(Collectors.toList());

            return DefaultRes.response(HttpStatus.OK.value(), "조회성공", SimpleProductResponseList);
        }
    }

    public DefaultRes updateProduct(UpdateProductRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());

        if(user.isPresent()){
            Optional<Product> product = productRepository.findById(request.getProductId());

            if(product.isPresent()){
                Product updatedProduct = product.get().updateBoard(request);
                productRepository.save(updatedProduct);

                return DefaultRes.response(HttpStatus.OK.value(), "수정 성공");
            }

            else return DefaultRes.response(HttpStatus.OK.value(), "수정 실패(절약 정보 없음)");
        }

        else return DefaultRes.response(HttpStatus.OK.value(), "수정 실패(사용자 정보 없음)");
    }

    public DefaultRes deleteProduct(Long productId, Long userId) {
        Optional<Product> product = productRepository.findById(productId);

        if(product.isPresent()){
            Product existingProduct = product.get();
            Long existingUserId = existingProduct.getUser().getId();

            if(existingUserId.equals(userId)){
                //productRepository.delete(existingProduct);
                //hard delete -> soft delete로 변경
                existingProduct.delete();
                productRepository.save(existingProduct);

                return DefaultRes.response(HttpStatus.OK.value(), "삭제 성공");
            }

            else return DefaultRes.response(HttpStatus.OK.value(), "삭제 실패(사용자 정보 없음)");
        }
        else return DefaultRes.response(HttpStatus.OK.value(), "삭제 실패(절약 정보 없음)");
    }
}
