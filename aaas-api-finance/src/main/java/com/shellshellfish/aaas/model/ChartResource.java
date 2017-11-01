package com.shellshellfish.aaas.model;

import com.shellshellfish.aaas.util.BaseResource;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.Map;

public class ChartResource extends BaseResource {
//    private List<Object> horizentalValues;
//
//    private List<Object> verticalValues;

    private List<List<List<Object>>> lineValues;

    private List<String> legends;

    public List<String> getLegends() {
        return legends;
    }

    public void setLegends(List<String> legends) {
        this.legends = legends;
    }

//    public List<Object> getHorizentalValues() {
//        return horizentalValues;
//    }
//
//    public void setHorizentalValues(List<Object> horizentalValues) {
//        this.horizentalValues = horizentalValues;
//    }
//
//    public List<Object> getVerticalValues() {
//        return verticalValues;
//    }
//
//    public void setVerticalValues(List<Object> verticalValues) {
//        this.verticalValues = verticalValues;
//    }

    public List<List<List<Object>>> getLineValues() {
        return lineValues;
    }

    public void setLineValues(List<List<List<Object>>> lineValues) {
        this.lineValues = lineValues;
    }

}
