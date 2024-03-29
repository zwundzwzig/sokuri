package com.sokuri.plog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MailController {
//    @Override
//    public String index() {
//        return "index";
//    }
//
//    // 임시 비밀번호 발급
//    @Override
//    public ResponseEntity sendPasswordMail(@RequestBody MailRequestDto dto) {
//        MailMessage emailMessage = MailMessage.builder()
//                .to(dto.getMail())
//                .subject("[So-Cool-It] 임시 비밀번호 발급")
//                .build();
//
//        mailService.sendMail(emailMessage, "password");
//
//        return ResponseEntity.ok().build();
//    }
//
//    // 회원가입 이메일 인증 - 요청 시 body로 인증번호 반환하도록 작성하였음
//    @Override
//    public ResponseEntity sendJoinMail(MailRequestDto dto) {
//        MailMessage emailMessage = MailMessage.builder()
//                .to(dto.getMail())
//                .subject("[So-Cool-It] 이메일 인증을 위한 인증 코드 발송")
//                .build();
//
//        String code = mailService.sendMail(emailMessage, "email");
//
//        MailResponseDto mailResponseDto = new MailResponseDto();
//        mailResponseDto.setCode(code);
//
//        return ResponseEntity.ok(mailResponseDto);
//    }
}
