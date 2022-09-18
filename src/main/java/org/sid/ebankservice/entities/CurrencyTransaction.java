package org.sid.ebankservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankservice.enums.TransactionType;

import javax.persistence.*;
import java.util.Date;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CurrencyTransaction {
    @Id
    private String id;
    private Date timestamp;
    private double amount;
    private String currencyCode;
    private String walletId;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private double currencyPrice;
    private double costs;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CurrencyDeposit currencyDeposit;
    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CurrencyTransaction originTransaction;
}
