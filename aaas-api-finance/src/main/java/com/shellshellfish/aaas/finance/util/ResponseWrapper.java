package com.shellshellfish.finance.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ResponseWrapper<T> {
    private String name;
    private T properties;

    @JsonProperty("_links")
    private FishLinks links;

    @JsonProperty("_schema_version")
    private String schemaVersion = "0.1.1";

    @JsonProperty("_service_id")
    private String serviceId = UUID.randomUUID().toString();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getProperties() {
        return properties;
    }

    public void setProperties(T properties) {
        this.properties = properties;
    }

    public FishLinks getLinks() {
        return links;
    }

    public void setLinks(FishLinks links) {
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
