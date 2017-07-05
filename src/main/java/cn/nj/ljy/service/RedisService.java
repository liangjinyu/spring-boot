package cn.nj.ljy.service;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.nj.ljy.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedisConfig config;

    private Jedis getJedis() {
        Jedis jedis = null;
        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
                jedis.select(config.getDatabase());
            }
        } catch (Exception e) {
            System.out.println("------ 获取redis连接出现异常 ------");
            e.printStackTrace();
        }
        return jedis;
    }

    /**
     * 
     * 功能描述: <br>
     * 释放链接
     *
     * @author 931636882@qq.com
     * @version [版本号, 2016年1月6日]
     * @param jedis redis连接
     * @param isBroken 是否是中断连接
     */
    private void release(Jedis jedis, boolean isBroken) {
        try {
            if (jedis != null) {
                if (isBroken) {
                    jedisPool.returnBrokenResource(jedis);
                } else {
                    jedisPool.returnResource(jedis);
                }
            }
        } catch (Exception e) {
            System.out.println("------ 释放redis连接出现异常  ------");
            e.printStackTrace();
        }
    }

    /**
     * 
     * 功能描述: <br>
     * 往redis中添加数据
     * 
     * @author 931636882@qq.com
     * @version [版本号, 2016年1月7日]
     * @param key 键
     * @param value 值
     * @param cacheSeconds 生命时间
     * @return 原数据
     */
    public String addString(String key, String value, int cacheSeconds) {
        Jedis jedis = null;
        boolean isBroken = false;
        String lastVal = null;
        try {
            jedis = getJedis();
            lastVal = jedis.getSet(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            } else {
                System.out.println("------ 往redis中保存数据的操作异常（设置生存时间为0），key:" + key + ",value:" + value);
            }
        } catch (Exception e) {
            isBroken = true;
            System.out.println("------ 往redis中保存数据出现异常，key:" + key + ",value:" + value);
            e.printStackTrace();
        } finally {
            release(jedis, isBroken);
        }
        return lastVal;
    }

    /**
     * 
     * 功能描述: <br>
     * 从redis中获取数据
     * 
     * @author 931636882@qq.com
     * @version [版本号, 2016年1月7日]
     * @param key 键
     * @return 值
     */
    public String getString(String key) {
        String value = null;
        boolean isBroken = false;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                Thread.sleep(50);
                jedis = getJedis();
            }
            value = jedis.get(key);
            value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
        } catch (Exception e) {
            isBroken = true;
            System.out.println("------ 从redis中获取数据出现异常，key:" + key);
            e.printStackTrace();
        } finally {
            release(jedis, isBroken);
        }
        return value;
    }

    /**
     * 功能描述: <br>
     * 根据条件查询键集合
     * 
     * @author 931636882@qq.com
     * @version [版本号, 2016年1月7日]
     * @param pattern 条件
     * @return 符合条件的键集合
     */
    public Set<String> keys(String pattern) {
        Set<String> list = null;
        boolean isBroken = false;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            list = jedis.keys(pattern);
        } catch (Exception e) {
            isBroken = true;
            System.out.println("------ 从redis中根据条件查询key集合出现异常，pattern:" + pattern + " ------");
            e.printStackTrace();
        } finally {
            release(jedis, isBroken);
        }
        return list;
    }

    /**
     * 功能描述: <br>
     * 从redis中清除数据
     * 
     * @author 931636882@qq.com
     * @version [版本号, 2016年1月7日]
     * @param cacheKey 键
     */
    public void delKey(String cacheKey) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            if (jedis.exists(cacheKey)) {
                jedis.del(cacheKey);
            }
        } catch (Exception e) {
            isBroken = true;
            System.out.println("------ 从redis中删除出现异常，cacheKey:" + cacheKey + " ------");
            e.printStackTrace();
        } finally {
            release(jedis, isBroken);
        }
    }

}
