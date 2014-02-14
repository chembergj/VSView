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

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import dk.vsview.domain.Booking;
import dk.vsview.domain.BookingData;
import dk.vsview.domain.ServerData;

public class ServerDataProviderTask extends AsyncTask<Void, Void, ServerData> {

	IServerDataConsumer serverDataConsumer;
	final String serverDataUrl = "http://status.vatsim.net/status.txt";
	private static ServerData cachedServerData;
	
	public ServerDataProviderTask(IServerDataConsumer serverDataConsumer) {
		this.serverDataConsumer = serverDataConsumer;
	}

	@Override
	protected ServerData doInBackground(Void... params) {

		if(cachedServerData != null) {
			return cachedServerData;
		}
		
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		ServerData result = new ServerData();

		HttpGet getRequest = new HttpGet(serverDataUrl);
		try {

			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ServerDataProviderTask", "Error " + statusCode
						+ " while retrieving booking data");
				return null;
			}

			final HttpEntity entity = response.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent()));

			ServerDataParser parser = new ServerDataParser();
			parser.parseServerData(reader);
			cachedServerData = parser.getServerData();
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			Log.w("ServerDataProviderTask",
					"Error while retrieving data" + e.toString());
			cancel(true);
		} finally {
			if (client != null) {
				client.close();
			}
		}

		return cachedServerData;
	}


	protected void onPostExecute(ServerData data) {
		serverDataConsumer.dataFetched(data);
	}
}
