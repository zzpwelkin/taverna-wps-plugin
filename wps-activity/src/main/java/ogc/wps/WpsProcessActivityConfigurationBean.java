package ogc.wps;

import java.io.Serializable;
import java.net.URI;
import java.util.List;


import net.opengis.wps.x100.InputDescriptionType;
import net.opengis.wps.x100.OutputDescriptionType;
import net.opengis.wps.x100.ProcessBriefType;
 
/**
 * Example activity configuration bean.
 * 
 */
public class WpsProcessActivityConfigurationBean implements Serializable {

	/*
	 * TODO: Remove this comment.
	 * 
	 * The configuration specifies the variable options and configurations for
	 * an activity that has been added to a workflow. For instance for a WSDL
	 * activity, the configuration contains the URL for the WSDL together with
	 * the method name. String constant configurations contain the string that
	 * is to be returned, while Beanshell script configurations contain both the
	 * scripts and the input/output ports (by subclassing
	 * ActivityPortsDefinitionBean).
	 * 
	 * Configuration beans are serialised as XML (currently by using XMLBeans)
	 * when Taverna is saving the workflow definitions. Therefore the
	 * configuration beans need to follow the JavaBeans style and only have
	 * fields of 'simple' types such as Strings, integers, etc. Other beans can
	 * be referenced as well, as long as they are part of the same plugin.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5237951810174324894L;
	// TODO: Remove the example fields and getters/setters and add your own	
	private static final String service = "WPS";
	private static final String svrversion = "1.0.0";

	// the uri of WPS service
	private URI wpsUri;
	
	// request method
	private int requestMethod;
	
	// is reference respond if support
	private boolean isReferenceRespond;
	
	// is asynchronous request if support
	private boolean isAsyned;
	
	// is reference supported
	private boolean isStatusSupported;
	
	// is stored supported
	private boolean isStoreSupported; 
	
	// process brief
	private ProcessBriefType	processBrief;
	
	// the Inputs of process
	private List<InputDescriptionType> inputList;
	
	// the Outputs of process
	private List<OutputDescriptionType> outputlist;

	public List<InputDescriptionType> getInputList() {
		return inputList;
	}

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
	 * @return the requestMethod
	 */
	public int getRequestMethod() {
		return requestMethod;
	}

	/**
	 * @param requestMethod the requestMethod to set
	 */
	public void setRequestMethod(int requestMethod) {
		this.requestMethod = requestMethod;
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
	 * @return the isAsyned
	 */
	public boolean isAsyned() {
		return isAsyned;
	}

	/**
	 * @param isAsyned the isAsyned to set
	 */
	public void setAsyned(boolean isAsyned) {
		this.isAsyned = isAsyned;
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

	/**
	 * @return the isReferenceRespond
	 */
	public boolean isReferenceRespond() {
		return isReferenceRespond;
	}

	/**
	 * @param isReferenceRespond the isReferenceRespond to set
	 */
	public void setReferenceRespond(boolean isReferenceRespond) {
		this.isReferenceRespond = isReferenceRespond;
	}
}
