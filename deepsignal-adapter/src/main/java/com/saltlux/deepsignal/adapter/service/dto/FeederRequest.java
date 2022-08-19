package com.saltlux.deepsignal.adapter.service.dto;

import java.io.Serializable;

public class FeederRequest implements Serializable {

    private static final long serialVersionUID = -6623867234196422229L;

    public final String connectome_id;
    public final String next_id;
    public final int return_count;

    public FeederRequest() {
        this.connectome_id = "CIDSALTLUXHQKAY";
        this.next_id = null;
        this.return_count = 10;
    }
}
