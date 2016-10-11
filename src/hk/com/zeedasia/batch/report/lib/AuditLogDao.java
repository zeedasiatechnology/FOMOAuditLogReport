package hk.com.zeedasia.batch.report.lib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AuditLogDao extends Dao {
	private final String columns = "id, log_date, log_timestamp, sub_system, user, "
			+ "from_value, to_value, deleted, message";
	
	public AuditLogDao() {
		super();
	}

	public AuditLogDao(Properties prop) {
		super(prop);
	}

	public AuditLog getlogById(String accountNo) {
		AuditLog auditLog = new AuditLog();;
		PreparedStatement ps = null;

		try {
			String sql = "select " + columns + " from auditlog where id = ?";
			ps = connection.prepareStatement(sql);
			ps.setString(1, accountNo);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				auditLog = setAuidtLog(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException se2) {
				// do nothing
			}
		}

		return auditLog;
	}
	
	public List<AuditLog> getMOErrorLog(String interval){
		List<AuditLog> auditLogs = new ArrayList<AuditLog>();
		PreparedStatement ps = null;

		try {
			String sql = "select " + columns + " from auditlog where message like '%errorCode%' and message not like '%errorCode\" : 0%' and log_date <= now() and log_date >= Date_SUB(now(),INTERVAL "+interval+") ";
			ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				auditLogs.add(setAuidtLog(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException se2) {
				// do nothing
			}
		}

		return auditLogs;
	}

	private AuditLog setAuidtLog(ResultSet rs) throws SQLException {
		AuditLog auditLog = new AuditLog();
		auditLog.setId(rs.getString("id"));
		auditLog.setLogDate(rs.getDate("log_date"));
		auditLog.setLogTimestamp(rs.getBigDecimal("log_timestamp"));
		auditLog.setSubSystem(rs.getString("sub_system"));
		auditLog.setUser(rs.getString("user"));
		auditLog.setFromValue(rs.getString("from_value"));
		auditLog.setToValue(rs.getString("to_value"));
		auditLog.setDeleted(rs.getObject("deleted", Boolean.class));
		auditLog.setMessage(rs.getString("message"));
		return auditLog;
	}
}
