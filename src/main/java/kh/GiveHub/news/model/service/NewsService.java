package kh.GiveHub.news.model.service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import kh.GiveHub.news.model.mapper.NewsMapper;
import kh.GiveHub.news.model.vo.News;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsMapper mapper;

    public ArrayList<News> selectNewsList() {
        return mapper.selectNewsList();
    }

    public int deleteNews(String nNo) {
        return mapper.deleteNews(nNo);
    }

    public ArrayList<News> nnewsList(int i) {
        return mapper.nnewsList(i);
    }

    public int setContent(int newsNo, String content) {
        StringBuilder newContent = new StringBuilder(content);
        Pattern pattern = Pattern.compile("<img[^>]+?src=\"([^\"]+)\"[^>]*?>");
        Matcher matcher = pattern.matcher(content);
        
        int offset = 0;
        
        while(matcher.find()) {
            String oldPath = matcher.group(1);
            String newPath = oldPath.replace("/temp/", "/upload/");
            
            int startIndex = matcher.start(1) + offset;
            int endIndex = matcher.end(1) + offset;
            
            newContent.replace(startIndex, endIndex, newPath);
            
            offset += newPath.length() - oldPath.length();
        }
        
        return mapper.setContent(newsNo, newContent.toString());
    }

	public int insertNews(News n) {
		return mapper.insertNews(n);
	}

    public News newsDetail(int newsNo) {return mapper.newsDetail(newsNo);}
}