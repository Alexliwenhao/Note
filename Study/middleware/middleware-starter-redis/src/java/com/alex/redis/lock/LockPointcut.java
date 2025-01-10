package com.alex.redis.lock;

import org.redisson.api.RedissonClient;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.core.annotation.MergedAnnotations;

import java.lang.reflect.Method;

/**
 * @author liwenhao
 * @date 2023/5/17 14:44
 * getClassFilter()和getMethodMatcher()。
 *
 * 在getClassFilter()方法中，返回了一个ClassFilter对象，该对象的作用是过滤要被代理的类。在这里，类过滤器返回了一个lambda表达式，该表达式始终返回true，表示所有的类都可以被代理。
 *
 * 在getMethodMatcher()方法中，返回了一个MethodMatcher对象，该对象的作用是过滤要被代理的方法。在这里，方法过滤器返回了一个匿名内部类，该类实现了MethodMatcher接口中的三个方法：matches()、isRuntime()和matches()。
 *
 * matches()方法用于判断是否需要代理某个方法，这里始终返回false，表示不需要代理任何方法。
 *
 * isRuntime()方法用于告诉Spring AOP是否需要在运行时动态匹配方法。这里返回false，表示不需要运行时匹配方法。
 *
 * matches()方法用于在运行时动态匹配方法，这里也返回false，表示不需要动态匹配方法。
 */
public class LockPointcut implements Pointcut {
    @Override
    public ClassFilter getClassFilter() {
        return clazz -> true;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {
            @Override
            public boolean matches(Method method, Class<?> aClass) {
                return matches(method, aClass, new Object());
            }

            @Override
            public boolean isRuntime() {
                return false;
            }

            @Override
            public boolean matches(Method method, Class<?> aClass, Object... objects) {
                MergedAnnotations from = MergedAnnotations.from(aClass);
                if (!from.isPresent(Lock.class)) {
                    return false;
                }
                return true;
            }
        };
    }
}
