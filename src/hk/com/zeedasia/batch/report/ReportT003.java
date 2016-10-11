package hk.com.zeedasia.batch.report;

import java.util.logging.Logger;

public class ReportT003 {
	private static Logger logger = Logger.getLogger("ReportT003.class");

	public static void main(String[] args) throws Exception {
		logger.info("Initialising MORejectionReportClient");
		ReportT003Service reportService = new ReportT003Service();
		reportService.start();

	}
}
