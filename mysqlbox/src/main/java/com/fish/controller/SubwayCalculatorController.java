package com.fish.controller;

import com.fish.dto.SubwayCalculatorDO;
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
            SubwayCalculatorDO dto = checkParam(subwayCalculatorVO);
            Date date = new SimpleDateFormat("yyyyMMdd").parse(subwayCalculatorVO.getStartDate());
            Calendar calendar = Calendar.getInstance();
            Calendar monthStart = Calendar.getInstance();
            calendar.setTime(date);
            monthStart.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            monthStart.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            monthStart.set(Calendar.DATE, 1);
            while (monthStart.get(Calendar.DATE) < calendar.get(Calendar.DATE)){
                monthStart.add(Calendar.DATE, 1);
                int weekday = monthStart.get(Calendar.DAY_OF_WEEK);
                if(1 == weekday || 7 == weekday){
                    continue;
                }
                addWithoutDiscount(dto);
                addWithoutDiscount(dto);
            }
            for(int i = 0; i < 30; i++){
                if(monthStart.get(Calendar.DATE) == 1){
                    dto.setThisMonthTotal(BigDecimal.ZERO);
                }
                monthStart.add(Calendar.DATE, 1);
                int weekday = monthStart.get(Calendar.DAY_OF_WEEK);
                if(1 == weekday || 7 == weekday){
                    continue;
                }
                addWithDiscount(dto);
                addWithDiscount(dto);
            }

            result.put("price", dto.getDiscountTotal().toString());
        } catch (ParseException e) {
            result.put("errMsg", "日期输入有误");
        } catch (Exception e) {
            result.put("errMsg", "计算出错");
        }
        return result;
    }

    private SubwayCalculatorDO checkParam(SubwayCalculatorVO subwayCalculatorVO){
        SubwayCalculatorDO dto = new SubwayCalculatorDO();
        String startDateStr = subwayCalculatorVO.getStartDate();
        String oncePriceStr = subwayCalculatorVO.getOncePrice();
        String discountStr = subwayCalculatorVO.getDiscount();
        if(!CheckUtil.checkNumber(oncePriceStr)){
            dto.setErrMsg("单次票价输入有误");
            return dto;
        }
        if(!CheckUtil.checkNumber(discountStr)){
            dto.setErrMsg("折扣输入有误");
            return dto;
        }
        if(!CheckUtil.checkDateByPattern(startDateStr, "yyyyMMdd")){
            dto.setErrMsg("日期输入有误");
            return dto;
        }
        BigDecimal discount = new BigDecimal(discountStr).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
        if(discount.compareTo(BigDecimal.ZERO) == -1 || discount.compareTo(BigDecimal.ONE) == 1){
            dto.setErrMsg("折扣输入有误,0~9.9");
            return dto;
        }
        dto.setDiscount(discount);

        BigDecimal oncePrice = new BigDecimal(oncePriceStr);
        if(oncePrice.compareTo(BigDecimal.ZERO) != 1){
            dto.setErrMsg("单次票价应该大于0");
            return dto;
        }
        dto.setOncePrice(oncePrice);
        dto.setThisMonthTotal(BigDecimal.ZERO);
        dto.setDiscountTotal(BigDecimal.ZERO);
        return dto;
    }

    private void addWithoutDiscount(SubwayCalculatorDO dto){
        BigDecimal thisMonthTotal = dto.getThisMonthTotal();
        if(thisMonthTotal.compareTo(new BigDecimal(150)) >= 0){
            thisMonthTotal = thisMonthTotal.add(dto.getPointFive());
        } else if (thisMonthTotal.compareTo(new BigDecimal(100)) >= 0){
            thisMonthTotal = thisMonthTotal.add(dto.getPointEight());
        } else {
            thisMonthTotal = thisMonthTotal.add(dto.getOncePrice());
        }
        dto.setThisMonthTotal(thisMonthTotal);
    }

    private void addWithDiscount(SubwayCalculatorDO dto){
        BigDecimal thisMonthTotal = dto.getThisMonthTotal();
        BigDecimal discountTotal = dto.getDiscountTotal();
        BigDecimal pointFive = dto.getPointFive();
        BigDecimal pointEight = dto.getPointEight();
        BigDecimal discount = dto.getDiscount();
        BigDecimal oncePrice = dto.getOncePrice();
        if(thisMonthTotal.compareTo(new BigDecimal(150)) >= 0){
            BigDecimal discountMoney = pointFive.multiply(discount).setScale(2, BigDecimal.ROUND_HALF_UP);
            discountMoney = discountMoney.compareTo(BigDecimal.ONE) < 0 ? discountMoney : BigDecimal.ONE;
            thisMonthTotal = thisMonthTotal.add(pointFive.subtract(discountMoney));
            discountTotal = discountTotal.add(discountMoney);
        } else if (thisMonthTotal.compareTo(new BigDecimal(100)) >= 0){
            BigDecimal discountMoney = pointEight.multiply(BigDecimal.ONE.subtract(discount)).setScale(2, BigDecimal.ROUND_HALF_UP);
            discountMoney = discountMoney.compareTo(BigDecimal.ONE) < 0 ? discountMoney : BigDecimal.ONE;
            thisMonthTotal = thisMonthTotal.add(pointEight.subtract(discountMoney));
            discountTotal = discountTotal.add(discountMoney);
        } else {
            BigDecimal discountMoney = oncePrice.multiply(BigDecimal.ONE.subtract(discount)).setScale(2, BigDecimal.ROUND_HALF_UP);
            discountMoney = discountMoney.compareTo(BigDecimal.ONE) < 0 ? discountMoney : BigDecimal.ONE;
            thisMonthTotal = thisMonthTotal.add(oncePrice.subtract(discountMoney));
            discountTotal = discountTotal.add(discountMoney);
        }
        dto.setThisMonthTotal(thisMonthTotal);
        dto.setDiscountTotal(discountTotal);
    }
}
