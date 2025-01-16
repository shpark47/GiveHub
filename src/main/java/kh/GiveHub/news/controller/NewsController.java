package kh.GiveHub.news.controller;

import kh.GiveHub.member.model.exception.MemberException;
import kh.GiveHub.news.model.vo.News;
import org.springframework.stereotype.Controller;

import kh.GiveHub.news.model.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class NewsController {
	private final NewsService nService;

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
}
