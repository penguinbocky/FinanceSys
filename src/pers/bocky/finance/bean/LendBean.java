package pers.bocky.finance.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class LendBean extends BaseBean {
	//The number is mapped to the id in database category_dfntn.
	public final static int CATEGORY_ID = 4;
	
	private Integer lendId;
	private Integer typeId;
	private String typeName;
	private String toWho;
	private Double amount;
	private String description;
	private Timestamp addTs;
	private Timestamp lastUpdateTs;
	private Timestamp occurTs;
	private Double paybackedAmt;
	private Double leftAmt;
	
	public Integer getLendId() {
		return lendId;
	}
	public void setLendId(Integer lendId) {
		this.lendId = lendId;
	}
	public String getToWho() {
		return toWho;
	}
	public void setToWho(String toWho) {
		this.toWho = toWho;
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

	@Override
	public LendBean buildFrom(ResultSet rs) throws SQLException {
		clear();
		setLendId(rs.getInt("lend_id"));
		setTypeId(rs.getInt("type_id"));
		setTypeName(rs.getString("type_name"));
		setToWho(rs.getString("to_who"));
		setAmount(rs.getDouble("amount"));
		setDescription(rs.getString("description"));
		setAddTs(rs.getTimestamp("add_ts"));
		setLastUpdateTs(rs.getTimestamp("last_update_ts"));
		setOccurTs(rs.getTimestamp("occur_ts"));
		setPaybackedAmt(rs.getDouble("paybackedAmt"));
		setLeftAmt(rs.getDouble("leftAmt"));
		return this;
	}
	
	private void clear() {
		//
	}
	
}
