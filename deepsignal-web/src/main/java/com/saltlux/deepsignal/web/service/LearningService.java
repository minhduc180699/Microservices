package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.service.dto.PreviewUrlDTO;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LearningService {

    private final Logger log = LoggerFactory.getLogger(LearningService.class);

    public PreviewUrlDTO previewByUrl(String url) throws IOException {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        Document document = Jsoup.connect(url).get();
        String title = getMetaTagContent(document, "meta[name=title]");
        String desc = getMetaTagContent(document, "meta[name=description]");
        String ogUrl = StringUtils.defaultIfBlank(getMetaTagContent(document, "meta[property=og:url]"), url);
        String ogTitle = getMetaTagContent(document, "meta[property=og:title]");
        String ogDesc = getMetaTagContent(document, "meta[property=og:description]");
        String ogImage = getMetaTagContent(document, "meta[property=og:image]");
        String ogImageAlt = getMetaTagContent(document, "meta[property=og:image:alt]");
        //        try {
        //            domain = InternetDomainName.from(new URL(ogUrl).getHost()).topPrivateDomain().toString();
        //        } catch (Exception e) {
        //            log.warn("Unable to connect to extract domain name from : {}", url);
        //        }
        return new PreviewUrlDTO(
            ogUrl,
            url,
            StringUtils.defaultIfBlank(ogTitle, title),
            StringUtils.defaultIfBlank(ogDesc, desc),
            ogImage,
            ogImageAlt
        );
    }

    private String getMetaTagContent(Document document, String cssQuery) {
        Element elm = document.select(cssQuery).first();
        if (elm != null) {
            return elm.attr("content");
        }
        return "";
    }
}
