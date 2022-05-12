package yapp.bestFriend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * [ WebMvcTest ]
 *  - MVC를 위한 테스트. 컨트롤러가 예상대로 동작하는지 테스트하는데 사용됨.
 *  - 아래 어노테이션만 사용하도록 제한하여 보다 가벼운 테스팅이 가능함
 *    - @Controller, @ControllerAdvice, @JsonComponent, Converter, GenericConverter, Filter, HandlerInterceptor, WebMvcConfigurer, HandlerMethodArgumentResolver
 *  - MockBean, MockMVC를 자동 구성하여 테스트 가능하도록 한다.
 *  - Spring Security의 테스트도 지원한다.
 *  - @WebMvcTest를 사용하기 위해 테스트할 특정 컨트롤러 클래스를 명시하도록 한다
 */
@WebMvcTest
class SampleControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("샘플 응답 Test")
    public void sample_response_test() throws Exception {
        mvc.perform(get("/sample")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo("정상")))
                .andDo(print())
                .andExpect(status().isOk());
    }
}