package yapp.bestFriend.controller.api.savingrecord;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import yapp.bestFriend.model.dto.DefaultRes;
import yapp.bestFriend.model.dto.res.SavingRecordDto;
import yapp.bestFriend.model.utils.JwtUtil;
import yapp.bestFriend.model.utils.UserUtil;
import yapp.bestFriend.service.savingRecord.SavingRecordService;
import yapp.bestFriend.service.user.UserDetailsService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                        .resolution("더워도 조금만 참아")
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
}