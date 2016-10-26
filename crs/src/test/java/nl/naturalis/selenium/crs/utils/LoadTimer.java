package nl.naturalis.selenium.crs.utils;

public class LoadTimer {
	
	private long start; 
	private long end; 
	
	public void start() {
		this.start = System.currentTimeMillis();
	}

	public void finish() {
		this.end = System.currentTimeMillis();
	}

	public long tookMilliSeconds() {
		return this.end - this.start; 
	}

	public long tookSeconds() {
		return ((this.end - this.start) / 1000); 
	}

}
