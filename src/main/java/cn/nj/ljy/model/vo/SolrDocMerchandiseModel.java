package cn.nj.ljy.model.vo;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;

public class SolrDocMerchandiseModel implements Serializable {

    /**
     */
    private static final long serialVersionUID = 4333815312221070429L;

    @Field
    private String id;

    @Field
    private String name;

    @Field("description")
    private String desc;

    @Field
    private int price;

    @Field
    private int inventory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return "SolrDocMerchandiseModel [id=" + id + ", name=" + name + ", desc=" + desc + ", price=" + price
                + ", inventory=" + inventory + "]";
    }

}
