
package report;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReporterNG implements IReporter 
{
	
	private ExtentReports extent;

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) 
	{
		extent = new ExtentReports(outputDirectory + File.separator + "Extent.html", true);

		for (ISuite suite : suites) 
		{
			Map<String, ISuiteResult> result = suite.getResults();
			for (ISuiteResult r : result.values()) 
			{
				ITestContext context = r.getTestContext();
				buildTestNodes(context.getPassedTests(), LogStatus.PASS);
				buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
				buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
			}
		}

		extent.flush();
		extent.close();
	}

	private void buildTestNodes(IResultMap tests, LogStatus status) 
	{
		ExtentTest test;

		if (tests.size() > 0) 
		{
			for (ITestResult result : tests.getAllResults())
			{
				test = extent.startTest(result.getMethod().getMethodName());

				test.setStartedTime(getTime(result.getStartMillis()));
				test.setEndedTime(getTime(result.getEndMillis()));

				for (String group : result.getMethod().getGroups())
					test.assignCategory(group);

				if (result.getThrowable() != null) 
				{
					test.log(status, result.getThrowable());
				} else {
					test.log(status, "Test " + status.toString().toLowerCase()
							+ "ed");
				}
				
				if (result.getStatus()==ITestResult.FAILURE) {
					String captureFile = "unable to find";
					String source = System.getProperty("user.dir")+"/target/surefire-reports/FailedTestsScreenshots/";
					File directoryPath = new File (source);
					File filesList[] = directoryPath.listFiles();
				      for(File file : filesList) {
				          if (file.getName().contains(result.getMethod().getMethodName())) {
				        	  captureFile = file.getName();
				          }
				       }
				      System.out.println();
				
					   try 
					      {
						   
						   String userWindows = System.getenv("PATH");
						   
					       if (userWindows.contains("eclipse"))
					       {
					    	   test.log(LogStatus.FAIL, test.addScreenCapture(source+captureFile));
					       }
					       else 
					       {
					    	   test.log(LogStatus.FAIL, test.addScreenCapture(".\\FailedTestsScreenshots\\"+captureFile));
					       }
						   
					      } catch (Exception e) {
						
							e.printStackTrace();
						}
				}
				extent.endTest(test);
			}
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
}
