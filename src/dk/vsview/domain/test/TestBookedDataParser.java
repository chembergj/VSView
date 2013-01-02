package dk.vsview.domain.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dk.vsview.BookedDataParser;
import dk.vsview.OnlineDataParser;
import dk.vsview.domain.Booking;
import dk.vsview.domain.BookingData;
import dk.vsview.domain.OnlineClient;
import dk.vsview.domain.OnlineData;
import junit.framework.TestCase;

public class TestBookedDataParser extends TestCase {

	BookedDataParser parser = new BookedDataParser();
	BookingData bookingData;
	int[] friendCIDs = { 879396, 880543, 989939, 1008925, 1217411, 1224384, 1138158, 1003540, 1012739, 1185615};
	
	protected void setUp() throws Exception {
		super.setUp();
		
		FileInputStream fstream;
		fstream = new FileInputStream("testdata/bookings.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		parser.parseOnlineData(br);
		fstream.close();
		bookingData = parser.getBookingData();
	}

	public void testGetBookings() {
		assertEquals(148, bookingData.getBookings().size());
	}

	public void testGetFriendsBookedATCs() throws IOException {

		// ARRANGE
		List<Integer> friendCidList = new ArrayList<Integer>();
		for (int cid : friendCIDs) {
			friendCidList.add(cid);
		}
		
		// ACT
		List<Booking> friendsATCOnline = bookingData.getFriendsBookedATC(friendCidList);

		// ASSERT
		assertEquals(7, friendsATCOnline.size());
		assertEquals(friendCIDs[7], friendsATCOnline.get(0).getCID());
		assertEquals("EKCH_APP", friendsATCOnline.get(0).getCallsign());
		
		assertEquals(friendCIDs[3], friendsATCOnline.get(1).getCID());
		assertEquals("EKCH_TWR", friendsATCOnline.get(1).getCallsign());
		
		assertEquals(friendCIDs[8], friendsATCOnline.get(2).getCID());
		assertEquals("EKDK_CTR", friendsATCOnline.get(2).getCallsign());
		
		assertEquals(friendCIDs[6], friendsATCOnline.get(3).getCID());
		assertEquals("EKBI_TWR", friendsATCOnline.get(3).getCallsign());
		
		assertEquals(friendCIDs[0], friendsATCOnline.get(4).getCID());
		assertEquals("EKDK_CTR", friendsATCOnline.get(4).getCallsign());
		
		assertEquals(friendCIDs[9], friendsATCOnline.get(5).getCID());
		assertEquals("EKCH_TWR", friendsATCOnline.get(5).getCallsign());
		
		assertEquals(friendCIDs[6], friendsATCOnline.get(6).getCID());
		assertEquals("EKCH_APP", friendsATCOnline.get(6).getCallsign());
	}
	
	public void testGetFriendsBookedPilots() throws IOException {

		// ARRANGE
		List<Integer> friendCidList = new ArrayList<Integer>();
		for (int cid : friendCIDs) {
			friendCidList.add(cid);
		}
		// ACT
		List<Booking> friendsBookings = bookingData.getFriendsBookedPilots(friendCidList);

		// ASSERT
		assertEquals(0, friendsBookings.size());
	}

}
