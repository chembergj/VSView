package dk.vsview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import dk.vsview.domain.OnlineClient;
import dk.vsview.domain.OnlineClient.ClientType;
import dk.vsview.domain.OnlineData;
import dk.vsview.domain.ServerData;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class OnlineDataProviderTask extends
		AsyncTask<Integer, Void, OnlineData> {

	IOnlineDataConsumer onlineDataConsumer;
	private static OnlineData cachedOnlineData = new OnlineData();

	final String onlineDataUrl = "http://info.vroute.net/vatsim-data.txt";
	//final String onlineDataUrl = "http://www.net-flyer.net/DataFeed/vatsim-data.txt";
	int mode;
	ServerData serverData;
	
	public OnlineDataProviderTask(IOnlineDataConsumer onlineDataConsumer) {
		this.onlineDataConsumer = onlineDataConsumer;
	}

	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}
	
	@Override
	protected OnlineData doInBackground(Integer... params) {

		if (cachedOnlineData.isOutdated()) {

			cachedOnlineData = new OnlineData();
			mode = params[0];

			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			HttpGet getRequest = new HttpGet(serverData.getRandomCompleteDataFileURLs());

			try {
				HttpResponse response = client.execute(getRequest);
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					Log.w("OnlineDataProviderTask", "Error " + statusCode
							+ " while retrieving online data");
					return null;
				}

				final HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent()));

				OnlineDataParser parser = new OnlineDataParser();
				parser.parseOnlineData(reader);
				cachedOnlineData = parser.getOnlineData();
			} catch (Exception e) {
				// Could provide a more explicit error message for IOException
				// or IllegalStateException
				getRequest.abort();
				Log.w("OnlineDataProviderTask", "Error while retrieving data"
						+ e.toString());
				cancel(true);
			} finally {
				if (client != null) {
					client.close();
				}
			}
		}

		return cachedOnlineData;
	}



	protected void onPostExecute(OnlineData data) {
		if (mode == 0)
			onlineDataConsumer.dataFetched(data);
	}

}
