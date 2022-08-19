package com.saltlux.deepsignal.web.service.dto;

import lombok.Getter;

@Getter
public enum FileFormat {
    DOCS("docs", 1),
    DOC("doc", 1),
    DOCX("docx", 1),
    XLS("xls", 1),
    XLSX("xlsx", 1),
    PPT("ppt", 1),
    PPTX("pptx", 1),
    PDF("pdf", 1),
    CSV("csv", 1),
    TSV("tsv", 1),
    QA_DOCS("docs", 2),
    QA_PDF("pdf", 2),
    QA_JPG("jpg", 2),
    QA_JPEG("jpeg", 2),
    QA_GIF("gif", 2);

    private final String value;
    private final Integer type;

    FileFormat(String value, Integer type) {
        this.value = value;
        this.type = type;
    }

    public static FileFormat validate(String name, Integer type) throws IllegalArgumentException {
        for (FileFormat val : values()) {
            if (val.value.equalsIgnoreCase(name) && val.type.equals(type)) return val;
        }
        return null;
    }
}
