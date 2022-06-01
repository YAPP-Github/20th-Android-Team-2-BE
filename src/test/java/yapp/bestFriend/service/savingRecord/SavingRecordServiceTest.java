package yapp.bestFriend.service.savingRecord;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import yapp.bestFriend.model.dto.res.SavingRecordDto;
import yapp.bestFriend.model.entity.Product;
import yapp.bestFriend.model.entity.SavingRecord;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.SavingRecordRepositoryCustom;
import yapp.bestFriend.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SavingRecordServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SavingRecordRepositoryCustom savingRecordRepositoryCustom;

    @InjectMocks
    private SavingRecordService savingRecordService;

    private static List<SavingRecordDto> result;
    private static User user;
    private static String recordMM = "202206";

    @BeforeAll
    static void BeforeAll(){
        //given 1 - user 정보
        String nickName = "test1";
        user = User.builder()
                .nickName(nickName)
                .email("test@naver.com")
                .password("123456")
                .build();

        //given 2 - product 정보
        Product prodInput = Product.builder().user(user)
                .name("빙수")
                .price("8000")
                .resolution("더워도 참아")
                .build();

        //given 3 - user product 정보 update
        user.setSavingRecord(Arrays.asList(
                SavingRecord.builder()
                        .product(prodInput)
                        .user(user)
                        .recordYmd(LocalDate.now())
                        .build()
        ));

        result = new ArrayList<>();
        for(SavingRecord savingRecord:user.getSavingList()){
            result.add(SavingRecordDto.builder()
                    .recordYmd(savingRecord.getRecordYmd())
                    .productId(savingRecord.getProduct().getId())
                    .name(savingRecord.getProduct().getName())
                    .price(savingRecord.getProduct().getPrice())
                    .resolution(savingRecord.getProduct().getResolution())
                    .build()
            );
        }
    }

    @Test
    @DisplayName("절약 기록 조회 API")
    void getSavingList(){
        //when
        doReturn(result).when(this.savingRecordRepositoryCustom)
                        .findByUserId(user, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        //then
        assertThat(savingRecordService.getSavingList(user.getId(),recordMM))
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("Message","조회성공")
                .hasFieldOrPropertyWithValue("data",result);
    }

    @Test
    @DisplayName("절약 기록 조회 API - 잘못된 기록일자 파라미터")
    void wrongRecordYmd(){
        //when
        doReturn(result).when(this.savingRecordRepositoryCustom)
                .findByUserId(user, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        //then
        assertThat(savingRecordService.getSavingList(user.getId(),recordMM+"1"))
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("Message","조회실패(기록일자 파라미터 오류)");
    }
}