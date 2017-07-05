package cn.nj.ljy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.nj.ljy.third.MessageProducer;

@Configuration
@EnableAutoConfiguration
@ConfigurationProperties
public class ThirdBeanConfig {

    private String accesskey;

    private String accesssecret;
    
    @Autowired
    private MessageProducer messageProducer;

    @Bean
    public MessageProducer getMessageProducer() {
//        MessageProducer messageProducer = new MessageProducer();
        messageProducer.setAccesskey(this.accesskey);
        messageProducer.setAccesssecret(this.accesssecret);
        return messageProducer;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getAccesssecret() {
        return accesssecret;
    }

    public void setAccesssecret(String accesssecret) {
        this.accesssecret = accesssecret;
    }

}
