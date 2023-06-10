package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.entity.BranchEntity;
import com.bank.publicinfo.service.AtmService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AtmController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AtmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AtmService atmService;
    @Autowired
    private ObjectMapper objectMapper;
    private LocalTime localTime;

    private AtmEntity atmEntity;
    private AtmDto atmDto;
    private AtmDto atmDto2;
    private BranchDto branchDto;

    @BeforeEach
    public void init() {
        localTime = LocalTime.of(10, 20, 30, 123456789);
        branchDto = new BranchDto(1L, "One", 2L, "Two", localTime, localTime);
        atmEntity = AtmEntity.builder().id(1L).address("Riga")
                .startOfWork(localTime)
                .endOfWork(localTime)
                .allHours(true).branch
                        (new BranchEntity(1L, "One", 2L, "Two", localTime, localTime))
                .build();
        atmDto = AtmDto.builder().id(1L).address("Riga")
                .startOfWork(localTime)
                .endOfWork(localTime)
                .allHours(true).branch(branchDto)
                .build();
        atmDto2 = AtmDto.builder().id(2L).address("Moscow")
                .startOfWork(localTime)
                .endOfWork(localTime)
                .allHours(true).branch(branchDto)
                .build();

    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        given(atmService.create(ArgumentMatchers.any())).willAnswer((invocationOnMock ->
                invocationOnMock.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/atm/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atmDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.address", Matchers.is("Riga")));
    }

    @Test
    @DisplayName("создание null, негативный сценарий")
    void createNullNegativeTest() throws Exception {
        when(atmService.create(ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(post("/atm/create")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Long id = 1L;
        when(atmService.findById(id)).thenReturn(atmDto);

        ResultActions response = mockMvc.perform(get("/atm/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atmDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.address", Matchers.is("Riga")));
    }

    @Test
    @DisplayName("чтение по id равному null, негативный сценарий")
    void readByNullIdNegativeTest() throws Exception {
        when(atmService.findById(ArgumentMatchers.anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/atm/")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("обновление, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        Long id = 1L;
        when(atmService.update(id, atmDto)).thenReturn(atmDto);

        ResultActions response = mockMvc.perform(put("/atm/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atmDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.address", Matchers.is("Riga")))
                .andExpect(jsonPath("@.allHours", Matchers.is(true)));
    }

    @Test
    @DisplayName("обновление, негативный сценарий")
    void updateNegativeTest() throws Exception {
        when(atmService.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(put("/atm/update/1")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        List<AtmDto> atmDtoList = List.of(atmDto, atmDto2);
        List<Long> ids = List.of(1L, 2L);
        when(atmService.findAllById(ids)).thenReturn(atmDtoList);

        ResultActions response = mockMvc.perform(get("/atm/read/all")
                .contentType(MediaType.APPLICATION_JSON)
                .param("ids", "1, 2"));


        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    @DisplayName("чтение по нескольким id, негативный сценарий")
    void readAllByNonExpectIdNegativeTest() throws Exception {
        when(atmService.findAllById(ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/atm/read/all")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}