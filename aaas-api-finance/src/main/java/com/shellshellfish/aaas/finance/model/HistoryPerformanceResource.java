package com.shellshellfish.finance.model;

public class HistoryPerformanceResource {
    private String title;
    private Double value;
    private Double benchmark;

    public HistoryPerformanceResource(String title, Double value, Double benchmark) {
        this.title = title;
        this.value = value;
        this.benchmark = benchmark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(Double benchmark) {
        this.benchmark = benchmark;
    }
}
