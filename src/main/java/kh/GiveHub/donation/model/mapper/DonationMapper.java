package kh.GiveHub.donation.model.mapper;

import java.util.ArrayList;

import kh.GiveHub.donation.model.vo.Donation;
import org.apache.ibatis.annotations.Mapper;

import kh.GiveHub.donation.model.vo.Donation;
import java.util.ArrayList;

@Mapper
public interface DonationMapper {

	ArrayList<Donation> donationlist(String category);

    int deleteDona(String no);

    ArrayList<Donation> selectDonaList();
}
