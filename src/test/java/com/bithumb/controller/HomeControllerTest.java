package com.bithumb.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HomeController.class)
public class HomeControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void Home_test() throws Exception {
        String hello = "Home";

        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    public void Home_dto_test() throws Exception {
        String name = "Hello";
        int amount = 10000;

        mvc.perform(
                get("/home/name")
                        .param("name", name)
                        .param("amount", String.valueOf(amount))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))  // jsonPath => $를 기준으로 필드명을 명시한다.
                .andExpect(jsonPath("$.amount", is(amount)));

    }
}
