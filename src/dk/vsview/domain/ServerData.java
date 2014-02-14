package dk.vsview.domain;

import java.util.List;
import java.util.Random;

public class ServerData {
	private List<String> completeDataFileURLs;
	private List<String> serverlistDataFileURLs;
	
	public List<String> getCompleteDataFileURLs() {
		return completeDataFileURLs;
	}
	
	public void setCompleteDataFileURLs(List<String> completeDataFileURLs) {
		this.completeDataFileURLs = completeDataFileURLs;
	}
	
	public List<String> getServerlistDataFileURLs() {
		return serverlistDataFileURLs;
	}
	
	public void setServerlistDataFileURLs(List<String> serverlistDataFileURLs) {
		this.serverlistDataFileURLs = serverlistDataFileURLs;
	}
	
	public String getRandomCompleteDataFileURLs() {
		return completeDataFileURLs.get(new Random().nextInt(completeDataFileURLs.size()));
	}
}
