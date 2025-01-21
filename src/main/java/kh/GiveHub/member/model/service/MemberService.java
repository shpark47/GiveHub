package kh.GiveHub.member.model.service;

import kh.GiveHub.member.model.vo.Member;
import org.springframework.stereotype.Service;

import kh.GiveHub.member.model.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberMapper mapper;

    public ArrayList<Member> selectMemberList() {
		return mapper.selectMemberList();
    }

    //로그인
	public Member login(Member m) {
		return mapper.login(m);
	}

	public Member selectNo(int no) {
		return mapper.selectNo(no);
	}

	public int adminMemberUpdate(Member m) {
		return mapper.adminMemberUpdate(m);
	}

	public int adminMemberDelete(Member m) {
		return mapper.adminMemberDelete(m);
	}

	public int checkId(String id) {
		return mapper.checkId(id);
	}

	public int memberJoin(Member m) {
		return mapper.memberJoin(m);
	}

	public int checkIdDuplication(String email) {
		return mapper.checkIdDuplication(email);
		
	}

	public int editMemberInfo(Member m) {
		return mapper.editMemberInfo(m);
	}
}
