package yapp.bestFriend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CreateProductRequest;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.ProductRepository;
import yapp.bestFriend.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct() {
            //given
            String name = "던힐 아이스큐브";
            String price = "4500원";
            String resolution = "금연과 절약";
            Long userId = 35L;
            User user = new User();

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            CreateProductRequest request = new CreateProductRequest(userId, name, price, resolution);
            Product mockProduct = request.toEntity(userRepository.findById(userId).get());

            //when
            given(productRepository.save(any())).willReturn(mockProduct);
            DefaultRes defaultRes = productService.createProduct(request);

            //then
            assertThat(defaultRes.getMessage()).isEqualTo("등록 성공");
    }
}