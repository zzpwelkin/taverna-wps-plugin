package ogc.wps;

import static org.junit.Assert.assertFalse;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.opengis.ows.x11.CodeType;
import net.opengis.wps.x100.ProcessBriefType;
import net.sf.taverna.t2.activities.testutils.ActivityInvoker;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import ogc.wps.WpsProcessActivity;
import ogc.wps.WpsProcessActivityConfigurationBean;

import org.junit.Before;
import org.junit.Test;
import org.n52.wps.client.WPSClientException;

public class ActivityWithVectorDataTest {
	WpsProcessActivityConfigurationBean configBean;
	WpsProcessActivity activity;
	// static configure information
	final String serviceURI = "http://localhost/cgi-bin/pywps.cgi";
	final String identifier = "v.extract";
	final String version = "1";
	
	// input
	final String localKMLFile = "/home/zzpwelkin/workstation/workbench/taverna-maven/wps/wps-activity/src/test/java/testdata/classfiled.kml";
	final String localGMLFile = "/home/zzpwelkin/workstation/workbench/taverna-maven/wps/wps-activity/src/test/java/testdata/classfiled_gml.gml";
	final String wfsGML = "http://127.0.0.1/wps/wpsoutputs/points.gml";
	byte[] input = null;
	
	@Before 
	public void makeConfigBeanTest()
	throws FileNotFoundException,IOException,WPSClientException
	{
		configBean = ExampleActivityConfigurationBeanFactory.newInstance(serviceURI, identifier, version);
		
		// input initial
		FileInputStream in = new FileInputStream(localGMLFile);
		byte[] tempBuf = new byte[in.available()];
		in.read();
		in.close();
		input = tempBuf.toString().getBytes("UTF-8");
	}
	
	//@Test
	public void executeAsync1()
	throws  ActivityConfigurationException
	{
		// Vector_GML-> Vector_GML
		// cmd: v.extract input=${localKMLFile} cats=1,2
		
		// In([Raster_Reference,Raster_Reference]) --> Out(GML_RawData)
		activity.configure(configBean);
		activity.getConfiguration().setAsyned(false);
		
		// set inputs
		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("input", input);

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		try
		{
			Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
					activity, inputs, expectedOutputTypes);
		}
		catch(InterruptedException e)
		{
			assertFalse(e.getMessage(), false);
		}
	}
	
	//@Test
	public void executeAsync2()
	{
		// Vector_GML -> Vector_KML
		// cmd: v.extract input=${localGMLFile} cats=1,2
	}
	
	//@Test
	public void executeAsync3()
	throws  ActivityConfigurationException
	{
		// Vector_Reference-> Vector_GML
		// cmd: v.extract input=${localGMLFile} cats=1,2
		
		// In([Raster_Reference,Raster_Reference]) --> Out(GML_RawData)
		activity.configure(configBean);
		activity.getConfiguration().setAsyned(false);
		
		// set inputs
		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("input", wfsGML);
		inputs.put("cats", "1");

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put("output", Object.class);

		try
		{
			Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
					activity, inputs, expectedOutputTypes);
		}
		catch(InterruptedException e)
		{
			assertFalse(e.getMessage(), false);
		}
	}

}
