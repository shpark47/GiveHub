package kh.GiveHub.member.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import kh.GiveHub.payment.model.service.PaymentService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpSession;
import kh.GiveHub.member.model.exception.MemberException;
import kh.GiveHub.member.model.service.MemberService;
import kh.GiveHub.member.model.vo.Member;
import kh.GiveHub.payment.model.vo.Payment;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class MemberController {

    private final MemberService mService;
    private final PaymentService pService;

    private final BCryptPasswordEncoder bcrypt;

    //로그인 화면 연결
    @GetMapping("/member/login")
    public String logIn() {
        return "/member/login";
    }

    @PostMapping("/member/login")
    public String login(Member m, Model model, HttpSession session) {
        Member loginUser = mService.login(m);

        if (loginUser != null && bcrypt.matches(m.getMemPwd(), loginUser.getMemPwd())) {
            session.setAttribute("loginUser", loginUser);
            if (loginUser.getMemType().equals("2")) {
                return "redirect:/admin/main";
            }
            return "redirect:/";
        }

        model.addAttribute("loginError", "로그인 실패");

        return "/member/login";
    }

    //로그아웃
    @GetMapping("/member/logout")
    public String logout(SessionStatus session) {
        session.setComplete();
        return "redirect:/";
    }

    // 회원가입
    @GetMapping("/member/join")
    public String Join() {
        return "/member/join";
    }

    @PostMapping("/member/join")
    public String Join(Member m) {
        if (m.getMemType().equals("1")){
            m.setMemConfirm("W");
        }else{
            m.setMemConfirm("Y");

        }
        m.setMemGrade("UNRANK");
        m.setMemPwd(bcrypt.encode(m.getMemPwd()));
        int result = mService.memberJoin(m);
        if (result > 0) {
            pService.memberJoin(m.getMemNo());
            return "/member/join-success";
        }
        System.out.println(bcrypt);
        throw new MemberException("실패");
    }

    @GetMapping("/member/join.id")
    @ResponseBody
    public int checkId(@RequestParam("id") String id) {
        return mService.checkId(id);
    }

    @GetMapping("/member/mypage")
    public String mypage(Model model) {
        int no = ((Member) model.getAttribute("loginUser")).getMemNo();
        model.addAttribute("list", mService.selectDonationList(no, 2));
        return "/member/mypage";
    }

    @GetMapping("/ongoingList")
    @ResponseBody
    public ArrayList<Payment> ongoingList(Model model) {
        int no = ((Member) model.getAttribute("loginUser")).getMemNo();
        return mService.selectDonationList(no, 0);
    }

    @GetMapping("/finishedList")
    @ResponseBody
    public ArrayList<Payment> finishedList(Model model) {
        int no = ((Member) model.getAttribute("loginUser")).getMemNo();
        return mService.selectDonationList(no, 1);
    }

    // 관리자 메인페이지(회원관리 페이지)
    @GetMapping("/admin/main")
    public String adminMain(Model model) {
        if (model.getAttribute("loginUser") != null) {
            ArrayList<Member> list = mService.selectMemberList();
            model.addAttribute("list", list);
            return "/admin/main";
        }
        throw new MemberException("실패");
    }

    @GetMapping("/admin/selectNo")
    @ResponseBody
    public Member selectNo(@RequestParam("no") int no) {
        return mService.selectNo(no);
    }

    @PostMapping("/admin/memberUpdate")
    public String adminMemberUpdate(Member m) {
        if (m.getMemType().equals("0")){
            m.setMemConfirm("Y");
        }
        int result = mService.adminMemberUpdate(m);
        if (result > 0) {
            return "redirect:/admin/main";
        }
        throw new MemberException("실패");
    }

    @PostMapping("/admin/memberDelete")
    public String adminMemberDelete(Member m) {
        int result = mService.adminMemberDelete(m);
        if (result > 0) {
            return "redirect:/admin/main";
        }
        throw new MemberException("실패");
    }

    @GetMapping(value="checkEmail",produces="application/json; charset=UTF-8")
    @ResponseBody
    public int checkIdDuplication(@RequestParam("email") String email) {
        System.out.println(email);
        int result = mService.checkIdDuplication(email);
        System.out.println(result);

        return result;
    }

    @PostMapping("/member/editMyInfo")
    public String editMemberInfo(@ModelAttribute Member m , HttpSession session,Model model) {
        Member loginUser = (Member)session.getAttribute("loginUser");
        m.setMemNo(loginUser.getMemNo());

        if(m.getMemPwd().trim().equals("")) {
            m.setMemPwd(loginUser.getMemPwd());
        }else {
            m.setMemPwd(bcrypt.encode(m.getMemPwd()));
        }
        int result = mService.editMemberInfo(m);

        System.out.println(result);
        if(result>0) {
            model.addAttribute("loginUser",mService.login(m));
            return "redirect:/";
        }else {
            throw new MemberException("회원 정보 수정 중 오류가 남");
        }
    }
    
    @GetMapping("/findmyid")
    public String findmyIdPage() {
    	return "/member/findmyid";
    }
    
    @PostMapping("/member/findMyId")
    public void findMyId(@RequestParam("name") String name) {
    	
    }
    
    @GetMapping("/findmyidsuccess")
    public String findMyIdSuccess(@RequestParam("email") String email , Model model) {
    	String memId = mService.findIdByEmail(email);
    	System.out.println("잘 들어옴" + email);
    	System.out.println(memId);
    	model.addAttribute("memId",memId);
    	
    	return "/member/findmyidsuccess";
    	
    }
    
    @GetMapping("/findpassword")
    public String findpasswordPage() {
    	return "/member/findpassword";
    }
    
    @PostMapping("/temporaryPwd")
    @ResponseBody
    public HashMap<String,String> temporaryPwd(@RequestParam("email") String email) {
//    	System.out.println("들어온 이메일은 " + email);
    	
    	String tempPwd = tempPwdMk();
    	String encodePwd =  bcrypt.encode(tempPwd);
    	
    	
    	int updatePwdCode = mService.updateTempPwd(email,encodePwd);
    	String memName = mService.findMemNameByEmail(email);
    	
    	HashMap<String,String> result = new HashMap<>();
    	
    	result.put("pwd", tempPwd);
    	result.put("memName", memName);
    	
    	System.out.println(updatePwdCode);
    	System.out.println(memName);
    	
    	if(updatePwdCode > 0) {
    		return result;
    	}else {
    		throw new MemberException("오류 발생");
    	}
    }
    
    
    @GetMapping("/findmypasswordsuccess")
    public String findMyPwdSuccess(@RequestParam("pwd") String pwd ,@RequestParam("memName") String memName, Model model) {
    	System.out.println("잘 들어옴" + pwd);
    	System.out.println("아주 잘 들어옴 " + memName);
    	model.addAttribute("memName",memName);
    	model.addAttribute("memPwd",pwd);
    	
    	return "/member/findpasswordsuccess";
    	
    }
    
    
    
    //임시 비번 생성 메소드
    private String tempPwdMk() {
    	int length = 10;
    	String chars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    	StringBuilder password = new StringBuilder();
    	Random random = new Random();
    	
    	for(int i=0;i<length;i++) {
    		password.append(chars.charAt(random.nextInt(chars.length())));
    	}
    	
    	
    	return password.toString();
    	
    }

    @GetMapping("/member/delete")
    public String delete(HttpSession session) {
        int result= mService.deleteMember(((Member)session.getAttribute("loginUser")).getMemId());

        if(result > 0) {
            return "redirect:/member/logout";
        }else {
            throw new MemberException("삭제  실패 ㅋㅋㅋ");
        }

    }
    
    
    @GetMapping("/member/join.email")
    @ResponseBody
    public int checkEmail(@RequestParam("email") String email) {
//    	System.out.println(email);
    	return mService.checkEmail(email);
    }
    
    
    
    
    
    
    
    
    
}
