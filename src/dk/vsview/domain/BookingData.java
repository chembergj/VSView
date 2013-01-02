package dk.vsview.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.vsview.domain.OnlineClient.ClientType;

public class BookingData  extends TimestampedData {
	java.util.Date updated;
	List<Booking> bookings;
	private final static String positionPrefixRegex = "^(E[KNSF]|BI)\\w\\w_\\w+$";

	public java.util.Date getUpdated() {
		return updated;
	}

	public void setUpdated(java.util.Date updated) {
		this.updated = updated;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public List<Booking> getFilteredBookings() {

		Pattern p = Pattern.compile(positionPrefixRegex);

		List<Booking> result = new ArrayList<Booking>();
		for (Booking b : bookings) {
			if (Pattern.matches(positionPrefixRegex, b.callsign))
				result.add(b);
		}

		return result;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	public List<Booking> getFriendsBookedATC(List<Integer> friendsCids) {
		List<Booking> atcs = new ArrayList<Booking>();

		for (Booking atcBooking : bookings) {

			if (friendsCids.contains(atcBooking.getCID()))
				atcs.add(atcBooking);
		}

		return atcs;
	}

	public List<Booking> getFriendsBookedPilots(List<Integer> friendsCids) {
		List<Booking> atcs = new ArrayList<Booking>();

		for (Booking booking : bookings) {

			if (friendsCids.contains(booking.getCID()) && booking.getClienttype() == Booking.ClientType.PILOT)
				atcs.add(booking);
		}

		return atcs;
	}

}
