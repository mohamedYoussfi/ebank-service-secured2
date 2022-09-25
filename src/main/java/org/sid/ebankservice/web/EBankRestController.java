package org.sid.ebankservice.web;

import org.sid.ebankservice.dto.CurrencyTransferResponse;
import org.sid.ebankservice.dto.NewWalletTransferRequest;
import org.sid.ebankservice.entities.CurrencyDeposit;
import org.sid.ebankservice.service.EBankServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class EBankRestController {
    @Autowired
    private EBankServiceImpl eBankService;
    @PostMapping("/currencyTransfer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CurrencyTransferResponse currencyTransfer(@RequestBody NewWalletTransferRequest request){
        return this.eBankService.newWalletTransaction(request);
    }
    @GetMapping("/currencyDeposits")
    @PreAuthorize("hasAuthority('USER')")
    public List<CurrencyDeposit> currencyDepositList(){
        return eBankService.currencyDeposits();
    }
}
