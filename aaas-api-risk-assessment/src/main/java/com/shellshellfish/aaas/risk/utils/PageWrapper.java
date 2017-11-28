package com.shellshellfish.aaas.risk.utils;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PageWrapper<T> {
	public static final int MAX_PAGE_ITEM_DISPLAY = 5;
	private Page<T> page;
	private int currentNumber; // 当前页 页数
	private Map<String, Object> _links;// related/self/execute
	private String name = "";
	@JsonIgnore
	private Sort sort;

	/**
	 *  _items 		当前页显示内容
	 *  _total 		显示总数量 
	 *  number		当前显示页的页数（第几页）+1
	 *  size 		每页显示数 
	 *  totalPages 	总共页数
	 *  
	 */

	public PageWrapper(Page<T> page) {
		this.page = page;
		currentNumber = page.getNumber() + 1;
	}

	public Map<String, Object> get_links() {
		return _links;
	}

	public void set_links(Map<String, Object> _links) {
		if(this._links !=null){
			this._links.putAll(_links);
		}else {
			this._links = _links;
		}
	}
	
	@JsonIgnore
	public int getNumber() {
		return currentNumber;
	}

	public List<T> get_items() {
		return page.getContent();
	}

	public long get_total() {
		if (page == null) {
			return 0L;
		} else {
			return page.getTotalElements();
		}
	}

	public int getSize() {
		return page.getSize();
	}

	public int getTotalPages() {
		return page.getTotalPages();
	}

	public boolean isFirstPage() {
		return page.isFirst();
	}

	public boolean isLastPage() {
		return page.isLast();
	}

	public boolean isHasPreviousPage() {
		return page.hasPrevious();
	}

	public boolean isHasNextPage() {
		return page.hasNext();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNext(String next) {
		return next;
	}
	
	public String getPrev(String prev) {
		return prev;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}
	
}
