package hk.com.zeedasia.batch.report.lib;

import java.math.BigDecimal;
import java.util.Date;

public class AuditLog {
	private String id;
	private Date logDate;
	private BigDecimal logTimestamp;
	private String refNo;
	private String serverMessage;
	private String orderType;
	private int extRefNo;
	private String accountNo;
	private String camId;
	private String aeCode;
	private String productCode;
	private String productName;
	private String ccy;
	private String settleCcy;
	private BigDecimal amount;
	private double foFee;
	private BigDecimal switchFee;
	private String paymentMethod;
	private String bankCode;
	private String bankAcctNo;
	private String approve_ref_no;
	private String arrpove_date;

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

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getServerMessage() {
		return serverMessage;
	}

	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public int getExtRefNo() {
		return extRefNo;
	}

	public void setExtRefNo(int extRefNo) {
		this.extRefNo = extRefNo;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCamId() {
		return camId;
	}

	public void setCamId(String camId) {
		this.camId = camId;
	}

	public String getAeCode() {
		return aeCode;
	}

	public void setAeCode(String aeCode) {
		this.aeCode = aeCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getSettleCcy() {
		return settleCcy;
	}

	public void setSettleCcy(String settleCcy) {
		this.settleCcy = settleCcy;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public double getFoFee() {
		return foFee;
	}

	public void setFoFee(double foFee) {
		this.foFee = foFee;
	}

	public BigDecimal getSwitchFee() {
		return switchFee;
	}

	public void setSwitchFee(BigDecimal switchFee) {
		this.switchFee = switchFee;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAcctNo() {
		return bankAcctNo;
	}

	public void setBankAcctNo(String bankAcctNo) {
		this.bankAcctNo = bankAcctNo;
	}

	public String getApprove_ref_no() {
		return approve_ref_no;
	}

	public void setApprove_ref_no(String approve_ref_no) {
		this.approve_ref_no = approve_ref_no;
	}

	public String getArrpove_date() {
		return arrpove_date;
	}

	public void setArrpove_date(String arrpove_date) {
		this.arrpove_date = arrpove_date;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("hk.com.zeedasia.fos.batch.report.lib.AuditLog {");
		sb.append("ID=" + id + ";");
		sb.append("LogDate=" + logDate + ";");
		sb.append("LogTimeStamp=" + logTimestamp + ";");
		sb.append("RefNo=" + refNo + ";");
		sb.append("ServerMessage=" + serverMessage + ";");
		sb.append("OrderType=" + orderType + ";");
		sb.append("ExtRefNo=" + extRefNo + ";");
		sb.append("AccountNo=" + accountNo + ";");
		sb.append("CamId=" + camId + ";");
		sb.append("AeCode=" + aeCode + ";");
		sb.append("ProductCode=" + productCode + ";");
		sb.append("ProductName=" + productName + ";");
		sb.append("CCY=" + ccy + ";");
		sb.append("SettleCcy=" + settleCcy + ";");
		sb.append("Amount=" + amount + ";");
		sb.append("FoFee=" + foFee + ";");
		sb.append("SwitchFee=" + switchFee + ";");
		sb.append("PaymentMethod=" + paymentMethod + ";");
		sb.append("BankCode=" + bankCode + ";");
		sb.append("BankAcctNo=" + bankAcctNo + ";");
		sb.append("Approve_ref_no=" + approve_ref_no + ";");
		sb.append("Arrpove_date=" + arrpove_date + ";");
		sb.append("}");

		return sb.toString();
	}
}