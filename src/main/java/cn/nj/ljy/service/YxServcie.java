package cn.nj.ljy.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.nj.ljy.util.DateUtil;
import cn.nj.ljy.util.ParseMD5;
import cn.nj.ljy.util.http.HttpConstants;
import cn.nj.ljy.util.http.HttpUtil;

@Component
public class YxServcie {

    private static final Logger LOGGER = LoggerFactory.getLogger(YxServcie.class);
    
    private static final String PARAM_SPLITER = "&amp";

    @Value("${yxAppUrl}")
    private String URL;

    @Value("${yxAppKey}")
    private String appKey;

    @Value("${yxAppSecret}")
    private String appSerrcet;


    public String queryById(String id) throws UnsupportedEncodingException {

        List<NameValuePair> params = buildParams(id);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpConstants.CONTENT_TYPE, HttpConstants.CONTENT_TYPE_FORM);
        Map<String, Object> response = HttpUtil.postEntity(URL, headers, entity);
        String resultBody = response.get(HttpUtil.RESP_MAP_KEY_BODY).toString();
        LOGGER.info(resultBody);
        return resultBody;
    }

    public String queryCurl(String id) {
        List<NameValuePair> params = buildParams(id);

        StringBuilder sb = new StringBuilder(
                "curl -H \"Content-Type: application/x-www-form-urlencoded\" -X POST -d \"");

        for (NameValuePair param : params) {
            sb.append(param.getName()).append("=").append(param.getValue()).append(PARAM_SPLITER);
        }
        sb.delete(sb.length() - PARAM_SPLITER.length(), sb.length());
        sb.append("\" ");
        sb.append(URL);
        return sb.toString();
    }

    private List<NameValuePair> buildParams(String id) {
        LOGGER.info("id = " + id);
        HashMap<String, String> data = new HashMap<>();
        data.put("itemIds", id == null ? "10018002" : id);
        data.put("method", "yanxuan.item.batch.get");
        // 构建时间戳
        long currentTime = System.currentTimeMillis();
        String timestamp = DateUtil.parseLongToString(currentTime, "yyyy-MM-dd HH:mm:ss");

        // 计算签名
        String sign = getSign(appKey, appSerrcet, timestamp, data);

        // 组装参数
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appKey", appKey));
        params.add(new BasicNameValuePair("sign", sign));
        params.add(new BasicNameValuePair("timestamp", timestamp));
        for (Map.Entry<String, String> entry : data.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return params;
    }

    public static String getSign(String appKey, String appSecret, String timestamp, Map<String, String> paramMap) {
        // 将请求参数按名称排序
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("appKey", appKey);
        treeMap.put("timestamp", timestamp);
        if (null != paramMap) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                treeMap.put(entry.getKey(), entry.getValue());
            }
        }

        // 遍历treeMap，将参数值进行拼接
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = treeMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            sb.append(key).append("=");
            sb.append(treeMap.get(key));
        }

        // 参数值拼接的字符串收尾添加appSecret值
        String waitSignStr = appSecret + sb.toString() + appSecret;

        // 获取MD5加密后的字符串
        String sign = ParseMD5.parseStrToMd5U32(waitSignStr);

        System.out.println(".getSign() param={" + treeMap + "} sign={" + sign + "}");

        return sign;
    }

}
