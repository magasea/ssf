package com.shellshellfish.aaas.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class BaseResource {
    private String name;

    @JsonProperty("_links")
    private FishLinks links;

    @JsonProperty("_schemaVersion")
    private String schemaVersion = "0.1.1";

    @JsonProperty("_serviceId")
    private String serviceId = UUID.randomUUID().toString();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
