package pers.bocky.finance.bean;

import java.sql.Timestamp;

public class BorrowBean {
	//The number is mapped to the id in database category_dfntn.
	public final static int CATEGORY_ID = 3;
	
	private Integer borrowId;
	private Integer typeId;
	private String typeName;
	private String fromWho;
	private Double amount;
	private String description;
	private Timestamp addTs;
	private Timestamp lastUpdateTs;
	private Timestamp occurTs;
	private Double paybackedAmt;
	private Double leftAmt;
	
	public Integer getBorrowId() {
		return borrowId;
	}
	public void setBorrowId(Integer borrowId) {
		this.borrowId = borrowId;
	}
	public String getFromWho() {
		return fromWho;
	}
	public void setFromWho(String fromWho) {
		this.fromWho = fromWho;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	public Double getPaybackedAmt() {
		return paybackedAmt;
	}
	public void setPaybackedAmt(Double paybackedAmt) {
		this.paybackedAmt = paybackedAmt;
	}
	public Double getLeftAmt() {
		return leftAmt;
	}
	public void setLeftAmt(Double leftAmt) {
		this.leftAmt = leftAmt;
	}
	
}
