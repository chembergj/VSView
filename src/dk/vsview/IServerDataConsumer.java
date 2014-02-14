package dk.vsview;

import dk.vsview.domain.ServerData;

public interface IServerDataConsumer {
	public void dataFetched(ServerData data);
}
