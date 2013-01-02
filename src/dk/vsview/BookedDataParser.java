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
import dk.vsview.domain.TimestampedData;

public class BookedDataParser extends DataParser {

	BookingData bookedData = new BookingData();
	
	public BookingData getBookingData() {
		return bookedData;
	}

	public void parseOnlineData(BufferedReader reader) throws IOException {

		String section = skipCommentsAndGetNextSection(reader);

		while (section != null) {
			if (section.equals("!GENERAL:"))
				readGeneral(reader, bookedData);
			else if (section.equals("!CLIENTS:"))
				bookedData.setBookings(readClients(reader));
			else if (section.equals("!SERVERS:"))
				readServers(reader);

			section = skipCommentsAndGetNextSection(reader);
		}
	}

	private List<Booking> readClients(BufferedReader reader)
			throws IOException {

		List<Booking> clients = new ArrayList<Booking>();
		String line;
		while (!(line = reader.readLine()).equals(";")) {
			Booking client = new Booking();
			String[] values = line.split(":");
			client.setCallsign(values[0]);
			client.setCid(Integer.parseInt(values[1]));
			client.setRealname(values[2]);
			client.setEndtime(values[14]);
			client.setDate(values[16]);
			client.setStarttime(values[37]);
			client.setClienttype((Booking.ClientType.valueOf(values[3].length() == 0 ? "UNKNOWN"
					: values[3])));
			clients.add(client);
		}

		return clients;
	}

	protected void readGeneral(BufferedReader reader, TimestampedData tsData) throws IOException {

		String reloadLine = reader.readLine(); // RELOAD
		tsData.setMinutesToReload(Integer.parseInt(reloadLine.substring(9)));

		String updateLine = reader.readLine(); // UPDATE
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddHHmmss")
				.withZone(DateTimeZone.UTC);
		tsData.setUpdateDate(fmt.parseDateTime(updateLine.substring(9)));

		// Skip the remains of the block
		String line = reader.readLine();
		while (line != null && !line.startsWith(";")) {
			line = reader.readLine();
		}
	}
}
