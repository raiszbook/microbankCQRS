package com.banking.account.cmd.api.command;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.core.handlers.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountCommandHandler implements CommandHandler{

    @Autowired
    private EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand command) {
var aggregate= new AccountAggregate(command);
eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DepositFundsCommand command) {
        var aggregate= eventSourcingHandler.getById(command.getId());
        aggregate.depositFund(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(WithdrawFundsCommand command) {
        var aggregate= eventSourcingHandler.getById(command.getId());
        if(command.getAmount()>aggregate.getBalance()){
            throw new IllegalStateException("Insuficientes fondos, no se puede retirar dinero");
        }
        aggregate.withdrawFunds(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        var aggregate= eventSourcingHandler.getById(command.getId());
        aggregate.closeAccount();
        eventSourcingHandler.save(aggregate);

    }
}
