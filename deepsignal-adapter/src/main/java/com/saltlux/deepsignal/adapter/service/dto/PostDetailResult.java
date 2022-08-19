package com.saltlux.deepsignal.adapter.service.dto;

import java.util.Set;

public class PostDetailResult {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public final String _id;

    public final boolean is_assacible;

    public final String title;
    public final String content;
    public final String writer;
    public final String images[];
    public final String published_at;
    /////////////////////////////////////////////

    public final String ta_person[];
    public final String ta_location[];
    public final String ta_organization[];

    /////////////////////////////////////////////
    //It is same for all user;
    public final double ta_sentiment_score;
    public final double ta_content_reliability = Math.random();

    //It is different by user.
    public final double ta_post_important = Math.random();

    public PostDetailResult(
        String connectome_id,
        String post_id,
        String post_type,
        com.saltlux.bigo.restapi.client.dto.SearchResult.Document doc
    ) {
        final Set<String> connectome_ids = getStringSet(doc, FieldIndex.project_id);

        this._id = doc.getID();
        this.is_assacible = connectome_ids.contains(connectome_id);

        this.title = doc.getFieldValue(FieldIndex.title);
        this.content = doc.getFieldValue(FieldIndex.content) + doc.getFieldValue(FieldIndex.message);
        this.writer = doc.getFieldValue(FieldIndex.writer);
        this.published_at = doc.getFieldValue(FieldIndex.published_at);

        this.images = getStringArray(doc, FieldIndex.images);

        this.ta_person = getStringArray(doc, FieldIndex.tms_ne_person);
        this.ta_location = getStringArray(doc, FieldIndex.tms_ne_location);
        this.ta_organization = getStringArray(doc, FieldIndex.tms_ne_organization);

        this.ta_sentiment_score = getDouble(doc, FieldIndex.tms_sentiment_polarity_score);
    }

    private Set<String> getStringSet(com.saltlux.bigo.restapi.client.dto.SearchResult.Document doc, String field_name) {
        final Set<String> set = new java.util.HashSet<String>();
        final String s = doc.getFieldValue(field_name);
        if (s == null || s.length() <= 0) return set;
        for (String ss : s.split("\r\n")) {
            String sss = ss.trim();
            if (sss == null || sss.length() <= 0) continue;
            set.add(sss);
        }
        return set;
    }

    private String[] getStringArray(com.saltlux.bigo.restapi.client.dto.SearchResult.Document doc, String field_name) {
        final String s = doc.getFieldValue(field_name);
        if (s == null || s.length() <= 0) return new String[0];
        return s.split("\r\n");
    }

    private double getDouble(com.saltlux.bigo.restapi.client.dto.SearchResult.Document doc, String field_name) {
        try {
            final String d_str = doc.getFieldValue(field_name);
            if (d_str == null || d_str.length() <= 0) return 0;
            return Double.parseDouble(d_str);
        } catch (Throwable e) {
            return 0;
        }
    }
}
