package cn.nj.ljy.util.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

/**
 * 返回map 成功map包含 header和body 失败map包含 message 默认最大连接 400 , 每个host最多 200 个连接 15秒内建立连接失败 返回空 15 秒内数据传输失败返回空
 * DEFAULT_MAX_PER_ROUTE 建议不能超过300(经测试超过300会出问题) , http请求的相应时间同样会影响 ,接口的并发量 ,建议接口的并发量 为
 * DEFAULT_MAX_PER_ROUTE/http的平均响应时间 默认 1秒内无法从httpClient连接池获取连接 返回空
 * <p/>
 * 如需自定义连接数和超时时间 , 需要使用 buildCustomerHttpClient 和 buildCustomerRequestConfig 创建自定义的 HttpClient和RequestConfig
 * 并在传参的时候传入自定义的 HttpClient和RequestConfig Created by liuxinyi on 2016/5/24.
 */
public class HttpUtil {

    public static final String RESP_MAP_KEY_HEAD = "header";
    public static final String RESP_MAP_KEY_BODY = "body";
    public static final String RESP_MAP_KEY_STAT = "status";
    public static final String RESP_MAP_KEY_MSG = "message";

    // private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);
    private static final CloseableHttpClient defaultHttpClient;
    /**
     * SOCKET_TIMEOUT 数据传输超时时间 默认15秒
     */
    private static final int SOCKET_TIMEOUT = 15000;
    /**
     * CONNECTION_TIMEOUT 建立连接超时时间 默认15秒
     */
    private static final int CONNECTION_TIMEOUT = 15000;
    /**
     * 从httpClient的连接池获取连接的超时时间 ,httpClient默认会一直等待,直到获取连接,这样可能会导致dubbo超时 httpUtil设置默认时间为1秒
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 1000;

    /**
     * 最大连接数,单独使用httpUtil的默认最大连接数,如果同时使用的httpUtil默认的工具方法和自定义的httpClient ,最大连接数会是两者相加
     */
    private static final int MAX_TOTAL = 400;

    /**
     * 每个ip地址对应的最大连接数,假如一个应用只连接到某一个ip地址,最大连接数MAX_TOTAL没有意义, DEFAULT_MAX_PER_ROUTE是最大的连接数
     */
    private static final int DEFAULT_MAX_PER_ROUTE = 200;

    private static RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();

    private static SSLConnectionSocketFactory sslSocketFactory;

    static {
        try {
            sslSocketFactory = new SSLConnectionSocketFactory(
                    new SSLContextBuilder().loadTrustMaterial(new TrustSelfSignedStrategy()).build());
        } catch (Exception e) {
        }

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register(HttpConstants.PROTOCOL_HTTP, PlainConnectionSocketFactory.getSocketFactory())
                .register(HttpConstants.PROTOCOL_HTTPS, sslSocketFactory).build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(MAX_TOTAL);
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        defaultHttpClient = HttpClients.custom().setConnectionManager(cm)
                // .setSSLSocketFactory(sslSocketFactory)
                // .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
    }

    /**
     * 
     * 发送get请求
     *
     * @version [V1.0, 2017年7月6日]
     * @param url
     * @param parameters
     * @return map
     */
    public static Map<String, Object> get(String url, List<NameValuePair> parameters) {
        String queryString = null;
        if (parameters != null && parameters.size() > 0) {
            queryString = URLEncodedUtils.format(parameters, "utf-8");
        }
        String requestUrl = url;
        if (queryString != null) {
            if (!url.contains("?"))
                requestUrl += "?" + queryString;
            else
                requestUrl += "&" + queryString;
        }

        return get(requestUrl, null, defaultHttpClient, defaultRequestConfig);
    }

    /**
     * 发送get请求
     *
     * @param url 请求行
     * @param headers 请求头
     * @return map
     */
    public static Map<String, Object> get(String url, Map<String, String> headers) {
        return get(url, headers, defaultHttpClient, defaultRequestConfig);
    }

