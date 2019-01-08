package com.fish.controller;

import com.fish.vo.CreditCalculatorResultVO;
import com.fish.vo.CreditCalculatorVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CreditCalculatorController {

    @PostMapping("/credit/calculate")
    public CreditCalculatorResultVO calculate(@RequestBody CreditCalculatorVO creditCalculatorVO){
        BigDecimal capital, outRate, monthOut;
        int monthes;
        CreditCalculatorResultVO resultVO = new CreditCalculatorResultVO();
        try {
            capital = new BigDecimal(creditCalculatorVO.getCapital()).setScale(2, BigDecimal.ROUND_DOWN);
            monthOut = new BigDecimal(creditCalculatorVO.getMonthOut()).setScale(2, BigDecimal.ROUND_DOWN);
            outRate = new BigDecimal(creditCalculatorVO.getOutRate()).divide(new BigDecimal(1200),4, BigDecimal.ROUND_HALF_UP);
            monthes = Integer.parseInt(creditCalculatorVO.getMonthes());

            resultVO.setMonthOut(monthOut);
            List<CreditCalculatorResultVO.Record> list = new ArrayList<>(monthes * 2);
            for (int i = 1; i <= monthes; i++){
                CreditCalculatorResultVO.Record record = resultVO.new Record();
                record.setMonth(i);
                BigDecimal interstOut = capital.multiply(outRate).setScale(2, BigDecimal.ROUND_HALF_UP);
                record.setInterestOut(interstOut);
                BigDecimal capitalOut = monthOut.subtract(interstOut);
                record.setCapitalOut(capitalOut);
                capital = capital.subtract(capitalOut);
                record.setSurplusCapital(capital);

                list.add(record);
            }
            resultVO.setList(list);
            resultVO.setSuccess(Boolean.TRUE);
        } catch (NumberFormatException e) {
            System.out.println("输入数字非法");
        }
        System.out.println("返回数据：" + resultVO);
        return resultVO;
    }
}
