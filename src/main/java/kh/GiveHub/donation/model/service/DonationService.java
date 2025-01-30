package kh.GiveHub.donation.model.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import kh.GiveHub.donation.model.mapper.DonationMapper;
import kh.GiveHub.donation.model.vo.Donation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class DonationService {
    private final DonationMapper mapper;

    public int deleteDona(String no) {
        return mapper.deleteDona(no);
    }

    public ArrayList<Donation> selectDonaList(int i) {
        return mapper.selectDonaList(i);
    }

    public ArrayList<Donation> selectCategory(Map<String, Object> map) {
        return mapper.selectCategory(map);
    }

    public Donation selectDonation(int doNo, Integer id) {
        Donation d = mapper.selectDonation(doNo);
        if (d != null && id != null && d.getMemNo()!=id) {
            int result = mapper.updateCount(doNo);
            if (result > 0) {
                d.setDoViews(d.getDoViews() + 1);
            }
        }
        return d;
    }

    public int setContent(int doNo, String content) {
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
        
        return mapper.setContent(doNo, newContent.toString());
	}

	public int insertDonation(Donation d) {
		return mapper.insertDonation(d);
	}


    public ArrayList<Donation> selectNew() {
        return mapper.selectNew();
    }
}
