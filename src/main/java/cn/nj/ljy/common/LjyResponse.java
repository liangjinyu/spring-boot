package cn.nj.ljy.common;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class LjyResponse<T> implements Serializable {

    static final String CODE = "code";
    static final String DESC = "desc";
    static final String CONTENT = "content";

    /**
     * 序列化
     */
    private static final long serialVersionUID = -6010932515432454307L;

    /**
     * 结果编码
     */
    private String code;

    /**
     * 结果信息
     */
    private String desc;

    /**
     * 返回结果
     */
    private T content;

    /**
     * 无参构造
     */
    public LjyResponse() {
    }

    /**
     * @param code
     * @param desc
     */
    public LjyResponse(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @param code
     * @param desc
     * @param content
     */
    public LjyResponse(String code, String desc, T content) {
        this.code = code;
        this.desc = desc;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public T getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ResultBean [code=");
        builder.append(code);
        builder.append(", desc=");
        builder.append(desc);
        builder.append(", content=");
        builder.append(content);
        builder.append("]");
        return builder.toString();
    }

    /**
     * 功能描述: <br>
     * 转化成string
     *
     * @version [V1.0, 2017年1月16日]
     * @return
     */
    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    public static void main(String[] args) {
        LjyResponse<String> r = new LjyResponse<String>();
        r.setCode("0");
        r.setDesc("success");
        r.setContent("aaa");
        System.out.println(JSON.toJSONString(r));

    }

}
