package cn.nj.ljy.common;

import java.io.Serializable;

public class PageResponse<T> extends LjyResponse<T>implements Serializable {

    static final String TOTAL_NUM = "totalNum";
    static final String TOTAL_PAGE = "totalPage";

    private static final long serialVersionUID = 593902787529066826L;

    private int totalNum;
    private int totalPage;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

}
