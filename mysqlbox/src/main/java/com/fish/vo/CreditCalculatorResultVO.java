package com.fish.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@ToString
public class CreditCalculatorResultVO {

    //调用是否成功
    private boolean success = false;
    //月还款额
    private BigDecimal monthOut;

    private List<Record> list;

    @Setter
    @Getter
    @ToString
    public class Record{
        //月数
        private int month;
        //剩余本金
        private BigDecimal surplusCapital;
        //当月本金支出
        private BigDecimal capitalOut;
        //当月利息支出
        private BigDecimal interestOut;
    }
}
