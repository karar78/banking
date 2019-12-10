package com.fractal.banking.model;

import java.util.Objects;

public class Company {
	private int yEndDay;
	private String address;
	private int yEndMonth;
	private String name;
	private String website;
	private String industry;
	private String currency;
	
	
	public Company(int yEndDay, String address, int yEndMonth, String name, String website, String industry,
			String currency) {
		super();
		this.yEndDay = yEndDay;
		this.address = address;
		this.yEndMonth = yEndMonth;
		this.name = name;
		this.website = website;
		this.industry = industry;
		this.currency = currency;
	}
	public int getyEndDay() {
		return yEndDay;
	}
	public void setyEndDay(int yEndDay) {
		this.yEndDay = yEndDay;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getyEndMonth() {
		return yEndMonth;
	}
	public void setyEndMonth(int yEndMonth) {
		this.yEndMonth = yEndMonth;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Override
	public int hashCode() {
		return Objects.hash(address, currency, industry, name, website, yEndDay, yEndMonth);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		return Objects.equals(address, other.address) && Objects.equals(currency, other.currency)
				&& Objects.equals(industry, other.industry) && Objects.equals(name, other.name)
				&& Objects.equals(website, other.website) && yEndDay == other.yEndDay && yEndMonth == other.yEndMonth;
	}
	@Override
	public String toString() {
		return "Company [yEndDay=" + yEndDay + ", address=" + address + ", yEndMonth=" + yEndMonth + ", name=" + name
				+ ", website=" + website + ", industry=" + industry + ", currency=" + currency + "]";
	}
	
	
}
