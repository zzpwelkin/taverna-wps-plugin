package ogc.wps.ui.serviceprovider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import ogc.wps.WpsProcessActivity;
import ogc.wps.WpsProcessActivityConfigurationBean;

import net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata;
import net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification;
import net.opengis.ows.x11.ServiceProviderDocument.ServiceProvider;
import net.opengis.wps.x100.InputDescriptionType;
import net.opengis.wps.x100.OutputDescriptionType;
import net.opengis.wps.x100.ProcessBriefType;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;


public class WpsProcessServiceDesc extends ServiceDescription<WpsProcessActivityConfigurationBean> {

	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<WpsProcessActivityConfigurationBean>> getActivityClass() {
		return WpsProcessActivity.class;
	}

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public WpsProcessActivityConfigurationBean getActivityConfiguration() {
		WpsProcessActivityConfigurationBean bean = new WpsProcessActivityConfigurationBean();
		
		bean.setWpsUri(wpsUri);
		bean.setProcessBrief(processBrief);
		bean.setInputList(inputList);
		bean.setOutputlist(outputlist);
		bean.setRequestMethod(0);
		bean.setStatusSupported(isStatusSupported);
		bean.setStoreSupported(isStoreSupported);
		bean.setAsyned(false);
		bean.setReferenceRespond(false);
		return bean;
	}

	/**
	 * An icon to represent this service description in the service palette.
	 */
	@Override
	public Icon getIcon() {
		return WpsProcessServiceIcon.getIcon();
	}
	
	/**
	 * The display name that will be shown in service palette and will
	 * be used as a template for processor name when added to workflow.
	 */
	@Override
	public String getName() {
		return getProcessBrief().getIdentifier().getStringValue() + "-V" + 
				getProcessBrief().getProcessVersion();
	}

	/**
	 * The path to this service description in the service palette. Folders
	 * will be created for each element of the returned path.
	 */
	@Override
	public List<String> getPath() {
		// For deeper paths you may return several strings
		return Arrays.asList("OGC-WPS services");
	}

	/**
	 * Return a list of data values uniquely identifying this service
	 * description (to avoid duplicates). Include only primary key like fields,
	 * ie. ignore descriptions, icons, etc.
	 */
	@Override
	protected List<? extends Object> getIdentifyingData() {
		// FIXME: Use your fields instead of example fields
		return Arrays.<Object>asList(wpsUri.toASCIIString(), getProcessBrief().getIdentifier().getStringValue(), 
				getProcessBrief().getProcessVersion());
	}

	/**
	 * @return the serviceIdentification
	 */
	public ServiceIdentification getServiceIdentification() {
		return serviceIdentification;
	}

	/**
	 * @param serviceIdentification the serviceIdentification to set
	 */
	public void setServiceIdentification(ServiceIdentification serviceIdentification) {
		this.serviceIdentification = serviceIdentification;
	}

	/**
	 * @return the operationMetadata
	 */
	public OperationsMetadata getOperationMetadata() {
		return operationMetadata;
	}

	/**
	 * @param operationMetadata the operationMetadata to set
	 */
	public void setOperationMetadata(OperationsMetadata operationMetadata) {
		this.operationMetadata = operationMetadata;
	}

	/**
	 * @return the serviceProvider
	 */
	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	/**
	 * @param serviceProvider the serviceProvider to set
	 */
	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	/**
	 * @return the processBrief
	 */
	public ProcessBriefType getProcessBrief() {
		return processBrief;
	}

	/**
	 * @param processBrief the processBrief to set
	 */
	public void setProcessBrief(ProcessBriefType processBrief) {
		this.processBrief = processBrief;
	}

	/**
	 * @return the inputList
	 */
	public List<InputDescriptionType> getInputList() {
		return inputList;
	}

	/**
	 * @param inputList the inputList to set
	 */
	public void setInputList(List<InputDescriptionType> inputList) {
		this.inputList = inputList;
	}

	/**
	 * @return the outputlist
	 */
	public List<OutputDescriptionType> getOutputlist() {
		return outputlist;
	}

	/**
	 * @param outputlist the outputlist to set
	 */
	public void setOutputlist(List<OutputDescriptionType> outputlist) {
		this.outputlist = outputlist;
	}

	/**
	 * @return the isStoreSupported
	 */
	public boolean isStoreSupported() {
		return isStoreSupported;
	}

	/**
	 * @param isStoreSupported the isStoreSupported to set
	 */
	public void setStoreSupported(boolean isStoreSupported) {
		this.isStoreSupported = isStoreSupported;
	}

	/**
	 * @return the wpsUri
	 */
	public URI getWpsUri() {
		return wpsUri;
	}

	/**
	 * @param wpsUri the wpsUri to set
	 */
	public void setWpsUri(URI wpsUri) {
		this.wpsUri = wpsUri;
	}

	/**
	 * @return the isStatusSupported
	 */
	public boolean isStatusSupported() {
		return isStatusSupported;
	}

	/**
	 * @param isStatusSupported the isStatusSupported to set
	 */
	public void setStatusSupported(boolean isStatusSupported) {
		this.isStatusSupported = isStatusSupported;
	}

	// FIXME: Replace example fields and getters/setters with any required
	// and optional fields. (All fields are searchable in the Service palette,
	// for instance try a search for exampleString:3)
	private URI wpsUri;
	
	// request method 
	private boolean isStatusSupported;
	
	// is storing supported
	private boolean isStoreSupported;
	
	// service identification
	private ServiceIdentification serviceIdentification;
	
	// operation metadata
	private OperationsMetadata operationMetadata;
	
	//service provider
	private ServiceProvider serviceProvider;
	
	// process brief
	private ProcessBriefType	processBrief;
	
	// the Inputs of process
	private List<InputDescriptionType> inputList;
	
	// the Outputs of process
	private List<OutputDescriptionType> outputlist;
}
