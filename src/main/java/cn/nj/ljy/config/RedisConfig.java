package cn.nj.ljy.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    private boolean testOnBorrow;
    private boolean testOnReturn;
    private int maxIdle;
    private int minIdle;
    private int maxWait;
    private int maxActive;
    private String host;
    private int port;
    private String password;
    private int timeout;
    private int database;

    
    
//    public static void main(String[] args) {
//        
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxIdle(15);
//        config.setMinIdle(10);
//        config.setMaxWaitMillis(30000);
//        config.setMaxTotal(20);
//        config.setTestOnBorrow(true);
//        config.setTestOnReturn(true);
//        
////        JedisPool pool = new JedisPool(config, "99.48.66.13", 6379, 1000, "1qaz@WSX");
//        JedisPool pool = new JedisPool(config, "127.0.0.1", 6379, 1000);
//        Jedis jedis = null;
//        
//        try {
//            if (pool != null) {
//                jedis = pool.getResource();
//                jedis.select(0);
//            }
//        } catch (Exception e) {
//            System.out.println("------ 获取redis连接出现异常 ------");
//            e.printStackTrace();
//        }
////        jedis.set("a", "1");
//       System.out.println( jedis.get("a"));
//    }
    
    
    @Bean
    public JedisPoolConfig getRedisConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWait);
        config.setMaxTotal(maxActive);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        return config;
    }

    @Bean
    public JedisPool getJedisPool() {
        JedisPoolConfig config = getRedisConfig();
//        JedisPool pool = new JedisPool(config, host, port, timeout, password);
        JedisPool pool = new JedisPool(config, host, port, timeout);
        return pool;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

}
