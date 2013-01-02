package dk.vsview.domain;

public class Booking {
	
	public enum ClientType {
		UNKNOWN, PILOT, ATC;
	}

	private ClientType clienttype;
	String callsign;
	String realname;
	String date;
	String starttime;
	String endtime;
	int cid;
	
	public String getCallsign() {
		return callsign;
	}
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public int getCID() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setClienttype(ClientType clienttype) {
		this.clienttype = clienttype;
	}
	
	public ClientType getClienttype() {
		return clienttype;
	}
	
}
