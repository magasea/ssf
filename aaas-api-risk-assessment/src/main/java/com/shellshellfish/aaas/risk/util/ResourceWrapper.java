package com.shellshellfish.aaas.risk.util;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ResourceWrapper<T> {
	@JsonProperty("_to_be_modified")
	private T resource;
	
    public T getResource() {
		return resource;
	}

	public void setResource(T resource) {
		this.resource = resource;
	}

	private String name;    

    @JsonProperty("_links")
    private Links links;

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

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
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
