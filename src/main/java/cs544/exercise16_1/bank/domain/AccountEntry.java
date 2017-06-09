package cs544.exercise16_1.bank.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AccountEntry {
	@Id
	@GeneratedValue
	private int id;
	private Date date;
	private double amount;
	private String description;
	private String fromAccountNumber;
	private String fromPersonName;
	@ManyToOne
	private Account account;
	
	public AccountEntry() {
	}

	public int getId() {
		return id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AccountEntry(Date date, double amount, String description, String fromAccountNumber, String fromPersonName) {
		super();
		this.date = date;
		this.amount = amount;
		this.description = description;
		this.fromAccountNumber = fromAccountNumber;
		this.fromPersonName = fromPersonName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getFromPersonName() {
		return fromPersonName;
	}

	public void setFromPersonName(String fromPersonName) {
		this.fromPersonName = fromPersonName;
	}

	@Override
	public String toString() {
		return "AccountEntry [id=" + id + ", date=" + date + ", amount=" + amount + ", description=" + description
				+ ", fromAccountNumber=" + fromAccountNumber + ", fromPersonName=" + fromPersonName + ", account="
				+ account + "]";
	}
	
	
}
