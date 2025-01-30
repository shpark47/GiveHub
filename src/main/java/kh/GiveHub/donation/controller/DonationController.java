package kh.GiveHub.donation.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import kh.GiveHub.news.model.service.NewsService;
import kh.GiveHub.news.model.vo.News;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import kh.GiveHub.donation.model.exception.DonationException;
import kh.GiveHub.donation.model.service.DonationService;
import kh.GiveHub.donation.model.vo.Donation;
import kh.GiveHub.member.model.exception.MemberException;
import kh.GiveHub.member.model.vo.Member;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DonationController {

	private final DonationService dService;
	private final NewsService nService;


	@GetMapping("/admin/donaList")
	public String newsList (Model model){
		ArrayList<Donation> list = dService.selectDonaList(0);
		model.addAttribute("list", list);
		return "/admin/donaList";
	}

	@GetMapping("/admin/donaDelete/{no}")
	public String deleteDona (@PathVariable("no") String no){
		int result = dService.deleteDona(no);
		if (result > 0) {
			return "redirect:/admin/donaList";
		} else {
			throw new MemberException("실패");
		}
	}

	@GetMapping("payment")
	public String paymentPage (HttpSession session , Model model , @RequestParam("doNo") String doNo) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		model.addAttribute("memName",loginUser.getMemName());
		model.addAttribute("doNo",doNo);
		return "page/paymentPage";
	}

	@GetMapping("/donation/donationWrite")
	public String donationWrite () {
		return "donation/donationWrite";
	}


	@GetMapping("/donation/donationlist")
	public String donationList(Model model) {
		return "donation/donationlist";
	}

	@GetMapping("/category")
	@ResponseBody
	public ArrayList<Donation> category (@RequestParam("categorySelect") String categorySelect, @RequestParam("searchItem") String searchItem, @RequestParam("searchInput") String searchInput, @RequestParam("optionSelect") String optionSelect){
		System.out.println(categorySelect);
		Map<String, Object> map = new HashMap<>();
		map.put("categorySelect", categorySelect);
		map.put("optionSelect", optionSelect);
		Donation d = new Donation();
		if (!searchInput.equals("")) {
			if (searchItem.equals("doTitle")){
				d.setDoTitle(searchInput);
			}else{
				d.setMemName(searchInput);
			}
		}
		map.put("d", d);
		System.out.println(map);

		ArrayList<Donation> list = dService.selectCategory(map);
		System.out.println(list);

		return list;
	}

	@PostMapping("/donation/insert")
	@ResponseBody
	public ResponseEntity<Integer> insertDonation(@ModelAttribute Donation d, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		d.setMemNo(loginUser.getMemNo());
		d.setMemName(loginUser.getMemName());
		int result = dService.insertDonation(d);
		if (result>0) {
			System.out.println(d.getDoNo());
			return ResponseEntity.ok(d.getDoNo());
		}else {
			throw new DonationException("failed : insert donation");
		}
	}

	//기부페이지 상세보기
	@GetMapping("/donation/donationdetail/{doNo}")
	public ModelAndView selectDona(@PathVariable("doNo") int doNo,HttpSession session, ModelAndView mv , Model model) {
		// 글 상세조회 + 조회수 수정(내가 내 글 조회 or 비회원 조회 -> 조회수 올라가지 않음)
		ArrayList<News> list = nService.selectNewsList();
		model.addAttribute("list", list);

		Member loginUser = (Member)session.getAttribute("loginUser");
		Integer id = null;
		if(loginUser != null) {
			id = loginUser.getMemNo();
		}

		//doNo, memId 를 서비스에 넘겨서 글쓴이 비교 로직 작성
		Donation d = dService.selectDonation(doNo, id);
		System.out.println(d);
		long dates = d.getDoEndDate().getTime() - Date.valueOf(LocalDate.now()).getTime();
		long date = dates / (1000 * 60 * 60 * 24);
		//게시글이 존재하면, 게시글 데이터(b)를 donationdetail.html로 전달
		//게시글이 존재하지 않으면 사용자 정의 예외 발생
		if(d != null) {
			mv.addObject("d", d).addObject("date", date).setViewName("/donation/donationdetail");
			
			return mv;
		}else {
			throw new MemberException("게시글 상세보기를 실패하셨습니다.");
		}
	}

	@GetMapping("/donation/new")
	@ResponseBody
	public String selectNew(HttpServletResponse response) {
		System.out.println("테스트");
		ArrayList<Donation> list = dService.selectNew();
		System.out.println(list);
		JSONArray array = new JSONArray();

		for (Donation d : list) {
			JSONObject json = new JSONObject();
			json.put("doCategory", d.getDoCategory());
			json.put("doTitle", d.getDoTitle());
			json.put("doNo", d.getDoNo()); //
			json.put("thumbnailPath", d.getThumbnailPath());

			array.put(json);
		}

		response.setContentType("application/json; charset=UTF-8");
		return array.toString();
	}
}