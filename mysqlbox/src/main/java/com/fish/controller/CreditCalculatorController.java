package com.fish.controller;

import com.fish.vo.CreditCalculatorResultVO;
import com.fish.vo.CreditCalculatorVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class CreditCalculatorController {

    @PostMapping("/credit/calculate")
    public CreditCalculatorResultVO calculate(@RequestBody CreditCalculatorVO creditCalculatorVO){
        BigDecimal capital = null, outRate = null, monthOut = null;
        int monthes;
        CreditCalculatorResultVO resultVO = new CreditCalculatorResultVO();
        try {
            //本金
            capital = new BigDecimal(creditCalculatorVO.getCapital()).setScale(2, BigDecimal.ROUND_DOWN);
            //月支出
            monthOut = new BigDecimal(creditCalculatorVO.getMonthOut()).setScale(2, BigDecimal.ROUND_DOWN);
            //利率
            outRate = new BigDecimal(creditCalculatorVO.getOutRate()).divide(new BigDecimal(1200),4, BigDecimal.ROUND_HALF_UP);
            //还款月数
            monthes = Integer.parseInt(creditCalculatorVO.getMonthes());

            resultVO.setMonthOut(monthOut);
            List<CreditCalculatorResultVO.Record> list = new ArrayList<>(monthes * 2);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (int i = 1; i <= monthes; i++){
                CreditCalculatorResultVO.Record record = resultVO.new Record();
                Date date = calendar.getTime();
                record.setMonth(sdf.format(date));
                calendar.add(Calendar.MONTH, 1);
                BigDecimal interstOut = capital.multiply(outRate).setScale(2, BigDecimal.ROUND_HALF_UP);
                record.setInterestOut(interstOut);
                if(1 == capital.compareTo(monthOut)){
                    BigDecimal capitalOut = monthOut.subtract(interstOut);
                    record.setCapitalOut(capitalOut);
                    capital = capital.subtract(capitalOut);
                    record.setSurplusCapital(capital);
                    list.add(record);
                } else {
                    record.setCapitalOut(capital);
                    record.setSurplusCapital(new BigDecimal(0));
                    list.add(record);
                    break;
                }
            }
            resultVO.setList(list);
            resultVO.setSuccess(Boolean.TRUE);
        } catch (NumberFormatException e) {
            String errMsg = "";
            if(null != outRate){
                errMsg = "还款月数应为正整数";
            } else if (null != monthOut){
                errMsg = "还款利率输入有误";
            } else if (null != capital){
                errMsg = "月还款额输入有误";
            } else {
                errMsg = "剩余本金输入有误";
            }
            System.out.println(errMsg);
            resultVO.setErrMsg(errMsg);
        }
        System.out.println("返回数据：" + resultVO);
        return resultVO;
    }
}
