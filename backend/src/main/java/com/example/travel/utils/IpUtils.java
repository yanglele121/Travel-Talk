package com.example.travel.utils;

/**
 * @author 杨乐乐
 * @time 2023-04-25 13:43
 */
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class IpUtils {
    public static String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress;
    }

    public static String getAddress(String ip) {
        String address = "";
        try {
            URL url = new URL("https://apis.map.qq.com/ws/location/v1/ip?ip=" + ip +"&key=NXNBZ-KNLWQ-OIM52-BVGNY-5QLJV-UAFOY");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            JSONObject json = JSONObject.parseObject(result.toString());
            System.out.println(json);
            if (Objects.equals(json.getString("message"), "Success")) {
                JSONObject data = json.getJSONObject("result");
                System.out.println(data);
                if(data.getString("ad_info")!=null){
                    JSONObject area = data.getJSONObject("ad_info");
                    String province = area.getString("province");
                    String city = area.getString("city");
                    if(province.endsWith("省")||province.endsWith("市")){
                        province=province.substring(0,province.length()-1);
                    }
                    if(city.endsWith("市")){
                        city=city.substring(0,city.length()-1);
                    }
                    address=province+"·"+city;
                }
                else address="中国·内网";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return address;
        }
        System.out.println(address);
        return address;
    }
}

