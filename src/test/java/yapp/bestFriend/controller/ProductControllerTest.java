package yapp.bestFriend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import yapp.bestFriend.controller.api.product.ProductController;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.service.ProductService;
import yapp.bestFriend.service.user.UserDetailsService;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductController.class, includeFilters = {
        // to include JwtUtil in spring context
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtil.class)})
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
//    @Rollback(false)
    @DisplayName("절약 등록하기")
    void createProduct() throws Exception {
        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"userId\": \"35\",\n" +
                                "  \"name\": \"커피\",\n" +
                                "  \"price\": \"5500원\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("항목을 다 입력하지 않았을 때 절약 등록하기")
    void createProductWhenAllItemsHaveNotBeenEntered() throws Exception {
        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"userId\": \"35\",\n" +
                                "  \"name\": \"커피\"\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data", is(nullValue())))
                .andExpect(jsonPath("$.message", is("[price](은)는 must not be blank 입력된 값: [null]")))
                .andDo(print());
    }

    @Test
    @DisplayName("절약 목록 조회하기")
    void getProductList() throws Exception {
        Long userId = 65L;

        //when
        try (MockedStatic<UserUtil> mockedStatic = mockStatic(UserUtil.class)) {
            mockedStatic.when(()-> UserUtil.getId()).thenReturn(userId);

            mvc.perform(get("/api/products/").param("recordYmd","20220605"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("절약 수정하기")
    void updateProduct() throws Exception {
        mvc.perform(patch("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"userId\": \"35\",\n" +
                                "  \"productId\": \"34\",\n" +
                                "  \"name\": \"수정 테스트\",\n" +
                                "  \"price\": \"5500원\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @Rollback(false)
    @DisplayName("절약 삭제하기")
    void deleteProduct() throws Exception {

        //when
        try (MockedStatic<UserUtil> mockedStatic = mockStatic(UserUtil.class)) {
            Long userId = 35L;
            Long productId = 65L;
            mvc.perform(delete("/api/products/" + productId + "?userId=" + userId))
                    .andExpect(status().isOk());
        }
    }

}