package com.sevendwarfs.sms.controller.http;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sevendwarfs.sms.controller.http.dto.response.ChartResponseDto;
import com.sevendwarfs.sms.controller.http.dto.response.OddListResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChartControllerTest {

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  final String domain = "/charts";

  @Test
  void 금일_차트조회_응답_테스트() throws Exception {

    String url = domain + "/today_abbreviation";

    String result = mockMvc.perform(get(url))
        .andExpect(
            status().isOk()
        )
        .andReturn().getResponse().getContentAsString();

    ChartResponseDto mock = ChartResponseDto.mock();
    ChartResponseDto response = objectMapper.readValue(result, ChartResponseDto.class);

    assertTrue(mock.equals(response));
  }

  @Test
  void 이상발화및행동_응답_테스트() throws Exception {
    String url = domain + "/odd";

    String result = mockMvc.perform(get(url))
        .andExpect(
            status().isOk()
        )
        .andReturn().getResponse().getContentAsString();

    OddListResponseDto mock = OddListResponseDto.mock();
    OddListResponseDto response = objectMapper.readValue(result, OddListResponseDto.class);

    assertTrue(mock.equals(response));
  }
}