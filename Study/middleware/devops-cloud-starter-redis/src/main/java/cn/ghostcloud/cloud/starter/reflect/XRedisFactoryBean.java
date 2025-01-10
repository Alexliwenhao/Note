package cn.ghostcloud.cloud.starter.reflect;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author liwenhao
 * @since 2022/12/26 上午10:44
 */
public class XRedisFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mapperInterface;

    public XRedisFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Autowired
    private Jedis jedis;

    @Override
    public T getObject() throws Exception {
        InvocationHandler handler = (proxy, method, args) -> {
            String name = method.getName();
            if ("set".equals(name)) {
               return jedis.srandmember(name);
            } else if ("get".equals(name)) {
                return jedis.sadd(args[0].toString(), args[1].toString());
            }
            return "你被代理了，执行Redis操作！";
        };
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface},  handler);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

}