    /**
     * <pre>
     * 发送get请求, 获取原生结果。
     * 适用于那些需要获取输入流的使用场景。
     * 注意：使用完毕一定要执行：<code>response.close()</code>
     *
     * &#64;param url     请求行
     * &#64;param headers 请求头
     * &#64;return CloseableHttpResponse
     * </pre>
     */
    public static CloseableHttpResponse getRawResult(String url, Map<String, String> headers) {
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(url);
            get.setConfig(defaultRequestConfig);
            get.setHeader("Accept-Charset", HttpConstants.UTF_8);
            if (MapUtils.isNotEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    get.setHeader(entry.getKey(), entry.getValue());
                }
            }
            response = defaultHttpClient.execute(get);
        } catch (IOException e) {
            // LOG.error("get method , url: {} ", url, e);
        }
        return response;
    }

    /**
     * @param url 请求行
     * @param headers 请求头
     * @param httpClient httpClient
     * @param requestConfig requestConfig
     * @return
     */
    public static Map<String, Object> get(String url, Map<String, String> headers, CloseableHttpClient httpClient,
            RequestConfig requestConfig) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(url);
            get.setConfig(requestConfig);
            get.setHeader(HttpConstants.ACCEPT_CHARSET, HttpConstants.UTF_8);
            if (MapUtils.isNotEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    get.setHeader(entry.getKey(), entry.getValue());
                }
            }
            response = httpClient.execute(get);
            buildDefaultMap(result, response);
        } catch (IOException e) {
            // LOG.error("get method , url: {} ", url, e);
            result.put(RESP_MAP_KEY_MSG, "get url : " + url + " ; errorMessage" + e.getMessage());
        } finally {
            try {
                response.close();
            } catch (Exception e) {
            }
        }

        return result;
    }

    private static void buildDefaultMap(Map<String, Object> result, CloseableHttpResponse response) throws IOException {
        result.put(RESP_MAP_KEY_HEAD, response.getAllHeaders());
        HttpEntity httpResponseEntity = response.getEntity();
        String responseBody = EntityUtils.toString(httpResponseEntity, HttpConstants.UTF_8);
        result.put(RESP_MAP_KEY_BODY, responseBody);
        result.put(RESP_MAP_KEY_STAT, response.getStatusLine().getStatusCode());
    }

    /**
     * 返回响应码是否是成功的200
     * 
     * @return
     */
    public static boolean isSucc200(Map<String, Object> result) {
        try {
            return result == null ? false : (200 == (int) result.get(RESP_MAP_KEY_STAT));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getBody(Map<String, Object> result) {
        try {
            return result == null ? "" : (String) result.get(RESP_MAP_KEY_BODY);
        } catch (Exception e) {
            return "";
        }
    }

    public static Header[] getHeader(Map<String, Object> result) {
        try {
            return result == null ? null : (Header[]) result.get(RESP_MAP_KEY_HEAD);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 发送post请求 专门用于使用最频繁的Content-Type 为json的请求
     *
     * @param url 请求行
     * @param headers 请求头 Content-Type 已经设置为json
     * @param content 请求体 json string 格式
     * @return map
     */
    public static Map<String, Object> postJson(String url, Map<String, String> headers, String content) {
        return postJson(url, headers, content, defaultHttpClient, defaultRequestConfig);
    }

    /**
     * @param url 请求行
     * @param headers 请求头
     * @param content 请求体
     * @param httpClient httpClient
     * @param requestConfig requestConfig
     * @return
     */
    public static Map<String, Object> postJson(String url, Map<String, String> headers, String content,
            CloseableHttpClient httpClient, RequestConfig requestConfig) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        CloseableHttpResponse response = null;
        try {
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setHeader(HttpConstants.CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
            post.setHeader(HttpConstants.ACCEPT_CHARSET, HttpConstants.UTF_8);
            if (MapUtils.isNotEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            }
            post.setEntity(HttpEntityUtil.buildJsonStringEntity(content));

            response = httpClient.execute(post);

            buildDefaultMap(result, response);

        } catch (IOException e) {
            // LOG.error("postJson , url : {} , content : {}", url, content, e);
            result.put(RESP_MAP_KEY_MSG, "postJson url : " + url + " ; errorMessage" + e.getMessage());
        } finally {
            try {
                response.close();
            } catch (Exception e) {
            }
        }

        return result;
    }

    /**
     * 发送post 请求 用于发送各种各样的post请求 该方法并不会根据具体的HttpEntity的具体类型做相应的处理 关于错误日志,该方法无法打印出entity的内容,需要传参之前自己打印日志 文件上传
     * MultipartEntityBuilder 生产的entity 可以不设置Content-Type 也可以具体设置为 multipart/form-data 需要根据具体情况在
     *
     * @param headers 里面添加具体的Content-Type 不写默认为 text/html 常见的 Content-Type有 application/json
     *            ,application/x-www-form-urlencoded , application/atom+xml , application/xml , multipart/form-data
     * @param url 请求行
     * @param entity 请求体 HttpEntity 格式
     * @return map key header body
     */
    public static Map<String, Object> postEntity(String url, Map<String, String> headers, HttpEntity entity) {
        return postEntity(url, headers, entity, defaultHttpClient, defaultRequestConfig);
    }

    /**
     * @param url 请求行
     * @param headers 请求头
     * @param entity 请求体
     * @param httpClient httpClient
     * @param requestConfig requestConfig
     * @return
     */
    public static Map<String, Object> postEntity(String url, Map<String, String> headers, HttpEntity entity,
            CloseableHttpClient httpClient, RequestConfig requestConfig) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        CloseableHttpResponse response = null;
        try {
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setHeader(HttpConstants.ACCEPT_CHARSET, HttpConstants.UTF_8);
            if (MapUtils.isNotEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            }
            post.setEntity(entity);

            response = httpClient.execute(post);

            buildDefaultMap(result, response);

        } catch (IOException e) {
            // LOG.error("postEntity method , url : {} ", url, e);
            result.put(RESP_MAP_KEY_MSG, "postEntity url : " + url + " ; errorMessage" + e.getMessage());
        } finally {
            try {
                response.close();
            } catch (Exception e) {
            }
        }

        return result;
    }

    /**
     * 获取自定义的httpClient
     *
     * @param maxTotal httpclient最大连接数
     * @param maxPerRoute 每个ip地址最大连接数
     * @return httpclient
     */
    public static CloseableHttpClient buildCustomerHttpClient(int maxTotal, int maxPerRoute) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * 获取超时时间设置 单位毫秒
     *
     * @param socketTimeout 数据传输超时时间
     * @param connectionTimeout 建立连接超时时间
     * @param connectionRequestTimeout 从httpClient的连接池获取连接超时时间
     * @return RequestConfig
     */
    public static RequestConfig buildCustomerRequestConfig(int socketTimeout, int connectionTimeout,
            int connectionRequestTimeout) {
        return RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout).build();
    }

}
