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
	private String messageSystem ="";
	private String messageUser ="";
	private List<String> errorList;
	private String[] errorCodeList;
	private boolean messageHeader = false;
	private boolean mailToUser = false;

	public ReportT003Service() throws IOException {
		PropertiesUtils.loadLogProperties(BatchConstants.LOG_PROPERTIES_FILE);
	}

	public void start() throws IOException {
		logger.info("ReprotT003Service Started");
		prop = PropertiesUtils.getProperties(BatchConstants.CONFIG_PROPERTIES_FILE);
		String errorCodes = prop.getProperty("errorSentUser").trim();
		errorCodeList = errorCodes.split(";");
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
						messageSystem = BatchConstants.MESSAGE_HEADER;
						messageHeader = true;
					}
					logger.info("Error Found: " + auditLog.toString());
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String logDate = df.format(auditLog.getLogTimestamp());
					messageSystem += "<tr><td>" + logDate + "</td><td>" + auditLog.getRefNo() + "</td><td>" + auditLog.getServerMessage() + "</td><td>"
							+ auditLog.getId() + "</td></tr>";
					addToErrorList(auditLog.getId());
					String serverMessage = auditLog.getServerMessage().replace("\"", "'").trim();
					for(String errorCode:errorCodeList){
						if(serverMessage.contains(errorCode) && !errorCode.isEmpty()){
							mailToUser = true;
							messageUser += "<div><p><ul type=square>";
							messageUser += "<li><u>Log Date: "+logDate+"; Error Message: "+auditLog.getServerMessage()+"</u></li>";
							messageUser += "<ul type=circle>";
							messageUser += "<li><b>Order Type: </b>"+auditLog.getOrderType()+"</li>";
							messageUser += "<li><b>Reference No.: </b>"+auditLog.getRefNo()+"</li>";
							String inputDate = df.format(auditLog.getCreateDate());
							messageUser += "<li><b>Input Date.: </b>"+inputDate+"</li>";
							messageUser += "<li><b>Account No.: </b>"+auditLog.getAccountNo()+"</li>";
							messageUser += "<li><b>CAM ID: </b>"+auditLog.getCamId()+"</li>";
							messageUser += "<li><b>Account Name: </b>"+auditLog.getAccountName()+"</li>";
							messageUser += "<li><b>Account Email: </b>"+auditLog.getClientEmail()+"</li>";
							messageUser += "<li><b>Account Tel: </b>"+auditLog.getClientTel()+"</li>";
							messageUser += "<li><b>AE Code: </b>"+auditLog.getAeCode()+"</li>";
							messageUser += "<li><b>AE Name: </b>"+auditLog.getAeName()+"</li>";
							messageUser += "<li><b>AE Email: </b>"+auditLog.getAeEmail()+"</li>";
							messageUser += "<li><b>AE Tel: </b>"+auditLog.getAeTel()+"</li>";
							messageUser += "<li><b>Product Code: </b>"+auditLog.getProductCode()+"</li>";
							messageUser += "<li><b>Product Name: </b>"+auditLog.getProductName()+"</li>";
							messageUser += "<li><b>CCY: </b>"+auditLog.getCcy()+"</li>";
							messageUser += "<li><b>Settle CCY: </b>"+auditLog.getSettleCcy()+"</li>";
							messageUser += "<li><b>Amount: </b>"+auditLog.getAmount()+"</li>";
							messageUser += "<li><b>FO Fee: </b>"+auditLog.getFoFee()+"</li>";
							messageUser += "<li><b>Switch Fee: </b>"+auditLog.getSwitchFee()+"</li>";
							messageUser += "<li><b>Payment Method: </b>"+auditLog.getPaymentMethod()+"</li>";
							messageUser += "<li><b>Bank Code: </b>"+auditLog.getBankCode()+"</li>";
							messageUser += "<li><b>Bank Acct: </b>"+auditLog.getBankAcctNo()+"</li>";
							messageUser += "<li><b>Approve Date: </b>"+auditLog.getArrpove_date()+"</li>";
							messageUser += "</ul></ul></p></div>";
							break;
						}
					}
					
				}
			}
		}
		if (messageHeader) {
			Properties mailProp = PropertiesUtils.getProperties(BatchConstants.MAIL_PROPERTIES_FILE);	
			String templateFile = mailProp.getProperty(BatchConstants.PROP_MAIL_TEMPLATE);
			String to = mailProp.getProperty(BatchConstants.PROP_MAIL_SYSTEM);
			sendEmail(to, messageSystem, templateFile);
			if(mailToUser){
				to = mailProp.getProperty(BatchConstants.PROP_MAIL_USER);
				sendEmail(to, messageUser,templateFile);
			}
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
		logger.info("Add logID:"+logID+" into ErrorList File");
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

	private void sendEmail(String to, String message, String Template) {
		File templateFile = new File(Template);
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
		} catch (Exception e) {
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
