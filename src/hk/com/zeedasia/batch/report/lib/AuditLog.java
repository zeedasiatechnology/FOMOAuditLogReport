package hk.com.zeedasia.batch.report.lib;

import java.math.BigDecimal;
import java.util.Date;

public class AuditLog{
	private String id;
	private Date logDate;
	private BigDecimal logTimestamp;
	private String subSystem;
	private String user;
	private String fromValue;
	private String toValue;
	private Boolean deleted;
	private String message;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	public BigDecimal getLogTimestamp() {
		return logTimestamp;
	}
	public void setLogTimestamp(BigDecimal logTimestamp) {
		this.logTimestamp = logTimestamp;
	}
	public String getSubSystem() {
		return subSystem;
	}
	public void setSubSystem(String subSystem) {
		this.subSystem = subSystem;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getFromValue() {
		return fromValue;
	}
	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}
	public String getToValue() {
		return toValue;
	}
	public void setToValue(String toValue) {
		this.toValue = toValue;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("hk.com.zeedasia.fos.batch.lib.entity.AuditLog {");
		sb.append("ID=" + id + ";");
		sb.append("LogDate=" + logDate + ";");
		sb.append("LogTimeStamp=" + logTimestamp + ";");
		sb.append("subSystem=" + subSystem + ";");
		sb.append("User=" + user + ";");
		sb.append("FromValue=" + fromValue + ";");
		sb.append("ToValue=" + toValue + ";");
		sb.append("Deleted=" + deleted + ";");
		sb.append("Message=" + message + ";");
		sb.append("}");

		return sb.toString();
	}
}