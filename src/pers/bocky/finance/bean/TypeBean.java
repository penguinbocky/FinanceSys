package pers.bocky.finance.bean;

import java.sql.Timestamp;

public class TypeBean {
	public final static Integer BORROW_FROM_RELATIONSHIPS = 7;
	public final static Integer BORROW_FROM_LOAN = 17;
	public final static Integer CONSUME_FOR_PAY_LOAN = 14;
	public final static Integer CONSUME_FOR_PAY_RELATIONSHIPS = 19;
	public final static Integer CONSUME_COSTING_DEPOSIT = 39;
	
	private Integer typeId;
	private Integer categoryId;
	private String categoryName;
	private String typeName;
	private String description;
	private Timestamp addTs;
	private Timestamp lastUpdateTs;
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	
	
	public Timestamp getLastUpdateTs() {
		return lastUpdateTs;
	}
	public void setLastUpdateTs(Timestamp lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}
	//For dropdown render
	@Override
	public String toString() {
		return typeName;
	}
	
	
}
