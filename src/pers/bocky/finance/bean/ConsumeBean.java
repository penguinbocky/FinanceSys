package pers.bocky.finance.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ConsumeBean extends BaseBean {
	//The number is mapped to the id in database category_dfntn.
	public final static int CATEGORY_ID = 2;
	
	private Integer consumeId;
	private Integer typeId;
	private String typeName;
	private String dest;
	private Double amount;
	private String description;
	private Timestamp addTs;
	private Timestamp lastUpdateTs;
	private Timestamp occurTs;
	
	public Integer getConsumeId() {
		return consumeId;
	}
	public void setConsumeId(Integer consumeId) {
		this.consumeId = consumeId;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
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
	@Override
	public ConsumeBean buildFrom(ResultSet rs) throws SQLException {
		setConsumeId(rs.getInt("consume_id"));
		setTypeId(rs.getInt("type_id"));
		setTypeName(rs.getString("type_name"));
		setDest(rs.getString("dest"));
		setAmount(rs.getDouble("amount"));
		setDescription(rs.getString("description"));
		setAddTs(rs.getTimestamp("add_ts"));
		setLastUpdateTs(rs.getTimestamp("last_update_ts"));
		setOccurTs(rs.getTimestamp("occur_ts"));
		return this;
	}
	
}
