package kh.GiveHub.image.controller;

import java.io.File;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kh.GiveHub.donation.model.service.DonationService;
import kh.GiveHub.image.model.service.ImageService;
import kh.GiveHub.news.model.service.NewsService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
	private final ImageService iService;
	private final DonationService dService;
	private final NewsService nService;
	
	@PostMapping("/temp")
	@ResponseBody
	public ResponseEntity<String> saveTemp(
		@RequestParam("image") MultipartFile file,
		@RequestParam("imgType") String imgType,
		@RequestParam("imgName") String imgName) {
		String tempname = iService.saveTemp(file, imgName, imgType);
		System.out.println("/temp/"+tempname);
		return ResponseEntity.ok("/temp/"+tempname);
	}
	
	@PostMapping("/delete")
	@ResponseBody
	public boolean deleteTemp(
			@RequestParam("tempFiles") List<String> list) {
		int length = list.size();
		System.out.println(length);
		System.out.println(list);
		int i = 0;
		for(String name : list) {
			File tempFile = new File("C:/GiveHub"+name);
			if (tempFile.exists()) {
				tempFile.delete();
				i++;
			}
		}
		System.out.println(i);
		return length==i? true:false;
	}
	
	@PostMapping("/upload")
	@ResponseBody
	public boolean saveUpload(
			@RequestParam("uploadFiles") List<String> list,
			@RequestParam("bid") int bid, 
			@RequestParam("boardType") String boardType,
			@RequestParam("content") String content) {
		boolean isUploaded = iService.saveUpload(list, bid, boardType);
		System.out.println(content);
		System.out.println(boardType);
		System.out.println(bid);
		System.out.println(isUploaded);
		int result = 0;
		if (isUploaded) {
			if(boardType.equals("donation")) {
				result = dService.setContent(bid, content);
			}else {
				result = nService.setContent(bid, content);
			}
		}
		if(result>0) {
			return true;
		}
		return false;
	}
}
