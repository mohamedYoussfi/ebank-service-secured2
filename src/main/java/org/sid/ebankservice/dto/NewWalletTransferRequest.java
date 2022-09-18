package org.sid.ebankservice.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class NewWalletTransferRequest {
    private String sourceWalletId;
    private String sourceWalletCurrency;
    private String destinationWalletId;
    private String destinationWalletCurrency;
    private double amount;
}
