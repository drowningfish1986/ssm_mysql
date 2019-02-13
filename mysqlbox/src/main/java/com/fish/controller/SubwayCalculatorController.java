package com.fish.controller;

import com.fish.util.CheckUtil;
import com.fish.vo.SubwayCalculatorVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SubwayCalculatorController {

    @PostMapping("/subway/calculate")
    public Map<String, String> calculate(@RequestBody SubwayCalculatorVO subwayCalculatorVO){
        Map<String, String> result = new HashMap<>(2);
        try {
            String startDateStr = subwayCalculatorVO.getStartDate();
            if(!CheckUtil.checkNumber(subwayCalculatorVO.getOncePrice())){
                result.put("errMsg", "单次票价输入有误");
                return result;
            }
            if(!CheckUtil.checkNumber(subwayCalculatorVO.getDiscount())){
                result.put("errMsg", "折扣输入有误");
                return result;
            }
            if(!CheckUtil.checkDateByPattern(startDateStr, "yyyyMMdd")){
                result.put("errMsg", "日期输入有误");
                return result;
            }
            Date date = new SimpleDateFormat("yyyyMMdd").parse(startDateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int workdays = 0;
            for(int i = 0; i < 30; i++){
                int weekday = calendar.get(Calendar.DAY_OF_WEEK);
                calendar.add(Calendar.DATE, 1);
                if(1 == weekday || 7 == weekday){
                    continue;
                }
                workdays++;
            }
            String discountStr = subwayCalculatorVO.getDiscount();
            BigDecimal discount = new BigDecimal(discountStr);
            if(discount.compareTo(BigDecimal.ZERO) == -1 || discount.compareTo(BigDecimal.TEN) == 1){
                result.put("errMsg", "折扣输入有误,0~9.9");
                return result;
            }
            String oncePriceStr = subwayCalculatorVO.getOncePrice();
            BigDecimal oncePrice = new BigDecimal(oncePriceStr);
            if(oncePrice.compareTo(BigDecimal.ZERO) != 1){
                result.put("errMsg", "单次票价应该大于0");
                return result;
            }
            BigDecimal money = oncePrice.multiply(BigDecimal.ONE.subtract(discount.divide(BigDecimal.TEN,2, BigDecimal.ROUND_HALF_UP)));
            if(money.compareTo(BigDecimal.ONE) == 1){
                money = BigDecimal.ONE;
            }
            result.put("price", money.multiply(new BigDecimal(workdays)).toString());
        } catch (ParseException e) {
            result.put("errMsg", "日期输入有误");
        } catch (Exception e) {
            result.put("errMsg", "计算出错");
        }
        return result;
    }
}
