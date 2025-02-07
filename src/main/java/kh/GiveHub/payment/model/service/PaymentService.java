package kh.GiveHub.payment.model.service;

import org.springframework.stereotype.Service;

import kh.GiveHub.payment.model.mapper.PaymentMapper;
import kh.GiveHub.payment.model.vo.PaymentRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
	private final PaymentMapper mapper;
	public int savePayment(PaymentRequest request) {
		
		return mapper.savePayment(request);
	}
	public int totalAmount(Integer memNo) {
		return mapper.totalAmount(memNo);
	}

	public void memberJoin(int memNo) {
		mapper.memberJoin(memNo);
	}
}
