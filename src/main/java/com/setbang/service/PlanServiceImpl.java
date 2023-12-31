package com.setbang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.setbang.dao.PlanDAO;
import com.setbang.domain.PlanVO;

@Service("PlanService")
public class PlanServiceImpl implements PlanService {
	
    @Autowired
    private PlanDAO planDAO;
 
    @Override
	// 서비스 플랜 결제
    public void planPayment(PlanVO vo) {
    	planDAO.planPayment(vo);
    }

    @Override
    // 월간 서비스 플랜 결제 시 다음달 자동결제
    public void autoPlanPayment(PlanVO vo) {
    	planDAO.autoPlanPayment(vo);
    }

    @Override
    // 스케줄러 이용 자동결제 업데이트
    public void updateAutoPlanPayment(PlanVO vo) {
    	planDAO.updateAutoPlanPayment(vo);
    }
    
    @Override
    // 스케줄러 이용 월간 서비스 플랜 결제 시 다음달 자동결제
    public void autoPlanPaymentAfterPlanEnd(PlanVO vo) {
    	planDAO.autoPlanPaymentAfterPlanEnd(vo);
    }
    
    @Override
    // 서비스 플랜 업그레이드
    public void planUpgrade(PlanVO vo) {
    	planDAO.planUpgrade(vo);
    }

    @Override
    // 스케줄러 이용 서비스 플랜 기간 시작시 등급 업그레이드
    public void autoMemPlanUpgrade(PlanVO vo) {
    	planDAO.autoMemPlanUpgrade(vo);
    }
    
	@Override
	// 서비스 플랜 업그레이드 시 직전 자동결제 삭제
	public void deletePrevPlanAfterPlanUpgrade(PlanVO vo) {
		planDAO.deletePrevPlanAfterPlanUpgrade(vo);
	}
	
    @Override
    // 결제 시 회원 서비스 플랜 등급 변경
    public void memPlanUpgrade(PlanVO vo) {
    	planDAO.memPlanUpgrade(vo);
    }

    @Override
    // 서비스 플랜 다운그레이드
    public void memPlanDowngrade(PlanVO vo) {
    	planDAO.memPlanDowngrade(vo);
    }

    @Override
    // 자동결제 취소 - 월간 서비스 플랜 다음달 결제내역 삭제
    public void insertCancelAutoPlanPayment(int cardCode) {
    	planDAO.insertCancelAutoPlanPayment(cardCode);
    }
    
    @Override
	// 자동결제 취소 - 월간 서비스 플랜 다음달 결제내역 삭제
    public void deleteCancelAutoPlanPayment(int cardCode) {
    	planDAO.deleteCancelAutoPlanPayment(cardCode);
    }

    @Override
    // 결제된 서비스 플랜 내역 조회
    public List<PlanVO> getPlanPaymentList(int memCode) {
    	return planDAO.getPlanPaymentList(memCode);
    }


}
