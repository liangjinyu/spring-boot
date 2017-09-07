package cn.nj.ljy.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import cn.nj.ljy.model.vo.SolrDocMerchandiseModel;

public class SolrTest {

    private SolrClient client;

    public static void main(String[] args) {

        SolrTest t = new SolrTest();
        t.client = t.createNewSolrClient();
        t.createDocs();
//         t.queryDocs();
//         t.deleteByQuery("*:*");
//        t.updateDocs();
        try {
            t.client.close();
            System.out.println("done");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void updateDocs() {
        try {
            
            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
            SolrInputDocument doc1 = new SolrInputDocument();
            doc1.addField("id","b1edfb25-81a7-4841-9250-8d9451691b77");
            doc1.addField("name", "商品名称");
            doc1.addField("description", "商品描述信息");
            doc1.addField("price", getRandomInt());
            doc1.addField("inventory", getRandomInt());
            docs.add(doc1);
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
        long begin = System.currentTimeMillis();
        System.out.println("======================query===================");

        params.set("q", "name:*");
        params.set("start", 0);
        params.set("rows", 10);
        params.set("sort", "price asc");
        params.set("facet", "on");
        params.set("facet.field", "inventory");
        try {
            QueryResponse rsp = client.query(params);
            SolrDocumentList docs = rsp.getResults();
            
            System.out.println("查询内容:" + params);
            System.out.println("文档数量：" + docs.getNumFound());
            System.out.println("查询花费时间:" + rsp.getQTime());
            System.out.println("------query data:------");
            for (SolrDocument doc : docs) {

                
                DocumentObjectBinder binder = new DocumentObjectBinder();
                SolrDocMerchandiseModel model =  binder.getBean(SolrDocMerchandiseModel.class, doc);
                System.out.println(model);
            }
            long time = System.currentTimeMillis()-begin;
            System.out.println("----------------------- time ="+time);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private SolrClient createNewSolrClient() {
        try {
            CloudSolrClient client = new CloudSolrClient.Builder().withZkHost("99.48.58.77:2181").build();
            client.setDefaultCollection("product");
            return client;

            // HttpSolrClient client = new HttpSolrClient("http://127.0.0.1:8983/solr/userinfo");
            // String url = "http://127.0.0.1:8983/solr/userinfo";
            // HttpSolrClient client = new HttpSolrClient.Builder(url).build();
            //
            // client.setConnectionTimeout(30000);
            // client.setDefaultMaxConnectionsPerHost(100);
            // client.setMaxTotalConnections(100);
            // client.setSoTimeout(30000);
            // return client;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void createDocs() {
        System.out.println("======================add doc ===================");
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();

         for (int i = 1; i <= 100; i++) {
         SolrInputDocument doc1 = new SolrInputDocument();
         doc1.addField("id", UUID.randomUUID().toString());
         doc1.addField("name", "name" + i);
         doc1.addField("description", getRandomString(10));
         doc1.addField("price", getRandomInt());
         doc1.addField("inventory", getRandomInt());
         docs.add(doc1);
         }

        File file = new File("C:\\Users\\liangjy\\Desktop\\merchandiseInfo.csv");
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                String[] infos = tempString.split(",\\$\\$\\$,");
                if (infos.length == 2) {
                    SolrInputDocument doc1 = new SolrInputDocument();
                    doc1.addField("id", UUID.randomUUID().toString());
                    doc1.addField("name", infos[0]);
                    doc1.addField("description", infos[1]);
                    doc1.addField("price", getRandomInt());
                    doc1.addField("inventory", getRandomInt());
                    docs.add(doc1);
                } else {
                    System.out.println(tempString);
                }

                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

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
