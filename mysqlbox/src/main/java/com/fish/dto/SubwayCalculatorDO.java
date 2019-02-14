package com.fish.dto;

import java.math.BigDecimal;

public class SubwayCalculatorDO {
    private BigDecimal oncePrice;
    private BigDecimal pointFive;
    private BigDecimal pointEight;
    private BigDecimal discount;
    private BigDecimal thisMonthTotal;
    private BigDecimal discountTotal;
    private String errMsg;

    public BigDecimal getOncePrice() {
        return oncePrice;
    }

    public void setOncePrice(BigDecimal oncePrice) {
        this.oncePrice = oncePrice;
        this.pointFive = oncePrice.multiply(new BigDecimal(0.5)).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.pointEight = oncePrice.multiply(new BigDecimal(0.8)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPointFive() {
        return pointFive;
    }

    public BigDecimal getPointEight() {
        return pointEight;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getThisMonthTotal() {
        return thisMonthTotal;
    }

    public void setThisMonthTotal(BigDecimal thisMonthTotal) {
        this.thisMonthTotal = thisMonthTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
