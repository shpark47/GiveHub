package kh.GiveHub.member.model.mapper;

import java.util.ArrayList;

import kh.GiveHub.payment.model.vo.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kh.GiveHub.member.model.vo.Member;

@Mapper 
public interface MemberMapper {

    ArrayList<Member> selectMemberList();

	Member login(Member m);

    Member selectNo(int no);

    int adminMemberUpdate(Member m);

	int adminMemberDelete(Member m);

    int checkId(String id);

    int memberJoin(Member m);

	int checkIdDuplication(String email);

	int editMemberInfo(Member m);

	String findMyId(String email);

	int updateTempPwd(@Param("email") String email,@Param("encodePwd") String encodePwd);

	String findMemNameByEmail(String email);

    ArrayList<Payment> selectDonationList(@Param("no") int no, @Param("type") int type);

    int deleteMember(String login);
}
