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

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @DisplayName("?????? ????????????")
    void createProduct() throws Exception {
        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"userId\": \"35\",\n" +
                                "  \"name\": \"??????\",\n" +
                                "  \"price\": \"5500???\",\n" +
                                "  \"resolution\": \"????????? ????????????\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("?????? ?????? ????????????")
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
    @DisplayName("?????? ????????????")
    void updateProduct() throws Exception {
        mvc.perform(patch("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"userId\": \"35\",\n" +
                                "  \"productId\": \"34\",\n" +
                                "  \"name\": \"?????? ?????????\",\n" +
                                "  \"price\": \"5500???\",\n" +
                                "  \"resolution\": \"????????????\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @Rollback(false)
    @DisplayName("?????? ????????????")
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