package com.shellshellfish.aaas.util;

import java.util.List;

public class Links {
    private String self;
    private String up;
    private String backward;
    private String forward;
    private List<FishExecution> execute;
    private List<FishIcon> icon;
    private String edit;
    private String delete;
    private String describedBy;
    private List<FishHisitory> history;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getBackward() {
        return backward;
    }

    public void setBackward(String backward) {
        this.backward = backward;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public List<FishExecution> getExecute() {
        return execute;
    }

    public void setExecute(List<FishExecution> execute) {
        this.execute = execute;
    }

    public List<FishIcon> getIcon() {
        return icon;
    }

    public void setIcon(List<FishIcon> icon) {
        this.icon = icon;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getDescribdBy() {
        return describedBy;
    }

    public void setDescribdBy(String describedBy) {
        this.describedBy = describedBy;
    }

    public List<FishHisitory> getHistory() {
        return history;
    }

    public void setHistory(List<FishHisitory> history) {
        this.history = history;
    }
}
