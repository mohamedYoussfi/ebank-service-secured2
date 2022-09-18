package org.sid.ebankservice.repository;

import org.sid.ebankservice.entities.CurrencyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyTransactionRepository extends JpaRepository<CurrencyTransaction, String> {
}
