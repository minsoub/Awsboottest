package com.bithumb.dto;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeResponseDtoTest {
    @Test
    public void dto_test() {
        String name = "test";
        int amount = 1000;

        // When
        HomeResponseDto dto = new HomeResponseDto(name, amount);

        // then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }
}
