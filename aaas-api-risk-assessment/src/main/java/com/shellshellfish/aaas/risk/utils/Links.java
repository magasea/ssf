package com.shellshellfish.aaas.risk.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Links {
    private String self;
    private String up;
    private String backward;
    private String forward;
    private String next;
    private String prev;

    private String edit;
    private String delete;
    private String describedBy;


    public String getDescribedBy() {
        return describedBy;
    }

    public void setDescribedBy(String describedBy) {
        this.describedBy = describedBy;
    }

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

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrev() {
		return prev;
	}

	public void setPrev(String prev) {
		this.prev = prev;
	}

}
