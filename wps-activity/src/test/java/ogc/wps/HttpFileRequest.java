package ogc.wps;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

public class HttpFileRequest {

	public static byte[] getBase64RasterFromHttp(String url)
			throws IOException
	{		
		return Base64.encodeBase64( getRasterFromHttp(url) );
	}
	
	public static byte[] getRasterFromHttp(String url)
			throws IOException
	{
		URL resultRef = new URL(url);
		
		byte[] res = null;
		
		res = new byte[resultRef.openStream().available()];
		
		resultRef.openStream().read(res);
		
		return res;
	}
	
	public static void main(String[] arg)
	{
		try
		{
			HttpFileRequest.getRasterFromHttp("http://127.0.0.1/wps/wpsoutputs/maxlike_result.tif");
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
