package com.skplanet.rakesample;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RakeJsonLog {
	private String compress;
	private List<Map<String, Object>> data;
	public String getCompress() {
		return compress;
	}
	public void setCompress(String compress) {
		this.compress = compress;
	}
	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	
	
}
