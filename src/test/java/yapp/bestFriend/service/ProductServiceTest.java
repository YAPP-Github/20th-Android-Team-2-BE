package yapp.bestFriend.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
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

@RunWith(SpringRunner.class)
class ProductServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void createProduct() {
            //given
            String name = "던힐 아이스큐브";
            String price = "4500원";
            String resolution = "금연과 절약";
            Long userId = 35L;


            CreateProductRequest request = new CreateProductRequest(userId, name, price, resolution);
            given(userRepository.findById(userId)).willReturn(Optional.of(userRepository.findById(userId).get()));

            Product mockProduct = request.toEntity(userRepository.findById(userId).get());
            //when
            given(productRepository.save(any())).willReturn(mockProduct);
            DefaultRes defaultRes = productService.createProduct(request);

            //then
            assertThat(defaultRes.getMessage()).isEqualTo("등록성공");
    }
}