package com.apifuze.obn.bank.service;




import ng.openbanking.api.payload.account.Account;
import ng.openbanking.api.payload.account.AccountBlock;
import ng.openbanking.api.payload.account.AccountType;
import ng.openbanking.api.payload.bank.exception.BankResourceNotFoundException;
import ng.openbanking.api.payload.bank.exception.ServiceOperationNotSupported;
import ng.openbanking.api.payload.bank.service.BankAccountService;
import ng.openbanking.api.payload.customer.PocessingOperationResponse;
import ng.openbanking.api.payload.definition.OperationStatus;
import ng.openbanking.api.payload.directdebit.DirectDebit;
import ng.openbanking.api.payload.directdebit.DirectDebitCancelRequest;
import ng.openbanking.api.payload.directdebit.DirectDebitSetup;
import ng.openbanking.api.payload.limit.Limit;
import ng.openbanking.api.payload.limit.LimitCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bankAccountService")
public class AccountServiceImpl extends AbsractBankService implements BankAccountService {

    private static final String ACCOUNT_MODEL_FILE_NAME = "Account";

    private static final String ACCOUNT_TYPE_MODEL_FILE_NAME = "AccountType";

    private static final String LIMIT_MODEL_FILE_NAME = "Limit";

    private static final String DD_MODEL_FILE_NAME = "DiectDebit";




    public Account getAccountByAccountNumber(String accountNumber) throws BankResourceNotFoundException, ServiceOperationNotSupported {
        return dataService.getSingleFromList(ACCOUNT_MODEL_FILE_NAME,Account.class);
    }

    
    public Account getAccountByCustomerId(String customerId)
            throws BankResourceNotFoundException, ServiceOperationNotSupported {
        return dataService.getSingleFromList(ACCOUNT_MODEL_FILE_NAME,Account.class);
    }

    
    public Account getAccountByBvn(String bvn) throws BankResourceNotFoundException, ServiceOperationNotSupported {
        return dataService.getSingleFromList(ACCOUNT_MODEL_FILE_NAME,Account.class);
    }

    
    public Account getAccountByPhoneNumber(String phoneNumber)
            throws BankResourceNotFoundException, ServiceOperationNotSupported {
        return dataService.getSingleFromList(ACCOUNT_MODEL_FILE_NAME,Account.class);
    }

    
    public Account getAccountByEmail(String emailAddress)
            throws BankResourceNotFoundException, ServiceOperationNotSupported {
        return dataService.getSingleFromList(ACCOUNT_MODEL_FILE_NAME,Account.class);
    }

    
    public PocessingOperationResponse blockAccount(AccountBlock accountBlock)
            throws BankResourceNotFoundException, ServiceOperationNotSupported {
        return generateProcessingResponse(OperationStatus.SUCCESSFUL);

    }


    public List<AccountType> getAccountTypes() {
        return dataService.getModelList(ACCOUNT_TYPE_MODEL_FILE_NAME,AccountType.class);
    }

    
    public LimitCustomer getCustomerTransactionLimit(String accountNumber)
            throws BankResourceNotFoundException, ServiceOperationNotSupported {
        return dataService.getSingleFromList(LIMIT_MODEL_FILE_NAME,LimitCustomer.class);
    }

    
    public Limit getGlobalTransactionLimit() throws ServiceOperationNotSupported {
        return dataService.getSingleFromList(LIMIT_MODEL_FILE_NAME,Limit.class);
    }


    
    public PocessingOperationResponse setupDirectDebit(DirectDebitSetup directDebitSetup)
            throws BankResourceNotFoundException, ServiceOperationNotSupported {
        return generateProcessingResponse(OperationStatus.SUCCESSFUL);
    }


    
    public PocessingOperationResponse cancelDirectDebit(DirectDebitCancelRequest directDebitCancelRequest)
            throws BankResourceNotFoundException, ServiceOperationNotSupported {
        return generateProcessingResponse(OperationStatus.SUCCESSFUL);
    }


    
    public DirectDebit getDirectDebit(String accountNumber, String referenceNumber)
            throws BankResourceNotFoundException, ServiceOperationNotSupported {
        return dataService.getSingleFromList(DD_MODEL_FILE_NAME,DirectDebit.class);
    }

}
