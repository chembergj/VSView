package dk.vsview.domain;

import org.joda.time.DateTime;

public abstract class TimestampedData {

	private DateTime updateDate = new DateTime(1900, 1, 1, 0, 0, 0, 0);
	private int minutesToReload;
	
	public void setUpdateDate(DateTime updateDate) {
		this.updateDate = updateDate;
	}
	protected DateTime getUpdateDate() {
		return updateDate;
	}
	public void setMinutesToReload(int minutesToReload) {
		this.minutesToReload = minutesToReload;
	}
	protected int getMinutesToReload() {
		return minutesToReload;
	}
	
	public boolean isOutdated() {
		return getUpdateDate().plusMinutes(getMinutesToReload()).isBeforeNow();
	}

}
