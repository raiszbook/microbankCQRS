package com.banking.account.query.api.controllers;


import com.banking.account.query.api.dto.AccountLookupResponse;
import com.banking.account.query.api.dto.EqualityType;
import com.banking.account.query.api.queries.FindAccountByHolderQuery;
import com.banking.account.query.api.queries.FindAccountByIdQuery;
import com.banking.account.query.api.queries.FindAccountWithBalanceQuery;
import com.banking.account.query.api.queries.FindAllAccountsQuery;
import com.banking.account.query.domain.BankAccount;
import com.banking.cqrs.core.infrastructure.QueryDispacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/bankAccountLookup")
public class AccountLookupController {
    private final Logger logger = Logger.getLogger(AccountLookupController.class.getName());

    @Autowired
    private QueryDispacher queryDispacher;

    @GetMapping(path = "/")
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        try {
            List<BankAccount> accounts = queryDispacher.send(new FindAllAccountsQuery());
            if (accounts == null || accounts.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Se realizo la consulta con exito", null))
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Errores ejecutando la consulta";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/byId/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable(value = "id") String id){
        try {
            List<BankAccount> accounts = queryDispacher.send(new FindAccountByIdQuery(id));
            if (accounts == null || accounts.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Se realizo la consulta con exito", null))
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Errores ejecutando la consulta";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(path = "/byHolder/{accountHolder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@PathVariable(value = "accountHolder") String accountHolder){
        try {
            List<BankAccount> accounts = queryDispacher.send(new FindAccountByHolderQuery(accountHolder));
            if (accounts == null || accounts.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Se realizo la consulta con exito", null))
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Errores ejecutando la consulta";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(path = "/withBalance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountWithBalance(
            @PathVariable(value = "equalityType")EqualityType equalityType,
            @PathVariable(value = "balance") double balance){
        try {
            List<BankAccount> accounts = queryDispacher.send(new FindAccountWithBalanceQuery(balance,equalityType));
            if (accounts == null || accounts.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Se realizo la consulta con exito", null))
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Errores ejecutando la consulta";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
