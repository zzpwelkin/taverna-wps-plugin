package ogc.wps;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.geotools.ows.ServiceException;
import org.n52.wps.client.ExecuteRequestBuilder;
import org.n52.wps.client.WPSClientException;
import org.n52.wps.client.WPSClientSession;
import org.n52.wps.io.data.GenericFileData;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GenericFileDataBinding;

import net.opengis.wps.x100.ComplexDataDescriptionType;
import net.opengis.wps.x100.DataType;
import net.opengis.wps.x100.ExecuteDocument;
import net.opengis.wps.x100.ExecuteResponseDocument;
import net.opengis.wps.x100.InputDescriptionType;
import net.opengis.wps.x100.OutputDescriptionType;
import net.opengis.wps.x100.ProcessDescriptionType;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

public class WpsProcessActivity extends
		AbstractAsynchronousActivity<WpsProcessActivityConfigurationBean>
		implements AsynchronousActivity<WpsProcessActivityConfigurationBean> {

	private WpsProcessActivityConfigurationBean configBean;

	@Override
	public void configure(WpsProcessActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		if (configBean.getProcessBrief() == null) {
			throw new ActivityConfigurationException(
					"Not set process's identifier");
		}
		// Store for getConfiguration(), but you could also make
		// getConfiguration() return a new bean from other sources
		this.configBean = configBean;
		
		// REQUIRED: (Re)create input/output ports depending on configuration
		configurePorts();
	}

	protected void configurePorts() {
		// In case we are being reconfigured - remove existing ports first
		// to avoid duplicates
		removeInputs();
		removeOutputs();

		// inputs
		for (InputDescriptionType input : this.configBean.getInputList()) {
			// TODO: add the annotation and detail information of input ports
			// and if there's a method
			// that set the default value
			Class<?> cls;
			int depth = 0;

			// if asynchronous request
			cls = Object.class;
			if (input.isSetComplexData()) {
				// add the format input of this identifier
				addInput(input.getIdentifier().getStringValue() + "_schema", 0,
						true, null, String.class);
				addInput(input.getIdentifier().getStringValue() + "_encode", 0,
						true, null, String.class);
				addInput(input.getIdentifier().getStringValue() + "_mimetype",
						0, true, null, String.class);
			}

			// the input if a list object
			if (input.getMaxOccurs().bitCount() > 1) {
				depth = 1;
			}

			addInput(input.getIdentifier().getStringValue(), depth, true, null,
					cls);
		}

		// outputs
		for (OutputDescriptionType output : this.configBean.getOutputlist()) {
			// TODO: add the annotation and detail information of output ports
			addOutput(output.getIdentifier().getStringValue(), 0);

			// if the OutputDatatype is Complexdata then set the Format output
			if (output.getComplexOutput() != null) {
				addOutput(output.getIdentifier().getStringValue() + "_schema",
						0);
				addOutput(output.getIdentifier().getStringValue() + "_encode",
						0);
				addOutput(
						output.getIdentifier().getStringValue() + "_mimetype",
						0);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs,
			final AsynchronousActivityCallback callback) {
		// Don't execute service directly now, request to be run ask to be run
		// from thread pool and return asynchronously
		callback.requestRun(new Runnable() {
			
			public void run() {
				InvocationContext context = callback
						.getContext();
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
				try 
				{
					// wps process client service
					WPSClientSession wpsClient = WPSClientSession.getInstance();

					ProcessDescriptionType processDescription = wpsClient.getProcessDescription(
							configBean.getWpsUri().toString(), 
							configBean.getProcessBrief().getIdentifier().getStringValue());
					
//					ProcessDescriptionType processDescription = wpsClient.retrieveDescriptionViaGET(
//							new String[]{configBean.getProcessBrief().getIdentifier().getStringValue()}, 
//							configBean.getWpsUri().toString()).getProcessDescriptions().getProcessDescriptionArray(0);

					ExecuteRequestBuilder executeBuilder = new ExecuteRequestBuilder(processDescription);

					// Resolve inputs
					_resolveInputs(inputs, context, processDescription, executeBuilder);

					// set rawdata or reference response
					_setOutputs(processDescription, executeBuilder);

					// execute request
					ExecuteDocument execute = executeBuilder.getExecute();
					//wpsClient.retrieveExecuteResponseViaPOST(configBean.getWpsUri().toString(), execute, false);
					execute.getExecute().setService("WPS");
					Object responseObject = null;
					try
					{
						responseObject = wpsClient.execute(configBean.getWpsUri().toString(), execute);
					}
					catch (WPSClientException e)
					{
						// if the an error return from service 
							callback.fail(e.getServerException().xmlText());
					}
					
					// resoulve response of Execute request and register outputs
					_resolveOuputs(context, execute, processDescription, responseObject, outputs);
			
				}
				catch (WPSClientException e)
				{
					callback.fail(e.getMessage());
				}
				catch(ServiceException e)
				{
					callback.fail(e.getMessage());
				}
				catch (IOException e)
				{
					callback.fail(e.getMessage());
				}
				// Parse ouput 
				
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public WpsProcessActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}
	
	// Resolve inputs
	private void _resolveInputs(final Map<String, T2Reference> inputs,
			final InvocationContext context, 
			final ProcessDescriptionType processDescription,
			ExecuteRequestBuilder executeBuilder)
	throws WPSClientException, IOException
	{		
		ReferenceService referenceService = context
				.getReferenceService();
		// Resolve inputs
		for(InputDescriptionType input : processDescription.getDataInputs().getInputArray())
		{
			String inputName = input.getIdentifier().getStringValue();
			T2Reference t2InputRef = inputs.get(inputName);
			
			if(( t2InputRef==null || t2InputRef.containsErrors()))
			{
				if (input.getMinOccurs().intValue()>0)
					throw new IOException(inputName + " not set but mandatory!");
				else
					continue;
			}

			Object[] values = null;
			Object tempVal = referenceService.renderIdentifier(t2InputRef, Object.class, context);
			if (input.getMaxOccurs().intValue() > 1)
			{
				values = ((List<?>)tempVal).toArray();
			}
			else
			{
				values = new Object[1];
				values[0] = tempVal;
			}
			for (Object val : values)
			{
				if (input.getLiteralData() != null) {
					if (val instanceof String) {
						executeBuilder.addLiteralData(inputName,
								(String) val);
					}
				} else if (input.getComplexData() != null) {
					
					if (val == null && input.getMinOccurs().intValue() > 0) {
						throw new IOException("Property not set, but mandatory: "
								+ inputName);
					}
					
					// get the complexdata's format information
					ComplexDataDescriptionType format = getComplexDataCombination(input, 
							inputs, context);
					// Complexdata Reference
					if((val instanceof String) && ((String)val).substring(0, 7).equals("http://"))
					{
						executeBuilder
						.addComplexDataReference(
								inputName,
								(String) val,
								format.getSchema(), 
								format.getEncoding(),
								format.getMimeType());
					}
					else
					{
						// get the data object
						String schema = format.getSchema() == null ? "":format.getSchema();
						String encoding = format.getEncoding() == null ? "base64":format.getEncoding();
						IData data = new GenericFileDataBinding(new GenericFileData(new ByteArrayInputStream((byte[])val), format.getMimeType()));
						executeBuilder.addComplexData(inputName, data, schema, encoding, format.getMimeType());
					}
				}
			}
		}
	}
	
	// Set outputs 
	private void _setOutputs(final ProcessDescriptionType processDescription,
			ExecuteRequestBuilder executeBuilder)
	{	
		for(OutputDescriptionType output : processDescription.getProcessOutputs().getOutputArray())
		{
			if(output.isSetComplexOutput())
			{
				// asynchronous request
				//TODO: add asynchronous request setting
				//reference reponse
				if(configBean.isStoreSupported() && configBean.isReferenceRespond())
				{
					executeBuilder.setStoreSupport(output.getIdentifier().getStringValue());
				}
			}
		}
	}
	
	// Resulve outputs
	private void _resolveOuputs(final InvocationContext context,
			final ExecuteDocument executeDoc, 
			final ProcessDescriptionType processDescription,
			final Object responseofExecute, 
			Map<String, T2Reference> outputs)
	throws WPSClientException, ServiceException, IOException
	{
		ReferenceService referenceService = context
				.getReferenceService();
		
		if (responseofExecute instanceof ExecuteResponseDocument)
		{
			ExecuteResponseDocument response = (ExecuteResponseDocument) responseofExecute;
			
			for(net.opengis.wps.x100.OutputDataType output : response.getExecuteResponse().getProcessOutputs().getOutputArray())
			{
				T2Reference t2Ref;
				DataType data = output.getData();
				if(output.isSetReference())
				{
					// reference 
					t2Ref = referenceService.register(output.getReference().getHref(), 0, true, context);
					
					// set the Format of the Complexdata output
					outputs.put(output.getIdentifier().getStringValue()+"_mimetype", referenceService.
							register(output.getReference().getMimeType(), 0, true, context));
					String temp;
					if((temp = output.getReference().getEncoding() )==null)
					{
						temp = "base64";
					}
					outputs.put(output.getIdentifier().getStringValue()+"_encode", referenceService.
							register(temp, 0, true, context));
					if((temp = output.getReference().getSchema() )==null)
					{
						temp = "";
					}
					outputs.put(output.getIdentifier().getStringValue()+"_schema", referenceService.
							register(temp, 0, true, context));
				}
				else if (data.isSetComplexData())
				{
					//String anyData = data.newCursor().getObject().toString();
					String anyData = data.newCursor().getTextValue();
					t2Ref = referenceService.register(anyData.getBytes(), 0, true, context);
					
					// set the Format of the Complexdata output
					outputs.put(output.getIdentifier().getStringValue()+"_mimetype", referenceService.
							register(data.getComplexData().getMimeType(), 0, true, context));
					String temp;
					if((temp = data.getComplexData().getEncoding() )==null)
					{
						temp = "base64";
					}
					outputs.put(output.getIdentifier().getStringValue()+"_encode", referenceService.
							register(temp, 0, true, context));
					if((temp = data.getComplexData().getSchema() )==null)
					{
						temp = "";
					}
					outputs.put(output.getIdentifier().getStringValue()+"_schema", referenceService.
							register(temp, 0, true, context));
					
				}
				else if (data.isSetLiteralData())
				{
					t2Ref = referenceService.register(data.getLiteralData().getStringValue(), 0, true, context);
				}
				else if (data.isSetBoundingBoxData())
				{
					throw new WPSClientException("The returned data type is BoundingBoxDataType but without care");
				}
				else
				{
					throw new WPSClientException("Unknown type of data returned");
				}
				outputs.put(output.getIdentifier().getStringValue(), t2Ref);
			}
		}
		else
		{
			throw new ServiceException(responseofExecute.toString());
		}
		
		return ;
	}
	// get the complexdata's valid format
	public ComplexDataDescriptionType getComplexDataCombination(final InputDescriptionType input, 
			final Map<String, T2Reference> inputs,
			final InvocationContext context)
	{
		// return format
		ComplexDataDescriptionType formatValid = net.opengis.wps.x100.ComplexDataDescriptionType.Factory.newInstance();
		// default format of complexdata
		ComplexDataDescriptionType defaultFormat = input.getComplexData().getDefault().getFormat();
		// complexdata format allowed
		ComplexDataDescriptionType[] supportedFormat = input.getComplexData().getSupported().getFormatArray();
		
		ReferenceService referenceService = context
				.getReferenceService();
		
		String identifier = input.getIdentifier().getStringValue();
		
		String schema = inputs.get( identifier+"_schema") == null ? "" : (String) referenceService.
				renderIdentifier ( inputs.get( identifier+"_schema"), String.class, context );
		
		String encode = inputs.get( identifier+"_encode") == null ? "" : (String) referenceService.
				renderIdentifier ( inputs.get( identifier+"_encode"), String.class, context );
		
		String mimetype = inputs.get(identifier+"_mimetype")==null? "" : (String) referenceService.
				renderIdentifier ( inputs.get(identifier+"_mimetype"), String.class, context );
		
		formatValid.setSchema(schema);
		formatValid.setEncoding(encode);
		formatValid.setMimeType(mimetype);
		
		// if the default format
		if ( formatValid.equals(defaultFormat))
		{
			return formatValid;
		}
		
		// if the set format not in the SupportedFormat list then return the default format
		boolean isValidSchema = false;
		for(ComplexDataDescriptionType formattype : supportedFormat)
		{
			if ( formattype.equals( formatValid) )
			{
				isValidSchema = true;
				break;
			}
		}
		
		return isValidSchema? formatValid :defaultFormat;
	}

}
