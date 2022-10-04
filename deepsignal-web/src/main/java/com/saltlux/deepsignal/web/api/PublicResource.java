package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.aop.userActivities.UserActivity;
import com.saltlux.deepsignal.web.api.consumer.RabbitmqConsumer;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.CountryInfo;
import com.saltlux.deepsignal.web.service.LearningService;
import com.saltlux.deepsignal.web.service.WeatherService;
import com.saltlux.deepsignal.web.service.dto.LocationDTO;
import com.saltlux.deepsignal.web.service.dto.WeatherInfoDTO;
import com.saltlux.deepsignal.web.util.AppUtil;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
@Tag(name = "Public Resource Management", description = "The public resource management API")
public class PublicResource {

    private final Logger log = LoggerFactory.getLogger(PublicUserResource.class);

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private LearningService learningService;

    @Autowired
    private ConnectAdapterApi connectAdapterApi;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @GetMapping("/getCountryCode")
    @Operation(summary = "Get the country code", tags = { "Public Resource Management" })
    public ResponseEntity<?> getCountryCode() {
        try {
            InputStream in = getClass().getResourceAsStream("/data/country-code.json");
            JSONParser jsonParser = new JSONParser();
            JSONObject data = (JSONObject) jsonParser.parse(new BufferedReader(new InputStreamReader(in)));
            List<CountryInfo> countryInfos = new ArrayList<>();
            for (Object key : data.keySet()) {
                CountryInfo countryInfo = new CountryInfo();
                countryInfo.setCode(key.toString());
                countryInfo.setName(key + " " + data.get(key));
                countryInfos.add(countryInfo);
            }
            Collections.sort(countryInfos);
            return ResponseEntity.status(HttpStatus.OK).body(AppUtil.moveToFisrtItem(countryInfos));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getLocationByIp/{ip}")
    @Operation(summary = "Get Location By Ip", tags = { "Public Resource Management" })
    public ResponseEntity<?> getLocationByIp(@PathVariable(name = "ip") String ipAddress) {
        try {
            LocationDTO location = weatherService.getLocationByIp(ipAddress);
            return ResponseEntity.status(HttpStatus.OK).body(location);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getWeather")
    @Operation(summary = "Get weather by Ip Address", tags = { "Public Resource Management" })
    public ResponseEntity<?> getWeather(
        @RequestParam(name = "ip-address") String ipAddress,
        @RequestParam(name = "language") String language,
        @RequestParam(name = "units", required = false) String units,
        @RequestParam(name = "locWeather", required = false) String locWeather
    ) {
        try {
            LocationDTO location = new LocationDTO();
            if (locWeather != null && !locWeather.isEmpty()) {
                location = weatherService.getLocationByIp(locWeather);
            } else {
                location = weatherService.getLocationByIp(ipAddress);
            }
            WeatherInfoDTO weatherInfoDTO = weatherService.getWeatherByLocation(location, language, units);
            return ResponseEntity.status(HttpStatus.OK).body(weatherInfoDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getTemperatureMinMax")
    @Operation(summary = "Get max and min temperature by Ip Address", tags = { "Public Resource Management" })
    public ResponseEntity<?> getTemperatureMinMax(
        @RequestParam(name = "ip-address") String ipAddress,
        @RequestParam(name = "language") String language
    ) {
        try {
            LocationDTO location = weatherService.getLocationByIp(ipAddress);
            String data = weatherService.getCurrentInfoWeather(location, language);
            return ResponseEntity.status(HttpStatus.OK).body(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/previewByUrl")
    @Operation(summary = "Get the location by Ip Address", tags = { "Public Resource Management" })
    @UserActivity(activityName = Constants.UserActivities.PREVIEW_URL)
    public ResponseEntity<?> previewByUrl(
        @RequestParam String url,
        @RequestParam(value = "connectomeId", required = false) String connectomeId
    ) {
        try {
            return ResponseEntity.ok().body(learningService.previewByUrl(url));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getFakeDataNetworkChart")
    @Operation(summary = "Get the data fake to build network chart", tags = { "Public Resource Management" })
    public ResponseEntity<?> getFakeDataNetworkChart() {
        try {
            String uri = "https://echarts.apache.org/examples/data/asset/data/les-miserables.json";
            String strJson = connectAdapterApi.getDataFromAdapterApi(uri, null, HttpMethod.GET);
            return ResponseEntity.ok(strJson);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/test/{connectomeId}")
    public void test(@PathVariable String connectomeId) throws ParseException {
        String date = "2022-05-28 03:00:02.422Z";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS'Z'");
        Date date1 = formatter.parse(date);

        String connectomeIdThat = StringUtil.isNullOrEmpty(connectomeId) ? "CID_fb4c7200-c03d-4fc1-9f5a-add01214a91c" : connectomeId;
        Long countNewFeed = 20L;
        //        Optional<Connectome> optionalConnectome = connectomeRepository.findById(connectomeId);
        //        if (optionalConnectome.isPresent()) {
        //            Long newFeed = connectomeFeedResource.countNewFeedByDate(connectomeId, date1.toInstant());
        //            List<String> receiverIds = new ArrayList<>(Collections.singletonList(optionalConnectome.get().getUser().getId()));
        //            notificationService.saveNotificationByAdminWithInterpolationType(
        //                receiverIds,
        //                Constants.NotificationName.LEARNING_COMPLETE,
        //                null,
        //                Utils.appendStyle(newFeed.toString(), "text-danger")
        //            );
        //        }
        messagingTemplate.convertAndSend("/topic/updateFeed", new RabbitmqConsumer.WebLoggingReceiver(connectomeId, date));
    }

    @PostMapping("/previewByUrl-v2")
    @Operation(summary = "Get the location by Ip Address", tags = { "Public Resource Management" })
    @UserActivity(activityName = Constants.UserActivities.PREVIEW_URL)
    public ResponseEntity<?> previewByUrlV2(
        @RequestBody String url,
        @RequestParam(value = "connectomeId", required = false) String connectomeId
    ) {
        try {
            return ResponseEntity.ok().body(learningService.previewByUrl(url));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
