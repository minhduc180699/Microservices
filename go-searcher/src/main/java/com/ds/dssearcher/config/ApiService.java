package com.ds.dssearcher.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ApiService {
    @Autowired
    private Environment environment;
    public static String http = "http://";
    @Value("${api-service.tornado-metadata-access.name}")
    public static String hostName;
    private static String tmHost;
    public static class PersonalAgentManager{
        public static String host;
        public static String apiPublic = "/deepsignal/personalAgentManagerApi";
    }
    public static class TornadoManager{
        @Autowired
        private Environment environment;
        @Bean
        void initTornadoManagerAPI(){
            host = http + hostName;
            apiGetInfoAgentTwitter= host + "/deepsignal/tornadoManagerApi/agents/twitter/getInfoAgentTwitter";
        }
        public static String host;
        public static String apiGetInfoAgentTwitter;
    }
//    @Configuration
    public static class PersonalAgentMetadataAccess{
        @Autowired
        private Environment environment;
        @Bean
        void initPersonalAgentMetadataAccessAPI(){
            host = http + environment.getProperty("api-service.personal-agent-metadata-access.name");
            apiGetInfoAgentTwitter= host + "/deepsignal/tornadoManagerApi/agents/twitter/getInfoAgentTwitter";
        }
        public static String host;
        public static String apiGetInfoAgentTwitter;
    }
    public static class TornadoMetadataAccess{
        @Autowired
        private Environment environment;
        @Bean
        void initTornadoMetadataAccessAPI(){
            host = http + environment.getProperty("api-service.tornado-metadata-access.name");
            apiCreateAgent = host + "/deepsignal/tornadoManagerApi/agents/createAgent";
            apiCheckRunableAgents = host + "/deepsignal/tornadoManagerApi/agents/checkRunableAgents";
            apipUdateAgentStatus = host + "/deepsignal/tornadoManagerApi/agents/updateAgentStatus";
            apiGetAgentScheduler = host + "/deepsignal/tornadoManagerApi/agents/getAgentScheduler";
            apicreateFacebookAgent = host + "/deepsignal/tornadoManagerApi/agents/facebook/createAgent";
            apiGetRunnableFacebookAgent = host +  "/deepsignal/tornadoManagerApi/agents/facebook/getRunnableFacebookAgent";
            apiGetInfoAgentTwitter= host + "/deepsignal/tornadoManagerApi/agents/twitter/getInfoAgentTwitter";
            apiUpdateTwitterAccount= host + "/deepsignal/tornadoManagerApi/agents/twitter/updateTwitterAccount";
        }
        public static String host;
        public static String apiCreateAgent;
        public static String apiCheckRunableAgents;
        public static String apiGetRunnableFacebookAgent;
        public static String apipUdateAgentStatus;
        public static String apiGetAgentScheduler;
        public static String apicreateFacebookAgent;

        public static String apiGetInfoAgentTwitter;
        public static String apiUpdateTwitterAccount;
    }

    public static class DataManager{
        @Autowired
        private Environment environment;
        @Bean
        void initDataManagerAPI(){
            host = http + environment.getProperty("api-service.data-manager.name");
            apiGetResultSearch = host + "/deepsignal/personalAgentManagerApi/search/getResultSearch";
            apiGetResultSearchByKeyword = host + "/deepsignal/personalAgentManagerApi/search/getResultSearchByKeyword";
            apicheckResultSearch = host + "/deepsignal/personalAgentManagerApi/search/checkResultSearch";
        }
        public static String host;
        public static String apiGetResultSearch;
        public static String apiGetResultSearchByKeyword;
        public static String apicheckResultSearch;
    }
    public static class MetaSearch{
        public static String apiSearch = "http://104.197.171.224:80/deepsignal/realtime/metasearch/getMetaSearch";
    }
}
