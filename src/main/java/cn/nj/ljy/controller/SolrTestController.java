package cn.nj.ljy.controller;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "solr")
public class SolrTestController {

    @Autowired
    private SolrClient client;

    @RequestMapping("/select")
    @ResponseBody
    public String select() {

        SolrQuery params = new SolrQuery();
        params.set("q", "desc_s:*yg* AND age_i:2");
        params.set("start", 0);
        params.set("rows", 5);
        params.set("sort", "age_i asc");
        StringBuilder sb = new StringBuilder();
        try {
            QueryResponse rsp = client.query("userinfo",params);
            SolrDocumentList docs = rsp.getResults();
            for (SolrDocument doc : docs) {
                sb.append(doc.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
