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

import kh.GiveHub.common.config.WebMvcConfig;
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
	private String basePath = WebMvcConfig.getBasePath();
	private String tempPath = basePath+"/temp/";
	
	@PostMapping("/temp")
	@ResponseBody
	public ResponseEntity<String> saveTemp(
		@RequestParam("image") MultipartFile file,
		@RequestParam("imgType") String imgType,
		@RequestParam("imgName") String imgName) {
		String temppath = "/temp/"+ iService.saveTemp(file, imgName, imgType);
		System.out.println("temppath : "+temppath);
		return ResponseEntity.ok(temppath);
	}
	
	@PostMapping("/delete")
	@ResponseBody
	public boolean deleteTemp(
			@RequestParam(value="tempFiles", required=false) List<String> list) {
		if (list == null) {
			return true;
		}
		int length = list.size();
		System.out.println("list.size() : "+length);
		System.out.println("list : "+list);
		int delcount = 0;
		//name은 "/temp/" + 파일이름 으로 되어있다.
		for(String name : list) {
			File tempFile = new File(basePath+name);
			if (tempFile.exists()) {
				tempFile.delete();
				delcount++;
			}
		}
		System.out.println("delcount : "+delcount);
		return length==delcount? true:false;
	}
	
	@PostMapping("/upload")
	@ResponseBody
	public boolean saveUpload(
			@RequestParam(value="uploadFiles", required=false) List<String> list,
			@RequestParam("bid") int bid, 
			@RequestParam("boardType") String boardType,
			@RequestParam("content") String content) {
		boolean isUploaded = iService.saveUpload(list, bid, boardType);
		System.out.println("==========saveUpload==========");
		System.out.println("boardType : "+boardType);
		System.out.println("bid : "+bid);
		System.out.println("--content before insert into db --\n"+content+"\n----------");
		System.out.println("isUploaded : "+isUploaded);
		System.out.println("==============================");
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
	
	//updateBoard(){
	//똑같이 iService.saveUpload(list, bid, boardType)로 사진 저장.
	//	- 그러면 리스트에 있는 사진들을 /upload/로 보냄
	//	- 그리고 db에 이미지 테이블에 정보 입력
	// 반환값이 true라면 -> compareContent(bid, boardType,  content) 를 사용
	//	- bid, boardType로 oldContent 가져와서 비교
	//	- oldcontent에서 <img src="/upload/a.png"> matcher로 찾음
	//	- 사진 a, b, c가 나오면 그게 newcontent에 있는지 확인 (a, b는 없고 c, +d, +e 만 있다고 가정)
	//	- a랑 b를 리스트에 담아서 compare() -> delete(list)로 보낸다.
	//	- delete에서 c:/GiveHub/upload 에 있는 a랑 b를 if exists해서 지운다.
	//	- 다 지워지면 db에도 지운 사진 반영 delete(list)-> deleteDB(list).
	//	- 완료되면 updateBoard()<- compare() <- delete() <- deleteDB() 로 돌아옴
	//	- updateBoard() -> setContent(bid, content)
	//	- 여기 컨텐트에는 /upload/ 1개 /temp/ 2개가 있음.
	// 	- pattern, matcher로 StringBuilder newContent 에 ../temp/ 를 /upload/로 바꿔버리고
	//	- db에 저장.
	@PostMapping("/update")
	@ResponseBody
	public boolean update(
			@RequestParam(value = "updateFiles", required=false) List<String> list,
			@RequestParam("bid") int bid,
			@RequestParam("boardType") String boardType,
			@RequestParam("content") String content) {
		boolean isUploaded = true;
		if (list != null) {
			isUploaded = iService.saveUpload(list, bid, boardType);
		}
		System.out.println("==========update()==========");
		System.out.println("boardType : "+boardType);
		System.out.println("bid : "+bid);
		System.out.println("--content before insert into db--\n"+content+"\n----------");
		System.out.println("isUploaded : "+isUploaded);
		//참고
		//oldContent 는 수정 전의 content를 말한다.
		//content는 작성한 내용(내용 안에 img src가  "/temp/"로 시작하는 content).
		//newContent는 content와 사실상 동일한 내용인데 img src가 "/upload/"로 시작.
		if (isUploaded) {
			int result = 0;
			boolean isDeleted = false;
			String oldcontent = null;
			if(boardType.equals("donation")) {
				oldcontent = dService.getOldContent(bid);
				System.out.println("oldcontent ----------\n"+oldcontent+"\n----------");
				List<String> delFiles = iService.compareContent(content, oldcontent);
				System.out.println("delFiles : "+ delFiles);
				isDeleted = iService.deleteImage(delFiles);
				System.out.println("isDeleted : "+ isDeleted);
				if (isDeleted) {
					result = dService.setContent(bid, content);
				}
			}else {
				oldcontent = nService.getOldContent(bid);
				System.out.println("oldcontent ----------\n"+oldcontent+"\n----------");
				List<String> delFiles = iService.compareContent(content, oldcontent);
				System.out.println("delFiles : "+ delFiles);
				isDeleted = iService.deleteImage(delFiles);
				System.out.println("isDeleted : "+ isDeleted);
				if (isDeleted) {
					result = nService.setContent(bid, content);
				}
			}
			System.out.println("setContent() result : "+result);
			System.out.println("==============================");
			if (result == 1) {
				return true;
			}
		}
		return false;
	}
}
