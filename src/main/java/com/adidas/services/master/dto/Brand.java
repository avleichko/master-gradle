package com.adidas.services.master.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Brand {
    ADIDAS("MDT_BRAND_v_11"),
    REEBOK("MDT_BRAND_v_26");


    private String brandCode;
}
