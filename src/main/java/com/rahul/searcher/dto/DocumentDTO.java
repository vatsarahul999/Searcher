package com.rahul.searcher.dto;

import java.util.Map;

public class DocumentDTO implements Comparable<DocumentDTO>{
	
	private double score;
	
	private Map<String,String> data;
	
	private String source;

	public DocumentDTO(double score, Map<String, String> data, String source) {
		this.score = score;
		this.data = data;
		this.source = source;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public int compareTo(DocumentDTO o) {
		if(o==null|| this.score>o.score)
			return -1;
		else if(this.score<o.score)
			return 1;
		return 0;
	}
	
}
