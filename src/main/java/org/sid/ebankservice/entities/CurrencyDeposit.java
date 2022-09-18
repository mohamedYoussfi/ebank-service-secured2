package org.sid.ebankservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CurrencyDeposit {
    @Id
    private String currencyId;
    private double saleCurrencyPrice;
    private double purchaseCurrencyPrice;
    private double balance;
    @OneToMany(mappedBy = "currencyDeposit")
    private List<CurrencyTransaction> currencyTransactions;
}
