package dk.vsview;

import dk.vsview.domain.OnlineData;

public interface IOnlineDataConsumer {
	public void dataFetched(OnlineData data);
}
