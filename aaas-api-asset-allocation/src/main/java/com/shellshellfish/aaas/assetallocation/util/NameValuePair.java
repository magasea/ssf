package com.shellshellfish.aaas.assetallocation.util;

public class NameValuePair<T1, T2> {
	
	private T1 name;
	private T2 value;
		
	
	public NameValuePair(T1 name, T2 value) {
		super();
		this.name = name;
		this.setValue(value);
	}
	
	public T1 getName() {
		return name;
	}
	public void setName(T1 name) {
		this.name = name;
	}

	public T2 getValue() {
		return value;
	}

	public void setValue(T2 value) {
		this.value = value;
	}

}
