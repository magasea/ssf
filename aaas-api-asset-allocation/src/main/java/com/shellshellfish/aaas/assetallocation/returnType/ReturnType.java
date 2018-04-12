package com.shellshellfish.aaas.assetallocation.returnType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyinuo on 2017/11/30.
 */
public class ReturnType implements Serializable {

	private int _total;
	private List<Map<String, Object>> _items;
	private String name;
	private Map _links;
	private Map maxMinMap;
	private Map maxMinBenchmarkMap;
	private Map expectedIncomeSizeMap;
	private Map highPercentMaxIncomeSizeMap;
	private Map highPercentMinIncomeSizeMap;
	private Map lowPercentMaxIncomeSizeMap;
	private Map lowPercentMinIncomeSizeMap;
	private String _schemaVersion;
	private String _serviceId;

	public int get_total() {
		return _total;
	}

	public void set_total(int _total) {
		this._total = _total;
	}

	public List<Map<String, Object>> get_items() {
		return _items;
	}

	public void set_items(List<Map<String, Object>> _items) {
		this._items = _items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map get_links() {
		return _links;
	}

	public void set_links(Map _links) {
		this._links = _links;
	}

	public String get_schemaVersion() {
		return _schemaVersion;
	}

	public void set_schemaVersion(String _schemaVersion) {
		this._schemaVersion = _schemaVersion;
	}

	public String get_serviceId() {
		return _serviceId;
	}

	public void set_serviceId(String _serviceId) {
		this._serviceId = _serviceId;
	}

	public Map getMaxMinMap() {
		return maxMinMap;
	}

	public void setMaxMinMap(Map maxMinMap) {
		this.maxMinMap = maxMinMap;
	}

	public Map getMaxMinBenchmarkMap() {
		return maxMinBenchmarkMap;
	}

	public void setMaxMinBenchmarkMap(Map maxMinBenchmarkMap) {
		this.maxMinBenchmarkMap = maxMinBenchmarkMap;
	}

	public Map getExpectedIncomeSizeMap() {
		return expectedIncomeSizeMap;
	}

	public void setExpectedIncomeSizeMap(Map expectedIncomeSizeMap) {
		this.expectedIncomeSizeMap = expectedIncomeSizeMap;
	}

	public Map getHighPercentMaxIncomeSizeMap() {
		return highPercentMaxIncomeSizeMap;
	}

	public void setHighPercentMaxIncomeSizeMap(Map highPercentMaxIncomeSizeMap) {
		this.highPercentMaxIncomeSizeMap = highPercentMaxIncomeSizeMap;
	}

	public Map getHighPercentMinIncomeSizeMap() {
		return highPercentMinIncomeSizeMap;
	}

	public void setHighPercentMinIncomeSizeMap(Map highPercentMinIncomeSizeMap) {
		this.highPercentMinIncomeSizeMap = highPercentMinIncomeSizeMap;
	}

	public Map getLowPercentMaxIncomeSizeMap() {
		return lowPercentMaxIncomeSizeMap;
	}

	public void setLowPercentMaxIncomeSizeMap(Map lowPercentMaxIncomeSizeMap) {
		this.lowPercentMaxIncomeSizeMap = lowPercentMaxIncomeSizeMap;
	}

	public Map getLowPercentMinIncomeSizeMap() {
		return lowPercentMinIncomeSizeMap;
	}

	public void setLowPercentMinIncomeSizeMap(Map lowPercentMinIncomeSizeMap) {
		this.lowPercentMinIncomeSizeMap = lowPercentMinIncomeSizeMap;
	}

}
