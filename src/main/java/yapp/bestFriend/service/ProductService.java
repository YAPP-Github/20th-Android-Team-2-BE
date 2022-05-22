package yapp.bestFriend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CreateProductRequest;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public DefaultRes createProduct(CreateProductRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());

        if(user.isPresent()){
            user.get().getProductList().add(productRepository.save(request.toEntity(user.get())));

            return DefaultRes.response(HttpStatus.OK.value(), "등록 성공");
        }
        else return DefaultRes.response(HttpStatus.OK.value(), "등록 실패(사용자 정보 없음)");
    }
}
