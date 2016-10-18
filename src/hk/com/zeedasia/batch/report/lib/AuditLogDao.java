package hk.com.zeedasia.batch.report.lib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class AuditLogDao extends Dao {
	private static Logger logger = Logger.getLogger("AuditLogDao.class");

	private String sql = "SELECT "
			+ "id, log_date, log_timestamp, trim(substring(substring_index(message,';',1), locate(':',message)+1)) as ref_no, "
			+ "substring_index(substring_index(message,'{',-1),'}',1) as server_message , approvalTable.* "
			+ "FROM auditlog as log inner join ( "
			+ "select case o.order_type when 'R' then 'Redemption' when 'S' then 'Subscription' else o.order_type end as order_type, "
			+ "o.create_date,	o.ext_ref_no,a.account_no, ud.value as CAM_ID,a.account_name,a.email as clientEmail,a.mobile as clientTel,ae.ae_code,ae.ae_name, ae.email1 as aeEmail,ae.tel1 as aeTel, "
			+ "p.product_code, p.product_name AS product_name, p.ccy, o.settle_ccy,	 "
			+ "case o.order_type when 'R' then o.qty when 'S' then o.net_amount else o.qty end as amount, "
			+ "o.dept_fee_rate as FO_fee, null as switch_fee, o.payment_method,o.bank_code,o.bank_acct_no, o.approve_ref_no, o.approve_date as approve_date	 "
			+ "from fo_fund_order o	inner join account_ud_type ud on o.account_no = ud.account_no "
			+ "inner join ae ae on o.ae_code = ae.ae_code inner join account a on a.account_no = o.account_no "
			+ "inner join product as p on o.product_code = p.product_code and p.exchange_code='FUNDS' "
			+ "where type_code = 'CAM_SUB_ID01' "
			+ "union "
			+ "select case io_type when 'I' then 'Switch In' when 'O' then 'Switch Out' else io_type end as order_type, "
			+ "sw.create_date,sw.ext_ref_no, a.account_no, ud.value as CAM_ID,a.account_name,a.email as clientEmail ,a.mobile as clientTel,ae.ae_code,ae.ae_name, ae.email1 as aeEmail,ae.tel1 as aeTel,"
			+ "p.product_code, p.product_name, p.ccy, sw.settle_ccy, "
			+ "d.qty as amount, d.dept_subs_fee_rate as FO_fee, d.dept_switch_fee_rate as Switch_fee, null as payment_method, null as bank_code, null as bank_acct_no, sw.approve_ref_no, sw.approve_date as approve_date "
			+ "from fo_fund_switching sw "
			+ "inner join fo_fund_switch_detail as d on sw.ext_ref_no = d.ext_ref_no "
			+ "inner join account_ud_type ud on sw.account_no = ud.account_no "
			+ "	inner join ae ae on sw.ae_code = ae.ae_code "
			+ "inner join account a on a.account_no = sw.account_no "
			+ "inner join product as p on d.product_code = p.product_code and p.exchange_code='FUNDS' where type_code = 'CAM_SUB_ID01' "
			+ ") as approvalTable on trim(substring(substring_index(log.message,';',1), locate(':',log.message)+1)) = approvalTable.approve_ref_no "
			+ "where approvalTable.approve_date > DATE_SUB(now(), interval 1 YEAR) "
			+ "AND log.sub_system ='IBOSS2-REST-API' "
			+ "and log.message like '%errorCode%' "
			+ "and log.message not like '%errorCode\" : 0%' "
			+ "and log.log_date >= date_sub(now(), INTERVAL  $[interval] ) "
			+ "order by log.id, log.log_date asc ";

	public AuditLogDao() {
		super();
	}

	public AuditLogDao(Properties prop) {
		super(prop);
	}

	public List<AuditLog> getMOErrorLog(String interval) {
		List<AuditLog> auditLogs = new ArrayList<AuditLog>();
		PreparedStatement ps = null;

		try {
			sql = sql.replace("$[interval] ", interval);
			ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				auditLogs.add(setAuidtLog(rs));
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException se2) {
				logger.info(se2.getMessage());
			}
		}

		return auditLogs;
	}

	private AuditLog setAuidtLog(ResultSet rs) throws SQLException {
		AuditLog auditLog = new AuditLog();
		auditLog.setAccountNo(rs.getString("account_no"));
		auditLog.setAccountName(rs.getString("account_name"));
		auditLog.setClientEmail(rs.getString("clientEmail"));
		auditLog.setClientTel(rs.getString("clientTel"));
		auditLog.setAeCode(rs.getString("ae_code"));
		auditLog.setAeEmail(rs.getString("aeEmail"));
		auditLog.setAeName(rs.getString("ae_name"));
		auditLog.setAeTel(rs.getString("aeTel"));
		auditLog.setAmount(rs.getBigDecimal("amount"));
		auditLog.setApprove_ref_no(rs.getString("approve_ref_no"));
		auditLog.setArrpove_date(rs.getTimestamp("approve_date"));
		auditLog.setBankAcctNo(rs.getString("bank_acct_no"));
		auditLog.setBankCode(rs.getString("bank_code"));
		auditLog.setCamId(rs.getString("CAM_ID"));
		auditLog.setCcy(rs.getString("ccy"));
		auditLog.setCreateDate(rs.getTimestamp("create_date"));
		auditLog.setExtRefNo(rs.getInt("ext_ref_no"));
		auditLog.setFoFee(rs.getDouble("FO_fee"));
		auditLog.setId(rs.getString("id"));
		auditLog.setLogDate(rs.getDate("log_date"));
		auditLog.setLogTimestamp(rs.getBigDecimal("log_timestamp"));
		auditLog.setOrderType(rs.getString("order_type"));
		auditLog.setPaymentMethod(rs.getString("payment_method"));
		auditLog.setProductCode(rs.getString("product_code"));
		auditLog.setProductName(rs.getString("product_name"));
		auditLog.setRefNo(rs.getString("ref_no"));
		auditLog.setServerMessage(rs.getString("server_message"));
		auditLog.setSettleCcy(rs.getString("settle_ccy"));
		auditLog.setSwitchFee(rs.getBigDecimal("switch_fee"));
		return auditLog;
	}
}
