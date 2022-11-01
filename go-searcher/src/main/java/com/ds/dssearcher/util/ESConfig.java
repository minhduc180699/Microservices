package com.ds.dssearcher.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="ESConfig")
public class ESConfig {

	private String cluster;
	private int numberOfShard;
	private int numberOfReplica;

	private Node nodes;

	@XmlElement
	public Node getNodes() {
		return nodes;
	}
}