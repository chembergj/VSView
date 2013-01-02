package dk.vsview.domain.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import dk.vsview.OnlineDataParser;
import dk.vsview.domain.OnlineClient;
import dk.vsview.domain.OnlineClient.ClientType;
import dk.vsview.domain.OnlineData;
import junit.framework.TestCase;

public class TestOnlineData extends TestCase {


	public void testIsOutdatedWithCurrentData() {

		OnlineData sut = new OnlineData();
		sut.setMinutesToReload(5);
		sut.setUpdateDate(new DateTime());
		assertFalse(sut.isOutdated());
	}

	public void testIsOutdatedWithOutdatedData() {
		OnlineData sut = new OnlineData();
		sut.setMinutesToReload(5);
		sut.setUpdateDate(new DateTime().minusMinutes(10));
		assertTrue(sut.isOutdated());
	}


	public void testGetOnlineATCForFavoriteFIRs() {

		// ARRANGE
		OnlineData sut = new OnlineData();
		List<OnlineClient> clientList = new ArrayList<OnlineClient>();
		String[] callsigns = {
				// Callsigns to be included
				"EKDK_CTR", "ESOS_CTR", "ENOS_CTR", "EFES_CTR", "BIKF_TWR",
				// Callsigns not to be included
				"ESSEX_APP", "ES_OBS" };

		for (String callsign : callsigns) {
			OnlineClient client = new OnlineClient();
			client.setCallsign(callsign);
			client.setClienttype(ClientType.ATC);
			clientList.add(client);
		}

		sut.setOnlineClients(clientList);

		// ACT
		List<OnlineClient> filteredClients = sut.getOnlineATCForFavoriteFIRs();

		// ASSERT
		assertEquals(5, filteredClients.size());
		for (int i = 0; i < 5; i++) {
			assertEquals(filteredClients.get(i).getCallsign(), callsigns[i]);
		}
	}

}
