package com.rahul.searcher.dto;

public class DocumenDTO {
	
	private double score;
	
	private String source;

	public DocumenDTO(double score, String source) {
		this.score = score;
		this.source = source;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	

}
