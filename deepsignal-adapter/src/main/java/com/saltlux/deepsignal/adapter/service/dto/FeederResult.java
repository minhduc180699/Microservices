package com.saltlux.deepsignal.adapter.service.dto;

public class FeederResult {

    public final String connectome_id;
    public final String scroll_id; //Set the scroll_id value passed from the previous result to search the next page.

    public final Object docs;

    public FeederResult(String connectome_id, String scroll_id, Object docs) {
        this.connectome_id = connectome_id;
        this.scroll_id = scroll_id;
        this.docs = docs;
    }
}
