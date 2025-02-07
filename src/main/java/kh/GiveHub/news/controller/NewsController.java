package kh.GiveHub.news.controller;

import java.util.ArrayList;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import jakarta.servlet.http.HttpSession;
import kh.GiveHub.donation.model.exception.DonationException;
import kh.GiveHub.donation.model.service.DonationService;
import kh.GiveHub.donation.model.vo.Donation;
import kh.GiveHub.member.model.exception.MemberException;
import kh.GiveHub.member.model.vo.Member;
import kh.GiveHub.news.model.exception.NewsException;
import kh.GiveHub.news.model.service.NewsService;
import kh.GiveHub.news.model.vo.News;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NewsController {
	private final NewsService nService;
	private final DonationService dService;
	
		// 관리자 소식관리 게시판
		@GetMapping("/admin/newsList")
		public String newsList(Model model) {
			ArrayList<News> list = nService.selectNewsList();
			model.addAttribute("list", list);
			return "/admin/newsList";
		}
	
		// 관리자 소식 상세 페이지(삭제)
		@GetMapping("/admin/newsDelete/{nNo}")
		public String newsManage(@PathVariable("nNo") String nNo) {
			int result = nService.deleteNews(nNo);
			if (result > 0) {
				return "redirect:/admin/newsList";
			}
			throw new MemberException("실패");
		}
	
		// 사용자 소식 게시판
		@GetMapping("/news/newsList")
		public String nnewsList(Model model) {
			ArrayList<News> nlist = nService.nnewsList(0);
			model.addAttribute("nlist", nlist);
			System.out.println("==========nList==========");
			for (News n : nlist) {
				System.out.println(n.getNewsNo()+" : "+ n.getThumbnailPath());
			}
			System.out.println("============================");
			return "/news/newsList";
		}
	
		// 디테일 페이지로 들어가기
		@GetMapping("/news/newsdetail/{newsNo}")
		public String newsDetail(Model model, @PathVariable("newsNo") int newsNo) {
			News n = nService.newsDetail(newsNo);
			System.out.println(n);
			model.addAttribute("n", n);
			return "/news/newsdetail/" + newsNo;
	  }
		
		//뉴스 작성 (윤정호)
		@GetMapping("/news/write/{doNo}")
		public String newsWrite(@PathVariable("doNo") int doNo, Model model,
				HttpSession session) {
			Member loginUser = (Member) session.getAttribute("loginUser");
			Donation d = dService.selectDonation(doNo, loginUser.getMemNo());
			model.addAttribute("d", d);
			return "/news/newsWrite";
		}
	
	//뉴스 작성 (윤정호)
	@PostMapping("/news/insert")
	@ResponseBody
	public ResponseEntity<Integer> insertNews(@ModelAttribute News n,
			HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		n.setMemNo(loginUser.getMemNo());
		n.setMemName(loginUser.getMemName());
		int result = nService.insertNews(n);
		System.out.println(n.getNewsNo());
		if (result>0) {
			return ResponseEntity.ok(n.getNewsNo());
		}else {
			throw new NewsException("failed : insert news to db");
		}
	}
	//뉴스 수정 페이지로 이동(윤정호)
	@GetMapping("/news/edit/{newsNo}")
	public String toEdit(@PathVariable("newsNo") int newsNo,
			Model model, HttpSession session
			) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		News n = nService.selectNews(newsNo);
		model.addAttribute("n",n);
		return "/news/newsEdit";
	}
	//뉴스 db에 업데이트(윤정호)
	@PostMapping("/news/update")
	@ResponseBody
	public ResponseEntity<Integer> updateNews(@ModelAttribute News n,
			HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		int result = 0;
		if (loginUser != null && loginUser.getMemNo()==n.getMemNo()) {
			result = nService.updateNews(n);
			if (result==1) {
				return ResponseEntity.ok(result);
			}else {
				throw new NewsException("failed : update news - result != 1");
			}
		}else {
			throw new NewsException("failed : update news - memNo != loginUser");
		}
	}






































	@GetMapping("/donation/newNews")
	@ResponseBody
	public String selectNewsNew(HttpServletResponse response) {
		ArrayList<News> list = nService.selectNewsNew();
		JSONArray array = new JSONArray();

		for (News n : list) {
			JSONObject news = new JSONObject();
			news.put("doCategory", n.getDoCategory());
			news.put("newsTitle", n.getNewsTitle());
			news.put("newsNo", n.getNewsNo());
			news.put("thumbnailPath", n.getThumbnailPath());
			news.put("newsCreateDate", n.getNewsCreateDate()); // 추가
			news.put("memName", n.getMemName()); // 추가

			array.put(news);
		}

		response.setContentType("application/json; charset=UTF-8");
		return array.toString();
	}

}
