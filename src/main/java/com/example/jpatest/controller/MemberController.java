package com.example.jpatest.controller;

import com.example.jpatest.dto.MemberDTO;
import com.example.jpatest.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("members")
    public ResponseEntity memberList() {
        List<MemberDTO> list = memberService.getList();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("members")
    public ResponseEntity insertMember(@RequestBody MemberDTO memberDTO) {
        log.info("insertMember dto : {}" , memberDTO);      // id는 null값으로 찍힘
        int result = memberService.insertMember(memberDTO);
        log.info("inserMember result : {}" , result);       // @GeneratedValue에 의해 자동으로 값 부여됨
        return ResponseEntity.status(HttpStatus.CREATED).body("멤버 추가 성공");
    }

    // 로그인
    @PostMapping("members/login")
    public ResponseEntity login(@RequestBody Map<String, String> map) {
        log.debug("login map : {}" , map);
        int result = memberService.login(map.get("userId"), map.get("password"));

        if ( result == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("로그인 성공");
        }
        else if ( result == -1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 틀림");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 id");
    }


    // 페이지 처리
    @GetMapping("list")
    public ResponseEntity list(@RequestParam(defaultValue = "0") int start,
                               @RequestParam(defaultValue = "4") int page) {
        log.debug("list start : {}" , start);
        log.debug("list page : {}" , page);
        List<MemberDTO> list = memberService.getListPage(start, page);
        return ResponseEntity.ok().body(list);
    }


    // 특정 데이터 조회
    @GetMapping("/api/content/{id}")
    public ResponseEntity getContent(@PathVariable long id) {
        MemberDTO memberDTO = memberService.getContent(id);
        if (memberDTO != null)
            return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("데이터 조회 실패");
    }

    // 특정 데이터 수정
    @PutMapping("/api/content/{id}")
    public ResponseEntity updateContent(@PathVariable("id") long id,
                                    @RequestBody MemberDTO memberDTO) {
        int result = memberService.updateContent(id, memberDTO);
        if (result == 1)
            return ResponseEntity.status(HttpStatus.OK).body("수정 성공");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("수정 실패");
    }

    // 특정 데이터 삭제
    @DeleteMapping("/api/content/{id}")
    public ResponseEntity deleteContent(@PathVariable("id") long id) {
        int result = memberService.deleteContent(id);
        if(result == 1)
            return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제 실패");
    }
}
