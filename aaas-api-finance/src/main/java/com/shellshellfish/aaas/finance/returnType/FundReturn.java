package com.shellshellfish.aaas.finance.returnType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyinuo on 2017/11/24.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FundReturn {
    private String groupId;
    private String subGroupId;
    private String name;
    private Map<String,String> investmentPeriod;
    private Map<String,String> riskToleranceLevel;
    private double minAnnualizedReturn;
    private double maxAnnualizedReturn;
    private double minRiskLevel;
    private double maxRiskLevel;
    private Map<String,String> _links;
    private Long creationTime;
    private String _schemaVersion;
    private String _serviceId;
    @JsonUnwrapped
    private List<Map<String,Double>> assetsRatios;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubGroupId() {
        return subGroupId;
    }

    public void setSubGroupId(String subGroupId) {
        this.subGroupId = subGroupId;
    }

    public double getMinAnnualizedReturn() {
        return minAnnualizedReturn;
    }

    public void setMinAnnualizedReturn(double minAnnualizedReturn) {
        this.minAnnualizedReturn = minAnnualizedReturn;
    }

    public double getMaxAnnualizedReturn() {
        return maxAnnualizedReturn;
    }

    public void setMaxAnnualizedReturn(double maxAnnualizedReturn) {
        this.maxAnnualizedReturn = maxAnnualizedReturn;
    }

    public double getMinRiskLevel() {
        return minRiskLevel;
    }

    public void setMinRiskLevel(double minRiskLevel) {
        this.minRiskLevel = minRiskLevel;
    }

    public double getMaxRiskLevel() {
        return maxRiskLevel;
    }

    public void setMaxRiskLevel(double maxRiskLevel) {
        this.maxRiskLevel = maxRiskLevel;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }
    public Map<String, String> get_links() {
        return _links;
    }

    public void set_links(Map<String, String> _links) {
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

    public Map<String, String> getRiskToleranceLevel() {
        return riskToleranceLevel;
    }

    public void setRiskToleranceLevel(Map<String, String> riskToleranceLevel) {
        this.riskToleranceLevel = riskToleranceLevel;
    }

    public Map<String, String> getInvestmentPeriod() {
        return investmentPeriod;
    }

    public void setInvestmentPeriod(Map<String, String> investmentPeriod) {
        this.investmentPeriod = investmentPeriod;
    }

    public List<Map<String, Double>> getAssetsRatios() {
        return assetsRatios;
    }

    public void setAssetsRatios(List<Map<String, Double>> assetsRatios) {
        this.assetsRatios = assetsRatios;
    }
}
