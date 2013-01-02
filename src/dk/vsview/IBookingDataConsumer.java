package dk.vsview;

import dk.vsview.domain.BookingData;

public interface IBookingDataConsumer {
	public void dataFetched(BookingData data);
}
