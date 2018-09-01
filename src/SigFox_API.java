import java.io.IOException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;


/*
 * Java code to get device data from SigFox Backend 
 * Author: Nishant Poddar 
 * 
 */



public class SigFox_API{

	static final String USERNAME     = "USER_NAME_FROM_GROUP_API";
	static final String PASSWORD     = "PASWWORD_FROM_GROUP_API";
	static final String LOGINURL     = "https://backend.sigfox.com/api/devices/DEV-ID/messages";
	

	public static void main(String[] args) {

		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials
		= new UsernamePasswordCredentials(USERNAME, PASSWORD);

		provider.setCredentials(AuthScope.ANY, credentials);

		HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();


		HttpResponse response = null;



		HttpGet request = new HttpGet(LOGINURL);

		// add request header

		try {

			response = httpclient.execute(request);
		} catch (ClientProtocolException cpException) {
			cpException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

		// verify response is HTTP OK
		final int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("error: "+statusCode);
			return;
		}

		String getResult = null;
		try {
			getResult = EntityUtils.toString(response.getEntity());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		JSONObject jsonObject = null;
		String device = null;
		String data = null;
		String time = null;
		try {
			jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
			for(int i=0;i<jsonObject.getJSONArray("data").length();i++) {
				System.out.println(jsonObject.getJSONArray("data").getJSONObject(i).get("device"));
				System.out.println(jsonObject.getJSONArray("data").getJSONObject(i).get("data"));
				System.out.println(jsonObject.getJSONArray("data").getJSONObject(i).get("time"));
				System.out.println();
			}

		} catch (JSONException jsonException) {
			jsonException.printStackTrace();

			request.releaseConnection();
		}
	}
}