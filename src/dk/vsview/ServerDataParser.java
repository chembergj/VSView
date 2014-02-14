package dk.vsview;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import dk.vsview.domain.Booking;
import dk.vsview.domain.BookingData;
import dk.vsview.domain.OnlineData;
import dk.vsview.domain.ServerData;
import dk.vsview.domain.TimestampedData;

public class ServerDataParser extends DataParser {

	ServerData serverData = new ServerData();
	
	public ServerData getServerData() {
		return serverData;
	}

	public void parseServerData(BufferedReader reader) throws IOException {

		serverData.setCompleteDataFileURLs(new ArrayList<String>());
		serverData.setServerlistDataFileURLs(new ArrayList<String>());
		
		String line = reader.readLine();
		while (line != null) {
			if(line.startsWith("url0=")) {
				serverData.getCompleteDataFileURLs().add(line.substring(5));
			}
			else if(line.startsWith("url1=")) {
				serverData.getServerlistDataFileURLs().add(line.substring(5));
			}
			
			line = reader.readLine();
		}
	}
}
