package com.shellshellfish.aaas.model;

import com.shellshellfish.aaas.util.BaseResource;

import java.util.List;

public class ChartResource extends BaseResource {
    private List<Object> horizentalValues;

    private List<Object> verticalValues;

    private List<List<Double>> lineValues;

    public List<Object> getHorizentalValues() {
        return horizentalValues;
    }

    public void setHorizentalValues(List<Object> horizentalValues) {
        this.horizentalValues = horizentalValues;
    }

    public List<Object> getVerticalValues() {
        return verticalValues;
    }

    public void setVerticalValues(List<Object> verticalValues) {
        this.verticalValues = verticalValues;
    }

    public List<List<Double>> getLineValues() {
        return lineValues;
    }

    public void setLineValues(List<List<Double>> lineValues) {
        this.lineValues = lineValues;
    }

}
