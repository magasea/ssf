package com.shellshellfish.aaas.finance.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonPropertyOrder({ "name", "_total", "_items", "_links"})
@JsonInclude(Include.NON_NULL)
public class CollectionResourceWrapper<T extends List<?>> extends ResourceWrapper<T> {

	public CollectionResourceWrapper() {
		
	}
	
	public CollectionResourceWrapper(T items) {
		//super();
		this.items = items;
	}

	@JsonProperty("_items")
	private T items;
	
	public T getItems() {
		return items;
	}

	public void setItems(T items) {
		this.items = items;
	}

	@JsonProperty("_total")
	private Integer total;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
		
}
