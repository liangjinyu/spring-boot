package cn.nj.ljy.third;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);
    private String accesskey;

    private String accesssecret;

    @Value("${thirdParam}")
    private String thirdParam;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.warn("afterPropertiesSet, accesskey={}, accessecret={}", accesskey, accesssecret);

    }

    public String getAccesskey() {
        return accesskey;
    }

    public String getAccesssecret() {
        return accesssecret;
    }

    public void setAccesssecret(String accesssecret) {
        this.accesssecret = accesssecret;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getThirdParam() {
        return thirdParam;
    }

    public void setThirdParam(String thirdParam) {
        this.thirdParam = thirdParam;
    }

    @Override
    public String toString() {
        return "MessageProducer [accesskey=" + accesskey + ", accesssecret=" + accesssecret + ", thirdParam="
                + thirdParam + "]";
    }

}
