package com.fish.vo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class CreditCalculatorVO implements Serializable {

    private static final long serialVersionUID = -8489149172584690871L;

    private String capital;

    private String monthes;

    private String outRate;

    private String monthOut;
}
