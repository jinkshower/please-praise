package com.spring.nbcijo.mypage;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.spring.nbcijo.common.ControllerTest;
import com.spring.nbcijo.common.UserFixture;
import com.spring.nbcijo.controller.MyPageController;
import com.spring.nbcijo.service.MyPageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(MyPageController.class)
class MyPageControllerTest extends ControllerTest implements UserFixture {

    @MockBean
    MyPageService myPageService;

    @Test
    @DisplayName("내 정보 조회 성공")
    void getMyInform_success() throws Exception {
        // given
        given(myPageService.getMyInform(eq(TEST_USER))).willReturn(TEST_USER_RESPONSE);

        // when
        var action = mockMvc.perform(get("/my")
            .accept(MediaType.APPLICATION_JSON));

        // then
        action.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value(TEST_USER_NAME))
            .andExpect(jsonPath("$.data.description").value(TEST_USER_DESCRIPTION))
            .andDo(print());
    }
}
