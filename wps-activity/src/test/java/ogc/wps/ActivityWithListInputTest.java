package ogc.wps;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import javax.management.DescriptorKey;

import net.sf.taverna.t2.activities.testutils.ActivityInvoker;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import ogc.wps.WpsProcessActivity;
import ogc.wps.WpsProcessActivityConfigurationBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class ActivityWithListInputTest {

	private WpsProcessActivityConfigurationBean configBean;
	private WpsProcessActivity activity = new WpsProcessActivity();
	
	// service configure
	final String serviceURI = "http://localhost/cgi-bin/pywps.cgi";
	final String identifier = "maxlik_unsuperivised_classfication";
	final String version = "0.1";

	// input files
	final String[] inputRef = new String[]{
		"http://127.0.0.1/wps/wpsoutputs/lsat5_1987_201.tif",
		"http://127.0.0.1/wps/wpsoutputs/lsat7_2000_101.tif"
	};
	
	// result file
	final String resultRef = "http://127.0.0.1/wps/wpsoutputs/maxlike_result.tif";
	
	@Before
	public void makeConfigBean() throws Exception {
		
		//TODO: configure the configbean
		configBean = ExampleActivityConfigurationBeanFactory.newInstance(serviceURI, identifier, version);
	}
	
	@Test
	@DescriptorKey("ListInputTest")
	public void executeActivityWithListInputTest() 
			throws ActivityConfigurationException, InterruptedException, MalformedURLException,IOException
	{
		// In([Raster_Reference,Raster_Reference]) --> Out(Raster_RawData)
		activity.configure(configBean);
		
		activity.getConfiguration().setAsyned(false);
		
		// set inputs
		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("inputs", Arrays.asList(inputRef));

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		Assert.assertEquals(HttpFileRequest.getRasterFromHttp(resultRef), ((byte[])outputs.get("output")).length);
	}
}
