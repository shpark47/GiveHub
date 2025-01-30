package kh.GiveHub.image.model.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kh.GiveHub.image.model.exception.ImageException;
import kh.GiveHub.image.model.mapper.ImageMapper;
import kh.GiveHub.image.model.vo.Image;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageMapper mapper;

	public String saveTemp(MultipartFile file,
			String imgName, String imgType) {
			
		try {
			String tempPath ="C:/GiveHub/temp/";
			File dir = new File(tempPath);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			int ranNum = (int)(Math.random()*10000);
			String rename = sdf.format(new Date())+ranNum+"_"+imgName;
			if (imgType.equals("0")) {
				rename = "T"+rename;
			}
			
			File destfile = new File(tempPath, rename);
			file.transferTo(destfile);
			return rename;
		} catch (IllegalStateException | IOException e) {
			throw new ImageException("failed :  img transfer to /temp");
		}
	}

	public boolean saveUpload(List<String> list, int bid, String BoardType) {
		String uploadPath ="C:/GiveHub/upload/";
		File dir = new File(uploadPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		for(String name : list) {
			String fileName = name.substring(name.lastIndexOf("/")+1);
			String sourcePath = "C:/GiveHub/temp/"+fileName;
			String destPath = "C:/GiveHub/upload/"+fileName;
	        
			try {
				Files.copy(Paths.get(sourcePath), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
				Image img = new Image();
				img.setImgPath("C:/GiveHub/upload/");
				img.setImgName(fileName.substring(fileName.lastIndexOf("_")+1));
				img.setImgRename(fileName);
				img.setImgType(fileName.startsWith("T")? "0":"1");
				img.setRefNo(bid);
				img.setBoardType(BoardType.equals("donation")? "D":"N");
				mapper.insertImage(img);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
