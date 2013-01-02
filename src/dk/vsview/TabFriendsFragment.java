package dk.vsview;

import java.util.ArrayList;
import java.util.List;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Typeface;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import dk.vsview.domain.Booking;
import dk.vsview.domain.BookingData;
import dk.vsview.domain.OnlineClient;
import dk.vsview.domain.OnlineData;

public class TabFriendsFragment extends Fragment implements IOnlineDataConsumer, IBookingDataConsumer {

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
	TableLayout tableLayoutOnlinePilots;
	ProgressDialog progressDialog;
	OnlineDataProviderTask onlineDataTask;
	BookedDataProviderTask bookedDataTask;
	private final int[] friendCIDs = { 832365, 848603, 857075, 861112, 862571, 869132,
			879396, 880543, 881843, 931070, 936927, 968376, 989939, 1002035, 1003540,
			1008925, 1010196, 1012739, 1020902, 1060884, 1062157, 1077756, 1080430, 1107796,
			1114532, 1124354, 1127083, 1130514, 1145889, 1148433, 1154817, 1177256, 1185615, 
			1217411, 1224384};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.friends, container, false);
		
		if(savedInstanceState == null) {
			loadATCData(view);
		}
		
		return view;
	}

	public void loadATCData() {
		loadATCData(getView());
	}

	private void loadATCData(View view) {

		onlineDataTask = new OnlineDataProviderTask(this);
		bookedDataTask = new BookedDataProviderTask(this);
		
		tableLayoutOnlineAtc = (TableLayout) view
				.findViewById(R.id.tableLayoutFriendsOnlineAtc);
		tableLayoutOnlinePilots = (TableLayout) view
			.findViewById(R.id.tableLayoutFriendsOnlinePilots);
		tableLayoutBookedAtc = (TableLayout) view
				.findViewById(R.id.tableLayoutFriendsBookedAtc);

		onlineDataTask.execute(0);
		bookedDataTask.execute();

		progressDialog = new ProgressDialog(view.getContext());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Loading online activity and bookings...");
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new ATCLoadCancelListener(bookedDataTask, onlineDataTask));
		progressDialog.show();
	}

	@Override
	public void dataFetched(OnlineData data) {
		showFriendsOnlineActivity(data);
	}

	
	/**
	 * Called when OnlineDataProviderTask has read online clients
	 * 
	 * @param data
	 */
	public void showFriendsOnlineActivity(OnlineData data) {
		Context context = getView().getContext();
		
		List<Integer> friendCidList = new ArrayList<Integer>();
		for (int cid : friendCIDs) {
			friendCidList.add(cid);
		}
		
		List<OnlineClient> onlineATCs = data.getFriendsOnlineATC(friendCidList);
		if (onlineATCs.isEmpty()) {
			TableRow row = new TableRow(this.getView().getContext());
			TextView noRowView = new TextView(this.getView().getContext());
			noRowView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
			noRowView.setText("No friends online as controllers");
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

		List<OnlineClient> onlinePilots = data.getFriendsOnlinePilots(friendCidList);
		if (onlinePilots.isEmpty()) {
			TableRow row = new TableRow(this.getView().getContext());
			TextView noRowView = new TextView(this.getView().getContext());
			noRowView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
			noRowView.setText("No friends online as pilots");
			row.addView(noRowView);
			tableLayoutOnlinePilots.addView(row);
		} else {
			for (final OnlineClient client : onlinePilots) {
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
				row.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						DialogFragment newFragment = new PilotInfoDialog(client);
					    newFragment.show(getFragmentManager(), "dialog");
					}
				});
				tableLayoutOnlinePilots.addView(row);
			}
		}
		
		if (bookedDataTask.getStatus() == Status.FINISHED)
			progressDialog.cancel();
	}

	@Override
	public void dataFetched(BookingData data) {
		showBookedATCs(data);
	}
	
	/**
	 * Called when BookedDataProviderTask has read online clients
	 * 
	 * @param data
	 */
	public void showBookedATCs(BookingData data) {
		
		
		Context context = getView().getContext();
		
		List<Integer> friendCidList = new ArrayList<Integer>();
		for (int cid : friendCIDs) {
			friendCidList.add(cid);
		}
		
		String currentDate = null;
		final int margin = 12;
		for (Booking client : data.getFriendsBookedATC(friendCidList)) {

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
