package com.shellshellfish.aaas.risk.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonPropertyOrder({ "name", "item", "_total", "_items", "_links"})
@JsonInclude(Include.NON_NULL)
public class ResourceWrapper<T> {
	
	public ResourceWrapper() {
		
	}
	
	public ResourceWrapper(T item) {
		this.item = item;
	}

	@JsonUnwrapped
	private T item;
	
	public T getItem() {
		return this.item;
	}

	public void setItem(T item) {
		this.item = item;
	}
	
	@JsonProperty("_links")
	private Links links;

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}
	
	private String name;  

    @JsonProperty("_schemaVersion")
    private String schemaVersion = "0.1.1";

    @JsonProperty("_serviceId")
    private String serviceId = "风险测评";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("_type")
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }   

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    
}
