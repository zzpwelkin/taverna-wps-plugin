package ogc.wps;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import net.sf.taverna.t2.activities.testutils.ActivityInvoker;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import ogc.wps.WpsProcessActivity;
import ogc.wps.WpsProcessActivityConfigurationBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.n52.wps.client.WPSClientException;

public class ChainActivityTest {

	private WpsProcessActivityConfigurationBean configBean;
	private WpsProcessActivity activity = new WpsProcessActivity();
	
	// service configure
	final String serviceURI = "http://localhost/cgi-bin/pywps.cgi";
	final String MAX_LIKE = "maxlik_unsuperivised_classfication";
	final String MAX_LIKE_VER = "0.1";
	
	final String R_TO_V = "r.to.vect";
	final String R_TO_V_VER = "0.1";

	// input files and requested output file
	// input files
	final String[] inputRef = new String[]{
		"http://127.0.0.1/wps/wpsoutputs/lsat5_1987_201.tif",
		"http://127.0.0.1/wps/wpsoutputs/lsat7_2000_101.tif"
	};
	
	// result file
	final String resultRef = "http://127.0.0.1/wps/wpsoutputs/maxlike_result.tif";
	
	@Before
	public void makeConfigBean() throws Exception {
		configBean = new WpsProcessActivityConfigurationBean();
		
		//TODO: configure the configbean
		configBean.setWpsUri(URI.create(serviceURI));
	}
	
	//@Test
	public void executeAsyn1() 
			throws ActivityConfigurationException, InterruptedException, MalformedURLException,
			IOException,WPSClientException
	{
		// In([Raster_Reference,Raster_Reference]) --> Out(Raster_RawData) --> In(Raster_RawData) --> Out(GML_RawData) 
		
		// process the WPS service with {identifier1}
		configBean = ExampleActivityConfigurationBeanFactory.newInstance(serviceURI, MAX_LIKE, MAX_LIKE_VER);
		
		activity.configure(configBean);
		activity.getConfiguration().setAsyned(false);
		
		// set inputs
		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("inputs", Arrays.asList(inputRef));

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		byte[] middledata = (byte[]) outputs.get("output");
		
		// process the WPS service with {identifier2}
		configBean = ExampleActivityConfigurationBeanFactory.newInstance(serviceURI, R_TO_V, R_TO_V_VER);
		
		activity.configure(configBean);
		activity.getConfiguration().setAsyned(false);
		
		inputs = new HashMap<String, Object>();
		inputs.put("input", middledata);
		inputs.put("type", "area");

		expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		Assert.assertEquals(HttpFileRequest.getRasterFromHttp(resultRef).length, ((byte[])outputs.get("output")).length);
	}
	
	//@Test
	public void executeAsyn2() 
			throws ActivityConfigurationException, InterruptedException, MalformedURLException,
			IOException,WPSClientException
	{
		// In([Raster_Reference,Raster_Reference]) --> Out(Reference_RawData) --> In(Reference_RawData) --> Out(GML_RawData)
		
		// process the WPS service with {identifier1}
		configBean = ExampleActivityConfigurationBeanFactory.newInstance(serviceURI, MAX_LIKE, MAX_LIKE_VER);
		
		activity.configure(configBean);
		activity.getConfiguration().setAsyned(true);
		
		// set inputs
		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("inputs", inputRef);

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		// process the WPS service with {identifier2}
		configBean = ExampleActivityConfigurationBeanFactory.newInstance(serviceURI, R_TO_V, R_TO_V_VER);
		
		activity.configure(configBean);
		activity.getConfiguration().setAsyned(true);
		
		inputs = new HashMap<String, Object>();
		inputs.put("input", (String)outputs.get("output"));
		inputs.put("type", "area");

		expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		Assert.assertEquals(HttpFileRequest.getRasterFromHttp(resultRef).length, (byte[])outputs.get("output"));
	}
}
