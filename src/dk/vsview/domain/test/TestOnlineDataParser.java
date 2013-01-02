package dk.vsview.domain.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dk.vsview.OnlineDataParser;
import dk.vsview.domain.OnlineClient;
import dk.vsview.domain.OnlineData;
import junit.framework.TestCase;

public class TestOnlineDataParser extends TestCase {

	OnlineDataParser parser = new OnlineDataParser();
	OnlineData onlineData;
	int[] friendCIDs = { 879396, 880543, 989939, 1008925, 1217411, 1224384 };
	
	protected void setUp() throws Exception {
		super.setUp();
		
		FileInputStream fstream;
		fstream = new FileInputStream("testdata/vatsim-data.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		parser.parseOnlineData(br);
		fstream.close();
		onlineData = parser.getOnlineData();
	}

	public void testGetOnlineClients() {
		assertEquals(661, onlineData.getOnlineClients().size());
	}

	public void testGetFriendsOnlineATCs() throws IOException {

		// ARRANGE
		List<Integer> friendCidList = new ArrayList<Integer>();
		for (int cid : friendCIDs) {
			friendCidList.add(cid);
		}
		
		// ACT
		List<OnlineClient> friendsATCOnline = onlineData.getFriendsOnlineATC(friendCidList);

		// ASSERT
		assertEquals(7, friendsATCOnline.size());
		assertEquals(friendCIDs[2], friendsATCOnline.get(0).getCID());
		assertEquals("EKBI_ATIS", friendsATCOnline.get(0).getCallsign());
		assertEquals(friendCIDs[0], friendsATCOnline.get(1).getCID());
		assertEquals("EKCH_APP", friendsATCOnline.get(1).getCallsign());
		assertEquals(friendCIDs[0], friendsATCOnline.get(2).getCID());
		assertEquals("EKCH_ATIS", friendsATCOnline.get(2).getCallsign());
		assertEquals(friendCIDs[2], friendsATCOnline.get(3).getCID());
		assertEquals("EKDK_CTR", friendsATCOnline.get(3).getCallsign());
		assertEquals(friendCIDs[1], friendsATCOnline.get(4).getCID());
		assertEquals("ESGG_ATIS", friendsATCOnline.get(4).getCallsign());
		assertEquals(friendCIDs[1], friendsATCOnline.get(5).getCID());
		assertEquals("ESOS_CTR", friendsATCOnline.get(5).getCallsign());
		assertEquals(friendCIDs[3], friendsATCOnline.get(6).getCID());
		assertEquals("NTS_OBS", friendsATCOnline.get(6).getCallsign());
	}
	
	public void testGetFriendsOnlinePilots() throws IOException {

		// ARRANGE
		List<Integer> friendCidList = new ArrayList<Integer>();
		friendCidList.add(827467);

		// ACT
		List<OnlineClient> friendsOnline = onlineData.getFriendsOnlinePilots(friendCidList);

		// ASSERT
		OnlineClient client = friendsOnline.get(0);
		assertEquals(1, friendsOnline.size());
		assertEquals(827467, client.getCID());
		assertEquals("AAL1503", client.getCallsign());
		assertEquals("Test Testpilot ETNG", client.getRealname());
		assertEquals(432, client.getGroundspeed());
		assertEquals("34000", client.getPlannedAltitude());
		assertEquals(33876, client.getAltitude());
		assertEquals("+VFPS+/V/MD82/M-SRWY/C REG/N473AA RMK/TCAS", client.getPlannedRemarks());
		assertEquals("KPIT", client.getPlannedDepAirport());
		assertEquals("KDFW", client.getPlannedDestAirport());
		assertEquals("DILNE TVT ROD VHP J24 STL RZC FSM BYP5", client.getPlannedRoute());
	}

}

