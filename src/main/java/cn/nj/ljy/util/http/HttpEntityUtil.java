package cn.nj.ljy.util.http;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.common.utils.CollectionUtils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpEntityUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HttpEntityUtil.class);

    /**
     * 生成StringEntity 编码为utf-8
     *
     * @param string StringEntity的字符串
     * @return HttpEntity
     */
    public static HttpEntity buildTextStringEntity(String string) {
        StringEntity entity = new StringEntity(string, HttpConstants.UTF_8);
        entity.setContentEncoding(HttpConstants.UTF_8);
        return entity;
    }

    /**
     * 生成StringEntity 编码为utf-8
     *
     * @param string StringEntity的字符串
     * @return HttpEntity
     */
    public static HttpEntity buildJsonStringEntity(String string) {
        StringEntity entity = new StringEntity(string, HttpConstants.UTF_8);
        entity.setContentType("application/json");
        entity.setContentEncoding(HttpConstants.UTF_8);
        return entity;
    }

    /**
     * 生成UrlEncodedFormEntity 用于 Content-Type 为 application/x-www-form-urlencoded 类型的post请求
     *
     * @param formParams 表单数据
     * @return HttpEntity
     */
    public static HttpEntity buildUrlEncodedFormEntity(Map<String, String> formParams) {
        UrlEncodedFormEntity entity = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (MapUtils.isNotEmpty(formParams)) {
            for (Map.Entry<String, String> entry : formParams.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
            entity = new UrlEncodedFormEntity(params);
        } catch (UnsupportedEncodingException e) {
            LOG.error("buildUrlEncodedFormEntity", e);
        }
        entity.setContentEncoding(HttpConstants.UTF_8);
        return entity;
    }

    /**
     * 生成 MultipartEntity 该方法不会验证文件是否存在,传参之前需要自己validate
     *
     * @param texts Texts
     * @param fileList 文件列表url
     * @return HttpEntity
     */
    public static HttpEntity buildMultipartEntity(Map<String, String> texts, List<String> fileList) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (MapUtils.isNotEmpty(texts)) {
            for (Map.Entry<String, String> entry : texts.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue());
            }
        }
        if (CollectionUtils.isNotEmpty(fileList)) {
            for (String filePath : fileList) {
                File file = new File(filePath);
                builder.addPart(file.getName(), new FileBody(file));
            }
        }
        HttpEntity entity = builder.build();
        return entity;
    }

    /**
     * 生成 MultipartEntity 该方法不会验证文件是否存在,传参之前需要自己validate
     *
     * @param texts
     * @param name
     * @param multipartFile
     * @param contentType
     * @param fileName
     * @return
     * @throws Exception
     */
    public static HttpEntity buildMultipartEntity(Map<String, String> texts, String name, MultipartFile multipartFile,
            ContentType contentType, String fileName) throws Exception {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (MapUtils.isNotEmpty(texts)) {
            for (Map.Entry<String, String> entry : texts.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue());
            }
        }
        builder.addBinaryBody(name, multipartFile.getInputStream(), contentType, fileName);
        HttpEntity entity = builder.build();
        return entity;
    }

    /**
     * 生成 MultipartEntity
     *
     * @param name
     * @param inputStream
     * @return
     */
    public static HttpEntity buildMultipartEntity(String name, InputStream inputStream) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody(name, inputStream);
        return builder.build();
    }

}
