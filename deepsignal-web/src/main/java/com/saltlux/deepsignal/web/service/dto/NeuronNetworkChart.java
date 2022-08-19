package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Data;

@Data
public class NeuronNetworkChart {

    private List<Node> nodes;

    private List<Link> links;
}

@Data
class Node {

    private String id;

    private String name;

    private String url;

    private boolean loaded;

    private String nerType;
}

@Data
class Link {

    private String id;

    private String from;

    private String to;
}
