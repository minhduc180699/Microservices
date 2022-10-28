package com.saltlux.deepsignal.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.service.dto.PreviewUrlDTO;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LearningService {

    private final Logger log = LoggerFactory.getLogger(LearningService.class);

    public PreviewUrlDTO previewByUrl(String url) throws IOException, URISyntaxException {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        URI uri = new URI(url);
        Document document = Jsoup.connect(url).get();
        //        String meTaTitle = getMetaTagContent(document, "meta[name=title]");
        String title = document.title();
        String desc = getMetaTagContent(document, "meta[name=description]");
        String ogUrl = StringUtils.defaultIfBlank(getMetaTagContent(document, "meta[property=og:url]"), url);
        String ogTitle = getMetaTagContent(document, "meta[property=og:title]");
        String ogDesc = getMetaTagContent(document, "meta[property=og:description]");
        String ogImage = getMetaTagContent(document, "meta[property=og:image]");
        String ogImageAlt = getMetaTagContent(document, "meta[property=og:image:alt]");
        String favicon = getFavicon(document, uri);
        String author = getAuthor(document);
        String publicTime = getPucblicTime(document);
        //        String keyword = uri.getPath().replace("/", "  ").trim().replace("  ", ",");
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
            ogImageAlt,
            author,
            favicon,
            publicTime,
            null
        );
    }

    public String getFavicon(Document document, URI uri) {
        String[] htmlCollectionCheck = { "link[rel='icon']", "link[rel='shortcut icon']" };
        int lengArr = htmlCollectionCheck.length;
        int i = 0;
        while (i < lengArr) {
            Elements elms = document.select(htmlCollectionCheck[i]);
            if (elms != null && elms.size() > 0) {
                String href = elms.first().attr("href");
                if (href.startsWith("http://") || href.startsWith("https://")) return href; else {
                    String domain = uri.getHost();
                    return uri.getScheme() + "://" + domain + elms.first().attr("href");
                }
            }
            i++;
        }
        return "";
    }

    public String getMetaTagContent(Document document, String cssQuery) {
        Element elm = document.select(cssQuery).first();
        if (elm != null) {
            return elm.attr("content");
        }
        return "";
    }

    public String getAuthor(Document document) {
        String[] htmlCollectionCheck = { "meta[property='article:author']", "meta[name='author']" };
        int lengArr = htmlCollectionCheck.length;
        int i = 0;
        while (i < lengArr) {
            Elements elms = document.select(htmlCollectionCheck[i]);
            if (elms != null && elms.size() > 0) return elms.first().attr("content");
            i++;
        }
        return "";
    }

    public String getPucblicTime(Document document) {
        String[] htmlCollectionCheck = {
            "meta[property='article:published_time']",
            "meta[name='pubdate']",
            "meta[name='date']",
            "meta[name='parsely-pub-date']",
            "meta[name='publication_date']",
            "meta[name='ailthru.date']",
            "meta[name='dcterms.created']",
            "meta[name='cXenseParse:recs:publishtime']",
            "meta[name='DCSext.articleFirstPublished']",
            "meta[property='story:published_time']",
            "meta[name='datePublished']",
            "meta[itemprop='datePublished']",
            "meta[name='Last-Modified']",
        };
        int lengArr = htmlCollectionCheck.length;
        int i = 0;
        while (i < lengArr) {
            Elements elms = document.select(htmlCollectionCheck[i]);
            if (elms != null && elms.size() > 0) return elms.first().attr("content");
            i++;
        }

        Elements scripts = document.getElementsByAttributeValue("type", "application/ld+json");
        String jsonText = "";
        for (Element e : scripts) {
            jsonText = e.data();
            if (!jsonText.contains("datePublished") && !jsonText.contains("dateModified") && !jsonText.contains("uploadDate")) continue;
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                JSONObject jsonObject = objectMapper.readValue(jsonText, JSONObject.class);
                if (jsonObject.has("datePublished")) {
                    return jsonObject.get("datePublished").toString().replace("\"", "");
                }
                if (jsonObject.has("dateModified")) {
                    return jsonObject.get("dateModified").toString().replace("\"", "");
                }
                if (jsonObject.has("uploadDate")) {
                    return jsonObject.get("uploadDate").toString().replace("\"", "");
                }
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return "";
    }
}
