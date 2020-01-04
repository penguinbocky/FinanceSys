package pers.bocky.finance.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DepositBean extends BaseBean {
	//The number is mapped to the id in database category_dfntn.
	public final static int CATEGORY_ID = 1;
	
	private Integer depositId;
	private Integer typeId;
	private String typeName;
	private String source;
	private Double amount;
	private String description;
	private Timestamp addTs;
	private Timestamp lastUpdateTs;
	private Timestamp occurTs;
	
	public Integer getDepositId() {
		return depositId;
	}
	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
	@Override
	public String toString() {
		return "DepositBean [depositId=" + depositId + ", typeId=" + typeId + ", source=" + source + ", amount="
				+ amount + ", description=" + description + ", addTs=" + addTs + "]";
	}
	@Override
	public DepositBean buildFrom(ResultSet rs) throws SQLException {
		setDepositId(rs.getInt("deposit_id"));
		setTypeId(rs.getInt("type_id"));
		setTypeName(rs.getString("type_name"));
		setSource(rs.getString("source"));
		setAmount(rs.getDouble("amount"));
		setDescription(rs.getString("description"));
		setAddTs(rs.getTimestamp("add_ts"));
		setLastUpdateTs(rs.getTimestamp("last_update_ts"));
		setOccurTs(rs.getTimestamp("occur_ts"));
		return this;
	}
	
}
