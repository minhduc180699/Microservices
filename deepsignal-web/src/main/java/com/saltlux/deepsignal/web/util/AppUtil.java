package com.saltlux.deepsignal.web.util;

import com.saltlux.deepsignal.web.domain.CountryInfo;
import com.saltlux.deepsignal.web.service.dto.ImageDTO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class AppUtil {

    public static ImageDTO getImageSizeByUrl(String source) {
        try {
            URL url = new URL(source);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedImage image = ImageIO.read(in);
            int width = image.getWidth();
            int height = image.getHeight();
            return new ImageDTO(width, height, source);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            e.getMessage();
            return null;
        }
    }

    public static String getIP() {
        String ip = "";
        try {
            InetAddress myIP = InetAddress.getLocalHost();
            ip = myIP.getHostAddress();
        } catch (Exception e) {
            e.getMessage();
        }
        return ip;
    }

    public static String getDClassOfIP() {
        String ip = getIP();
        String[] ips = ip.split("\\.");
        return ips[ips.length - 1];
    }

    public static List<CountryInfo> moveToFisrtItem(List<CountryInfo> lstCountryInfo) {
        List<CountryInfo> lstPriorities = new ArrayList<>();
        if (arrLangPriorities != null && arrLangPriorities.length > 0) for (int i = arrLangPriorities.length - 1; i >= 0; i--) {
            for (int j = lstCountryInfo.size() - 1; j >= 0; j--) {
                if (lstCountryInfo.get(j).getCode().equals(arrLangPriorities[i])) {
                    lstPriorities.add(0, lstCountryInfo.get(j));
                    lstCountryInfo.remove(lstCountryInfo.get(j));
                    continue;
                }
            }
        }
        lstPriorities.addAll(lstCountryInfo);
        return lstPriorities;
    }

    public static String[] arrLangPriorities = { "US", "KR", "VN" };
}
