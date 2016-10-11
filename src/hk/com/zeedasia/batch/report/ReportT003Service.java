package hk.com.zeedasia.batch.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import hk.com.zeedasia.batch.report.constant.BatchConstants;
import hk.com.zeedasia.batch.report.lib.AuditLog;
import hk.com.zeedasia.batch.report.lib.AuditLogDao;
import hk.com.zeedasia.framework.mail.MailException;
import hk.com.zeedasia.framework.mail.MailSender;
import hk.com.zeedasia.framework.mail.MailTemplate;
import hk.com.zeedasia.framework.mail.MailTemplateLoader;
import hk.com.zeedasia.framework.util.FileUtils;
import hk.com.zeedasia.framework.util.PropertiesUtils;

public class ReportT003Service {
	private static Logger logger = Logger.getLogger("ReportT003Service.class");

	private Properties prop;
	private MailSender sender;
	private String message;
	private List<String> errorList;
	private boolean messageHeader = false;

	public ReportT003Service() throws IOException {
		PropertiesUtils.loadLogProperties(BatchConstants.LOG_PROPERTIES_FILE);
	}

	public void start() throws IOException {
		prop = PropertiesUtils.getProperties(BatchConstants.CONFIG_PROPERTIES_FILE);
		CheckError();
	}

	private void CheckError() throws IOException {
		Properties daoProp = PropertiesUtils.getProperties(BatchConstants.DAO_PROPERTIES_FILE);
		String interval = prop.getProperty(BatchConstants.INTERVAL);
		AuditLogDao dao = new AuditLogDao(daoProp);
		List<AuditLog> auditLogs = dao.getMOErrorLog(interval);
		if (auditLogs.size() > 0) {
			getErrorList();
			boolean exist;
			for (AuditLog auditLog : auditLogs) {
				exist = false;
				for (String logID : errorList) {
					if (auditLog.getId().equals(logID)) {
						exist = true;
						continue;
					}
				}
				if (!exist) {
					if (!messageHeader) {
						message = BatchConstants.MESSAGE_HEADER;
						messageHeader = true;
					}
					logger.info("Error Found: " + auditLog.toString());
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String logDate = df.format(auditLog.getLogTimestamp());
					message += "<tr><td>" + logDate + "</td><td>" + auditLog.getMessage() + "</td><td>"
							+ auditLog.getId() + "</td></tr>";
					addToErrorList(auditLog.getId());
				}
			}
		}
		if (messageHeader) {
			sendEmail();
		} else {
			logger.info("No Error Found.");
		}
	}

	private void getErrorList() throws IOException {
		errorList = new ArrayList<String>();
		String errorFile = prop.getProperty(BatchConstants.ERROR_FILE_PATH)
				+ prop.getProperty(BatchConstants.ERROR_FILE_NAME);
		File file = new File(errorFile);
		if (!file.exists()) {
			file.createNewFile();
		}
		errorList = FileUtils.toStringList(errorFile);
	}

	private void addToErrorList(String logID) {
		errorList.add(logID);
		String errorFile = prop.getProperty(BatchConstants.ERROR_FILE_PATH)
				+ prop.getProperty(BatchConstants.ERROR_FILE_NAME);

		try {
			FileWriter fw = new FileWriter(errorFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			out.println(logID);
			out.flush();
			fw.close();
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	private void sendEmail() throws IOException {
		Properties mailProp = PropertiesUtils.getProperties(BatchConstants.MAIL_PROPERTIES_FILE);
		String to = mailProp.getProperty(BatchConstants.PROP_MAIL_TO);
		String templateFilePath = mailProp.getProperty(BatchConstants.PROP_MAIL_TEMPLATE);
		File templateFile = new File(templateFilePath);

		MailTemplate template = MailTemplateLoader.load(templateFile);
		String subject = template.getSubject();
		subject = subject.replace("${date}", getDateStr());

		String content = template.getContent();
		String interval = prop.getProperty(BatchConstants.INTERVAL);
		content = content.replace("${interval}", interval);

		try {
			sender = MailSender.getInstance();
			sender.init();
			content = content.replace("${message}", message);
			sender.sendHtml(to, subject, content);
			sender.close();
			logger.info("Email sent to: " + to);
		} catch (MessagingException | MailException e) {
			logger.info("Exception while sending email: " + e.getMessage());
		}
	}

	private String getDateStr() {
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String date = df.format(cal.getTime());
		return date;
	}
}
