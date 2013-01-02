package dk.vsview.domain;

import dk.vsview.domain.OnlineClient.FacilityType;

public class OnlineClient {

	public enum ClientType {
		UNKNOWN, PILOT, ATC;
	}

	public enum FacilityType {
		OBSERVER, FSS, DEL, GND, TWR, APP, CTR;

		public static FacilityType parseString(String facilityStr) {
			int facilityNo = Integer.parseInt(facilityStr);
			switch (facilityNo) {
			case 0:
				return OBSERVER;
			case 1:
				return FSS;
			case 2:
				return DEL;
			case 3:
				return GND;
			case 4:
				return TWR;
			case 5:
				return APP;
			case 6:
				return CTR;
			default:
				return null;
			}
		}
	}

	private int CID;
	String callsign;
	String realname;
	ClientType clienttype;
	private FacilityType facilityType;
	String frequency;
	private String plannedAltitude;
	private int altitude;
	private int groundspeed;
	private String plannedDepAirport;
	private String plannedDestAirport;
	private String plannedRoute;
	private String plannedRemarks;
	
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

	public ClientType getClienttype() {
		return clienttype;
	}

	public void setClienttype(ClientType clienttype) {
		this.clienttype = clienttype;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public void setCID(int cID) {
		CID = cID;
	}

	public int getCID() {
		return CID;
	}

	public void setFacilityType(FacilityType facilityType) {
		this.facilityType = facilityType;
	}

	public FacilityType getFacilityType() {
		return facilityType;
	}

	public void setPlannedAltitude(String altitude) {
		this.plannedAltitude = altitude;
	}

	public String getPlannedAltitude() {
		return plannedAltitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getAltitude() {
		return altitude;
	}
	
	public void setGroundspeed(int groundspeed) {
		this.groundspeed = groundspeed;
	}

	public int getGroundspeed() {
		return groundspeed;
	}

	public void setPlannedDepAirport(String plannedDepAirport) {
		this.plannedDepAirport = plannedDepAirport;
	}

	public String getPlannedDepAirport() {
		return plannedDepAirport;
	}

	public void setPlannedDestAirport(String plannedDestAirport) {
		this.plannedDestAirport = plannedDestAirport;
	}

	public String getPlannedDestAirport() {
		return plannedDestAirport;
	}

	public void setPlannedRoute(String plannedRoute) {
		this.plannedRoute = plannedRoute;
	}

	public String getPlannedRoute() {
		return plannedRoute;
	}

	public void setPlannedRemarks(String plannedRemarks) {
		this.plannedRemarks = plannedRemarks;
	}

	public String getPlannedRemarks() {
		return plannedRemarks;
	}
}
