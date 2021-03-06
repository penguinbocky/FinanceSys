package pers.bocky.finance.bean;

import java.sql.Timestamp;

public class LendHistoryBean {
	
	private Integer lendHistoryId;
	private Integer lendId;
	private Double amount;
	private String description;
	private Timestamp addTs;
	private Timestamp lastUpdateTs;
	private Timestamp occurTs;
	
	public Integer getLendHistoryId() {
		return lendHistoryId;
	}
	public void setLendHistoryId(Integer lendHistoryId) {
		this.lendHistoryId = lendHistoryId;
	}
	public Integer getLendId() {
		return lendId;
	}
	public void setLendId(Integer lendId) {
		this.lendId = lendId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getAddTs() {
		return addTs;
	}
	public void setAddTs(Timestamp addTs) {
		this.addTs = addTs;
	}
	
	public Timestamp getOccurTs() {
		return occurTs;
	}
	public void setOccurTs(Timestamp occurTs) {
		this.occurTs = occurTs;
	}
	public Timestamp getLastUpdateTs() {
		return lastUpdateTs;
	}
	public void setLastUpdateTs(Timestamp lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}
	
}
