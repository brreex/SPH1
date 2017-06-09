package cs544.exercise16_1.bank.dao;

import java.util.*;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cs544.exercise16_1.bank.domain.Account;

public class AccountDAO implements IAccountDAO {
	private SessionFactory sessionFactory ;
	
	@Transactional(propagation=Propagation.MANDATORY)
	public void saveAccount(Account account) {
		sessionFactory.getCurrentSession().save(account.getCustomer());
		sessionFactory.getCurrentSession().save(account);
	}
	@Transactional(propagation=Propagation.MANDATORY)
	public void updateAccount(Account account) {
		sessionFactory.getCurrentSession().merge(account);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public Account loadAccount(long accountnumber) {
		return sessionFactory.getCurrentSession().get(Account.class, accountnumber);
	}
	
	@Transactional(propagation=Propagation.MANDATORY)
	public Collection<Account> getAccounts() {
		return sessionFactory.getCurrentSession().createQuery("From Account",Account.class).list();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
