package com.example.helloworld.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import android.R.integer;
@JsonIgnoreProperties(ignoreUnknown=true)
public class Page<Article> {

	List<Article> content;
	Integer number;
	public List<Article> getContent() {
		return content;
	}
	public void setContent(List<Article> content) {
		this.content = content;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	
}
