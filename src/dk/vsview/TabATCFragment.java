package dk.vsview;

import java.util.List;

import dk.vsview.domain.Booking;
import dk.vsview.domain.BookingData;
import dk.vsview.domain.OnlineClient;
import dk.vsview.domain.OnlineData;
import dk.vsview.domain.ServerData;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TabATCFragment extends Fragment  implements IOnlineDataConsumer, IBookingDataConsumer, IServerDataConsumer {

	private final class ATCLoadCancelListener implements OnCancelListener {
		private final BookedDataProviderTask bookedDataTask;
		private final OnlineDataProviderTask onlineDataTask;

		private ATCLoadCancelListener(BookedDataProviderTask bookedDataTask,
				OnlineDataProviderTask onlineDataTask) {
			this.bookedDataTask = bookedDataTask;
			this.onlineDataTask = onlineDataTask;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			onlineDataTask.cancel(true);
			bookedDataTask.cancel(true);
		}
	}

	TableLayout tableLayoutOnlineAtc;
	TableLayout tableLayoutBookedAtc;
	ProgressDialog progressDialog;
	OnlineDataProviderTask onlineDataTask;
	BookedDataProviderTask bookedDataTask;
	ServerDataProviderTask serverdataProviderTask;
	ServerData serverData;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.atc, container, false);
		
		if(savedInstanceState == null) {
			loadATCData(view);
		}
		
		return view;
	}

	public void loadATCData() {
		loadATCData(getView());
	}
	
	private void loadATCData(View view) {
		startLoadOfBookingsAndServerData(view);
		setupTableLayouts(view);
	}
	
	private void startLoadOfBookingsAndServerData(View view) {
		bookedDataTask = new BookedDataProviderTask(this);
		serverdataProviderTask = new ServerDataProviderTask(this);
		onlineDataTask = new OnlineDataProviderTask(this);
		
		serverdataProviderTask.execute();
		bookedDataTask.execute();		
	}

	private void setupTableLayouts(View view) {
		tableLayoutOnlineAtc = (TableLayout) view
				.findViewById(R.id.tableLayoutOnlineAtc);
		tableLayoutBookedAtc = (TableLayout) view
				.findViewById(R.id.tableLayoutBookedAtc);

		progressDialog = new ProgressDialog(view.getContext());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Loading online activity and bookings...");
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new ATCLoadCancelListener(bookedDataTask, onlineDataTask));
		progressDialog.show();
	}

	/* 
	 * Called when serverdata has been fetched
	 * Triggers the reading of onlineData
	 * @see dk.vsview.IServerDataConsumer#dataFetched(dk.vsview.domain.ServerData)
	 */
	@Override
	public void dataFetched(ServerData serverData) {
		onlineDataTask.setServerData(serverData);
		onlineDataTask.execute(0);
	}

	@Override
	public void dataFetched(BookingData data) {
		showBookedATCs(data);
	}

	@Override
	public void dataFetched(OnlineData data) {
		showOnlineATCs(data);
	}

	
	/**
	 * Called when OnlineDataProviderTask has read online clients
	 * 
	 * @param data
	 */
	public void showOnlineATCs(OnlineData data) {
		Context context = getView().getContext();
		tableLayoutOnlineAtc.removeAllViews();
		
		List<OnlineClient> onlineATCs = data.getOnlineATCForFavoriteFIRs();

		if (onlineATCs.isEmpty()) {
			TableRow row = new TableRow(this.getView().getContext());
			TextView noRowView = new TextView(this.getView().getContext());
			noRowView.setText("No controllers online");
			row.addView(noRowView);
			tableLayoutOnlineAtc.addView(row);
		} else {
			for (OnlineClient client : onlineATCs) {
				TableRow row = new TableRow(context);

				TextView callsignView = new TextView(context);
				callsignView.setPadding(10, 0, 10, 0);
				callsignView.setText(client.getCallsign());
				row.addView(callsignView);

				TextView freqView = new TextView(context);
				freqView.setPadding(10, 0, 10, 0);
				freqView.setText(client.getFrequency());
				row.addView(freqView);

				TextView nameView = new TextView(context);
				nameView.setText(client.getRealname());
				nameView.setPadding(10, 0, 10, 0);
				row.addView(nameView);
				tableLayoutOnlineAtc.addView(row);
			}
		}

		if (bookedDataTask.getStatus() == Status.FINISHED)
			progressDialog.cancel();
	}

	/**
	 * Called when BookedDataProviderTask has read online clients
	 * 
	 * @param data
	 */
	public void showBookedATCs(BookingData data) {
		Context context = getView().getContext();
		tableLayoutBookedAtc.removeAllViews();
		
		String currentDate = null;
		final int margin = 12;
		for (Booking client : data.getFilteredBookings()) {

			if (currentDate == null || !currentDate.equals(client.getDate())) {
				TableRow row = new TableRow(context);
				TextView dateView = new TextView(context);
				dateView.setPadding(7, 0, 7, 0);
				dateView.setText(String.format("%s-%s-%s", client.getDate()
						.substring(0, 4), client.getDate().substring(4, 6),
						client.getDate().substring(6, 8)));
				row.addView(dateView);
				tableLayoutBookedAtc.addView(row);
				currentDate = client.getDate();
			}

			TableRow row = new TableRow(context);

			TextView callsignView = new TextView(context);
			callsignView.setPadding(margin, 0, margin, 0);
			callsignView.setText(client.getCallsign());
			row.addView(callsignView);

			TextView nameView = new TextView(context);
			nameView.setText(client.getRealname());
			nameView.setPadding(margin, 0, margin, 0);
			row.addView(nameView);

			TextView startView = new TextView(context);
			startView.setPadding(margin, 0, margin, 0);
			startView.setText(client.getStarttime());
			row.addView(startView);

			TextView endView = new TextView(context);
			endView.setPadding(margin, 0, margin, 0);
			endView.setText(client.getEndtime());
			row.addView(endView);

			tableLayoutBookedAtc.addView(row);
		}

		if (onlineDataTask.getStatus() == Status.FINISHED)
			progressDialog.cancel();
	}

}
