package com.sokuri.plog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sokuri.plog.domain.entity.User;
import com.sokuri.plog.global.dto.user.SignUpRequest;
import com.sokuri.plog.global.dto.user.UserCheckRequest;
import com.sokuri.plog.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  ObjectMapper mapper;
  @Autowired
  UserService userService;

  private static final String BASE_URL = "/api/v1/user";
  private static final String USER_EMAIL = "email@email.com";
  private static final String USER_NICKNAME = "nickname";
  private static final String USER_BIRTHDAY = "2022-01-01";
  private static final String USER_PASSWORD = "12341234";
  private static final String USER_PROFILEIMAGE = "https://profile-image.com";

  private static final String NOT_EXIST = "NOT_EXIST";

  private User saveUser;
  private User savedSignInUser;

  private SignUpRequest signUpRequest;
  private UserCheckRequest userInfoRequest;

  @BeforeEach
  void initData() {
    userInfoRequest = new UserCheckRequest(USER_EMAIL, USER_NICKNAME);
//    saveUser = userService.createUser(userInfoRequest);

    signUpRequest = SignUpRequest.builder()
            .email(USER_EMAIL)
            .nickname(USER_NICKNAME)
            .birthday(LocalDate.parse(USER_BIRTHDAY))
            .password(USER_PASSWORD)
            .imageUrl(USER_PROFILEIMAGE)
            .build();

    savedSignInUser = userService.createUser(signUpRequest);
  }

  @Test
  @DisplayName("이메일 존재여부 확인")
  @WithMockUser
  void isExistUserIdTest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
                    .get(BASE_URL+"/checkMail/{email}", NOT_EXIST)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.isExists").value("false"));

    mockMvc.perform(MockMvcRequestBuilders
                    .get(BASE_URL+"/checkMail/{email}", USER_EMAIL)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.isExists").value("true"));
  }

  @Test
  @DisplayName("닉네임 존재여부 확인")
  @WithMockUser
  void isExistNicknameTest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
                    .get(BASE_URL+"/checkNickname/{nickname}", NOT_EXIST)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.isExists").value("false"));

    mockMvc.perform(MockMvcRequestBuilders
                    .get(BASE_URL+"/checkNickname/{nickname}", USER_NICKNAME)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.isExists").value("true"));
  }

  @Test
  @DisplayName("회원 가입 후 로그인 후 바로 로그아웃")
  void signInTest() throws Exception {
    signUpRequest.setEmail("ee" + USER_EMAIL);
    signUpRequest.setNickname(USER_NICKNAME + "ee");

    MockMultipartFile multipartFile1 = new MockMultipartFile("files", "test.jpeg", "multipart/form-data", "test file".getBytes(StandardCharsets.UTF_8) );
    MockMultipartFile request = new MockMultipartFile("request", "request", "application/json", mapper.writeValueAsString(signUpRequest).getBytes(StandardCharsets.UTF_8));

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/sign-up")
                    .file(multipartFile1)
                    .file(request)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
            .andReturn();

    String token = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/auth/sign-out")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().is3xxRedirection())
            .andExpect(re -> "/".equals(re.getResponse().getRedirectedUrl()));
  }
}
