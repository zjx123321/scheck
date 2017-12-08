package com.simple.scheck.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private JredisFactory jredisFactory;

//    @Resource
//    private RedisTemplate<Object,Object> templateClient;

    /**
     * 断开连接
     */
    public void disconnect() {
        RedisConnection conn = jredisFactory.redisTemplate().getConnectionFactory().getConnection();
        if(!conn.isClosed()){
            conn.close();
        }
    }

    // ---------------------------------------------------------------------string操作----------------------------------------------
    /**
     * 设置单个值
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean set(String key, Object value) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            templateClient.opsForValue().set(key, value);
            flag = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }

    /**
     * 名为key进行赋值 如果不存在该字段则可以先创建在赋值返回true,否则返回false
     * @param key
     * @param value
     * @param timeout 单位：分钟
     * @return
     */
    public Boolean setnx(Object key, Object value,long timeout) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            if(templateClient.hasKey(key)){
                flag =false;
            }else{
                templateClient.opsForValue().set(key,value,timeout, TimeUnit.MINUTES);
                flag =true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }

    /**
     * 移除某个key
     *
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            templateClient.delete(key);
            flag = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }

    /**
     * 设置单个值的有效期
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean setex(String key, Object value, long timeout, TimeUnit unit) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            if(TimeUnit.DAYS.compareTo(unit)==0){
                //DAY级别，时间随机性为6小时
                int randomSecond =  RandomUtil.getRandomIntValue(1,6*60*60);
                long timeoutD = timeout*24*60*60+randomSecond;
                templateClient.opsForValue().set(key,value,timeoutD,TimeUnit.SECONDS);

            }else if (TimeUnit.HOURS.compareTo(unit)==0){
                //HOURS级别
                int randomSecond = RandomUtil.getRandomIntValue(1,10*60);
                long timeOutSecond = timeout*60*60+randomSecond;
                templateClient.opsForValue().set(key,value,timeOutSecond,TimeUnit.SECONDS);

            }else if (TimeUnit.MINUTES.compareTo(unit) == 0){
                //MINUTES 级别
                int randomMillis = RandomUtil.getRandomIntValue(1,1*60*1000);
                long timeoutMillis = timeout * 60 *1000+randomMillis;
                templateClient.opsForValue().set(key,value,timeoutMillis,TimeUnit.MILLISECONDS);
            }else if (TimeUnit.SECONDS.compareTo(unit) == 0){
                //SECONDS 级别
                int randomMillis = RandomUtil.getRandomIntValue(1,10000);
                long timeoutMillis = timeout *1000+randomMillis;
                templateClient.opsForValue().set(key,value,timeoutMillis,TimeUnit.MILLISECONDS);
            }else{
                templateClient.opsForValue().set(key,value,timeout,unit);

            }


            flag = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }
    /**
     * 获取单个值
     *
     * @param key
     * @return
     */
    public <T> Object get(String key) {
        Object result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }

        try {
            result =  templateClient.opsForValue().get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        Boolean result = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 判断hashKey是否存在
     * @param key
     * @return
     */
    public Boolean exists(String key,Object hashKey) {
        Boolean result = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForHash().hasKey(key,hashKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 模糊查询key
     * @param key
     * @return
     */
    public <T> Set<T> keys(String key) {
        Set<T> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.keys(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }
    /**
     * 获取key的type
     */
    public DataType type(String key) {
        DataType result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            DataType dataType = templateClient.type(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 在某段时间后失效
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, long timeout,TimeUnit unit) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            if(TimeUnit.DAYS.compareTo(unit)==0){
                //DAY级别，时间随机性为6小时
                int randomSecond =  RandomUtil.getRandomIntValue(1,6*60*60);
                long timeoutD = timeout*24*60*60+randomSecond;
                flag = templateClient.expire(key, timeoutD,TimeUnit.SECONDS);

            }else if (TimeUnit.HOURS.compareTo(unit)==0){
                //HOURS级别
                int randomSecond = RandomUtil.getRandomIntValue(1,10*60);
                long timeOutSecond = timeout*60*60+randomSecond;
                flag = templateClient.expire(key, timeOutSecond,TimeUnit.SECONDS);

            }else if (TimeUnit.MINUTES.compareTo(unit) == 0){
                //MINUTES 级别
                int randomMillis = RandomUtil.getRandomIntValue(1,1*60*1000);
                long timeoutMillis = timeout * 60 *1000+randomMillis;
                templateClient.expire(key,timeoutMillis,TimeUnit.MILLISECONDS);
            }else{
                flag = templateClient.expire(key, timeout,unit);

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }

    /**
     * 在某个时间点失效
     *
     * @param key
     * @param date
     * @return
     */
    public Boolean expireAt(String key, Date date) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            flag = templateClient.expireAt(key, date);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }

    /**
     * 获取key的活动时间或者有效期
     * @param key
     * @return
     */
    public Long ttl(String key) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.getExpire(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    public Long ttl(String key ,TimeUnit timeUnit){
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if(templateClient == null){
            return result;
        }
        try {
            result = templateClient.getExpire(key,timeUnit);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 修改指定key的某个位置对应的内容 为value
     * @param key
     * @param offset
     * @param value
     * @return
     */
    public boolean setbit(String key, long offset, boolean value) {

        boolean result = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForValue().setBit(key,offset,value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }
    /**
     * 读取key中对应的位置的内容 offset位置
     * @param key
     * @param offset
     * @return
     */
    public boolean getbit(String key, long offset) {
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        boolean result = false;
        if (templateClient == null) {
            return result;
        }

        try {
            result = templateClient.opsForValue().getBit(key,offset);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 设置key指定的位置开始所有的内容
     * @param key
     * @param offset
     * @param value
     * @return
     */
/*	public long setrange(String key, long offset, String value) {
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		long result = 0;
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = templateClient.opsForSet();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    /**
     * 获取指定key间隔端中的内容
     * @param key
     * @param startOffset
     * @param endOffset
     * @return
     */
	/*public String getrange(String key, long startOffset, long endOffset) {
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		String result = null;
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = templateClient.getrange(key, startOffset, endOffset);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    /**
     * 给名称为key的string赋予上一次的value
     * @param key
     * @param value
     * @return
     */
/*	public String getSet(String key, String value) {
		String result = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = templateClient.getSet(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    /**
     * 如果不存在名称为key的string，则向库中添加string，名称为key，值为value
     * @param key
     * @param value
     * @return
     */
    public Boolean setIfAbsent(String key, String value) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            flag = templateClient.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }

    /**
     * 向库中添加string（名称为key，值为value）同时，设定过期时间time
     * @param key
     * @param seconds
     * @param value
     * @return
     */
	/*public String setEx(String key, int seconds, String value) {
		String result = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = templateClient.setex(key, seconds, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    /**
     * 名称为key的string减少integer
     * @param key
     * @param integer
     * @return
     */
	/*public Long decrBy(String key, long integer) {
		Long result = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = templateClient.decrBy(key, integer);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    /**
     * 名称为key的string减1操作
     * @param key
     * @return
     */
	/*public Long decr(String key) {
		Long result = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = templateClient.decr(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    /**
     * 名称为key的string增integer操作
     * @param key
     * @param integer
     * @return
     */
	/*public Long incrBy(String key, long integer) {
		Long result = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = templateClient.incrBy(key, integer);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    /**
     * 名称为key的string增1
     * @param key
     * @return
     */
	/*public Long incr(String key) {
		Long result = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = templateClient.incr(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    /**
     * 名称为key的string的值附加value
     * @param key
     * @param value
     * @return
     */
    public Integer append(String key, String value) {
        Integer result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = templateClient.opsForValue().append(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的string的value的子串
     * @param key
     * @param start
     * @param end
     * @return
     */
/*	public String substr(String key, int start, int end) {
		String result = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = templateClient.substr(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    //---------------------------------------------------------------------hash------------------------------------------
    /**
     * 在key的Hash中所有hashKey的集合
     * @param key
     * @return
     */
    public Set<?> hKeys(String key) {
        Set<Object> set = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return set;
        }
        try {
            set = templateClient.opsForHash().keys(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return set;
    }

    /**
     * 向名称为key的hash中添加元素field<—>value
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Boolean hsetEx(String key, Object field, Object value,long timeout,TimeUnit unit) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            templateClient.opsForHash().put(key, field, value);

            if(TimeUnit.DAYS.compareTo(unit)==0){
                //DAY级别，时间随机性为6小时
                int randomSecond =  RandomUtil.getRandomIntValue(1,6*60*60);
                long timeoutD = timeout*24*60*60+randomSecond;
                flag = templateClient.expire(key, timeoutD,TimeUnit.SECONDS);

            }else if (TimeUnit.HOURS.compareTo(unit)==0){
                //TODO:
                flag = templateClient.expire(key, timeout,unit);

            }else{
                flag = templateClient.expire(key, timeout,unit);

            }


        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }


    /**
     * 向名称为key的hash中添加元素field<—>value
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Boolean hset(String key, Object field, Object value) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            templateClient.opsForHash().put(key, field, value);
            flag = true;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }

    /**
     * 返回名称为key的hash中field对应的value
     * @param key
     * @param field
     * @return
     */
    public Object hget(Object key, Object field) {
        Object result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = templateClient.opsForHash().get(key,field);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 名为key的field进行赋值 如果不存在该字段则可以先创建在赋值
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Boolean hsetnx(Object key, Object field, Object value) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            flag = templateClient.opsForHash().putIfAbsent(key,field,value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }
    /**
     * 向名称为key的hash中添加元素field i<—>value i
     * @param key
     * @param hash
     * @return
     */
    public boolean hmset(String key, Map<String, Object> hash) {
        boolean result = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            templateClient.opsForHash().putAll(key,hash);
            result = true;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }
    /**
     * 返回名称为key的hash中field i对应的value
     * @param key
     * @param fields
     * @return
     */
    public List hmget(String key, List fields) {
        List result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForHash().multiGet(key,fields);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }
    /**
     * 将名称为key的hash中field的value增加integer
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincrBy(String key, String field, long value) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForHash().increment(key, field, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 名称为key的hash中是否存在键为field的域
     * @param key
     * @param field
     * @return
     */
	/*public Boolean hexists(String key, String field) {
		Boolean result = false;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = templateClient.hexists(key, field);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/
    /**
     * 删除指定的key
     * @param key
     * @return
     */
/*	public Boolean del(Object key,Object ... fields) {
		Boolean flag = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return flag;
		}
		try {
			templateClient.opsForHash().delete(key, fields );
			flag = true;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			disconnect();
		}
		return flag;
	}*/
    /**
     * 删除名称为key的hash中键为field的域
     * @param key
     * @param fields
     * @return
     */
    public Boolean hdel(Object key,Object ... fields) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        boolean broken = false;
        try {
            templateClient.opsForHash().delete(key, fields );
            flag = true ;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }
    /**
     * 返回名称为key的hash中元素个数
     * @param key
     * @return
     */
    public Long hlen(Object key) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForHash().size(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }
    /**
     * 返回名称为key的hash中所有键
     * @param key
     * @return
     */
	/*public Map<Object,Object> hkeys(Object key) {
		Map<Object,Object> result = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		try {
			result = templateClient.opsForHash().entries(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			disconnect();
		}
		return result;
	}*/
    /**
     * 返回名称为key的hash中所有键对应的value
     * @param key
     * @return
     */
    public List<Object> hvals(Object key) {
        List<Object> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForHash().values(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的hash中所有的键（field）及其对应的value
     * @param key
     * @return
     */
    public Map<Object, Object> hgetAll(Object key) {
        Map<Object,Object> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForHash().entries(key);


        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    // ================list ====== l表示 left, r表示right====================
    /**
     * 在名称为key的list尾添加一个值为value的元素
     * @param key
     * @param value
     * @return
     */
    public Long rpush(Object key, Object value) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForList().rightPush(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 在名称为key的list头添加一个值为value的 元素
     * @param key
     * @param value
     * @return
     */
    public Long lpush(Object key, Object value) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 在名称为key的list头添加一个值为value的元素(从左向右插入)
     * @param key
     * @param value
     * @return
     */
    public Long lleftPushAll(Object key, Collection<Object> value) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForList().leftPushAll(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 在名称为key的list头添加一个值为value的元素(从右向左插入)
     * @param key
     * @param value
     * @return
     */
    public Long lrightPushAll(Object key, Collection<Object> value) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForList().rightPushAll(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    public Long lrightPushIfPresent(Object key, Object value) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForList().rightPushIfPresent(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的list的长度
     * @param key
     * @return
     */
    public Long llen(Object key) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForList().size(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的list中start至end之间的元素（下标从0开始，下同）
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List lrange(String key, long start, long end) {
        List result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForList().range(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 截取名称为key的list，保留start至end之间的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Boolean ltrim(Object key, long start, long end) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            templateClient.opsForList().trim(key, start, end);
            flag = true;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }

    /**
     * 返回名称为key的list中index位置的元素
     * @param key
     * @param index
     * @return
     */
    public Object lindex(Object key, long index) {
        Object result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return null;
        }
        try {
            result = templateClient.opsForList().index(key, index);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 给名称为key的list中index位置的元素赋值为value
     * @param key
     * @param index
     * @param value
     * @return
     */
    public Boolean lset(Object key, long index, Object value) {
        Boolean flag = false;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return flag;
        }
        try {
            templateClient.opsForList().set(key, index, value);
            flag = true;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return flag;
    }

    /**
     * 删除count个名称为key的list中值为value的元素。count为0，删除所有值为value的元素，count>0
     * 从头至尾删除count个值为value的元素，count<0从尾到头删除|count|个值为value的元素。
     * @param key
     * @param count
     * @param value
     * @return
     */
    public Long lrem(Object key, long count, Object value) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForList().remove(key, count, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回并删除名称为key的list中的首元素
     * @param key
     * @return
     */
    public Object lpop(Object key) {
        Object result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForList().leftPop(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回并删除名称为key的list中的尾元素
     * @param key
     * @return
     */
    public Object rpop(Object key) {
        Object result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForList().rightPop(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    // -------------------------------------------set-------------------------------------
    // return 1 add a not exist value ,
    // return 0 add a exist value
    /**
     * 向名称为key的set中添加元素member
     * @param key
     * @param values
     * @return
     */
    public Long sadd(String key, Object ... values) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForSet().add(key, values);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 测试member是否是名称为key的set的元素
     * @param key
     * @return
     */
    public Set<Object> smembers(Object key) {
        Set<Object> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForSet().members(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 删除名称为key的set中的元素member
     * @param key
     * @param values
     * @return
     */
    public Long srem(Object key, Object ... values) {
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        Long result = null;
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 随机返回并删除名称为key的set中一个元素
     * @param key
     * @return
     */
    public Object spop(Object key) {
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        Object result = null;
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForSet().pop(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的set的基数
     * @param key
     * @return
     */
    public Long scard(String key) {
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        Long result = null;
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForSet().size(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 测试member是否是名称为key的set的元素
     * @param key
     * @param member
     * @return
     */
    public Boolean sismember(Object key, Object member) {
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        Boolean result = null;
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForSet().isMember(key, member);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 随机返回名称为key的set的一个元素
     * @param key
     * @return
     */
    public Object srandmember(Object key) {
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        Object result = null;
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForSet().randomMember(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    //-----------------------------------------------------zset操作-----------------------------------
    /**
     * 向名称为key的zset中添加元素member，score用于排序。如果该元素已经存在，则根据score更新该元素的顺序。
     * @param key
     * @param member
     * @param score
     * @return
     */
    public Boolean zadd(Object key, Object member, double score) {
        Boolean result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForZSet().add(key,member, score);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    public Boolean zaddEx(Object key, Object member, double score,long timeout,TimeUnit unit) {
        Boolean result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForZSet().add(key,member, score);
            templateClient.expire(key, timeout,unit);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset（元素已按score从小到大排序）中的index从start到end的所有元素
     * @param key
     * @param start
     * @param end
     */
    public Set<Object> zrange(Object key, int start, int end) {
        Set<Object> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 删除名称为key的zset中的元素member
     * @param values
     */
    public Long zrem(Object key, Object ... values) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForZSet().remove(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 如果在名称为key的zset中已经存在元素member，则该元素的score增加increment；否则向集合中添加该元素，其score的值为increment
     * @param key
     * @param value
     * @param score
     */
    public Double zincrby(Object key, Object value, double score) {
        Double result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().incrementScore(key,value, score);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset（元素已按score从小到大排序）中member元素的rank（即index，从0开始），若没有member元素，返回“nil”
     * @param key
     * @param member
     * @return
     */
    public Long zrank(Object key, Object member) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().rank(key, member);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset（元素已按score从大到小排序）中member元素的rank（即index，从0开始），若没有member元素，返回“nil”
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(Object key, Object member) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().reverseRank(key, member);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset（元素已按score从大到小排序）中的index从start到end的所有元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public <T> Set<T> zrevrange(Object key, int start, int end) {
        Set<T> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = (Set<T>) templateClient.opsForZSet().reverseRange(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset中score >= min且score <= max的所有元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zrangeWithScores(String key, Double start, Double end) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = templateClient.opsForZSet().rangeByScoreWithScores(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset（元素已按score从大到小排序）中的index从start到end的所有元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zrevrangeWithScores(Object key, int start, int end) {
        Set<Object> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().reverseRangeByScore(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset的基数
     * @param key
     * @return
     */
    public Long zcard(Object key) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().zCard(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset中元素element的score
     * @param key
     * @param member
     */
    public Double zscore(Object key, Object member) {
        Double result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().score(key, member);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 排序
     */
/*	public List<String> sort(String key) {
		List<String> result = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = templateClient.sort(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    /**
     * 根据参数进行排序
     * @param key
     * @param sortingParameters
     * @return
     */
/*	public List<String> sort(String key, SortingParams sortingParameters) {
		List<String> result = null;
		RedisTemplate templateClient = jredisFactory.redisTemplate();
		if (templateClient == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = templateClient.sort(key, sortingParameters);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			disconnect();
		}
		return result;
	}*/

    /**
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(String key, double min, double max) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().count(key, min, max);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset中score >= min且score <= max的所有元素
     * @param key
     * @param min
     * @param max
     * @return
     */
    public <T> Set<T> zrangeByScore(Object key, double min, double max) {
        Set<T> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = (LinkedHashSet<T>) templateClient.opsForZSet().rangeByScore(key, min, max);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 删除名称为key的zset中score >= min且score <= max的所有元素
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Long zrevrangeByScore(Object key, double max, double min) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().removeRangeByScore(key, max, min);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset中score >= min且score <= max的所有元素
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<Object> zrangeByScore(Object key, double min, double max,
                                     Long offset, Long count) {
        Set<Object> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().rangeByScore(key, min, max, offset, count);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * 返回名称为key的zset（元素已按score从大到小排序）中的index从start到end的所有元素
     */
    public Set<Object> zrevrangeByScore(Object key, double max, double min,Long offset, Long count) {
        Set<Object> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().reverseRangeByScore(key, max, min, offset, count);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    public Set<ZSetOperations.TypedTuple<Object>> zrangeByScoreWithScores(Object key, double min, double max) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().rangeByScoreWithScores(key, min, max);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    public Set<ZSetOperations.TypedTuple<Object>> zrevrangeByScoreWithScores(Object key, double max,
                                                                             double min) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().reverseRangeByScoreWithScores(key, max, min);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    public Set<ZSetOperations.TypedTuple<Object>> zrangeByScoreWithScores(Object key, double min,double max, int offset, int count) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().reverseRangeByScoreWithScores(key, min, max,offset, count);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    public Set<ZSetOperations.TypedTuple<Object>> zrevrangeByScoreWithScores(String key, double max,double min, int offset, int count) {
        Set<ZSetOperations.TypedTuple<Object>> result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().reverseRangeByScoreWithScores(key, max, min,offset, count);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    public Long zremrangeByRank(Object key, Long start, Long end) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {

            result = templateClient.opsForZSet().removeRange(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    public Long zremrangeByScore(Object key, double start, double end) {
        Long result = null;
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        if (templateClient == null) {
            return result;
        }
        try {
            result = templateClient.opsForZSet().removeRangeByScore(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     *按顺序返回一组KEY的实体内容，只支持String数据类型的存储
     * @param keyList
     * @return
     */
    public List<Object> getStringDataBYPipeline(final  List<String> keyList){
        RedisTemplate templateClient = jredisFactory.redisTemplate();
        List returnlist = templateClient.executePipelined(new RedisCallback<Object>() {
            public Objects doInRedis(RedisConnection connection){
                StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
                for (int i=0;i<keyList.size();i++){
                    String redisKey = keyList.get(i);
                    stringRedisConn.get(redisKey);
                }
                return null;

            }

        });
        return returnlist;

    }


    /**
     *
     * @return
     */
    public RedisTemplate getRedisTemplate(){
        return jredisFactory.redisTemplate();
    }


}

class RandomUtil{

    public static Integer getRandomIntValue(int min,int max){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;

        return  s;
    }

    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            int r = getRandomIntValue(0,100);
            System.out.println(r);

        }

    }
}