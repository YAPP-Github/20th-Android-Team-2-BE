package yapp.bestFriend.controller.api.savingrecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import yapp.bestFriend.controller.api.v1.savingrecord.SavingRecordController;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.request.CheckProductRequest;
import yapp.bestFriend.model.dto.res.SavingRecordDto;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.Role;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.service.v1.savingRecord.SavingRecordService;
import yapp.bestFriend.service.v1.user.UserDetailsService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SavingRecordController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class savingRecordControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SavingRecordService savingRecordService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserUtil userUtil;

    @Test
    @DisplayName("절약 기록 조회 API Test")
    void getSavingListTest() throws Exception {
        //given
        Long userId = 1L;
        String recordMM = "220531";

        List<SavingRecordDto> serviceResult = Arrays.asList(
                SavingRecordDto.builder()
                        .recordYmd(LocalDate.now())
                        .productId(1L)
                        .name("빙수")
                        .price("8000")
                        .build());

        DefaultRes result = new DefaultRes<>(HttpStatus.OK.value(), "조회성공", serviceResult);

        //when
        try (MockedStatic<UserUtil> mockedStatic = mockStatic(UserUtil.class)) {
            mockedStatic.when(()-> UserUtil.getId()).thenReturn(userId);

        when(savingRecordService.getSavingList(userId,recordMM)).thenReturn(result);

        //then
        mvc.perform(get("/api/savingRecords")
                        .param("recordMM", recordMM))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", CoreMatchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("조회성공")));

        }
    }

    @Test
    @DisplayName("기록 체크 Test")
    void checkProductTest() throws Exception {
        //given
        User createdUser = User.builder()
                .email("test@naver.com")
                .password("123456")
                .nickName("best friend")
                .role(Role.USER)
                .userConnection(null)
                .build();

        Product createdProduct = Product.builder()
                .name("빙수")
                .price("8000")
                .build();

        //1. 오늘 체크한다.
        mvc.perform(post("/api/savingRecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .registerModule(new JavaTimeModule())
                                .writeValueAsString(CheckProductRequest.builder()
                                        .userId(createdUser.getId())
                                        .productId(createdProduct.getId())
                                        .today(LocalDate.now())
                                        .build())))
                .andExpect(status().isOk())
                .andDo(print());

        //2. 미래 체크한다.
        //3. 오늘 조회한다.
    }
}