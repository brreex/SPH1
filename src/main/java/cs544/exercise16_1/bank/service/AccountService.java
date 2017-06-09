package cs544.exercise16_1.bank.service;

import java.util.Collection;
import java.util.Date;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cs544.exercise16_1.bank.dao.IAccountDAO;
import cs544.exercise16_1.bank.domain.Account;
import cs544.exercise16_1.bank.domain.AccountEntry;
import cs544.exercise16_1.bank.domain.Customer;
import cs544.exercise16_1.bank.jms.IJMSSender;
import cs544.exercise16_1.bank.logging.ILogger;

public class AccountService implements IAccountService {
	private IAccountDAO accountDAO;
	private ICurrencyConverter currencyConverter;
	private IJMSSender jmsSender;
	private ILogger logger;
	
	public AccountService(){
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Account createAccount(long accountNumber, String customerName) {
		
		Account account = new Account(accountNumber);
		Customer customer = new Customer(customerName);
		account.setCustomer(customer);
		accountDAO.saveAccount(account);
		logger.log("createAccount with parameters accountNumber= "+accountNumber+" , customerName= "+customerName);
		
		return account;
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void deposit(long accountNumber, double amount) {
		
		Account account = accountDAO.loadAccount(accountNumber);
		AccountEntry accountEntry = new AccountEntry(new Date(), amount, "deposit", "", "");
		accountEntry.setAccount(account);
		
		account.deposit(accountEntry);

		accountDAO.updateAccount(account);
		logger.log("deposit with parameters accountNumber= "+accountNumber+" , amount= "+amount);
		if (amount > 10000){
			jmsSender.sendJMSMessage("Deposit of $ "+amount+" to account with accountNumber= "+accountNumber);
		}
		
	}
	@Transactional(readOnly=true)
	public Account getAccount(long accountNumber) {
		
		Account account = accountDAO.loadAccount(accountNumber);
		
		return account;
	}
	
	@Transactional(readOnly=true)
	public Collection<Account> getAllAccounts() {
		
		Collection<Account> accounts = accountDAO.getAccounts();
		
		return accounts;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void withdraw(long accountNumber, double amount) {
		
		Account account = accountDAO.loadAccount(accountNumber);
		account.withdraw(amount);
		accountDAO.updateAccount(account);
		
		logger.log("withdraw with parameters accountNumber= "+accountNumber+" , amount= "+amount);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void depositEuros(long accountNumber, double amount) {
		
		Account account = accountDAO.loadAccount(accountNumber);
		double amountDollars = currencyConverter.euroToDollars(amount);
		
		AccountEntry accountEntry = new AccountEntry(new Date(), amountDollars, "deposite", "", "");
		accountEntry.setAccount(account);
		
		account.deposit(accountEntry);
		accountDAO.updateAccount(account);
		
		logger.log("depositEuros with parameters accountNumber= "+accountNumber+" , amount= "+amount);
		if (amountDollars > 10000){
			jmsSender.sendJMSMessage("Deposit of $ "+amount+" to account with accountNumber= "+accountNumber);
		}
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void withdrawEuros(long accountNumber, double amount) {
		
		Account account = accountDAO.loadAccount(accountNumber);
		double amountDollars = currencyConverter.euroToDollars(amount);
		account.withdraw(amountDollars);
		accountDAO.updateAccount(account);
		
		logger.log("withdrawEuros with parameters accountNumber= "+accountNumber+" , amount= "+amount);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void transferFunds(long fromAccountNumber, long toAccountNumber, double amount, String description) {
		
		Account fromAccount = accountDAO.loadAccount(fromAccountNumber);
		Account toAccount = accountDAO.loadAccount(toAccountNumber);
		fromAccount.transferFunds(toAccount, amount, description);
		accountDAO.updateAccount(fromAccount);
		accountDAO.updateAccount(toAccount);
		
		logger.log("transferFunds with parameters fromAccountNumber= "+fromAccountNumber+" , toAccountNumber= "+toAccountNumber+" , amount= "+amount+" , description= "+description);
		if (amount > 10000){
			jmsSender.sendJMSMessage("TransferFunds of $ "+amount+" from account with accountNumber= "+fromAccount+" to account with accountNumber= "+toAccount);
		}
	}

	public void setAccountDAO(IAccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	public void setCurrencyConverter(ICurrencyConverter currencyConverter) {
		this.currencyConverter = currencyConverter;
	}

	public void setJmsSender(IJMSSender jmsSender) {
		this.jmsSender = jmsSender;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}
}
