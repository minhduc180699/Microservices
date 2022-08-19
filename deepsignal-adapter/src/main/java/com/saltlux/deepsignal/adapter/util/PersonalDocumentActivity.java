package com.saltlux.deepsignal.adapter.util;

public enum PersonalDocumentActivity {
    liked(1, true),
    disliked(2, true),
    bookmarked(1, true),
    memo(1, true),
    deleted(1, true);

    private int intValue;
    private boolean boolValue;

    PersonalDocumentActivity(int intValue, boolean boolValue) {
        this.intValue = intValue;
        this.boolValue = boolValue;
    }

    public int getIntValue() {
        return this.intValue;
    }

    public boolean getBoolValue() {
        return this.boolValue;
    }
}
