package com.ds.dssearcher.config;

import com.ds.dssearcher.runtime_config.AppConfig;
import com.ds.dssearcher.util.ESConfig;
import com.ds.dssearcher.util.ESNode;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Log4j2
public class ESConfiguration {
	@Autowired
	private AppConfig appConfig;
	private RestHighLevelClient client = null;
	private ESConfig config = null;

	private synchronized void initClient() throws Exception {
		if (client == null) {
//			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//			InputStream file = classLoader.getResourceAsStream("elastic.xml");
//
//			JAXBContext jaxbContext = JAXBContext.newInstance(ESConfig.class);
//
//			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//			config= (ESConfig) jaxbUnmarshaller.unmarshal(file);
//			List<ESNode> nodes =  config.getNodes().getEsNodes();
			List<ESNode> nodes = new ArrayList<>();
			appConfig.getElasticsearch().setNodes(appConfig.getElasticsearch().getNodes().replace(" ", ""));
			String[] nodeList = appConfig.getElasticsearch().getNodes().split(",");
			for (String nodeString : nodeList){
				String[] nodeSplit = nodeString.split(":");
				nodes.add(new ESNode(nodeSplit[0], Integer.parseInt(nodeSplit[1])));
			}
			if (nodes != null && nodes.size() > 0) {
				HttpHost[] esNodeCluster = new HttpHost[nodes.size()];
				for (int i=0; i<nodes.size(); ++i) {
					ESNode esNode = nodes.get(i);
					esNodeCluster[i] = new HttpHost(esNode.getHost(), esNode.getPort(), "http");
					log.info("es node cluster " + i+ ": " + esNodeCluster[i]);
				}

				RestClientBuilder builder = RestClient.builder(esNodeCluster);
				builder.setRequestConfigCallback(requestConfigBuilder ->
						requestConfigBuilder
								.setConnectTimeout(10000)
								.setSocketTimeout(90000)
								.setConnectionRequestTimeout(0)
				);
				client = new RestHighLevelClient(builder);
			} else {
				throw new Exception("Invalid Elastic configuration: Node list is EMPTY!");
			}
		}
	}

	@Bean(value = "client")
	public RestHighLevelClient getClient() throws Exception {
		if (client == null)
			initClient();
		return client;
	}

}
