package dk.vsview;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.util.Log;

import dk.vsview.domain.OnlineClient;
import dk.vsview.domain.TimestampedData;
import dk.vsview.domain.OnlineClient.ClientType;
import dk.vsview.domain.OnlineClient.FacilityType;
import dk.vsview.domain.OnlineData;

public class OnlineDataParser extends DataParser {

	OnlineData onlineData = new OnlineData();

	public OnlineData getOnlineData() {
		return onlineData;
	}

	public void parseOnlineData(BufferedReader reader) throws IOException {

		String section = skipCommentsAndGetNextSection(reader);

		while (section != null) {
			if (section.equals("!GENERAL:"))
				readGeneral(reader, onlineData);
			else if (section.equals("!VOICE SERVERS:"))
				readVoiceServers(reader);
			else if (section.equals("!CLIENTS:"))
				onlineData.setOnlineClients(readClients(reader));
			else if (section.equals("!SERVERS:"))
				readServers(reader);
			else if (section.equals("!PREFILE:"))
				readPrefile(reader);

			section = skipCommentsAndGetNextSection(reader);
		}
	}

	private List<OnlineClient> readClients(BufferedReader reader)
			throws IOException {

		List<OnlineClient> clients = new ArrayList<OnlineClient>();
		String line;
		while (!(line = reader.readLine()).equals(";")) {
			try {
				OnlineClient client = new OnlineClient();
				String[] values = line.split(":");
				client.setCallsign(values[0]);
				client.setCID(Integer.parseInt(values[1]));
				client.setRealname(values[2]);
				client.setClienttype((ClientType.valueOf(values[3].length() == 0 ? "UNKNOWN"
						: values[3])));
				client.setFacilityType(FacilityType.parseString(values[16]));
				client.setFrequency(values[4]);
				client.setAltitude(Integer.parseInt(values[7]));
				if(values[8].length() > 0)
					client.setGroundspeed(Integer.parseInt(values[8]));
				client.setPlannedDepAirport(values[11]);
				client.setPlannedAltitude(values[12]);
				client.setPlannedDestAirport(values[13]);
				client.setPlannedRemarks(values[29]);
				client.setPlannedRoute(values[30]);
				clients.add(client);
			}
			catch(Exception e) {
				Log.w("OnlineDataParser", "Error parsing online data" + line + "  Error: " + e.toString());
			}
		}

		return clients;
	}

	private void readGeneral(BufferedReader reader, TimestampedData tsData) throws IOException {

		reader.readLine(); // Skip VERSION
		String reloadLine = reader.readLine(); // RELOAD
		tsData.setMinutesToReload(Integer.parseInt(reloadLine
				.substring(9)));

		String updateLine = reader.readLine(); // UPDATE
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddHHmmss")
				.withZone(DateTimeZone.UTC);
		tsData.setUpdateDate(fmt.parseDateTime(updateLine
				.substring(9)));

		// Skip the remains of the block
		String line = reader.readLine();
		while (line != null && !line.startsWith(";")) {
			line = reader.readLine();
		}
	}

}
