/**
 * 
 */
package ogc.wps;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import net.opengis.ows.x11.CodeType;
import net.opengis.wps.x100.ProcessBriefType;
import net.opengis.wps.x100.ProcessDescriptionType;

import ogc.wps.WpsProcessActivityConfigurationBean;

import org.n52.wps.client.WPSClientException;
import org.n52.wps.client.WPSClientSession;

/**
 * @author zzpwelkin
 * 
 */
public class ExampleActivityConfigurationBeanFactory {

	public static WpsProcessActivityConfigurationBean newInstance(String url,
			String identifier, String version) throws WPSClientException,
			IOException {
		WpsProcessActivityConfigurationBean configBean = new WpsProcessActivityConfigurationBean();

		WPSClientSession wpsClient = WPSClientSession.getInstance();

		wpsClient.connect(url);

		ProcessDescriptionType describeProcess = wpsClient
				.getProcessDescription(url, identifier);

		// store and status supported
		configBean.setStoreSupported(describeProcess.getStoreSupported());

		configBean.setStatusSupported(false);

		if (describeProcess.getStoreSupported()) {
			configBean.setStatusSupported(describeProcess.getStatusSupported());
		}

		// process brief
		ProcessBriefType brief = ProcessBriefType.Factory.newInstance();
		CodeType code = CodeType.Factory.newInstance();
		code.setStringValue(identifier);
		brief.setIdentifier(code);
		brief.setProcessVersion(version);
		configBean.setWpsUri(URI.create(url));
		configBean.setProcessBrief(brief);

		// process inputs
		configBean.setInputList(Arrays.asList(describeProcess.getDataInputs()
				.getInputArray()));

		// process outputs
		configBean.setOutputlist(Arrays.asList(describeProcess
				.getProcessOutputs().getOutputArray()));
		return configBean;
	}
}
