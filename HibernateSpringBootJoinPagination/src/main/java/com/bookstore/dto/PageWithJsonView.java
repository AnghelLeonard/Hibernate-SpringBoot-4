/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 *
 * @author leopr
 */
public class PageWithJsonView<T> extends PageImpl<T> {

	public PageWithJsonView(Page<T> page) {
		super(page.getContent());
	}

	@JsonView(Views.Summary.class)
	@Override
	public int getTotalPages() {
		// TODO Auto-generated method stub
		return super.getTotalPages();
	}

	@JsonView(Views.Summary.class)
	@Override
	public long getTotalElements() {
		// TODO Auto-generated method stub
		return super.getTotalElements();
	}
	
	@JsonView(Views.Summary.class)
	@Override
	public int getNumberOfElements() {
		// TODO Auto-generated method stub
		return super.getNumberOfElements();
	}
	
	@JsonView(Views.Summary.class)
	@Override
	public List<T> getContent() {
		// TODO Auto-generated method stub
		return super.getContent();
	}

}
