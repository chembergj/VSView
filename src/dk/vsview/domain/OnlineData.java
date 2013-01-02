package dk.vsview.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import dk.vsview.domain.OnlineClient.ClientType;

public class OnlineData extends TimestampedData {
	
	private final static String positionPrefixRegex = "^(E[KNSF]|BI)\\w\\w_\\w+$";
	
	List<OnlineClient> onlineClients;


	public void setOnlineClients(List<OnlineClient> onlineClients) {
		this.onlineClients = onlineClients;
	}

	public List<OnlineClient> getOnlineClients() {
		return onlineClients;
	}

	public List<OnlineClient> getOnlineATCForFavoriteFIRs() {
		List<OnlineClient> atcs = new ArrayList<OnlineClient>();

		for (OnlineClient client : onlineClients) {

			if (client.getClienttype() == ClientType.ATC
					&& Pattern.matches(positionPrefixRegex,
							client.getCallsign()))
				atcs.add(client);
		}

		return atcs;
	}

	public List<OnlineClient> getFriendsOnlineATC(List<Integer> friendsCids) {
		List<OnlineClient> atcs = new ArrayList<OnlineClient>();

		for (OnlineClient client : onlineClients) {

			if (client.getClienttype() == ClientType.ATC
					&& friendsCids.contains(client.getCID()))
				atcs.add(client);
		}

		return atcs;
	}

	public List<OnlineClient> getFriendsOnlinePilots(List<Integer> friendsCids) {
		List<OnlineClient> atcs = new ArrayList<OnlineClient>();

		for (OnlineClient client : onlineClients) {

			if (client.getClienttype() == ClientType.PILOT
					&& friendsCids.contains(client.getCID()))
				atcs.add(client);
		}

		return atcs;
	}

}
