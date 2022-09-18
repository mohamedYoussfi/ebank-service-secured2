package org.sid.ebankservice.repository;

import org.sid.ebankservice.entities.CurrencyDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCurrencyDepositRepository extends JpaRepository<CurrencyDeposit, String> {
}
