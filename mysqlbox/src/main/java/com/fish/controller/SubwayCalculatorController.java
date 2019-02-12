package com.fish.controller;

import com.fish.vo.SubwayCalculatorVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
public class SubwayCalculatorController {

    @PostMapping("/subway/calculate")
    public String calculate(@RequestBody SubwayCalculatorVO subwayCalculatorVO) throws ParseException {
        String startDateStr = subwayCalculatorVO.getStartDate();
        Date date = new SimpleDateFormat("yyyyMMdd").parse(startDateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return "";
    }
}
