package cn.nj.ljy.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SolrTest {

    private SolrClient client;

    public static void main(String[] args) {

        SolrTest t = new SolrTest();
        t.client = t.createNewSolrClient();
         t.createDocs();
//        t.queryDocs();
//         t.deleteByQuery("*:*");
        try {
            t.client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void deleteById(String id) {
        System.out.println("======================deleteById ===================");
        try {
            UpdateResponse rsp = client.deleteById(id);
            client.commit();
            System.out.println("delete id:" + id + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByQuery(String queryCon) {
        System.out.println("======================deleteByQuery ===================");
        UpdateResponse rsp;

        try {
            UpdateRequest commit = new UpdateRequest();
            commit.deleteByQuery(queryCon);
            commit.setCommitWithin(5000);
            commit.process(client);
            System.out
                    .println("url:" + commit.getPath() + "\t xml:" + commit.getXML() + " method:" + commit.getMethod());
            // rsp = client.deleteByQuery(queryCon);
            // client.commit();
            // System.out.println("delete query:" + queryCon + " result:" + rsp.getStatus() + " Qtime:" +
            // rsp.getQTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void queryDocs() {
        SolrQuery params = new SolrQuery();
        System.out.println("======================query===================");

        params.set("q", "description:*yg* AND age_i:2");
        params.set("start", 0);
        params.set("rows", 5);
        params.set("sort", "age_i asc");

        try {
            QueryResponse rsp = client.query(params);
            SolrDocumentList docs = rsp.getResults();
            System.out.println("查询内容:" + params);
            System.out.println("文档数量：" + docs.getNumFound());
            System.out.println("查询花费时间:" + rsp.getQTime());

            System.out.println("------query data:------");
            for (SolrDocument doc : docs) {

                System.out.println(doc);
            }
            System.out.println("-----------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private SolrClient createNewSolrClient() {
        try {
            HttpSolrClient client = new HttpSolrClient("http://127.0.0.1:8983/solr/userinfo");
            client.setConnectionTimeout(30000);
            client.setDefaultMaxConnectionsPerHost(100);
            client.setMaxTotalConnections(100);
            client.setSoTimeout(30000);
            return client;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void createDocs() {
        System.out.println("======================add doc ===================");
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        
        SolrInputDocument doc1 = new SolrInputDocument();
        doc1.addField("id", UUID.randomUUID().toString());
        doc1.addField("name", "王大兵");
        doc1.addField("description", "王大兵是家里的老大，外号大兵，哈哈哈");
        doc1.addField("age_i", 30);
        docs.add(doc1);
        
        SolrInputDocument doc2 = new SolrInputDocument();
        doc2.addField("id", UUID.randomUUID().toString());
        doc2.addField("name", "王小兵");
        doc2.addField("description", "王小兵是家里的老幺，外号小兵，啊啊啊");
        doc2.addField("age_i", 30);
        docs.add(doc2);
        
        SolrInputDocument doc3 = new SolrInputDocument();
        doc3.addField("id", UUID.randomUUID().toString());
        doc3.addField("name", "王兵");
        doc3.addField("description", "王兵和大兵，小兵不认识，啦啦啦啊啊啊");
        doc3.addField("age_i", 30);
        docs.add(doc3);
        
        
        SolrInputDocument doc4 = new SolrInputDocument();
        doc4.addField("id", UUID.randomUUID().toString());
        doc4.addField("name", "么么么兽");
        doc4.addField("description", "我是米么的么么兽，阿萨德了放款件阿斯顿发生地方阿萨德发");
        doc4.addField("age_i", 30);
        docs.add(doc4);
        
        SolrInputDocument doc5 = new SolrInputDocument();
        doc5.addField("id", UUID.randomUUID().toString());
        doc5.addField("name", "jack jj good");
        doc5.addField("description", "aaa bbb  ccc good boy");
        doc5.addField("age_i", 30);
        docs.add(doc5);
        
        SolrInputDocument doc6 = new SolrInputDocument();
        doc6.addField("id", UUID.randomUUID().toString());
        doc6.addField("name", "rose mm bad");
        doc6.addField("description", "aaa bbb  ccc bad girl");
        doc6.addField("age_i", 30);
        docs.add(doc6);
        


//        
//        for (int i = 1; i <= 100; i++) {
//            SolrInputDocument doc1 = new SolrInputDocument();
//            doc1.addField("id", UUID.randomUUID().toString());
//            doc1.addField("name", "name" + i);
//            doc1.addField("description", getRandomString(10));
//            doc1.addField("age_i", getRandomInt());
//
//            docs.add(doc1);
//        }
        try {
            UpdateResponse rsp = client.add(docs);
            System.out
                    .println("Add doc size" + docs.size() + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());

            UpdateResponse rspcommit = client.commit();
            System.out.println("commit doc to index" + " result:" + rsp.getStatus() + " Qtime:" + rsp.getQTime());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private Object getRandomInt() {
        Random random = new Random();
        return random.nextInt(100);
    }

    public static String getRandomString(int length) {
        // String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String str = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
