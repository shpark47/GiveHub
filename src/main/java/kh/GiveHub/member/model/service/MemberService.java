package kh.GiveHub.member.model.service;

import kh.GiveHub.member.model.vo.Member;
import kh.GiveHub.payment.model.vo.Payment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import kh.GiveHub.member.model.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

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

	public String findIdByEmail(String email) {
		return mapper.findMyId(email);
	}

	public int updateTempPwd(String email, String encodePwd) {
		return mapper.updateTempPwd(email,encodePwd);
	}

	public String findMemNameByEmail(String email) {
		return mapper.findMemNameByEmail(email);
	}

    public ArrayList<Payment> selectDonationList(int no, int type) {
		return mapper.selectDonationList(no, type);
    }

	public int deleteMember(String login) { return mapper.deleteMember(login);}

	public int checkEmail(String email) {
		return mapper.checkEmail(email);
	}

	public int updateRank(HashMap<String, Object> rankMap) {
		return mapper.updateRank(rankMap);
	}

}
