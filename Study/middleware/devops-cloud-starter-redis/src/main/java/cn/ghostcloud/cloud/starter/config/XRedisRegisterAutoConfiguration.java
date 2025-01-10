package cn.ghostcloud.cloud.starter.config;

import cn.ghostcloud.cloud.starter.annotation.XRedis;
import cn.ghostcloud.cloud.starter.reflect.XRedisFactoryBean;
import cn.ghostcloud.cloud.starter.utils.SimpleMetadataReader;
import lombok.SneakyThrows;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.beans.Introspector;
import java.util.Date;
import java.util.List;

/**
 * @author liwenhao
 * @since 2022/12/22 上午6:44
 */
@Configuration
@EnableConfigurationProperties(XRedisProperties.class)
public class XRedisRegisterAutoConfiguration implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(XRedisRegisterAutoConfiguration.class);


    @Autowired
    private XRedisProperties xRedisProperties;


    @Bean
    public Jedis jedis(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setTestOnBorrow(false);
        JedisPool jedisPool = new JedisPool(config, xRedisProperties.getHost(), xRedisProperties.getPort());
        return jedisPool.getResource();
    }


    private static class XRedisRegister implements BeanFactoryAware, ImportBeanDefinitionRegistrar {

        private BeanFactory beanFactory;

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }


        @SneakyThrows
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            if (!AutoConfigurationPackages.has(beanFactory)) {
                return;
            }
            List<String> strings = AutoConfigurationPackages.get(beanFactory);

            String s = StringUtils.collectionToCommaDelimitedString(strings);

            String basePackge = "classpath*:" + s.replace(".", "/") + "/**/*.class";
            PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = patternResolver.getResources(basePackge);
            for (Resource resource : resources) {
                MetadataReader simpleMetadataReader = new SimpleMetadataReader(resource, ClassUtils.getDefaultClassLoader());
                XRedis xRedis = Class.forName(simpleMetadataReader.getClassMetadata().getClassName()).getAnnotation(XRedis.class);
                if (xRedis == null) {
                    continue;
                }

                ScannedGenericBeanDefinition beanDefinition = new ScannedGenericBeanDefinition(simpleMetadataReader);
                String beanName = Introspector.decapitalize(ClassUtils.getShortName(beanDefinition.getBeanClassName()));
                beanDefinition.setScope("singleton");
                beanDefinition.setBeanClass(XRedisFactoryBean.class);
                beanDefinition.setResource(resource);
                beanDefinition.setSource(resource);

                BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
                registry.registerBeanDefinition(definitionHolder.getBeanName(), definitionHolder.getBeanDefinition());
            }


        }
    }



    @Configuration
    @Import(XRedisRegister.class)
    class MapperScan implements InitializingBean{

        @Override
        public void afterPropertiesSet() throws Exception {

        }
    }




    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
