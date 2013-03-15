package ogc.wps.ui.serviceprovider;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;


import ogc.wps.ui.serviceprovider.WpsProcessServiceDesc;
import ogc.wps.ui.serviceprovider.WpsProcessServiceProviderConfig;

import org.n52.wps.client.WPSClientException;
import org.n52.wps.client.WPSClientSession;
import net.opengis.wps.x100.ProcessBriefType;
import net.opengis.wps.x100.ProcessDescriptionType;
import net.opengis.wps.x100.WPSCapabilitiesType;
import net.sf.taverna.t2.servicedescriptions.AbstractConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.ConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;

public class WpsProcessServiceProvider extends
AbstractConfigurableServiceProvider<WpsProcessServiceProviderConfig> implements
ConfigurableServiceProvider<WpsProcessServiceProviderConfig> {
	
	public WpsProcessServiceProvider() {
		super(new WpsProcessServiceProviderConfig());
	}

	private static final URI providerId = URI
		.create("http://example.com/2011/service-provider/foo");
	
	/**
	 * Do the actual search for services. Return using the callBack parameter.
	 */
	@SuppressWarnings("unchecked")
	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		// Use callback.status() for long-running searches
		callBack.status("Resolving OGC-WPS services");

		List<ServiceDescription> results = new ArrayList<ServiceDescription>();
		
		// init the wps client 
		WPSClientSession wpsClient = WPSClientSession.getInstance();
		
		// GetCapabilities request of WPS service and get all the processes
		String url = this.serviceProviderConfig.getUri().toString();
		//String url = "http://localhost/cgi-bin/pywps.cgi";
		
		try{
			wpsClient.connect(url);
			
			WPSCapabilitiesType capabilities = wpsClient.getWPSCaps(url).getCapabilities();
			
			//WPSCapabilitiesType capabilities = wpsClient.retrieveCapsViaGET(url).getCapabilities();
			
			results.clear();
			for (ProcessBriefType processBrief : capabilities.getProcessOfferings().getProcessArray())
			{
				//results.clear();
				
				WpsProcessServiceDesc service = new WpsProcessServiceDesc();
				
				service.setWpsUri(URI.create(url));
				
				service.setServiceIdentification(capabilities.getServiceIdentification());
				
				service.setServiceProvider(capabilities.getServiceProvider());
				
				service.setOperationMetadata(capabilities.getOperationsMetadata());
				
				ProcessDescriptionType describeProcess = wpsClient.getProcessDescription(
						url, processBrief.getIdentifier().getStringValue());
				
//				ProcessDescriptionType describeProcess = wpsClient.retrieveDescriptionViaGET(
//						new String[]{processBrief.getIdentifier().getStringValue()}, 
//						url).getProcessDescriptions().getProcessDescriptionArray(0);
				
				// store and status supported 
				service.setStoreSupported(describeProcess.getStoreSupported());
				
				service.setStatusSupported(false);
				
				if (describeProcess.getStoreSupported())
				{
					service.setStatusSupported(describeProcess.getStatusSupported());
				}
				
				// process brief
				service.setProcessBrief(processBrief);
				
				// process inputs
				service.setInputList(Arrays.asList(describeProcess.getDataInputs().getInputArray()));
				
				// process outputs
				service.setOutputlist(Arrays.asList(describeProcess.getProcessOutputs().getOutputArray()));
				
				results.add(service);
			}
		}
		catch(WPSClientException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		// partialResults() can also be called several times from inside
		// for-loop if the full search takes a long time
		callBack.partialResults(results);
		
		// No more results will be coming
		callBack.finished();
	}

	/**
	 * Icon for service provider
	 */
	public Icon getIcon() {
		return WpsProcessServiceIcon.getIcon();
	}

	/**
	 * Name of service provider, appears in right click for 'Remove service
	 * provider'
	 */
	public String getName() {
		return "OGC-WPS service";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getId() {
		return providerId.toASCIIString();
	}

	@Override
	protected List<? extends Object> getIdentifyingData() {
		// TODO Auto-generated method stub
		return Arrays.asList(getConfiguration().getUri());
	}

}
