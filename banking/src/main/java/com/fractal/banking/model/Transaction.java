package com.fractal.banking.model;

import java.util.Objects;

public class Transaction {	
	private String bankId;
	private String accountId;
    private String companyId;
    private String transactionId;
    private String bookingDate;
    private String description;
    private double amount;
    private String currencyCode;;
    private String type;
    private Category category;
  
    public Category getCategory() {
		return category;
	}
	public Transaction setCategory(Category category) {
		this.category = category;
		return this;
	}
	
	public String getBankId() {
		return bankId;
	}
	public Transaction setBankId(String bankId) {
		this.bankId = bankId;
		return this;
	}
	public String getAccountId() {
		return accountId;
	}
	public Transaction setAccountId(String accountId) {
		this.accountId = accountId;
		return this;
	}
	public String getCompanyId() {
		return companyId;
	}
	public Transaction setCompanyId(String companyId) {
		this.companyId = companyId;
		return this;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public Transaction setTransactionId(String transactionId) {
		this.transactionId = transactionId;
		return this;
	}
	public String getBookingDate() {
		return bookingDate;
	}
	public Transaction setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
		return this;
	}
	public String getDescription() {
		return description;
	}
	public Transaction setDescription(String description) {
		this.description = description;
		return this;
	}
	public double getAmount() {
		return amount;
	}
	public Transaction setAmount(double amount) {
		this.amount = amount;
		return this;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public Transaction setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
		return this;
	}
	public String getType() {
		return type;
	}
	public Transaction setType(String type) {
		this.type = type;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(transactionId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Objects.equals(transactionId, other.transactionId);
	}
	
	@Override
	public String toString() {
		return "Transaction [bankId=" + bankId + ", accountId=" + accountId + ", companyId=" + companyId
				+ ", transactionId=" + transactionId + ", bookingDate=" + bookingDate + ", description=" + description
				+ ", amount=" + amount + ", currencyCode=" + currencyCode + ", type=" + type + ", category=" + category
				+ "]";
	}
	    
	
}
