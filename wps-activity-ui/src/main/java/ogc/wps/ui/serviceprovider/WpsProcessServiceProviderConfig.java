package ogc.wps.ui.serviceprovider;

import java.net.URI;

import net.sf.taverna.t2.lang.beans.PropertyAnnotated;
import net.sf.taverna.t2.lang.beans.PropertyAnnotation;

public class WpsProcessServiceProviderConfig extends PropertyAnnotated {
	private URI uri;
	
	@PropertyAnnotation(preferred = true)
	public URI getUri() {
		return uri;
	}
	
	public void setUri(URI uri) {
		this.uri = uri;
	}

}
