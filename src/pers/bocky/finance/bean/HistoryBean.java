package pers.bocky.finance.bean;

import java.sql.Timestamp;

public class HistoryBean {
	
	private Integer historyId;
	private Integer categoryId;
	private Integer id;
	private Double amount;
	private String description;
	private Timestamp addTs;
	private Timestamp lastUpdateTs;
	private Timestamp occurTs;
	private HistoryType historyType;;
	
	public Integer getHistoryId() {
		return historyId;
	}
	
	public void setHistoryId(Integer historyId) {
		this.historyId = historyId;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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

	public HistoryType getHistoryType() {
		return historyType;
	}

	public void setHistoryType(HistoryType historyType) {
		this.historyType = historyType;
	}
	
}
