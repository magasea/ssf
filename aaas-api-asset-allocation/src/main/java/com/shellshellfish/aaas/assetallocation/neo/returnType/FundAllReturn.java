package com.shellshellfish.aaas.assetallocation.neo.returnType;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyinuo on 2017/11/30.
 */
public class FundAllReturn {
    private String name;
    private int _total;
    private List<Map<String,Object>> _items;
    private Map _links;
    private String _schemaVersion;
    private String _serviceId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
}
