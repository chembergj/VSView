package dk.vsview;

import dk.vsview.domain.OnlineClient;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PilotInfoDialog extends DialogFragment {
	
		OnlineClient onlineClient;
		
		public PilotInfoDialog(OnlineClient onlineClient) {
			this.onlineClient = onlineClient;
		}
		
		private TableRow createRow(Context context, String label, String value)
		{
			TableRow row = new TableRow(context);

			TextView captionView = new TextView(context);
			captionView.setPadding(10, 0, 10, 0);
			captionView.setText(label);
			row.addView(captionView);

			TextView valueView = new TextView(context);
			valueView.setPadding(10, 0, 10, 0);
			valueView.setText(value);
			row.addView(valueView);
			
			return row;
		}
		
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	    	View pilotInfoView = getActivity().getLayoutInflater().inflate(R.layout.pilotinfo, null);
	    	TableLayout pilotInfo = (TableLayout)pilotInfoView.findViewById(R.id.tableLayoutPilotInfo);

			pilotInfo.addView(createRow(pilotInfo.getContext(), "Name:", onlineClient.getRealname()));
			pilotInfo.addView(createRow(pilotInfo.getContext(), "Altitude:", Integer.toString(onlineClient.getAltitude())));
			pilotInfo.addView(createRow(pilotInfo.getContext(), "Ground speed:", Integer.toString(onlineClient.getGroundspeed())));
			pilotInfo.addView(createRow(pilotInfo.getContext(), "Flightplan", ""));
			pilotInfo.addView(createRow(pilotInfo.getContext(), "Departure:", onlineClient.getPlannedDepAirport()));
			pilotInfo.addView(createRow(pilotInfo.getContext(), "Arrival:", onlineClient.getPlannedDestAirport()));
			pilotInfo.addView(createRow(pilotInfo.getContext(), "Route:", onlineClient.getPlannedRoute()));
			pilotInfo.addView(createRow(pilotInfo.getContext(), "Remarks:", onlineClient.getPlannedRemarks()));
			pilotInfo.addView(createRow(pilotInfo.getContext(), "Cruise alt.:", onlineClient.getPlannedAltitude()));
	    	
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(onlineClient.getCallsign())
	        		.setView(pilotInfo)
	               .setPositiveButton(R.string.close, new OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // Close dialog
	                   }
	               })
	               ;
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
}
