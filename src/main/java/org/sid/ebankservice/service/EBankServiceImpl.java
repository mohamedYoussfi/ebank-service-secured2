package org.sid.ebankservice.service;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankservice.dto.CurrencyTransferResponse;
import org.sid.ebankservice.dto.NewWalletTransferRequest;
import org.sid.ebankservice.entities.CurrencyDeposit;
import org.sid.ebankservice.entities.CurrencyTransaction;
import org.sid.ebankservice.enums.TransactionType;
import org.sid.ebankservice.repository.BankCurrencyDepositRepository;
import org.sid.ebankservice.repository.CurrencyTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Service
@Slf4j
@Transactional
public class EBankServiceImpl {
    @Autowired
    private BankCurrencyDepositRepository bankCurrencyDepositRepository;
    @Autowired
    private CurrencyTransactionRepository currencyTransactionRepository;
    public List<CurrencyDeposit> currencyDeposits(){
        return bankCurrencyDepositRepository.findAll();
    }
    public CurrencyTransferResponse newWalletTransaction(NewWalletTransferRequest walletTransferRequest){
        log.info("=========================");
        log.info(walletTransferRequest.toString());
        log.info("==========================");
         CurrencyDeposit sourceCurrencyDeposit=bankCurrencyDepositRepository.findById(walletTransferRequest.getSourceWalletCurrency())
                 .orElseThrow(()->new RuntimeException(String.format("Currency %s not found",walletTransferRequest.getSourceWalletCurrency())));
        CurrencyDeposit destinationCurrencyDeposit=bankCurrencyDepositRepository.findById(walletTransferRequest.getDestinationWalletCurrency())
                .orElseThrow(()->new RuntimeException(String.format("Currency %s not found",walletTransferRequest.getDestinationWalletCurrency())));

        CurrencyTransaction currencyTransaction1=CurrencyTransaction.builder()
                .id(UUID.randomUUID().toString())
                .walletId(walletTransferRequest.getSourceWalletId())
                .currencyPrice(sourceCurrencyDeposit.getSaleCurrencyPrice())
                .amount(walletTransferRequest.getAmount())
                .costs(walletTransferRequest.getAmount()*(sourceCurrencyDeposit.getSaleCurrencyPrice()-sourceCurrencyDeposit.getPurchaseCurrencyPrice()))
                .currencyCode(walletTransferRequest.getSourceWalletCurrency())
                .timestamp(new Date())
                .currencyDeposit(sourceCurrencyDeposit)
                .type(TransactionType.PURCHASE)
                .build();
        currencyTransaction1=currencyTransactionRepository.save(currencyTransaction1);

        CurrencyTransaction currencyTransaction2=CurrencyTransaction.builder()
                .id(UUID.randomUUID().toString())
                .walletId(walletTransferRequest.getDestinationWalletId())
                .currencyPrice(destinationCurrencyDeposit.getSaleCurrencyPrice())
                .amount(walletTransferRequest.getAmount()*destinationCurrencyDeposit.getPurchaseCurrencyPrice())
                .costs(walletTransferRequest.getAmount()*destinationCurrencyDeposit.getPurchaseCurrencyPrice()*(destinationCurrencyDeposit.getSaleCurrencyPrice()-destinationCurrencyDeposit.getPurchaseCurrencyPrice()))
                .currencyCode(walletTransferRequest.getDestinationWalletCurrency())
                .type(TransactionType.SALE)
                .timestamp(new Date())
                .currencyDeposit(destinationCurrencyDeposit)
                .originTransaction(currencyTransaction1)
                .build();
        currencyTransaction2=currencyTransactionRepository.save(currencyTransaction2);
        sourceCurrencyDeposit.setBalance(sourceCurrencyDeposit.getBalance()+ currencyTransaction1.getCosts());
        destinationCurrencyDeposit.setBalance(destinationCurrencyDeposit.getBalance()+ currencyTransaction2.getCosts());
        return
                CurrencyTransferResponse.builder()
                        .sourceTransactionId(currencyTransaction1.getId())
                        .destinationTransactionId(currencyTransaction2.getId())
                        .build();

    }
    public CurrencyDeposit addNewCurrencyDeposit(String currencyId, double initialBalance, double currencySalePrice, double currencyPurchasePrice){
        CurrencyDeposit bankCurrencyDeposit= CurrencyDeposit.builder()
                .currencyId(currencyId)
                .balance(initialBalance)
                .saleCurrencyPrice(currencySalePrice)
                .purchaseCurrencyPrice(currencyPurchasePrice)
                .build();
        return bankCurrencyDepositRepository.save(bankCurrencyDeposit);
    }
    public void loadCurrencies() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("currencies.data.csv");
        List<String> lines = Files.readAllLines(Path.of(classPathResource.getURI()));
        for(int i=1;i<lines.size();i++){
            String[] line=lines.get(i).split(";");
            String currencyCode=line[0];
            double saleCurrencyPrice=Double.parseDouble(line[2]);
            double purchaseCurrencyPrice=Double.parseDouble(line[3]);
            CurrencyDeposit currencyDeposit=CurrencyDeposit.builder()
                    .currencyId(currencyCode)
                    .saleCurrencyPrice(saleCurrencyPrice)
                    .purchaseCurrencyPrice(purchaseCurrencyPrice)
                    .balance(10000)
                    .build();
            bankCurrencyDepositRepository.save(currencyDeposit);
        }
        NewWalletTransferRequest walletTransferRequest= NewWalletTransferRequest.builder()
                .amount(100+Math.random()*9000)
                .destinationWalletCurrency("EUR")
                .sourceWalletCurrency("USD")
                .sourceWalletId("W1")
                .destinationWalletId("W2")
                .build();
        this.newWalletTransaction(walletTransferRequest);
        bankCurrencyDepositRepository.findAll().forEach(dep->{

        });
    }
}
