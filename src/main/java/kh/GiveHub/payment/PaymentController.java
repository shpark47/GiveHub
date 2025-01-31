package kh.GiveHub.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import kh.GiveHub.member.model.exception.MemberException;
import kh.GiveHub.payment.model.service.PaymentService;
//import kh.GiveHub.payment.model.service.PaymentService;
import kh.GiveHub.payment.model.vo.Payment;
import kh.GiveHub.payment.model.vo.PaymentRequest;
//import kh.GiveHub.payment.model.vo.PaymentRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/pay")
public class PaymentController {
	
	private final PaymentService pService;
	
    @PostMapping("/payment/complete")
    public ResponseEntity<String> paymentComplete(@RequestBody PaymentRequest request) {
        // 결제 완료 후 처리
    	Payment payment = new Payment();
    	
//    	System.out.println(request.getAmount());
//    	System.out.println(request.getName());
//    	System.out.println(request.getDoNo());
//    	System.out.println("멤버 번호는 ? = = = " + request.getMemNo());
    	
    	payment.setAmount(request.getAmount());
    	payment.setDonorName(request.getName());
    	payment.setDonationNo(request.getDoNo());
    	payment.setMemNo(request.getMemNo());
    	payment.setSuccess(true);
    	
    	int result = pService.savePayment(request);
    	
    	System.out.println(result);
    	
    	if(result>0) {
    		return ResponseEntity.ok("결제 완료");
    		
    	}else {
    		throw new MemberException("결제 실패");
    	}

        // 여기에서 결제 정보를 서버에서 처리하고 저장할 수 있습니다.
        // 예를 들어 결제 완료 상태를 DB에 저장하거나, 알림을 보낼 수 있습니다.

    }
}
