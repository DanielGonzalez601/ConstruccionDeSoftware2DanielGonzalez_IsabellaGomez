package com.banco.app.domain.models;

import com.banco.app.domain.enums.ProductCategory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankingProduct {

    private String productCode;
    private String productName;
    private ProductCategory category;
    private boolean requiresApproval;

    public BankingProduct(String productCode, String productName,
                          ProductCategory category, boolean requiresApproval) {

        this.productCode = productCode;
        this.productName = productName;
        this.category = category;
        this.requiresApproval = requiresApproval;
    }

    @Override
    public String toString() {
        return "BankingProduct{productCode='" + productCode +
               "', productName='" + productName +
               "', category=" + category +
               ", requiresApproval=" + requiresApproval + "}";
    }
}