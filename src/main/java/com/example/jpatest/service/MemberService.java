package com.example.jpatest.service;

import com.example.jpatest.domain.MemberEntity;
import com.example.jpatest.dto.MemberDTO;
import com.example.jpatest.repo.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {

    @Autowired
    private final MemberRepository repo;

    private final HttpSession httpSession;


    public List<MemberDTO> getList() {
        List<MemberDTO> list = null;
        List<MemberEntity> listE = repo.findAll();
        if(listE.size() != 0) {
            list = listE.stream().map( m-> new MemberDTO(m)).toList();
        }
        log.info("getList list : {}" , list);
        log.info("list entity : {}" , listE);
        return list;
    }

    public int insertMember(MemberDTO memberDTO) {
        int result = 1;
        try {
            MemberEntity entity = repo.save(new MemberEntity(memberDTO));
            log.info("insertMember entity : {}" , entity);
        } catch (Exception e) {
            result = 0;
            e.printStackTrace();
        }
        return result;
    }

    //로그인
    public int login(String userId, String password) {
        Optional<MemberEntity> opM = repo.findByUserId(userId);
        log.info("login opM : {}" , opM);

        if(opM.isEmpty()) {
            return 0;       // 존재하지 않는 아이디. 값이 없으면 여기서 끝
        }

        MemberEntity member = opM.get();    // 이 시점에서는 값이 무조건 있음
        log.info("login opM.get() : {}" , opM.get());

        if(!member.getPassword().equals(password)) {
            return -1;
            // 비밀번호 틀림
        }

        httpSession.setAttribute("loginMember", new MemberDTO(member));
        return 1;
    }


    // 페이지 처리
    public List<MemberDTO> getListPage(int start, int page) {

        Pageable pageable = PageRequest.of(start, page, Sort.by(Sort.Order.desc("id")));    // id 역순으로 정렬
        Page<MemberEntity> pageEntity = repo.findAll(pageable);

        List<MemberEntity> listE = pageEntity.getContent();
        log.debug("getListPage listE : {}" , listE);

        List<MemberDTO> list = listE.stream().map( m -> new MemberDTO(m)).toList();
        return list;
    }


    // 특정 데이터 조회
    public MemberDTO getContent(long id) {
        MemberEntity entity = repo.findByContent(id);
        log.debug("getContent entity : {}" , entity);
        if(entity != null)
            return new MemberDTO(entity);
        return null;
    }


    // 특정 데이터 수정
    public int updateContent(long id, MemberDTO memberDTO) {
        int result = 0;
        Optional<MemberEntity> entity = repo.findById(id);

        try {
            if(entity.isPresent()) {
                result = repo.updateContent(memberDTO.getUserId(),
                        memberDTO.getUserName(),
                        memberDTO.getPassword(),
                        memberDTO.getRole(),
                        id);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 특정 데이터 삭제
    public int deleteContent(long id) {
        int result = 1;
        Optional<MemberEntity> entity = repo.findById(id);
        log.debug("deleteContent reop.findByID(id) entity : {}" , entity);
        if(entity.isPresent()) {
            repo.deleteById(id);
            return 1;
        }
        return 0;

    }
}
