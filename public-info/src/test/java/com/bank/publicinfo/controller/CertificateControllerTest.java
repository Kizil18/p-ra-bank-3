package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.service.CertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = CertificateController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CertificateControllerTest {
    @MockBean
    private CertificateService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private CertificateDto certificateDto;
    private CertificateDto secondCertificateDto;
    private final Byte[] byt = {0, 1, 2};
    private final BankDetailsDto bankDetailsDto = new BankDetailsDto
            (1L, 2L, 3L, 4L, BigDecimal.TEN, "city", "company"
                    , "name");

    @BeforeEach
    void init() {
        certificateDto = CertificateDto.builder()
                .id(1L).photoCertificate(byt)
                .bankDetails(bankDetailsDto).build();
        secondCertificateDto = CertificateDto.builder()
                .id(2L).photoCertificate(byt)
                .bankDetails(bankDetailsDto).build();
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Long id = 1L;
        Mockito.when(service.findById(id)).thenReturn(certificateDto);
        ResultActions response = mockMvc.perform(get("/certificate/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    @DisplayName("чтение по id равному null, негативный сценарий")
    void readByNonExpectIdNegativeTest() throws Exception {
        when(service.findById(ArgumentMatchers.anyLong())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/certificate/")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("чтение по нескольким id, позитивный сценарий")
    void readAllByIdPositiveTest() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        List<CertificateDto> dtoList = List.of(certificateDto, secondCertificateDto);
        when(service.findAllById(ids)).thenReturn(dtoList);
        ResultActions response = mockMvc.perform(get("/certificate/read/all")
                .contentType(MediaType.APPLICATION_JSON)
                .param("ids", "1,2"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    @DisplayName("чтение по нескольким id равным null, негативный сценарий")
    void readAllByNonExpectIdNegativeTest() throws Exception {
        when(service.findAllById(ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/certificate/read/all")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @DisplayName("создание, позитивный сценарий")
    void createPositiveTest() throws Exception {
        given(service.create(ArgumentMatchers.any())).willAnswer(inv ->
                inv.getArgument(0));
        ResultActions response = mockMvc.perform(post("/certificate/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    @DisplayName("создание, негативный сценарий")
    void createNegativeTest() throws Exception {
        when(service.create(ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(post("/certificate/create")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("обновление, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        Long id = 1L;
        when(service.update(id, certificateDto)).thenReturn(certificateDto);
        ResultActions response = mockMvc.perform(put("/certificate/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    @DisplayName("обновление, негативный сценарий")
    void updateNegativeTest() throws Exception {
        when(service.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(put("/certificate/update/1")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}