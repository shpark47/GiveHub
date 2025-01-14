package kh.GiveHub.news.controller;

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

	@GetMapping("/admin/newslist")
	public String newsList(Model model) {
		ArrayList<News> list = nService.selectNewsList();
		model.addAttribute("list", list);
		return "newslist";
	}

	@GetMapping("/admin/newsmanage/{nNo}")
	public String newsManage(@PathVariable("nNo") String nNo, Model model) {
		News n = new News();
		if (nNo != null){
			n = nService.selectNews(nNo);
		}
		model.addAttribute("n", n);
		return "newsmanage";
	}
}
