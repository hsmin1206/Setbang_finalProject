package com.setbang.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.setbang.domain.CardVO;
import com.setbang.domain.PlanVO;
import com.setbang.service.CardService;
import com.setbang.service.MemberService;
import com.setbang.service.PlanService;


@Controller
public class PlanController {
	private static final Logger logger = LoggerFactory.getLogger(PlanController.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private PlanService planService;
	
	
	// 마이페이지 서비스 플랜 자동결제 취소
	@RequestMapping(value = "cancelAutoPlanPayment.do", method = RequestMethod.POST)
	public String cancelAutoPlanPayment(HttpSession session, Model model)  {
		 String sessionId = (String) session.getAttribute("sessionId");
		 if (sessionId != null) {
		 int memCode = memberService.getMemCodeBySessionId(sessionId);
		 if (memCode != 0) {
             // 등록된 카드 불러오기
             CardVO card = new CardVO();
             card.setMem_code(memCode);
             List<CardVO> cardList = cardService.getCardList(card);
             int cardCode = cardList.get(0).getCard_code();

             // 자동결제 취소 insert, delete
             planService.insertCancelAutoPlanPayment(cardCode);
             planService.deleteCancelAutoPlanPayment(cardCode);
			 }
			return "redirect:/myPagePlanPayment.do";
		}
		return "redirect:/loginPage.do";
	}
	
	// 마이페이지 서비스 플랜 결제 내역
    @RequestMapping(value = "myPagePlanPayment.do", method = RequestMethod.GET)
    public String myPagePlanPayment(HttpSession session, Model model) {
    	
        String sessionId = (String) session.getAttribute("sessionId");
        if (sessionId != null) {
            int memCode = memberService.getMemCodeBySessionId(sessionId);
            if (memCode != 0) {
                // 등록된 카드 불러오기
                CardVO card = new CardVO();
                card.setMem_code(memCode);
                List<CardVO> cardList = cardService.getCardList(card);
                model.addAttribute("cardList", cardList);
                
                // 결제된 서비스 플랜 내역 가져오기
                List<PlanVO> planPaymentList = planService.getPlanPaymentList(memCode);
                model.addAttribute("planPaymentList", planPaymentList);
                
                return "/myPage/myPagePlanPayment";
            }
    	}
    	return "redirect:/loginPage.do";
    }
	
	
	// 서비스 플랜 업그레이드
	@RequestMapping(value = "planUpgrade.do", method = RequestMethod.POST)
	public String planUpgrade(HttpSession session, Model model,
	                          @RequestParam("plan_code") int planCode,
	                          @RequestParam("card_code") int cardCode,
	                          @RequestParam("card_easypw") int cardEasypw) {
	        int currentEasypw = cardService.getEasypwByCardcode(cardCode);
	        
	        if (currentEasypw != 0 && currentEasypw == cardEasypw) {
	            PlanVO plan = new PlanVO();
	            plan.setPlan_code(planCode);
	            plan.setCard_code(cardCode);
	            
	            // 서비스 플랜 업그레이드 시 직전 자동결제 삭제
	            planService.deletePrevPlanAfterPlanUpgrade(plan);
	            // 서비스 플랜 업그레이드
	            planService.planUpgrade(plan);
	            
		        // 세션아이디로 회원플랜등급 다시 가져오기
				String sessionId = (String) session.getAttribute("sessionId");
		        String memPlan = memberService.getMemPlanBySessionId(sessionId);
		        model.addAttribute("memPlan", memPlan);
		        session.setAttribute("sessionMemPlan", memPlan);
	            
		        // 결제 성공시
		        return "redirect:/myPagePlanPayment.do?message=success";
	        }
	        // 결제 실패시
	        return "redirect:/planApply.do?message=failed";
	}
	
	// 서비스 플랜 결제
	@RequestMapping(value = "planPayment.do", method = RequestMethod.POST)
	public String planPayment(HttpSession session, Model model,
	                          @RequestParam("plan_code") int planCode,
	                          @RequestParam("card_code") int cardCode,
	                          @RequestParam("card_easypw") int cardEasypw) {
	    int currentEasypw = cardService.getEasypwByCardcode(cardCode);

	    if (currentEasypw != 0 && currentEasypw == cardEasypw) {
	        PlanVO plan = new PlanVO();
	        plan.setPlan_code(planCode);
	        plan.setCard_code(cardCode);
	        
	        // 서비스 플랜 결제
	        planService.planPayment(plan);
	        
	        // 서비스 플랜 결제 시 등급 변경
	        planService.memPlanUpgrade(plan);
	        
	        // 월간 서비스 플랜 결제 시 다음달 자동결제 (하지만 결제여부는 'N')
	        if (planCode == 1 || planCode == 3) {
	            planService.autoPlanPayment(plan);
	        }
	        
	        // 세션아이디로 회원플랜등급 다시 가져오기
			String sessionId = (String) session.getAttribute("sessionId");
	        String memPlan = memberService.getMemPlanBySessionId(sessionId);
	        model.addAttribute("memPlan", memPlan);
	        session.setAttribute("sessionMemPlan", memPlan);

	        
	        // task - 연간결제는 또 다른 페이지로 보내야함
	        // 결제 성공시
	        return "redirect:/myPagePlanPayment.do?message=success";
	    } else {
	        // 결제 실패시
	        return "redirect:/planApply.do?message=failed";
	    }
	}
	
    // 서비스 플랜 결제 페이지로 이동
    @RequestMapping(value = "planApply.do", method = RequestMethod.GET)
    public String planApply(HttpSession session, Model model) {
    	
        String sessionId = (String) session.getAttribute("sessionId");
        if (sessionId != null) {
            int memCode = memberService.getMemCodeBySessionId(sessionId);
            if (memCode != 0) {
                // 등록된 카드 불러오기
                CardVO card = new CardVO();
                card.setMem_code(memCode);
                List<CardVO> cardList = cardService.getCardList(card);
                model.addAttribute("cardList", cardList);
                
                return "/plan/planApply";
            }
    	}
    	return "redirect:/loginPage.do";
    }
 
		
}
