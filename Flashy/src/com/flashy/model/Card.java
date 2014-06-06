package com.flashy.model;

import java.util.Date;

public class Card {

	long id;
	String word;
	String meaning;
	Date time_created;

	public Card(long id, String word, String meaning, Date time_created) {
		this.id = id;
		this.word = word;
		this.meaning = meaning;
		this.time_created = time_created;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public Date getTime_created() {
		return time_created;
	}

	public void setTime_created(Date time_created) {
		this.time_created = time_created;
	}

}