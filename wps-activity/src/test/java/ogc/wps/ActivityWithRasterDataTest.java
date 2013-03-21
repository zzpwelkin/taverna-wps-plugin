package ogc.wps;

import java.util.HashMap;
import java.util.Map;

import ogc.wps.WpsProcessActivity;
import ogc.wps.WpsProcessActivityConfigurationBean;

import net.sf.taverna.t2.activities.testutils.ActivityInvoker;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ActivityWithRasterDataTest {

	private WpsProcessActivityConfigurationBean configBean;

	private WpsProcessActivity activity = new WpsProcessActivity();
	
	final String serviceURI = "http://localhost/cgi-bin/pywps.cgi";
	final String identifier = "r.shaded.relief";
	final String version = "1.0";
	
	// inputs data  
	final String inputRef = "http://127.0.0.1/wps/wpsoutputs/ASTGTM_dem_samll.tif";
	final String resultRef = "http://127.0.0.1/wps/wpsoutputs/ASTGTM_dem_samll_result.tif";
	
	long resLength = 0;
	
	@Before
	public void makeConfigBean() throws Exception {
		configBean = ExampleActivityConfigurationBeanFactory.newInstance(serviceURI, identifier, version);
		
		resLength = HttpFileRequest.getRasterFromHttp(resultRef).length;
	}

	//@Test
	public void executeAsynch1() throws Exception {
		// In(Raster_RawData) --> Out(Raster_RawData)
		activity.configure(configBean);
		
		// reconfigure
		activity.getConfiguration().setAsyned(false);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("input", HttpFileRequest.getBase64RasterFromHttp(inputRef));
		inputs.put("input_encode", "base64");

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals(resLength, ((byte[])outputs.get("output")).length);
	}
	
	//@Test
	public void executeAsynch2() throws Exception {
		// In(Raster_RawData) --> Out(Raster_Reference)
		activity.configure(configBean);
		
		// reconfigure
		activity.getConfiguration().setAsyned(true);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("input", HttpFileRequest.getBase64RasterFromHttp(inputRef));
		inputs.put("input_encode", "base64");

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		assertEquals(resLength, HttpFileRequest.getRasterFromHttp((String)outputs.get("output")).length);
	}
	
	//@Test
	public void executeAsynch3() throws Exception {
		// In(Raster_Reference) --> Out(Raster_Reference)
		activity.configure(configBean);
		
		// reconfigure
		activity.getConfiguration().setReferenceRespond(true);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("input", inputRef);
		inputs.put("azimuth", 270);

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		assertEquals(resLength, HttpFileRequest.getRasterFromHttp((String)outputs.get("output")).length);
	}
	
	//@Test
	public void executeAsynch4() throws Exception {
		// In(Raster_Reference) --> Out(Raster_RawData)
		activity.configure(configBean);
		activity.getConfiguration().setAsyned(false);

		// set input
		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("input", inputRef);

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		assertEquals(resLength, (byte[])outputs.get("output"));
	}
}
