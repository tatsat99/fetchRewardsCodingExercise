package com.fetchrewards.codingexercise.payertransaction.model;

import java.time.LocalDateTime;

public class PayerTransaction implements Comparable<PayerTransaction> {
	private String payer;
	private long points;
	private LocalDateTime timestamp;

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(PayerTransaction o) {
		return this.timestamp.compareTo(o.timestamp);
	}
}
