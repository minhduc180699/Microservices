package com.ds.dssearcher.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ESNode {
	private String host;
	private int port;

	public ESNode(String host, int port) {
		this.host = host;
		this.port = port;
	}
}
