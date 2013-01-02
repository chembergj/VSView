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

public class BookedDataProviderTask extends AsyncTask<Void, Void, BookingData> {

	IBookingDataConsumer bookingDataConsumer;
	final String bookingDataUrl = "http://vatbook.euroutepro.com/servinfo.asp";
	private static BookingData cachedBookingData = new BookingData();
	
	public BookedDataProviderTask(IBookingDataConsumer bookingDataConsumer) {
		this.bookingDataConsumer = bookingDataConsumer;
	}

	@Override
	protected BookingData doInBackground(Void... params) {

		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		BookingData result = new BookingData();

		HttpGet getRequest = new HttpGet(bookingDataUrl);
		try {

			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("BookedDataProviderTask", "Error " + statusCode
						+ " while retrieving booking data");
				return null;
			}

			final HttpEntity entity = response.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent()));

			BookedDataParser parser = new BookedDataParser();
			parser.parseOnlineData(reader);
			cachedBookingData = parser.getBookingData();
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			Log.w("OnlineDataProviderTask",
					"Error while retrieving data" + e.toString());
			cancel(true);
		} finally {
			if (client != null) {
				client.close();
			}
		}

		return cachedBookingData;
	}


	protected void onPostExecute(BookingData data) {
		bookingDataConsumer.dataFetched(data);
	}
}
