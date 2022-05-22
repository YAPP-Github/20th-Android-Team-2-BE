package yapp.bestFriend.controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import yapp.bestFriend.service.ProductService;
import yapp.bestFriend.service.user.UserDetailsService;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @Rollback(false)
    @DisplayName("절약 등록하기")
    void createProduct() throws Exception {
        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"userId\": \"35\",\n" +
                                "  \"name\": \"커피\",\n" +
                                "  \"price\": \"5500원\",\n" +
                                "  \"resolution\": \"절약과 친해지자\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("절약 목록 조회하기")
    void getProductList() throws Exception {
        Long userId = 65L;
        mvc.perform(get("/api/products/" + userId))
                .andDo(print())
                .andExpect(status().isOk());
    }
}