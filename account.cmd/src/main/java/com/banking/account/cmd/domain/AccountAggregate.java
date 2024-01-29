package com.banking.account.cmd.domain;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawnEvent;
import com.banking.cqrs.core.domain.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {

    private Boolean active;


    @Setter
    @Getter
    private double balance;

    public AccountAggregate(OpenAccountCommand command){
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .createdDate(new Date())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build());
    }

    public void apply(AccountOpenedEvent event){
        id=event.getId();
        active=true;
        balance=event.getOpeningBalance();
    }

    public void depositFund(double amount){
        if (!active){
            throw new IllegalStateException("Los fondos no pueden ser depositados en esta cuenta");
        }

        if (amount<=0){
            throw new IllegalStateException("El deposito de dinero no puede ser cero menos que cero");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(id)
                .amount(amount)
                .build());
    }

    public void apply(FundsDepositedEvent event){
        id=event.getId();
        balance += event.getAmount();
    }

    public void withdrawFunds(double amount){
        if(!active){
            throw new IllegalStateException("La cuenta bancaria esta cerrada");
        }
        raiseEvent(FundsWithdrawnEvent.builder()
                .id(id)
                .amount(amount)
                .build());
    }

    public void apply(FundsWithdrawnEvent event){
        id=event.getId();
        balance -= event.getAmount();
    }

    public void closeAccount(){
        if(!active){
            throw new IllegalStateException("la cuenta de banco esta cerrada");
        }

        raiseEvent(AccountClosedEvent.builder()
                .id(id)
                .build());
    }

    public void apply(AccountClosedEvent event){
        id=event.getId();
        active=false;
    }

}
