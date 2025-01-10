# 1 SpringCloud概述
## 1.1 微服务中的相关概念
### 1.1.1 服务注册与发现
服务注册：服务实例将自身服务信息注册到注册中心。这部分服务信息包括服务所在主机IP和提供服务的Port，以及暴露服务自身状态以及访问协议等信息。
服务发现：服务实例请求注册中心获取所依赖服务信息。服务实例通过注册中心，获取到注册到其中的服务实例的信息，通过这些信息去请求它们提供的服务。
![](media/1648521197526-c09467c9-8d52-4aaa-8bb8-57b059e19c5e.png)
### 1.1.2 负载均衡
负载均衡（Load balancing）是一种在多个计算机（网络、CPU、磁盘）之间均匀分配资源，以提高资源利用的技术。使用负载均衡可以最大化服务吞吐量，可能最小化响应时间，同时由于使用负载均衡时，会使用多个服务器节点代单点服务，也提高了服务的可用性。
负载均衡的实现可以软件可以硬件，硬件如大名鼎鼎的 F5 负载均衡设备，软件如 NGINX 中的负载均衡实现，又如 Springcloud Ribbon 组件中的负载均衡实。
![](media/1648521198232-a0b0746b-544a-459f-828a-9298e13874a3.png)
### 1.1.3 熔断和降级
熔断这一概念来源于电子工程中的断路器（Circuit Breaker）。在互联网系统中，当下游服务因访问压力过大而响应变慢或失败，上游服务为了保护系统整体的可用性，可以暂时切断对下游服务的调用。这种牺牲局部，保全整体的措施就叫做熔断。
![](media/1648521201421-fe01ca7c-3002-4136-9856-d9ce0d21f1a9.png)
那么，什么是服务降级呢？
这里有两种场景：
当下游的服务因为某种原因响应过慢，下游服务主动停掉一些不太重要的业务，释放出服务器资源，增加响应速度。当下游的服务因为某种原因不可用，上游主动调用本地的一些降级逻辑，避免卡顿，迅速返回给用户。
其实应该要这么理解：
服务降级有很多种降级方式，如开关降级、限流降级、熔断降级。
服务熔断属于降级方式的一种。
### 1.1.4 链路追踪
随着微服务架构的流行，服务按照不同的维度进行拆分，一次请求往往需要涉及到多个服务。互联网应用构建在不同的软件模块集上，这些软件模块，有可能是由不同的团队开发、可能使用不同的编程语言来实现、有可能布在了几千台服务器，横跨多个不同的数据中心。因此，就需要对一次请求涉及的多个服务链路进行日志记录，性能监控即链路追踪。
![](media/1648521203062-72f35f6f-f89e-45d4-b673-f432074f9639.png)
### 1.1.5 API网关
随着微服务的不断增多，不同的微服务一般会有不同的网络地址，而外部客户端可能需要调用多个服务的接口才能完成一个业务需求，如果让客户端直接与各个微服务通信可能出现： 

- 客户端需要调用不同的url地址，增加难度 
- 再一定的场景下，存在跨域请求的问题 
- 每个微服务都需要进行单独的身份认证 

针对这些问题，API网关顺势而生。 API网关字面意思是将所有API调用统一接入到API网关层，由网关层统一接入和输出。一个网关的基本功能有：统一接入、安全防护、协议适配、流量管控、长短链接支持、容错能力。有了网关之后，各个 API服务提供团队可以专注于自己的的业务逻辑处理，而API网关更专注于安全、流量、路由等问题。
![](media/1648521204091-04862c43-8e38-4952-b352-2565e99f3575.png)
## 1.2 SpringCloud的架构
### 1.2.1 SpringCloud中的核心组件
Spring Cloud的本质是在 Spring Boot 的基础上，增加了一堆微服务相关的规范，并对应用上下文（Application Context）进行了功能增强。既然 Spring Cloud 是规范，那么就需要去实现，目前Spring Cloud 规范已有 Spring官方，Spring Cloud Netflix，Spring Cloud Alibaba等实现。通过组件化的方式，Spring Cloud将这些实现整合到一起构成全家桶式的微服务技术栈。

- Spring Cloud Netflix组件

![](media/1648521204972-8169d4f8-15f0-44e6-9cae-a476157846a6.png)

- Spring Cloud Alibaba组件

![](media/1648521205880-b977c098-620d-4ba4-ba80-cdf06b033b4b.png)

- Spring Cloud原生及其他组件

![](media/1648521207212-a9fc6200-63b2-443c-b33f-89265899e35d.png)
### 1.2.2 SpringCloud体系结构
![](media/1648521209091-1bf75578-9b75-4b5f-a7f6-2e7e4b0a115b.png)
从上图可以看出Spring Cloud各个组件相互配合，合作支持了一套完整的微服务架构。

- 注册中心负责服务的注册与发现，很好将各服务连接起来
- 断路器负责监控服务之间的调用情况，连续多次失败进行熔断保护。
- API网关负责转发所有对外的请求和服务
- 配置中心提供了统一的配置信息管理服务,可以实时的通知各个服务获取最新的配置信息
- 链路追踪技术可以将所有的请求数据记录下来，方便我们进行后续分析
- 各个组件又提供了功能完善的dashboard监控平台,可以方便的监控各组件的运行状况。
## 1.3 课后作业

1. 自学负载均衡相关算法，例举出常用的负载均衡算法有哪些，分别适用于什么样的场景。
2. 常用的服务限流算法有哪些，分别适用于什么样的场景。

作业以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 2 案例搭建
使用微服务架构的分布式系统,微服务之间通过网络通信。我们通过服务提供者与服务消费者来描述微服务间的调用关系。
服务提供者：服务的被调用方，提供调用接口的一方
服务消费者：服务的调用方，依赖于其他服务的一方
我们以电商系统中常见的用户下单为例，用户向订单微服务发起一个购买的请求。在进行保存订单之前需要调用商品微服务查询当前商品库存，单价等信息。在这种场景下，订单微服务就是一个服务消费者，商品微服务就是一个服务提供者。
![](media/1648521210232-f199154f-7023-4194-841b-119f26035b7f.png)
## 2.1 数据库建表
原则上不同的业务领域应该具有独立的数据库。

- 用户表

-- 设置sql_mode
**set** @@sql_mode **=** 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION'**;**
**CREATE** **TABLE** `tb_user` **(**
 `id` **int****(**11**)** **NOT** **NULL** AUTO_INCREMENT**,**
 `username` **varchar****(**40**)** **DEFAULT** **NULL** **COMMENT** '用户名'**,**
 `password` **varchar****(**40**)** **DEFAULT** **NULL** **COMMENT** '密码'**,**
 `age` **int****(**3**)** **DEFAULT** **NULL** **COMMENT** '年龄'**,**
 `balance` **decimal****(**10**,**2**)** **DEFAULT** **NULL** **COMMENT** '余额'**,**
 `address` **varchar****(**80**)** **DEFAULT** **NULL** **COMMENT** '地址'**,**
 `create_time` **TIMESTAMP** **DEFAULT** **CURRENT_TIMESTAMP** **COMMENT** '创建时间'**,**
 `update_time` **TIMESTAMP** **DEFAULT** **CURRENT_TIMESTAMP** **COMMENT** '修改时间'**,**
 **PRIMARY** **KEY** **(**`id`**)**
**)** ENGINE**=**InnoDB **DEFAULT** CHARSET**=**utf8**;**

- 商品表

**CREATE** **TABLE** `tb_product` **(**
 `id` **int****(**11**)** **NOT** **NULL** AUTO_INCREMENT**,**
 `product_name` **varchar****(**40**)** **DEFAULT** **NULL** **COMMENT** '名称'**,**
 `status` **int****(**2**)** **DEFAULT** **NULL** **COMMENT** '状态'**,**
 `price` **decimal****(**10**,**2**)** **DEFAULT** **NULL** **COMMENT** '单价'**,**
 `product_desc` **varchar****(**255**)** **DEFAULT** **NULL** **COMMENT** '描述'**,**
 `caption` **varchar****(**255**)** **DEFAULT** **NULL** **COMMENT** '标题'**,**
 `inventory` **int****(**11**)** **DEFAULT** **NULL** **COMMENT** '库存'**,**
 `create_time` **TIMESTAMP** **DEFAULT** **CURRENT_TIMESTAMP** **COMMENT** '创建时间'**,**
 `update_time` **TIMESTAMP** **DEFAULT** **CURRENT_TIMESTAMP** **COMMENT** '修改时间'**,**
 **PRIMARY** **KEY** **(**`id`**)**
**)** ENGINE**=**InnoDB AUTO_INCREMENT**=**2 **DEFAULT** CHARSET**=**utf8**;**

- 订单表

**CREATE** **TABLE** `tb_order` **(**
 `id` **int****(**11**)** **NOT** **NULL** AUTO_INCREMENT**,**
 `user_id` **int****(**11**)** **DEFAULT** **NULL** **COMMENT** '用户id'**,**
 `product_id` **int****(**11**)** **DEFAULT** **NULL** **COMMENT** '商品id'**,**
 `**number**` **int****(**11**)** **DEFAULT** **NULL** **COMMENT** '数量'**,**
 `price` **decimal****(**10**,**2**)** **DEFAULT** **NULL** **COMMENT** '单价'**,**
 `amount` **decimal****(**10**,**2**)** **DEFAULT** **NULL** **COMMENT** '总额'**,**
 `product_name` **varchar****(**40**)** **DEFAULT** **NULL** **COMMENT** '商品名'**,**
 `username` **varchar****(**40**)** **DEFAULT** **NULL** **COMMENT** '用户名'**,**
 `create_time` **TIMESTAMP** **DEFAULT** **CURRENT_TIMESTAMP** **COMMENT** '创建时间'**,**
 `update_time` **TIMESTAMP** **DEFAULT** **CURRENT_TIMESTAMP** **COMMENT** '修改时间'**,**
 **PRIMARY** **KEY** **(**`id`**)**
**)** ENGINE**=**InnoDB **DEFAULT** CHARSET**=**utf8**;**
## 2.2 搭建环境
### 2.2.1 Git的使用

- 从命令行创建一个新的仓库

touch README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin [http://git.ghostcloud.cn/anluming/mic-service.git](http://git.ghostcloud.cn/anluming/mic-service.git)
git push -u origin master

- 从命令行推送已经创建的仓库

git remote add origin [http://git.ghostcloud.cn/anluming/mic-service.git](http://git.ghostcloud.cn/anluming/mic-service.git)
git push -u origin master
### 2.2.2 搭建父工程mic-service
采用聚合工程的方式来搭建整体工程结构，先创建父工程mic-service，采用maven方式，参考pom文件如下：
_<?_**xml version****="1.0" ****encoding****="UTF-8"**_?>_<**project ****xmlns****="http://maven.apache.org/POM/4.0.0"
         ****xmlns:****xsi****="http://www.w3.org/2001/XMLSchema-instance"
         ****xsi****:schemaLocation****="http://maven.apache.org/POM/4.0.0 [http://maven.apache.org/xsd/maven-4.0.0.xsd"](http://maven.apache.org/xsd/maven-4.0.0.xsd%22)**>
    <**modelVersion**>4.0.0</**modelVersion**>

    <**groupId**>cn.ghostcloud</**groupId**>
    <**artifactId**>mic-service</**artifactId**>
    <**packaging**>pom</**packaging**>
    <**version**>1.0-SNAPSHOT</**version**>
    <**modules**>
        <**module**>product-service</**module**>
        <**module**>order_service</**module**>
        <**module**>eureka_server</**module**>
        <**module**>import_test</**module**>
    </**modules**>

    <**parent**>
        <**groupId**>org.springframework.boot</**groupId**>
        <**artifactId**>spring-boot-starter-parent</**artifactId**>
        <**version**>2.1.6.RELEASE</**version**>
    </**parent**>

    <**properties**>
        <**project.build.sourceEncoding**>UTF-8</**project.build.sourceEncoding**>
        <**project.reporting.outputEncoding**>UTF-8</**project.reporting.outputEncoding**>
        <**java.version**>1.8</**java.version**>
    </**properties**>

    <**dependencies**>
        <**dependency**>
            <**groupId**>org.springframework.boot</**groupId**>
            <**artifactId**>spring-boot-starter-web</**artifactId**>
        </**dependency**>
        <**dependency**>
            <**groupId**>org.springframework.boot</**groupId**>
            <**artifactId**>spring-boot-starter-logging</**artifactId**>
        </**dependency**>
        <**dependency**>
            <**groupId**>org.springframework.boot</**groupId**>
            <**artifactId**>spring-boot-starter-test</**artifactId**>
            <**scope**>test</**scope**>
        </**dependency**>
        <**dependency**>
            <**groupId**>org.projectlombok</**groupId**>
            <**artifactId**>lombok</**artifactId**>
            <**version**>1.18.4</**version**>
            <**scope**>provided</**scope**>
        </**dependency**>
    </**dependencies**>

    <**dependencyManagement**>
        <**dependencies**>
            <**dependency**>
                <**groupId**>org.springframework.cloud</**groupId**>
                <**artifactId**>spring-cloud-dependencies</**artifactId**>
                <**version**>Greenwich.RELEASE</**version**>
                <**type**>pom</**type**>
                <**scope**>import</**scope**>
            </**dependency**>
        </**dependencies**>
    </**dependencyManagement**>

    <**repositories**>
        <**repository**>
            <**id**>spring-milestones</**id**>
            <**name**>Spring Milestones</**name**>
            <**url**>https://repo.spring.io/milestone</**url**>
            <**snapshots**>
                <**enabled**>false</**enabled**>
            </**snapshots**>
        </**repository**>
    </**repositories**>

    <**build**>
        <**plugins**>
            <**plugin**>
                <**groupId**>org.springframework.boot</**groupId**>
                <**artifactId**>spring-boot-maven-plugin</**artifactId**>
            </**plugin**>
        </**plugins**>
    </**build**>
</**project**>
dependencies中引入的依赖会直接被子项目继承，dependencyManagement中的依赖子项目可以通过显示引用继承,此时子项目无需指定具体的依赖版本号,版本号由父工程统一管理。
### 2.2.3 构建商品微服务
#### 2.2.3.1 新建商品服务工程
在父工程mic-service下新建一个module，取名product-service，
![](media/1648521211266-3951bcee-3d98-4123-8bb2-1670a51508b9.png)
![](media/1648521212309-d1bdf2c7-f733-4126-b2e6-2ee1a7efb9dc.png)
商品微服务工程参考pom文件如下：
_<?_**xml version****="1.0" ****encoding****="UTF-8"**_?>_<**project ****xmlns****="http://maven.apache.org/POM/4.0.0"
         ****xmlns:****xsi****="http://www.w3.org/2001/XMLSchema-instance"
         ****xsi****:schemaLocation****="http://maven.apache.org/POM/4.0.0 [http://maven.apache.org/xsd/maven-4.0.0.xsd"](http://maven.apache.org/xsd/maven-4.0.0.xsd%22)**>
    <**parent**>
        <**artifactId**>mic-service</**artifactId**>
        <**groupId**>cn.ghostcloud</**groupId**>
        <**version**>1.0-SNAPSHOT</**version**>
    </**parent**>
    <**modelVersion**>4.0.0</**modelVersion**>

    <**artifactId**>product_service</**artifactId**>

    <**dependencies**>
        <**dependency**>
            <**groupId**>mysql</**groupId**>
            <**artifactId**>mysql-connector-java</**artifactId**>
            <**version**>8.0.13</**version**>
        </**dependency**>
        <**dependency**>
            <**groupId**>org.mybatis.spring.boot</**groupId**>
            <**artifactId**>mybatis-spring-boot-starter</**artifactId**>
            <**version**>2.0.0</**version**>
        </**dependency**>
    </**dependencies**>
    <**build**>
        <**resources**>
            <**resource**>
                <**directory**>${basedir}/src/main/java</**directory**>
                <**excludes**>
                    <**exclude**>**/*.java</**exclude**>
                    <**exclude**>**/.svn/*</**exclude**>
                </**excludes**>
            </**resource**>
            <**resource**>
                <**directory**>src/main/resources</**directory**>
                <**filtering**>true</**filtering**>
            </**resource**>

            <**resource**>
                <**directory**>src/test/resources</**directory**>
                <**filtering**>true</**filtering**>
            </**resource**>
        </**resources**>
    </**build**>
</**project**>

#### 2.2.3.2 编辑工程配置文件
在resources目录下新建application.yml文件：
**server**:
  **port**: 8081**spring**:
  **application**:
    **name**: product-service
  **datasource**:
    **driver-class-name**: com.mysql.jdbc.Driver
    **url**: jdbc:mysql://192.168.10.204:3306/mic_service_sku_db?useUnicode=true&characterEncoding=utf8
    **username**: root
    **password**: 123456
_#控制台打印SQL_**mybatis**:
  **configuration**:
    **map-underscore-to-camel-case**: **true
    log-impl**: org.apache.ibatis.logging.stdout.StdOutImpl**logging**:
  **level**:
    **cn.ghostcloud.**.dao**: trace _#调整日志级别_
#### 2.2.3.3 添加启动类
为product-service服务添加启动类ProductApplication,
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.**class**})@MapperScan(basePackages = **"cn.ghostcloud.**.dao"**)**public class **ProductApplication {
    **public static void **main(String[] args) {
        SpringApplication._run_(ProductApplication.**class**, args);
    }
}
@MapperScan注解用于指定SQL映射文件（xml文件）的扫描路径。
#### 2.2.3.4 编写商品实体
编写商品实体类cn.ghostcloud.product.entity.Product，字段与数据库字段名一一对应，
@Data**public class **Product {
    **private **Long **id**;
    **private **String **productName**;
    **private **Integer **status**;
    **private **BigDecimal **price**;
    **private **String **productDesc**;
    **private **String **caption**;
    **private **Integer **inventory**;
    **private **String **createTime**;
    **private **String **updateTime**;
}
使用lombok简化实体类的开发，lombok能以简单的注解形式来帮助自动生成getter、setter、toString等方法。
#### 2.2.3.5 编写dao接口
编写商品对象的数据库访问接口cn.ghostcloud.product.dao.ProductDao，采用mybatis作为ORM框架，需要在类的声明上添加注解@Mapper。
@Mapper**public interface **ProductDao {_    _**void **save(Product product);
    **void **update(Product product);_    _Product findById(@Param(**"id"**) Long id);_    _**void **deleteById(@Param(**"id"**) Long id);
}
编写对应的xml映射文件ProductDao.xml，放在在与dao接口相同目录下，
_<?_**xml version****="1.0" ****encoding****="UTF-8"**_?>_**<!DOCTYPE ****mapper ****PUBLIC ****"-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd"****_>_**<**mapper ****namespace****="cn.ghostcloud.product.dao.ProductDao"**>
    <**insert ****id****="save" ****parameterType****="cn.ghostcloud.product.entity.Product" ****useGeneratedKeys****="true"**>
        insert into tb_product(product_name,status,price,product_desc,caption,inventory)
        values(#{productName},#{status},#{price},#{productDesc},#{caption},#{inventory})
    </**insert**>

    <**select ****id****="findById" ****parameterType****="java.lang.Long" ****resultType****="cn.ghostcloud.product.entity.Product"**>
        SELECT
            id,
            product_name,
            status,
            price,
            product_desc,
            caption,
            inventory,
            create_time,
            update_time
        FROM
            tb_product
        WHERE
            id = #{id}
    </**select**>
</**mapper**>
#### 2.2.3.6 编写service接口
编写商品操作逻辑的service接口cn.ghostcloud.product.service.ProductService及实现类cn.ghostcloud.product.service.impl.ProductServiceImpl，
**public interface **ProductService {_    _**void **save(Product product);_    _**void **update(Product product);_    _**void **deleteById(Long id);_    _Product findById(Long id);
}

@Service**public class **ProductServiceImpl **implements **ProductService {

    @Resource
    **private **ProductDao **productDao**;
    **private final **Logger **logger **= LoggerFactory._getLogger_(ProductServiceImpl.**class**);

    @Override
    **public void **save(Product product) {
        **logger**.info(**"save product:" **+ product);
        **this**.**productDao**.save(product);
    }

    @Override
    **public void **update(Product product) {
        **logger**.info(**"update old product to product:" **+ product);
        **this**.**productDao**.update(product);
    }

    @Override
    **public void **deleteById(Long id) {
        **logger**.info(**"delete product by id:" **+ id);
        **this**.**productDao**.deleteById(id);
    }

    @Override
    **public **Product findById(Long id) {
        **logger**.info(**"find product by id:" **+ id);
        **return this**.**productDao**.findById(id);
    }
}

#### 2.2.3.7 编写controller接口
编写商品服务对外访问入口cn.ghostcloud.product.controller.ProductController。
@RestController
@RequestMapping(**"/product"**)**public class **ProductController {
    @Resource
    **private **ProductService **productService**;

    _/**
     * 根据ID查询商品
     *
     * _**_@param _****_id _**_*
     * _**_@return _**_商品
     */
    _@RequestMapping(value = **"/{id}"**, method = RequestMethod.**_GET_**)
    **public **ApiResult<Product> findById(@PathVariable Long id) {
        Product product = **this**.**productService**.findById(id);
        **return **ApiResult._success_(product);
    }

    _/**
     * 新增商品
     *
     * _**_@param _****_product _**_*
     * _**_@return _**_*
     */
    _@RequestMapping(value = **""**, method = RequestMethod.**_POST_**)
    **public **ApiResult save(@RequestBody Product product) {
        **this**.**productService**.save(product);
        **return **ApiResult._success_();
    }
}
#### 2.2.3.8 接口返回模板类
统一接口返回格式模板类ApiResult,
**public class **ApiResult<T> **implements **Serializable {
    **public static **ApiResult success() {
        **return new **ApiResult(**true**, **"200"**, **"success"**);
    }

    **public static **<T> ApiResult<T> success(T data) {
        **return new **ApiResult<>(**true**, **"200"**, **"success"**, data);
    }

    **public static **ApiResult fail(String message) {
        **return new **ApiResult(**false**, **"500"**, message);
    }

    **public **ApiResult() {}

    **public **ApiResult(Boolean success, String message) {
        **this**.**success **= success;
        **this**.**message **= message;
    }

    **public **ApiResult(Boolean success, String code, String message) {
        **this**.**success **= success;
        **this**.**code **= code;
        **this**.**message **= message;
    }

    **public **ApiResult(Boolean success, String code, String message, T data) {
        **this**.**success **= success;
        **this**.**code **= code;
        **this**.**message **= message;
        **this**.**data **= data;
    }

    _    _**private static final long ****_serialVersionUID _**= -2402122704294916086L;

    **private **Boolean **success **= Boolean.**_TRUE_**;
    **private **String **code**;
    **private **String **message**;
    **private **T **data**;

    **public **Boolean getSuccess() {
        **return ****success**;
    }

    **public void **setSuccess(Boolean success) {
        **this**.**success **= success;
    }

    **public **String getCode() {
        **return ****code**;
    }

    **public void **setCode(String code) {
        **this**.**code **= code;
    }

    **public **String getMessage() {
        **return ****message**;
    }

    **public void **setMessage(String message) {
        **this**.**message **= message;
    }

    **public **T getData() {
        **return ****data**;
    }

    **public void **setData(T data) {
        **this**.**data **= data;
    }
}
#### 2.2.3.9 服务访问测试
启动数据库，启动商品微服务，通过接口访问工具Postman进行接口访问测试，参考下图：
新增一条商品信息：
![](media/1648521213471-02613efd-7938-4904-a33f-e9271ed408f1.png)
查询商品信息：
![](media/1648521215127-4bd6c9b2-c8ad-4ad9-897b-348d8f4536a3.png)
### 2.2.4 开发其他微服务
采用与商品微服务类似的步骤依次完成用户微服务和订单微服务的开发。
## 2.3 服务间的调用
前文已经编写了三个基础的微服务，从业务的视角看，它们之间的关系如下图所示：
![](media/1648521216123-61038eaa-2f4d-4908-908a-045f042cec28.png)
在用户下单时需要调用商品微服务获取商品数据。那应该怎么做呢？商品微服务提供了供调用的HTTP接口。所以可以在下定单的时候使用http请求的相关工具类完成，如常见的HttpClient，OkHttp，当然也可以使用Spring提供的RestTemplate。
### 2.3.1 RestTemplate介绍
Spring框架提供的RestTemplate类可用于在应用中调用rest服务，它简化了与http服务的通信方式，统一了RESTful的标准，封装了http链接， 我们只需要传入url及返回值类型即可。相较于之前常用的HttpClient，RestTemplate是一种更优雅的调用RESTful服务的方式。
在Spring应用程序中访问第三方REST服务与使用Spring RestTemplate类有关。RestTemplate类的设计原则与许多其他Spring 模板类(例如JdbcTemplate、JmsTemplate)相同，为执行复杂任务提供了一种具有默认行为的简化方法。
RestTemplate默认依赖JDK提供http连接的能力（HttpURLConnection），如果有需要的话也可以通过setRequestFactory方法替换为例如 Apache HttpComponents、Netty或OkHttp等其它HTTP library。
考虑到RestTemplate类是为调用REST服务而设计的，因此它的主要方法与REST的基础紧密相连就不足为奇了，后者是HTTP协议的方法:HEAD、GET、POST、PUT、DELETE和OPTIONS。例如，RestTemplate类具有headForHeaders()、getForObject()、postForObject()、put()和delete()等方法。
### 2.3.2 通过RestTemplate调用微服务
#### 2.3.2.1 RestTemplate配置
在order-service中增加RestTemplate的配置类，代码如下：
@Configuration**public class **RestTemplateConfiguration {
    _/**
     * 配置RestTemplate交给Spring管理
     * _**_@return
     _**_*/
    _@Bean
    **public **RestTemplate getRestTemplate() {
        RestTemplate restTemplate = **new **RestTemplate();
        _//设置JSON数据转换器
        _restTemplate.getMessageConverters().add(**new **MappingJackson2HttpMessageConverter());
        _//使用OkHttp3Client作为http请求工具
        _OkHttp3ClientHttpRequestFactory requestFactory = **new **OkHttp3ClientHttpRequestFactory();
        _//设置连接超时时间,单位毫秒
        _requestFactory.setConnectTimeout(500000);
        _//设置读取超时时间，单位毫秒
        _requestFactory.setReadTimeout(500000);
        restTemplate.setRequestFactory(requestFactory);
        **return **restTemplate;
    }
}
#### 2.3.2.2 使用RestTemplate发起调用
在OrderController中使用RestTemplate对product-service发起调用，简单的模拟调用代码如下：
@Resource**private **RestTemplate **restTemplate**;
_/**
 * 硬编码方式
 * 测试从order服务调用商品服务
 *
 * _**_@param _****_pId _**_商品ID
 * _**_@return _**_*
 */_@RequestMapping(value = **"/buy/{pId}"**, method = RequestMethod.**_GET_**)**public **ApiResult<Product> testOrder(@PathVariable Long pId) **throws **URISyntaxException {
    URI uri = **new **URI(**"http://localhost:8081/product/" **+ pId);
    _//构建请求实体
    _RequestEntity<String> requestEntity = **new **RequestEntity<>(HttpMethod.**_GET_**, uri);
    _//因为product-service的接口返回的是ApiResult<Product>,所以需要使用exchange方法发起调用
    _ResponseEntity<ApiResult<Product>> responseEntity = **this**.**restTemplate**.exchange(
        requestEntity, **new **ParameterizedTypeReference<ApiResult<Product>>() {
        });
    **return **responseEntity.getBody();
}
#### 2.3.2.3 硬编码存在的问题
在上面的模拟调用代码中采用了硬编码的方式发起调用，即把服务提供者的网络地址（ip,端口）等硬编码到了代码中，这种做法存在以下问题：

- 应用场景有局限

加入有多个微服务需要调用，服务消费者必须要记录每一个服务提供者的具体地址，加大消费者代码的开发难度。
当有多个服务实例时，负载均衡实现麻烦。

- 无法动态调整

一旦服务提供者的地址发生变更，必须要修改消费者的代码。
而解决硬编码的办法就需要通过注册中心动态的实现服务注册和服务发现。
## 2.4 课后作业

1. 根据课程内容，动手完成以上三个服务的搭建实践，将实践过程中的关键步骤、输出结果、遇到的问题、解决问题的方法等进行记录，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
2. 课程示例工程中有多个类在不同的工程重复被使用，比如：ApiResult、Product、User等类，请自行思考如何解决该问题，避免重复在多个工程中多次开发相同的代码，并给出解决方法和过程，将方法和关键步骤进行记录，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 3 服务注册Eureka基础
注册中心可以说是微服务架构中的”通讯录“，它记录了服务和服务地址的映射关系。在分布式架构中，服务会注册到这里，当服务需要调用其它服务时，就这里找到服务的地址，进行调用。
![](media/1648521217302-722ab358-1bbd-4faa-928c-b15c311a0a36.png)
## 3.1 注册中心的主要作用
服务注册中心（下称注册中心）是微服务架构非常重要的一个组件，在微服务架构里主要起到了协调者的一个作用。注册中心一般包含如下几个功能：

1. 服务发现

服务注册/反注册：保存服务提供者和服务调用者的信息 
服务订阅/取消订阅：服务调用者订阅服务提供者的信息，最好有实时推送的功能 
服务路由（可选）：具有筛选整合服务提供者的能力。

1. 服务配置 

配置订阅：服务提供者和服务调用者订阅微服务相关的配置 
配置下发：主动将配置推送给服务提供者和服务调用者。

1. 服务健康检测 

检测服务提供者的健康情况
## 3.2 常见的注册中心

- Zookeeper

zookeeper它是一个分布式服务框架，是Apache Hadoop 的一个子项目，它主要是用来解决分布式应用中经常遇到的一些数据管理问题，如：统一命名服务、状态同步服务、集群管理、分布式应用配置项的管理等。简单来说zookeeper=文件系统+监听通知机制。

- Eureka

Eureka是在Java语言上，基于Restful Api开发的服务注册与发现组件，Springcloud Netflix中的重要组件。

- Consul

Consul是由HashiCorp基于Go语言开发的支持多数据中心分布式高可用的服务发布和注册服务软件， 采用Raft算法保证服务的一致性，且支持健康检查。

- Nacos

Nacos是一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。简单来说 Nacos 就是 注册中心 + 配置中心的组合，提供简单易用的特性集，帮助我们解决微服务开发必会涉及到的服务注册与发现，服务配置，服务管理等问题。Nacos 还是 Spring Cloud Alibaba 组件之一，负责服务注册与发现。
各种服务注册与发现组件的对比：
![](media/1648521220182-4eca671f-33b1-429c-b128-e21c75160c05.png)
## 3.3 Eureka概述
Eureka是Netflix开发的服务发现框架，SpringCloud将它集成在自己的子项目spring-cloud-netflix中，实现SpringCloud的服务发现功能。
![](media/1648521224415-4851045b-5cbd-4ec8-876d-f00d3c39b910.png)
Eureka Server：提供服务注册和发现，多个Eureka Server之间会同步数据，做到状态一致（最终一致性）。
Service Provider：服务提供方，将自身服务注册到Eureka，从而使服务消费方能够找到。
Service Consumer：服务消费方，从Eureka获取注册服务列表，从而能够消费服务。
注意，上图中的3个角色都是逻辑角色，在实际运行中，这几个角色甚至可以是同一个项目（JVM进程）中。
## 3.4 Eureka的交互流程与原理
![](media/1648521225556-6905bcea-e015-4dce-b352-5dad02b450f4.png)
上图中主要名称说明：
**Register**：EurekaClient注册（Http请求）到EurekaServer，EurekaClient会发送自己元数据(ip,port,主页等)，EurekaServer会将其添加到服务注册列表ConcurrentHashMap里。
**Renew**：EurekaClient默认每30秒发送心跳（timer定时任务，发送Http请求）到EurekaServer续约，向EurekaServer证明其活性，EurekaServer将EurekaClient心跳中的时间戳参数与已有服务列表中对应的该服务的时间戳进行比较，不相等就更新对应的服务列表；如果EurekaServer 90秒都没收到某个EurekaClient的续约，并且没有进入保护模式，就会将该服务从服务列表将其剔除（Eviction）。
**Get Registry**：EurekaClient默认每30秒从EurekaServer获取服务注册列表，并且会与自身本地已经缓存过的服务列表进行比较合并，有点像本地仓库从git仓库进行git pull。
**Cancel**：EurekaClient服务的下线。
**Make Remote Call**：EurekaClient服务间进行远程调用，比如通过RestTemplate+Ribbon或Fegin。
**Replicate**：EurekaServer集群节点之间数据（主要是服务注册列表信息）同步。
由上图可知，Eureka包含两个组件：Eureka Server 和 Eureka Client，它们的作用如下：

- Eureka Client是一个Java客户端，用于简化与Eureka Server的交互； 
- Eureka Server提供服务发现的能力，各个微服务启动时，会通过Eureka Client向Eureka Server 进行注册自己的信息（例如网络信息），Eureka Server会存储该服务的信息； 
- 微服务启动后，会周期性地向Eureka Server发送心跳（默认周期为30秒）以续约自己的信息。如果Eureka Server在一定时间内没有接收到某个微服务节点的心跳，Eureka Server将会注销该微服 务节点（默认90秒）； 
- 每个Eureka Server同时也是Eureka Client，多个Eureka Server之间通过复制的方式完成服务注 册表的同步； 
- Eureka Client会缓存Eureka Server中的信息。即使所有的Eureka Server节点都宕掉，服务消费 者依然可以使用缓存中的信息找到服务提供者。

综上，Eureka通过心跳检测、健康检查和客户端缓存等机制，提高了系统的灵活性、可伸缩性和可用性。
## 3.5 搭建Eureka注册中心
搭建eureka server的步骤：1、创建工程；2、导入坐标；3、配置application.yml；4、配置启动类。
### 3.5.1 创建工程
在mic-service工程下新建eureka-server的module。
### 3.5.2 导入坐标
<**dependencies**>
    <**dependency**>
        <**groupId**>org.springframework.cloud</**groupId**>
        <**artifactId**>spring-cloud-starter-netflix-eureka-server</**artifactId**>
    </**dependency**>
</**dependencies**>
### 3.5.3 配置application.yml
**server**:
  **port**: 8888**eureka**:
  **instance**:
    **hostname**: localhost
  **client**:
    **register-with-eureka**: **false **_# 是否将自己注册到eureka服务中
    _**fetch-registry**: **false **_#是否从eureka中获取注册信息
    _**service-url**: 
      **defaultZone**: http://${**eureka.instance.hostname**}:${**server.port**}/eureka/ 
### 3.5.4 配置启动类
@SpringBootApplication
@EnableEurekaServer**public class **EurekaServerApplication {
    **public static void **main(String[] args) {
        SpringApplication._run_(EurekaServerApplication.**class**, args);
    }
}
@EnableEurekaServer注解用于激活Eureka Server端配置。
### 3.5.5 注册中心管理后台
打开浏览器访问[http://localhost:8888](http://localhost:8888)即可进入EurekaServer内置的管理控制台，显示效果如下：
![](media/1648521227050-7054f9d1-cca9-4ec6-89ba-9311e5cb907d.png)
## 3.6 Eureka配置项说明
### 3.6.1 通用配置项
# 应用名称，将会显示在Eureka界面的应用名称列
spring.application.name=config-service
# 应用端口，Eureka服务端默认为：8761
server.port=3333
### 3.6.2 eureka.server前缀的配置项
# 是否允许开启自我保护模式，缺省：true
# 当Eureka服务器在短时间内丢失过多客户端时，自我保护模式可使服务端不再删除失去连接的客户端
eureka.server.enable-self-preservation = false
# Peer节点更新间隔，单位：毫秒
eureka.server.peer-eureka-nodes-update-interval-ms = 
# Eureka服务器清理无效节点的时间间隔，单位：毫秒，缺省：60000，即60秒
eureka.server.eviction-interval-timer-in-ms = 60000
### 3.6.3 eureka.instance前缀的配置项
# 服务名，默认取 spring.application.name 配置值，如果没有则为 unknown
eureka.instance.appname = eureka-client
# 实例ID
eureka.instance.instance-id = eureka-client-instance1
# 应用实例主机名
eureka.instance.hostname = localhost
# 客户端在注册时使用自己的IP而不是主机名，缺省：false
eureka.instance.prefer-ip-address = false
# 应用实例IP
eureka.instance.ip-address = 127.0.0.1
# 服务失效时间，失效的服务将被剔除。单位：秒，默认：90
eureka.instance.lease-expiration-duration-in-seconds = 90
# 服务续约（心跳）频率，单位：秒，缺省30
eureka.instance.lease-renewal-interval-in-seconds = 30
# 状态页面的URL，相对路径，默认使用 HTTP 访问，如需使用 HTTPS则要使用绝对路径配置，缺省：/info
eureka.instance.status-page-url-path = /info
# 健康检查页面的URL，相对路径，默认使用 HTTP 访问，如需使用 HTTPS则要使用绝对路径配置，缺省：/health
eureka.instance.health-check-url-path = /health
### 3.6.4 eureka.client前缀的配置项
# Eureka服务器的地址，类型为HashMap，缺省的Key为 defaultZone；缺省的Value为 http://localhost:8761/eureka
# 如果服务注册中心为高可用集群时，多个注册中心地址以逗号分隔。 
eureka.client.service-url.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka
# 是否向注册中心注册自己，缺省：true
# 一般情况下，Eureka服务端是不需要再注册自己的
eureka.client.register-with-eureka = true
# 是否从Eureka获取注册信息，缺省：true
# 一般情况下，Eureka服务端是不需要的
eureka.client.fetch-registry = true
# 客户端拉取服务注册信息间隔，单位：秒，缺省：30
eureka.client.registry-fetch-interval-seconds = 30
# 是否启用客户端健康检查
eureka.client.health-check.enabled = true
# 
eureka.client.eureka-service-url-poll-interval-seconds = 60
# 连接Eureka服务器的超时时间，单位：秒，缺省：5
eureka.client.eureka-server-connect-timeout-seconds = 5
# 从Eureka服务器读取信息的超时时间，单位：秒，缺省：8
eureka.client.eureka-server-read-timeout-seconds = 8
# 获取实例时是否只保留状态为 UP 的实例，缺省：true
eureka.client.filter-only-up-instances = true
# Eureka服务端连接空闲时的关闭时间，单位：秒，缺省：30
eureka.client.eureka-connection-idle-timeout-seconds = 30
# 从Eureka客户端到所有Eureka服务端的连接总数，缺省：200
eureka.client.eureka-server-total-connections = 200
# 从Eureka客户端到每个Eureka服务主机的连接总数，缺省：50
eureka.client.eureka-server-total-connections-per-host = 50

### 3.6.5 配置项说明
Eureka中有一些配置项，通过查看网上的资料可以大概知道其含义，然而如果没有一个直观的指导，并不能清晰地认识他们实际的作用效果。下面通过截图对部分配置项加以解释说明。

- spring.application.name和eureka.instance.appname

同时配置时，eureka.instance.appname优先级更高。
如果没有配置eureka.instance.appname，则使用spring.application.name的值，如果连spring.application.name都没有配置，则取unknown。
该配置项对应Eureka界面下图红框的内容：
![](media/1648521228240-63f4051e-f4c0-47a0-85fe-9a56f3f73766.png)

- eureka.instance.instance-id

配置项eureka.instance.instance-id的值决定了下图右侧红框中的显示内容：
![](media/1648521229518-de87554d-d1ff-4122-80c2-9914c19eda2c.png)
如果没有设置eureka.instance.instance-id，那么显示的值将是一个由Eureka自动判断生成的编号：
![](media/1648521231070-e130a42e-f2f3-429c-936e-7c8df5249d7e.png)

- eureka.instance.prefer-ip-address、eureka.instance.hostname、eureka.instance.ip-address

在eureka.instance.prefer-ip-address = true时，实例优先使用eureka.instance.ip-address的值进行注册，如果没有配置eureka.instance.ip-address，则使用第一个非回环IP地址进行注册。
此时，我们打开Eureka的界面，在实例上右键，复制链接地址；或将鼠标放在下图右上方的链接上（不点击），就可以获取实例的地址，如下图左下角，可见此时实例注册的是IP：
![](media/1648521232480-3e5fb78a-3c27-4238-8dd9-e9ae5d19cd22.png)
而当eureka.instance.prefer-ip-address = false时，同样的方式可以查看实例注册地址采用了主机名eureka.instance.hostname的值：
![](media/1648521234485-8bcd17ae-382d-43b8-9d62-6c379542f5e3.png)
### 3.6.6 配置Bean源码
对应于本文出现的配置项，Eureka中定义的源码类如下。

- eureka.server前缀的配置项

org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean

- eureka.instance前缀的配置项

org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean

- eureka.client前缀的配置项

org.springframework.cloud.netflix.eureka.EurekaClientConfigBean
## 3.7 服务注册到Eureka注册中心
### 3.7.1 商品服务注册

1. 在商品模块中引入坐标

<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-commons</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-eureka-client</**artifactId**>
</**dependency**>

1. 配置application.yml文件

在商品工程的application.yml中添加eureka server的主机地址。
**eureka**:
  **client**:
    **service-url**:
      **defaultZone**: http://localhost:8888/eureka/
  **instance**:
    **prefer-ip-address**: **true  **_#使用ip注册_

1. 修改启动类添加服务注册注解

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.**class**})@EnableEurekaClient
@MapperScan(basePackages = **"cn.ghostcloud.**.dao"**)**public class **ProductApplication {
    **public static void **main(String[] args) {
        SpringApplication._run_(ProductApplication.**class**, args);
    }
}
从Spring Cloud Edgware版本开始， @EnableDiscoveryClient 或 @EnableEurekaClient 可省略。只需加上相关依赖，并进行相应配置，即可将微服务注册到服务发现组件上。

1. 在管理后台查看效果

![](media/1648521235893-5f63b3ab-5df6-43a3-9026-79a87d9bca12.png)
### 3.7.2 订单服务注册
和商品微服务一样,只需要引入坐标依赖,在工程的 application.yml 中添加Eureka Server的主机地址即可。
### 3.7.3 用户服务注册
和商品微服务一样,只需要引入坐标依赖,在工程的 application.yml 中添加Eureka Server的主机地址即可。
## 3.8 Eureka中的自我保护
微服务第一次注册成功之后，每30秒会发送一次心跳将服务的实例信息注册到注册中心。通知 Eureka Server 该实例仍然存在。如果超过90秒没有发送更新，则服务器将从注册信息中将此服务移除。
Eureka Server在运行期间，会统计心跳失败的比例在15分钟之内是否低于85%，如果出现低于的情况（在单机调试的时候很容易满足，实际在生产环境上通常是由于网络不稳定导致），Eureka Server会将当前的实例注册信息保护起来，同时提示这个警告。保护模式主要用于一组客户端和Eureka Server之间存在网络分区场景下的保护。一旦进入保护模式，Eureka Server将会尝试保护其服务注册表中的信息，不再删除服务注册表中的数据（也就是不会注销任何微服务）验证完自我保护机制开启后，并不会马上呈现到web上，而是默认需等待 5 分钟（可以通过eureka.server.wait-time-in-ms-when-sync-empty 配置），即 5 分钟后会看到下面的提示信息：
![](media/1648521237589-2ea336af-4b0f-42fb-bb1f-261cb201884e.png)
通过设置 eureka.enableSelfPreservation=false 可以关闭自我保护功能。
## 3.9 Eureka中的元数据
Eureka的元数据有两种：标准元数据和自定义元数据。
**标准元数据**：主机名、IP地址、端口号、状态页和健康检查等信息，这些信息都会被发布在服务注册表中，用于服务之间的调用。 
**自定义元数据**：可以使用eureka.instance.metadata-map配置，符合KEY/VALUE的存储格式。这些元数据可以在远程客户端中访问。

- **从元数据获取服务信息发起服务调用**

@Resource**private **DiscoveryClient **discoveryClient**;
@Resource**private **RestTemplate **restTemplate**;
_/**
 * 从注册中心获取商品服务信息
 * 再发起调用
 *
 * _**_@param _****_pid
 _**_* _**_@return
 _**_*/_@RequestMapping(value = **"/buy/model2/{pid}"**, method = RequestMethod.**_GET_**)**public **ApiResult<Product> testOrderByModel2(@PathVariable Long pid) **throws **URISyntaxException {
    List<ServiceInstance> instances = **this**.**discoveryClient**.getInstances(**"product-service"**);
    ServiceInstance instance = instances.get(0);
_//如果使用了ribbon做负载均衡，则应该使用instance.getServiceId()_
    String url = instance.getHost() + **":" **+ instance.getPort() + **"/product/" **+ pid;
    _//构建请求实体
    _RequestEntity<String> requestEntity = **new **RequestEntity(HttpMethod.**_GET_**, **new **URI(url));
    _//因为product-service的接口返回的是ApiResult<Product>,所以需要使用exchange方法发起调用
    _ResponseEntity<ApiResult<Product>> responseEntity = **this**.**restTemplate**.exchange(
        requestEntity, **new **ParameterizedTypeReference<ApiResult<Product>>() {
        });
    **return **responseEntity.getBody();
}
**注意DiscoveryClient的完整限定名为org.springframework.cloud.client.discovery.DiscoveryClient。**
## 3.10 课后作业

1. 根据课堂内容动手搭建Eureka注册中心，并完成商品、订单、用户等微服务的注册和调用。对关键步骤、遇到的问题、解决方法进行记录，并以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
2. 采用多实例形式部署商品服务，自行实现一套负载均衡机制向商品服务发起调用。对关键步骤、遇到的问题、解决方法进行记录，并以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 4 服务注册Eureka进阶
## 4.1 Eureka Server高可用集群
在上一个章节，实现了单节点的Eureka Server的服务注册与服务发现功能。Eureka Client会定时连接 Eureka Server，获取注册表中的信息并缓存到本地。微服务在消费远程API时总是使用本地缓存中的数据。因此一般来说，即使Eureka Server发生宕机，也不会影响到服务之间的调用。但如果Eureka Server宕机时，某些微服务也出现了不可用的情况，Eureka Server中的缓存若不被刷新，就可能会影响到微服务的调用，甚至影响到整个应用系统的高可用。因此，在生成环境中，通常会部署一个高可用的Eureka Server集群。
Eureka Server可以通过运行多个实例并相互注册的方式实现高可用部署，Eureka Server实例会彼此增量地同步信息，从而确保所有节点数据一致。事实上，节点之间相互注册是Eureka Server的默认行为。
![](media/1648521238376-09976da1-4589-4f92-8477-5d67b7767479.png)
### 4.1.1 搭建Eureka Server高可用集群
使用单机模拟多主机，修改eureka-server工程中的yml配置文件，如下：
_#集群化运行eureka_---_#eureka1的配置_**spring**:
  **profiles**: eureka1
  **application**:
    **name**: eureka-server**server**:
  **port**: 8888**eureka**:
  **client**:
    **register-with-eureka**: **true **_# 是否将自己注册到eureka服务中,单节点运行时为false,多节点运行时为true
    _**fetch-registry**: **true **_#是否从eureka中获取注册信息,单节点运行时为false,多节点运行时为true
    _**service-url**:
      **defaultZone**: [http://127.0.0.1:8889/eureka/](http://127.0.0.1:8889/eureka/)

---_#eureka2的配置_**spring**:
  **profiles**: eureka2
  **application**:
    **name**: eureka-server**server**:
  **port**: 8889**eureka**:
  **client**:
    **register-with-eureka**: **true **_# 是否将自己注册到eureka服务中,单节点运行时为false,多节点运行时为true
    _**fetch-registry**: **true **_#是否从eureka中获取注册信息,单节点运行时为false,多节点运行时为true
    _**service-url**:
      **defaultZone**: [http://127.0.0.1:8888/eureka/](http://127.0.0.1:8888/eureka/)
**_在配置文件中通过连字符（---）将文件分为三个部分，第一部分为应用名称，第二部分和第三部分是根据不同的profiles选项动态添加，可以在IDEA启动时进行激活配置。_**![](media/1648521240558-8f9cc18e-95ba-4897-a04d-9892aefd0924.png)
![](media/1648521241564-3bb56241-b4dc-498b-96d9-0235120706ee.png)
在IDEA中一次启动eureka1和eureka2，自动激活配置eureka1和配置eureka2。访问[http://localhost:8888](http://localhost:8888)和[http://localhost:8889](http://localhost:8889) 可以看到eureka-server有两个节点，并且registered-replicas（相邻集群复制节点）中已经包含对方。
![](media/1648521242848-6747a3a6-7940-47b6-b33e-c29e218b5d8c.png)
### 4.1.2 服务注册到Eureka Server集群
如果需要将微服务注册到Eureka Server集群只需修改yml配置文件，如下所示：
**eureka**:
  **client**:
    **service-url**: _#多个注册中心地址使用逗号分割
      _**defaultZone**: http://localhost:8888/eureka/,http://localhost:8889/eureka/
  **instance**:
    **prefer-ip-address**: **true  **_#使用ip注册_

## 4.2 Eureka中的常见问题
### 4.2.1 服务注册慢
默认情况下，服务注册到Eureka Server的过程较慢。SpringCloud官方文档中给出了详细的原因:
1.10 Why Is It so Slow to Register a Service?
Being an instance also involves a periodic heartbeat to the registry (through the client’s serviceUrl) with a default duration of 30 seconds. A service is not available for discovery by clients until the instance, the server, and the client all have the same metadata in their local cache (so it could take 3 heartbeats). You can change the period by setting eureka.instance.leaseRenewalIntervalInSeconds. Setting it to a value of less than 30 speeds up the process of getting clients connected to other services. In production, it is probably better to stick with the default, because of internal computations in the server that make assumptions about the lease renewal period.
服务的注册涉及到心跳，默认心跳间隔为30s。在实例、服务器、客户端都在本地缓存中具有相同的元数据之前，服务不可用于客户端发现（所以可能需要3次心跳）。可以通过配置 eureka.instance.leaseRenewalIntervalInSeconds (心跳频率)加快客户端连接到其他服务的过程。在生产中，最好坚持使用默认值，因为在服务器内部有一些计算，他们对续约做出假设。
### 4.2.2 服务节点剔除问题
默认情况下，由于Eureka Server剔除失效服务间隔时间为90s且存在自我保护的机制。所以不能有效而 迅速的剔除失效节点，这对开发或测试会造成困扰。解决方案如下：
**Eureka Server:**
配置关闭自我保护，设置剔除无效节点的时间间隔。
_#eureka1的配置_**spring**:
  **profiles**: eureka1
  **application**:
    **name**: eureka-server**server**:
  **port**: 8888**eureka**:
  **client**:
    **register-with-eureka**: **true **_# 是否将自己注册到eureka服务中,单节点运行时为false,多节点运行时为true
    _**fetch-registry**: **true **_#是否从eureka中获取注册信息,单节点运行时为false,多节点运行时为true
    _**service-url**:
      **defaultZone**: [http://127.0.0.1:8889/eureka/](http://127.0.0.1:8889/eureka/)
  **server**:
    **enable-self-preservation**: **false **_#关闭自我保护
    _**eviction-interval-timer-in-ms**: 5000 _#剔除时间间隔，单位：毫秒_
**Eureka Client：**
配置开启健康检查，并设置续约时间。
**eureka**:
  **client**:
    **healthcheck**: true _#开启健康检查（依赖spring-boot-starter-actuator）
    _**service-url**: _#多个注册中心地址使用逗号分割
      _**defaultZone**: http://localhost:8888/eureka/,http://localhost:8889/eureka/
  **instance**:
    **prefer-ip-address**: **true  **_#使用ip注册
    _**lease-expiration-duration-in-seconds**: 10 _#eureka client发送心跳给server端后，续约到期时间(默认90秒)
    _**lease-renewal-interval-in-seconds**: 5 _#发送心跳续约间隔_
eureka.client.healthcheck = true , 使用health端点来代替心跳表明服务是否可用，反应到eureka server ui上服务的UP还是DOWN。这个配置属性在IDEA里面不会自动提示。
### 4.2.3 监控页面显示ip
在Eureka Server的管控台中，显示的服务实例名称默认情况下是微服务定义的名称和端口。为了更好的对所有服务进行定位，微服务注册到Eureka Server的时候可以手动配置示例ID。配置方式如下：
**eureka**:
  **client**:
    **healthcheck**: true _#开启健康检查（依赖spring-boot-starter-actuator）
    _**service-url**: _#多个注册中心地址使用逗号分割
      _**defaultZone**: http://localhost:8888/eureka/,http://localhost:8889/eureka/
  **instance**:
    **prefer-ip-address**: **true  **_#使用ip注册
    _**instance-id**: ${**spring.cloud.client.ip-address**}:${**server.port**}
${spring.cloud.client.ip-address} 获取ip地址。
![](media/1648521243974-619f73fa-af6e-4040-8d3a-e00efafa67a3.png)
## 4.3 课后作业
动手完成Eureka Server高可用集群部署和使用，记录过程步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 5 负载均衡Ribbon
经过前面章节的学习，已经实现了服务的注册和服务发现。当启动某个服务的时候，可以通过HTTP的形式将信息注册到注册中心，并且可以通过SpringCloud提供的工具获取注册中心的服务列表。但是服务之间的调用还存在很多的问题，如何更加方便的调用微服务，多个微服务的提供者如何选择，如何负载均衡等。
## 5.1 Ribbon概述
### 5.1.1 什么是Ribbon
Ribbon是 Netflix 发布的一个负载均衡器，有助于控制 HTTP 和 TCP客户端行为。在 SpringCloud 中， Eureka一般配合Ribbon进行使用，Ribbon提供了客户端负载均衡的功能，Ribbon利用从Eureka中读取到的服务信息，在调用服务节点提供的服务时，会合理的进行负载。
在SpringCloud中可以将注册中心和Ribbon配合使用，Ribbon自动的从注册中心中获取服务提供者的 列表信息，并基于内置的负载均衡算法，请求服务。
### 5.1.2 Ribbon的主要作用

1. 服务调用

基于Ribbon实现服务调用，是通过拉取到的所有服务列表组成（服务名-请求路径的）映射关系。借助 RestTemplate 最终进行调用。

1. 负载均衡

当有多个服务提供者时，Ribbon可以根据负载均衡的算法自动的选择需要调用的服务地址。
## 5.2 基于Ribbon实现订单调用商品服务
### 5.2.1 坐标依赖
在Springcloud提供的服务发现的jar中已经包含了Ribbon的依赖。所以这里不需要导入任何额外的坐标。
![](media/1648521244432-9fab0f22-db15-4b60-829c-b2311c0da05b.png)
### 5.2.2 工程改造

1. 服务提供者

修改product-service模块中ProductController#findById方法：
@Value(**"${server.port}"**)**private **String **port**;@Value(**"${spring.cloud.client.ip-address}"**)**private **String **ip**;
_/**
 * 根据ID查询商品
 *
 * _**_@param _****_id _**_*
 * _**_@return _**_商品
 */_@RequestMapping(value = **"/{id}"**, method = RequestMethod.**_GET_**)**public **ApiResult<Product> findById(@PathVariable Long id) {
    Product product = **this**.**productService**.findById(id);
    product.setProductDesc(**"调用商品服务ip:" **+ **ip **+ **"端口:" **+ **port**);
    **return **ApiResult._success_(product);
}

1. 服务消费者

修改order-service中RestTemplateConfiguration，在创建RestTemplate方法上添加@LoadBalanced注解：
@LoadBalanced
@Bean**public **RestTemplate getRestTemplate() {
    RestTemplate restTemplate = **new **RestTemplate();
    _//设置JSON数据转换器
    _restTemplate.getMessageConverters().add(**new **MappingJackson2HttpMessageConverter());
    _//使用OkHttp3Client作为http请求工具
    _OkHttp3ClientHttpRequestFactory requestFactory = **new **OkHttp3ClientHttpRequestFactory();
    _//设置连接超时时间,单位毫秒
    _requestFactory.setConnectTimeout(500000);
    _//设置读取超时时间，单位毫秒
    _requestFactory.setReadTimeout(500000);
    restTemplate.setRequestFactory(requestFactory);
    **return **restTemplate;
}
再在OrderController中新增一个下单方法，并使用RestTemplate完成服务调用。
_/**
 * 使用Ribbon实现调用
 *
 * _**_@param _****_pid
 _**_* _**_@return
 _**_*/_@GetMapping(**"/buy/model3/{pid}"**)**public **ApiResult<Product> testOrderByModel3(@PathVariable Long pid) **throws **URISyntaxException {
    String url = **"http://product-service/product/" **+ pid;
    _//构建请求实体
    _RequestEntity<String> requestEntity = **new **RequestEntity(HttpMethod.**_GET_**, **new **URI(url));
    _//因为product-service的接口返回的是ApiResult<Product>,所以需要使用exchange方法发起调用
    _ResponseEntity<ApiResult<Product>> responseEntity = **this**.**restTemplate**.exchange(
        requestEntity, **new **ParameterizedTypeReference<ApiResult<Product>>() {
        });
    **return **responseEntity.getBody();
}
### 5.2.3 代码测试
在浏览器中请求[http://localhost:8083/order/buy/model3/1](http://localhost:8083/order/buy/model3/1)查看展示效果如下：
![](media/1648521244876-084c9081-87ca-4db6-87de-a572245c3c2d.png)
## 5.3 负载均衡概述
在搭建网站时，如果单节点的 web服务性能和可靠性都无法达到要求；或者是在使用外网服务时，经常担心被人攻破，一不小心就会有打开外网端口的情况，通常这个时候加入负载均衡就能有效解决服务问题。
负载均衡是一种基础的网络服务，其原理是通过运行在前面的负载均衡服务，按照指定的负载均衡算法，将流量分配到后端服务集群上，从而为系统提供并行扩展的能力。
负载均衡的应用场景包括流量包、转发规则以及后端服务，由于该服务有内外网个例、健康检查等功能，能够有效提供系统的安全性和可用性。
![](media/1648521245807-38158168-1733-4ebc-ad82-e7316b78e295.png)

- 服务端负载均衡

先发送请求到负载均衡服务器或者软件，然后通过负载均衡算法，在多个服务器之间选择一个进行访 问；即在服务器端再进行负载均衡算法分配。

- 客户端负载均衡

客户端会有一个服务器地址列表，在发送请求前通过负载均衡算法选择一个服务器，然后进行访问，这是客户端负载均衡；即在客户端就进行负载均衡算法分配。
## 5.4 多实例负载均衡测试
修改product-service的application.yml配置文件，添加多个profile配置：
---_#product实例1_**server**:
  **port**: 8081**spring**:
  **profiles**: product1
---_#product实例2_**server**:
  **port**: 8085**spring**:
  **profiles**: product2
修改IDEA启动的启动配置Active profiles，分别绑定product1和product2，然后启动两个product服务实例，如下图所示：
![](media/1648521246790-e58fa6e8-7065-4a05-b022-05fb657cfb94.png)
通过浏览器多次访问[http://localhost:8083/order/buy/model3/1](http://localhost:8083/order/buy/model3/1)可看到负载均衡效果：
![](media/1648521247522-8097828a-e3bd-4435-a8f4-0bf60d891432.png)
![](media/1648521247985-2651d7f0-cfca-4897-9cb3-85eceeba74f6.png)
## 5.5 负载均衡策略
Ribbon内置了多种负载均衡策略，内部负责复杂均衡的顶级接口为 com.netflix.loadbalancer.IRule ，实现方式如下：
![](media/1648521249111-5d015df1-2540-4dfe-a49d-0f6a7c1316ad.png)
com.netflix.loadbalancer.RoundRobinRule ：以轮询的方式进行负载均衡。 
com.netflix.loadbalancer.RandomRule ：随机策略 
com.netflix.loadbalancer.RetryRule ：重试策略。
com.netflix.loadbalancer.WeightedResponseTimeRule ：权重策略。会计算每个服务的权重，越高的被调用的可能性越大。
com.netflix.loadbalancer.BestAvailableRule ：最佳策略。遍历所有的服务实例，过滤掉故障实例，并返回请求数最小的实例返回。
com.netflix.loadbalancer.AvailabilityFilteringRule ：可用过滤策略。过滤掉故障和请求数超过阈值的服务实例，再从剩下的实力中轮询调用。
如果需要修改负载均衡策略，可以在消费者服务的application.yml配置文件中添加配置：
_#修改默认的负载均衡策略_**product-service**: _#需要调用的微服务名称
  _**ribbon**:
    **NFLoadBalancerRuleClassName**: com.netflix.loadbalancer.RandomRule
**策略选择：**
1、如果每个机器配置一样，则建议不修改策略 (推荐) 
2、如果部分机器配置强，则可以改为 WeightedResponseTimeRule
## 5.6 Ribbon其他可用配置
#服务名
eureka-provider:
  ribbon:
    ConnectTimeout: 250 #单位ms,请求连接超时时间
    ReadTimeout: 1000 #单位ms,请求处理的超时时间
    OkToRetryOnAllOperations: true #对所有操作请求都进行重试
    MaxAutoRetriesNextServer: 2 #切换实例的重试次数
    MaxAutoRetries: 1 #对当前实例的重试次数
## 5.7 课后作业
根据课程内容，动手实践完成ribbon的使用案例和多实例负载均衡使用案例，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 6 服务调用Feign
前面的章节中我们使用RestTemplate实现服务间的调用，代码大致如下：
@GetMapping(**"/buy/model3/{pid}"**)**public **ApiResult<Product> testOrderByModel3(@PathVariable Long pid) **throws **URISyntaxException {
    String url = **"http://product-service/product/" **+ pid;
    _//构建请求实体
    _RequestEntity<String> requestEntity = **new **RequestEntity(HttpMethod.**_GET_**, **new **URI(url));
    _//因为product-service的接口返回的是ApiResult<Product>,所以需要使用exchange方法发起调用
    _ResponseEntity<ApiResult<Product>> responseEntity = **this**.**restTemplate**.exchange(
        requestEntity, **new **ParameterizedTypeReference<ApiResult<Product>>() {
        });
    **return **responseEntity.getBody();
}
由代码可知，我们是使用拼接字符串的方式构造URL的，该URL只有一个参数。但是，在现实中，URL 中往往含有多个参数。这时候我们如果还用这种方式构造URL，那么就会非常麻烦。那应该如何解决？我们带着这样的问题进入到本章的学习。
## 6.1 Feign简介
Feign是Netflix开发的声明式，模板化的HTTP客户端，其灵感来自Retrofit,JAXRS-2.0以及WebSocket。

- Feign可帮助我们更加便捷，优雅的调用HTTP API。 
- 在SpringCloud中，使用Feign非常简单——创建一个接口，并在接口上添加一些注解，代码就完成了。 
- Feign支持多种注解，例如Feign自带的注解或者JAX-RS注解等。
- SpringCloud对Feign进行了增强，使Feign支持了SpringMVC注解，并整合了Ribbon和Eureka，从而让Feign的使用更加方便。
## 6.2 基于Feign的服务调用

1. **引入依赖**

在消费者服务order-service中添加Feign依赖。
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-openfeign</**artifactId**>
</**dependency**>

1. **在启动类上添加Feign支持**

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan(**"cn.ghostcloud.**.dao"**)**public class **OrderApplication {
    **public static void **main(String[] args) {
        SpringApplication._run_(OrderApplication.**class**, args);
    }
}
通过@EnableFeignClients注解开启Spring Cloud Feign的支持功能。

1. **创建Feign调用接口**

创建一个Feign接口，此接口是在Feign中调用微服务的核心接口，在消费者服务order-service中添加一个商品服务调用的Feign接口ProductFeignClient，代码如下：
@FeignClient(name=**"product-service"**)**public interface **ProductFeignClient {
    _/**
     * 通过id查询商品
     *
     * _**_@param _****_id
     _**_* _**_@return
     _**_*/
    _@RequestMapping(value = **"/product/{id}"**, method = RequestMethod.**_GET_**)
    **public **ApiResult<Product> findById(@PathVariable(**"id"**) Long id);
}

- 定义各参数绑定时，@PathVariable、@RequestParam、@RequestHeader等可以指定参数属性，在Feign中绑定参数必须通过value属性来指明具体的参数名，不然会抛出异常。 
- @FeignClient：注解通过name指定需要调用的微服务的名称，用于创建Ribbon的负载均衡器。所以Ribbon会把 product-service 解析为注册中心的服务。同时name属性也是指定该FeignClient的名称，可以通过该名称在yml配置文件中修改feign的配置。
1. **调用*FeignClient接口**

在OrderController中新增一个方法，引入ProductFeignClient完成对商品服务的调用。
@Resource**private **ProductFeignClient **productFeignClient**;
_/**
 * 通过Feign接口调用商品服务
 *
 * _**_@param _****_id _**_商品id
 * _**_@return _**_*
 */_@RequestMapping(value = **"/buy/model4/{id}"**, method = RequestMethod.**_GET_**)**public **ApiResult<Product> testOrderByModel4(@PathVariable(**"id"**) Long id) {
    **return this**.**productFeignClient**.findById(id);
}

1. **访问测试**

![](media/1648521250200-260fe355-71b4-431c-b06e-c8d2742fe8fa.png)
## 6.3 Feign和Ribbon的联系
Ribbon是一个基于 HTTP 和 TCP 客户端 的负载均衡的工具。它可以在客户端配置RibbonServerList（服务端列表），使用HttpClient 或 RestTemplate 模拟http请求，步骤相当繁琐。
## 6.4 负载均衡
Feign中本身已经集成了Ribbon依赖和自动配置，因此我们不需要额外引入依赖，也不需要再注册 RestTemplate 对象。另外，我们可以像之前课中讲的那样去配置Ribbon，可以通过 ribbon.xx 来进行全局配置。也可以通过{服务名}.ribbon.xx 来对指定服务配置。
按照前面讲过的方法，通过IDEA启动两个商品服务实例，来测试负载均衡效果。
![](media/1648521251286-67029ae0-dcf4-422e-af9e-be908777cc27.png)
![](media/1648521252185-af153205-e311-4ae9-bb21-b1898d2944f4.png)
## 6.5 Feign的配置
从Spring Cloud Edgware开始，Feign支持使用属性自定义Feign。对于一个指定名称的Feign Client（例如该Feign Client的名称为 feignName），Feign支持如下配置项：
feign:
  client:
    config:
      feignName:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: full
        errorDecoder: com.example.SimpleErrorDecoder
        retryer: com.example.SimpleRetryer
        requestInterceptors:
          - com.example.FooRequestInterceptor
          - com.example.BarRequestInterceptor
        decode404: false
        encoder: com.example.SimpleEncoder
        decoder: com.example.SimpleDecoder

- feignName：FeginClient的名称 
- connectTimeout ： 建立链接的超时时长 
- readTimeout ： 读取超时时长 
- loggerLevel: Fegin的日志级别 
- errorDecoder ：Feign的错误解码器 
- retryer ： 配置重试 
- requestInterceptors ： 添加请求拦截器 
- decode404 ： 配置熔断不处理404异常
- encoder: 编码
- decoder：解码

**Feign默认会采用Ribbon配置的超时时间，如果单独对Feign的超时时间进行配置，则会覆盖掉Ribbon的超时配置。**
## 6.6 请求压缩
Spring Cloud Feign 支持对请求和响应进行GZIP压缩，以减少通信过程中的性能损耗。通过下面的参数即可开启请求与响应的压缩功能：
**feign**:
  **compression**:
    **request**:
      **enabled**: **true **_#开启请求压缩
    _**response**:
      **enabled**: **true **_#开启响应压缩_
同时，我们也可以对请求的数据类型，以及触发压缩的大小下限进行设置：
**feign**:
  **compression**:
    **request**:
      **enabled**: **true **_#开启请求压缩
      _**mime-types**: text/html,application/xml,application/json _# 设置压缩的数据类型
      _**min-request-size**: 2048 _# 设置触发压缩的大小下限_
## 6.7 日志级别
在开发或者运行阶段往往希望看到Feign请求过程的日志记录，默认情况下Feign的日志是没有开启的。要想用属性配置方式来达到日志效果，只需在 application.yml 中添加如下内容即可：
_#配置feign日志输出_**feign**:
  **client**:
    **config**:
      **product-service**:
        **loggerLevel**: FULL
**logging**:
  **level**:
    **cn.ghostcloud.**.dao**: trace _#调整日志级别
    _**cn.ghostcloud.order.feign.ProductFeignClient**: debug

- loggin.level.xx:debug 控制某个类输出的日志级别为debug
- feign.client.config.product-service.loggerLevel : 配置Feign的日志。Feign有四种日志级别：
- NONE【性能最佳，适用于生产】：不记录任何日志（默认值） 
- BASIC【适用于生产环境追踪问题】：仅记录请求方法、URL、响应状态代码以及执行时间 
- HEADERS：记录BASIC级别的基础上，记录请求和响应的header。 
- FULL【比较适用于开发及测试环境定位问题】：记录请求和响应的header、body和元数据。

![](media/1648521252934-739a663d-b52b-47ce-b6cf-3b60f2c3a94e.png)
## 6.8 服务注册与发现总结

1. **Eureka**
- 搭建注册中心
- 引入依赖 spring-cloud-starter-netflix-eureka-server 
- 配置EurekaServer 
- 通过 @EnableEurekaServer 激活Eureka Server端配置
- 服务注册
- 服务提供者引入 spring-cloud-starter-netflix-eureka-client 依赖 
- 通过 eureka.client.serviceUrl.defaultZone 配置注册中心地址
2. **Ribbon**
- 通过Ribbon结合RestTemplate方式进行服务调用只需要在声明RestTemplate的方法上添加注解 @LoadBalanced即可 
- 可以通过 {服务名称}.ribbon.NFLoadBalancerRuleClassName 配置负载均衡策略
3. **Feign**
- 服务消费者引入 spring-cloud-starter-openfeign 依赖 
- 通过 @FeignClient 声明一个调用远程微服务接口 
- 启动类上通过 @EnableFeignClients 激活Feign
## 6.9 课后作业

1. 根据课程内容，动手实践完成Feign的使用案例，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
2. 使用Feign调用商品服务的新增商品接口，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 7 微服务架构的高并发问题
通过注册中心已经实现了微服务的服务注册和服务发现，并且通过Ribbon实现了负载均衡，借助 Feign可以优雅的进行微服务调用。那么我们编写的微服务的性能怎么样呢，是否存在问题呢？
## 7.1 准备模拟环境
为了模拟高并发场景下可能存在的问题，需要准备一套新的模拟环境，新创建一个hystrix-demo的工程，里面添加注册中心服务eureka-server、商品服务product-service、订单服务order-service，参考下图：
![](media/1648521253332-e9aef7fb-7201-4410-b22e-0a9137dd1c45.png)
_图中的mic-service-common是一个公共工具模块，将一些复用率较高的工具类和实体放到里面，在其他模块中通过依赖导入，避免了重复编写代码。_
eureka-server的相关配置如下pom.xml文件：
<**parent**>
    <**artifactId**>hystrix-demo01</**artifactId**>
    <**groupId**>cn.ghostcloud</**groupId**>
    <**version**>1.0-SNAPSHOT</**version**>
</**parent**>
<**modelVersion**>4.0.0</**modelVersion**>

<**artifactId**>eureka-server</**artifactId**>

<**dependencies**>
    <**dependency**>
        <**groupId**>org.springframework.cloud</**groupId**>
        <**artifactId**>spring-cloud-starter-netflix-eureka-server</**artifactId**>
    </**dependency**>
</**dependencies**>
application.yml文件：
**spring**:
  **application**:
    **name**: eureka-server_#单节点运行eureka_**eureka**:
  **instance**:
    **hostname**: localhost
  **client**:
    **register-with-eureka**: **false **_# 是否将自己注册到eureka服务中,单节点运行时为false,多节点运行时为true
    _**fetch-registry**: **false **_#是否从eureka中获取注册信息,单节点运行时为false,多节点运行时为true
    _**service-url**:
      **defaultZone**: http://${**eureka.instance.hostname**}:${**server.port**}/eureka/**server**:
  **port**: 8761
启动类的代码如下：
@SpringBootApplication
@EnableEurekaServer**public class **EurekaServerApplication {
    **public static void **main(String[] args) {
        SpringApplication._run_(EurekaServerApplication.**class**, args);
    }
}
product-service和order-service的配置参考前面章节中示例，注意需要添加对公共模块的依赖，新增如下配置：
<**dependency**>
    <**groupId**>cn.ghostcloud</**groupId**>
    <**artifactId**>mic-service-common</**artifactId**>
    <**version**>1.0-SNAPSHOT</**version**>
</**dependency**>
### 7.1.1 修改商品服务
为了模拟网络波动，演示请求和响应的延迟，需要对商品服务的查询接口进行改动，参考如下：
@RequestMapping(value = **"/{id}"**, method = RequestMethod.**_GET_**)**public **ApiResult<Product> findById(@PathVariable Long id) **throws **InterruptedException {
    _//模拟网络延迟
    _Thread._sleep_(2000);
    Product product = **this**.**productService**.findById(id);
    product.setProductDesc(**"调用商品服务ip:" **+ **ip **+ **"端口:" **+ **port**);
    **return **ApiResult._success_(product);
}
添加Thread.sleep(2000)，从而增加服务的响应时间。可以通过chrome浏览器的F12查看网页响应时间查看效果。
### 7.1.2 修改订单服务
为了模拟微服务之间访问调用存在的问题，还需对消费者服务(订单服务)进行修改，首先是修改yml配置文件，调整tomcat服务器的线程池大小：
**server**:
  **port**: 8083
  **tomcat**:
    **max-threads**: 10 _#最大线程数修改为10，用于模拟服务器高负载情况_
server.tomcat.max-threads 可以调整tomcat线程池默认值。
为了模拟服务单个接口处理过慢从而对其他接口造成影响，在OrderController中新增一个演示用的查询方法testQuery()：
@RequestMapping(value = **"/buy/model4/{id}"**, method = RequestMethod.**_GET_**)**public **ApiResult<Product> testOrderByModel4(@PathVariable(**"id"**) Long id) {
    **return this**.**productFeignClient**.findById(id);
}
_/**
 * 测试查询：用于高负载的情况下演示服务器响应过慢的情况
 *
 * _**_@return _**_*
 */_@RequestMapping(value = **"/query"**, method = RequestMethod.**_GET_**)**public **ApiResult<String> testQuery() {
    **return **ApiResult._success_(**"测试查询"**);
}
### 7.1.3 访问测试
![](media/1648521254251-6529a602-943e-4c47-82d3-f6f3afdd19af.png)
![](media/1648521255353-d900aba1-f976-488e-a43b-67b735169c71.png)
修改后查询测试接口响应时间为44ms，下单接口响应时间为2.06s。接下来通过性能压测工具Jmeter为下单接口施加压力。
## 7.2 性能测试工具Jmeter
![](media/1648521256443-71827fd1-98cc-4e02-a531-42c1f2b133f1.png)
Apache JMeter是Apache组织开发的基于Java的压力测试工具。用于对软件做压力测试，它最初被设计用于Web应用测试，但后来扩展到其他测试领域。它可以用于测试静态和动态资源，例如静态文件、Java 小服务程序、CGI 脚本、Java 对象、数据库、FTP 服务器， 等等。JMeter 可以用于对服务器、网 络或对象模拟巨大的负载，来自不同压力类别下测试它们的强度和分析整体性能。另外JMeter能够对应用程序做功能/回归测试，通过创建带有断言的脚本来验证程序返回了期望的结果。为了最大限度的灵活性，JMeter允许使用正则表达式创建断言。
### 7.2.1 安装Jmeter
准备Jmeter的安装包（apache-jmeter-2.13.zip），解压缩之后找到bin目录下的jmeter.bat以管理员身份启动即可。
![](media/1648521257963-2c0ae2d8-b6ae-4452-8e5e-b53ca26034ab.png)
### 7.2.2 配置Jmeter

1. 在测试计划下创建发起请求的线程组：

![](media/1648521259044-9eefd528-a82b-44b7-af84-5c23df5de075.png)
![](media/1648521260049-7b5b3904-afdf-4669-8039-eddabce41df3.png)
对线程数和线程循环次数进行配置。

1. 创建http请求模板

![](media/1648521260795-fd95f306-17f1-4ab3-962b-0ee5721cb689.png)

1. 配置测试接口信息

![](media/1648521261926-79d8b526-1164-4590-9da8-6ed06e504bed.png)
配置完成之后启动Jmeter，根据配置，Jmeter会启动50个线程同时访问订单服务的下单接口。此时在通过浏览器访问下单接口，发现响应时延已经明显提高。
![](media/1648521264370-2c2f60da-47f6-4ca1-bb75-3f57e757ca9e.png)
同时再次访问查询测试接口，发现测试接口的响应时延也被加长。
![](media/1648521264909-464ebe64-d901-41de-9b0e-66c7cb26987b.png)
## 7.3 高负载存在的问题
### 7.3.1 问题分析
在微服务架构中，我们将业务拆分成一个个的服务，服务与服务之间可以相互调用，由于网络原因或者 自身的原因，服务并不能保证服务的100%可用，如果单个服务出现问题，调用这个服务就会出现网络 延迟，此时若有大量的网络涌入，会形成任务累计，导致服务瘫痪。
在SpringBoot程序中，默认使用内置tomcat作为web服务器。单tomcat支持最大的并发请求是有限的，如果某一接口阻塞，待执行的任务积压越来越多，那么势必会影响其他接口的调用。
![](media/1648521266063-c23125fc-8567-4631-bc6c-1e93055a8e27.png)
解决上述问题的方法之一就是对多个服务/接口之间的访问进行隔离，每个服务分配单独的线程池进行访问，这样可避免影响其他接口的正常访问。
![](media/1648521267197-cb327dc1-fe76-4461-8585-43eb2d479ea1.png)
### 7.3.2 线程池实现服务隔离
使用Hystrix组件来实现线程池对服务的隔离。

1. 配置依赖坐标

在order-service模块的pom文件中添加如下配置：
_<!--引入hystrix依赖-->_<**dependency**>
    <**groupId**>com.netflix.hystrix</**groupId**>
    <**artifactId**>hystrix-metrics-event-stream</**artifactId**>
    <**version**>1.5.12</**version**>
</**dependency**>
<**dependency**>
    <**groupId**>com.netflix.hystrix</**groupId**>
    <**artifactId**>hystrix-javanica</**artifactId**>
    <**version**>1.5.12</**version**>
</**dependency**>

1. 配置线程池

在order-service模块中配置HystrixCommand接口的实现类，在实现类中可以对线程池进行配置。
**public class **OrderCommand **extends **HystrixCommand<ApiResult<Product>> {
    **private **RestTemplate **restTemplate**;
    **private **Long **productId**;

    **public **OrderCommand(RestTemplate restTemplate, Long productId) {
        **super**(_setter_());
        **this**.**restTemplate **= restTemplate;
        **this**.**productId **= productId;
    }

    **private static **Setter setter() {
        _//服务分组,设置分组名称
        _HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory._asKey_(**"order_product"**);
        _//服务标识
        _HystrixCommandKey commandKey = HystrixCommandKey.Factory._asKey_(**"product"**);
        _//设置线程池名称
        _HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory._asKey_(**"order-product-pool"**);
        _//线程池配置，核心线程5个，闲置时间15分钟，队列大小100，执行拒绝策略
        _HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties._Setter_().withCoreSize(5)
            .withKeepAliveTimeMinutes(15).withQueueSizeRejectionThreshold(100);
        _//命令属性配置
        _HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties._Setter_()
            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.**_THREAD_**)
            .withExecutionTimeoutEnabled(**false**);
        **return **HystrixCommand.Setter._withGroupKey_(groupKey).andCommandKey(commandKey).andThreadPoolKey(threadPoolKey)
            .andThreadPoolPropertiesDefaults(threadPoolProperties).andCommandPropertiesDefaults(commandProperties);
    }

    @Override
    **protected **ApiResult<Product> run() **throws **Exception {
        URI uri = **new **URI(**"http://product-service/product/" **+ **this**.**productId**);
        _//构建请求实体
        _RequestEntity<String> requestEntity = **new **RequestEntity<>(HttpMethod.**_GET_**, uri);
        _//因为product-service的接口返回的是ApiResult<Product>,所以需要使用exchange方法发起调用
        _ResponseEntity<ApiResult<Product>> responseEntity = **this**.**restTemplate**.exchange(
            requestEntity, **new **ParameterizedTypeReference<ApiResult<Product>>() {
            });
        **return **responseEntity.getBody();
    }

    @Override
    **protected **ApiResult<Product> getFallback() {
        **return **ApiResult._fail_(**"服务器繁忙"**);
    }

1. 修改服务调用方式

修改OrderController，使用自定义的OrderCommand完成调用，代码参考如下：
@RequestMapping(value = **"/buy/model4/{id}"**, method = RequestMethod.**_GET_**)
    **public **ApiResult<Product> testOrderByModel4(@PathVariable(**"id"**) Long id) {_//        return this.productFeignClient.findById(id);
        _**return new **OrderCommand(**this**.**restTemplate**, id).execute();
    }

1. 效果测试

启动Jemter执行压力测试脚本，等服务器负载升高后再通过浏览器访问测试查询接口，发现响应时间已恢复为毫秒级，参考下图：
![](media/1648521268474-c9b3ecc9-94a7-48b5-8a58-504fdf0066ea.png)
## 7.4 课后作业
根据课程内容，安装Jmeter，并动手完成高负载和线程池隔离案例，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 8 服务熔断Hystrix
## 8.1 服务容错
### 8.1.1 雪崩效应
在微服务架构中，一个请求需要调用多个服务是非常常见的。如客户端访问A服务，而A服务需要调用B 服务，B服务需要调用C服务，由于网络原因或者自身的原因，如果B服务或者C服务不能及时响应，A服务将处于阻塞状态，直到B服务C服务响应。此时若有大量的请求涌入，容器的线程资源会被消耗完毕，导致服务瘫痪。服务与服务之间的依赖性，故障会传播，造成连锁反应，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的“雪崩”效应。
![](media/1648521269152-6a5e8b87-7557-4434-bcb2-bf8103f07fa0.png)
雪崩是系统中的蝴蝶效应导致其发生的原因多种多样，有不合理的容量设计，或者是高并发下某一个方法响应变慢，亦或是某台机器的资源耗尽。从源头上我们无法完全杜绝雪崩源头的发生，但是雪崩的根本原因来源于服务之间的强依赖，所以我们可以提前评估，做好**熔断，隔离，限流**。
### 8.1.2 服务隔离
顾名思义，它是指将系统按照一定的原则划分为若干个服务模块，各个模块之间相对独立，无强依赖。当有故障发生时，能将问题和影响隔离在某个模块内部，而不扩散风险，不波及其它模块，不影响整体的系统服务。
### 8.1.3 熔断降级
熔断这一概念来源于电子工程中的断路器（Circuit Breaker）。在互联网系统中，当下游服务因访问压 力过大而响应变慢或失败，上游服务为了保护系统整体的可用性，可以暂时切断对下游服务的调用。这种牺牲局部，保全整体的措施就叫做熔断。
![](media/1648521270016-8007d741-915b-4417-ab09-ef1d7bb67e83.png)
所谓降级，就是当某个服务熔断之后，服务器将不再被调用，此时客户端可以自己准备一个本地的 fallback回调，返回一个缺省值，也可以理解为兜底。
### 8.1.4 服务限流
限流可以认为服务降级的一种，限流就是限制系统的输入和输出流量已达到保护系统的目的。一般来说系统的吞吐量是可以被测算的，为了保证系统的稳固运行，一旦达到的需要限制的阈值，就需要限制流量并采取少量措施以完成限制流量的目的。比方：推迟解决，拒绝解决，或者者部分拒绝解决等等。
## 8.2 Hystrix介绍
Hystrix是由Netflix开源的一个延迟和容错库，用于隔离访问远程系统、服务或者第三方库，防止级联失败，从而提升系统的可用性与容错性。Hystrix主要通过以下几点实现延迟和容错。

- 包裹请求：使用HystrixCommand包裹对依赖的调用逻辑，每个命令在独立线程中执行。这使用了设计模式中的“命令模式”。 
- 跳闸机制：当某服务的错误率超过一定的阈值时，Hystrix可以自动或手动跳闸，停止请求该服务一段时间。 
- 资源隔离：Hystrix为每个依赖都维护了一个小型的线程池（或者信号量）。如果该线程池已满，发往该依赖的请求就被立即拒绝，而不是排队等待，从而加速失败判定。 
- 监控：Hystrix可以近乎实时地监控运行指标和配置的变化，例如成功、失败、超时、以及被拒绝的请求等。 
- 回退机制：当请求失败、超时、被拒绝，或当断路器打开时，执行回退逻辑。回退逻辑由开发人员自行提供，例如返回一个缺省值。 
- 自我修复：断路器打开一段时间后，会自动进入“半开”状态。
### 8.2.1 Hystrix对RestTemplate的支持

1. 创建新模块

从order-service复制一个订单服务order-hystrix-rest，用于试验Hystrix和RestTemplate的结合使用。

1. 导入依赖

在order-hystrix-rest中添加Hystrix的依赖：
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-hystrix</**artifactId**>
</**dependency**>

1. 启动熔断

在启动类上添加 @EnableCircuitBreaker 注解开启对熔断器的支持。
@SpringBootApplication
@EnableCircuitBreaker
@EnableEurekaClient**public class **HystrixRestApplication {
    **public static void **main(String[] args) {
        SpringApplication._run_(HystrixRestApplication.**class**, args);
    }
}
@SpringBootApplication、@EnableCircuitBreaker、@EnableEurekaClient可合并为@SpringCloudAppliation注解。

1. 编写熔断降级业务逻辑

@RestController
@RequestMapping(**"/order"**)**public class **OrderController {

    @Resource
    **private **RestTemplate **restTemplate**;

    @HystrixCommand(fallbackMethod = **"fallBack"**)
    @RequestMapping(value = **"/buy/{productId}"**, method = RequestMethod.**_GET_**)
    **public **ApiResult<Product> testOrder(@PathVariable Long productId) **throws **URISyntaxException {
        String url = **"http://product-service/product/" **+ productId;
        URI uri = **new **URI(url);
        RequestEntity<String> requestEntity = **new **RequestEntity(HttpMethod.**_GET_**, uri);
        ResponseEntity<ApiResult<Product>> responseEntity = **this**.**restTemplate**.exchange(requestEntity,
            **new **ParameterizedTypeReference<ApiResult<Product>>() {
            });
        **return **responseEntity.getBody();
    }

    _/**
     * 为testOrder方法订制的降级方法
     *
     * _**_@param _****_productId
     _**_* _**_@return
     _**_*/
    _**public **ApiResult<Product> fallBack(Long productId) {
        Product product = **new **Product();
        product.setProductDesc(**"订制的降级方法"**);
        **return **ApiResult._success_(product);
    }
}
由上面代码可见，我们为testOrder方法编写了一个回退方法fallBack，该方法与testOrder方法具有相同的参数与返回值类型，该方法返回一个默认的错误信息。
在testOrder方法上，使用注解@HystrixCommand的fallbackMethod属性，指定熔断触发的降级方法是fallBack。

- 熔断的降级逻辑方法必须跟正常逻辑方法保证：**相同的参数列表和返回值声明。**
- 在testOrder方法上 HystrixCommand(fallbackMethod = "fallback") 用来声明一个降级逻辑的方法。
1. 超时配置

由于在之前的案例中为了演示网络延迟效果，让product-service的商品查询接口延迟2s返回，而hystrix的默认超时时长为1s，这会导致直接请求商品服务会返回错误的信息，可以通过修改配置改变hystrix的默认超时时长：
_#配置hystrix默认超时时长_**hystrix**:
  **command**:
    **default**:
      **execution**:
        **isolation**:
          **thread**:
            **timeoutInMilliseconds**: 5000
**这种方式设置全局超时时间，如果要设置某一个服务的超时时间，可以将default换位服务的名称即可。当Hystrix和ribbon配合使用的时候，如果希望 Hystrix的超时时间大于ribbon配置的超时时间，要考虑到重试次数，例如 Ribbon的超时时间是一秒，重试三次，那Hystrix超时时间必须大于三秒。**
IDEA对以上配置没有自动提示功能。

1. 测试熔断效果

当product-service正常运行时，浏览器访问结果如下：
![](media/1648521271003-f7e36f0b-93b7-4bbb-97a4-a7d63271de13.png)
然后停掉product-service服务，模拟服务故障，再次通过浏览器发送请求，访问结果如下：
![](media/1648521272047-31b5ea39-ba01-448d-8128-58dca7124116.png)
从上图可见，熔断器已生效，服务响应出错后进入了降级方法返回。

1. 配置默认的FallBack

前面的实现方式中把fallback方法写在了某个业务方法上，如果这样的方法很多，就需要写很多fallback方法。所以我们可以把Fallback配置加在类上，实现默认fallback：
@RestController
@DefaultProperties(defaultFallback = **"defaultFallBack"**)@RequestMapping(**"/order"**)**public class **OrderController {

    @Resource
    **private **RestTemplate **restTemplate**;

    _//    @HystrixCommand(fallbackMethod = "fallBack")
    _@HystrixCommand
    @RequestMapping(value = **"/buy/{productId}"**, method = RequestMethod.**_GET_**)
    **public **ApiResult<Product> testOrder(@PathVariable Long productId) **throws **URISyntaxException {
        String url = **"http://product-service/product/" **+ productId;
        URI uri = **new **URI(url);
        RequestEntity<String> requestEntity = **new **RequestEntity(HttpMethod.**_GET_**, uri);
        ResponseEntity<ApiResult<Product>> responseEntity = **this**.**restTemplate**.exchange(requestEntity,
            **new **ParameterizedTypeReference<ApiResult<Product>>() {
            });
        **return **responseEntity.getBody();
    }

    _/**
     * 全局默认的fallBack方法
     *
     * _**_@return
     _**_*/
    _**public **ApiResult<Product> defaultFallBack() {
        Product product = **new **Product();
        product.setProductDesc(**"默认的降级方法"**);
        **return **ApiResult._success_(product);
    }
@DefaultProperties(defaultFallback=”defaultFallBack”)注解用于指定默认的降级方法。需要注意通用默认的降级方法是没有方法参数的，并且方法返回值需是通用的，因此在定义服务接口时，最好将返回值类型采用统一模板定义，确保各接口返回值的类型能一致。
### 8.2.2 Hystrix对Feign的支持
Spring Cloud Fegin默认已为Feign整合了hystrix，所以添加Feign依赖后就不用在添加hystrix，在Feign中启用熔断机制，只要按以下步骤开发：

1. 创建新模块

从order-service复制一个订单服务order-hystrix-feign，用于试验Hystrix和Feign的结合使用。**注意OrderController中需要改为feign的方式进行远程调用。**

1. 在配置文件中开启hystrix

_#配置feign日志输出_**feign**:
  **client**:
    **config**:
      **product-service**:
        **loggerLevel**: FULL
  **hystrix**: _#在feign中开启hystrix熔断
    _**enabled**: **true**

1. 定义熔断降级的实现类

基于Feign实现熔断降级，那么降级方法需要配置到FeignClient接口的实现类中。
_/**
 * 实现自定义ProductFeignClient接口,
 * 编写熔断降级方法
 
 */_@Component**public class **ProductFeignClientCallBack **implements **ProductFeignClient {
    @Override
    **public **ApiResult<Product> findById(Long productId) {
        Product product = **new **Product();
        product.setProductDesc(**"Feign client 降级返回"**);
        **return **ApiResult._success_(product);
    }
}

1. 在FeignClient中添加hystrix熔断

@FeignClient(name = **"product-service"**, fallback = ProductFeignClientCallBack.**class**)**public interface **ProductFeignClient {
    @RequestMapping(value = **"/product/{productId}"**, method = RequestMethod.**_GET_**)
    **public **ApiResult<Product> findById(@PathVariable Long productId);
}
@FeignClient注解中以fallback声明降级方法。
## 8.3 Hystrix的监控平台
我们知道，当请求失败，被拒绝，超时的时候，都会进入到降级方法中。但进入降级方法并不意味着断路器已经被打开。那么如何才能了解断路器中的状态呢？
除了实现容错功能，Hystrix还提供了近乎实时的监控，HystrixCommand和 HystrixObservableCommand在执行时，会生成执行结果和运行指标。比如每秒的请求数量，成功数量等。这些状态会暴露在Actuator提供的/health端点中。

1. 项目中添加依赖

<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-hystrix</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.boot</**groupId**>
    <**artifactId**>spring-boot-starter-actuator</**artifactId**>
</**dependency**>

1. 在配置文件中暴露监控断点

_#暴露所有actuator监控的端点_**management**:
  **endpoints**:
    **web**:
      **exposure**:
        **include**: **'*'**

1. 访问测试

浏览器访问http://localhost:8083/actuator/hystrix.stream ,即可看到实时的监控数据。
![](media/1648521274259-b3f243dd-1082-42f0-b155-2ed66eae7916.png)
_注如果监控页面一直显示ping，而没有数据展示，需要访问一下添加了熔断机制的接口。_
### 8.3.1 搭建Hystrix DashBoard监控
访问/hystrix.stream接口获取的都是已文字形式展示的信息，很难通过文 字直观的展示系统的运行状态，所以Hystrix官方还提供了基于图形化的DashBoard（仪表板）监控平 台。Hystrix仪表板可以显示每个断路器（被@HystrixCommand注解的方法）的状态。

1. 导入依赖

完整依赖包含如下：
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-hystrix</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-hystrix-dashboard</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.boot</**groupId**>
    <**artifactId**>spring-boot-starter-actuator</**artifactId**>
</**dependency**>

1. 启动类上激活仪表盘

在启动类使用@EnableHystrixDashboard注解激活仪表盘项目。
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrixDashboard**public class **HystrixFeignApplication {
    **public static void **main(String[] args) {
        SpringApplication._run_(HystrixFeignApplication.**class**, args);
    }
}

1. 访问测试

在浏览器中输入[http://localhost:8083/hystrix](http://localhost:8083/hystrix)，效果如下
![](media/1648521277828-23dba514-4cbe-4851-8a74-60d2a9683df7.png)
输入[http://localhost:8083/actuator/hystrix.stream](http://localhost:8083/actuator/hystrix.stream)然后点击“Monitor Stream”，会进入如下页面：
![](media/1648521279476-cf2fc655-7488-4446-a482-7769c7077e95.png)
### 8.3.2 断路器聚合监控Turbine
在微服务架构体系中，每个服务都需要配置Hystrix DashBoard监控。如果每次只能查看单个实例的监控数据，就需要不断切换监控地址，这显然很不方便。要想看这个系统的Hystrix Dashboard数据就需要用到Hystrix Turbine。Turbine是一个聚合Hystrix 监控数据的工具，他可以将所有相关微服务的 Hystrix 监控数据聚合到一起，方便使用。引入Turbine后，整个监控系统架构如下：
![](media/1648521280566-50bc393a-7ffe-48a1-a0c1-2c0e6e275ab1.png)
使用Turbine聚合多个微服务监控操作步骤如下：

1. 搭建TurbineServer

新建一个模块hystrix-turbine，引入相关依赖。
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-turbine</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-hystrix</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-hystrix-dashboard</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-eureka-client</**artifactId**>
</**dependency**>

1. 配置多个微服务的监控

在hystrix-turbine模块的application.yml文件中进行如下配置：
**server**:
  **port**: 8086**spring**:
  **application**:
    **name**: hystrix-turbine**eureka**:
  **client**:
    **service-url**:
      **defautlZone**: http://localhost:8761/eureka/
  **instance**:
    **prefer-ip-address**: **true
turbine**: _#要监控的微服务名称，多个服务用逗号分割
  _**app-config**: order-hystrix-feign,order-hystrix-rest
  **cluster-name-expression**: **"'default'"**
**turbine.appConfig **：配置Eureka中的serviceId列表，表明监控哪些服务。
**turbine.aggregator.clusterConfig** ：指定聚合哪些集群，多个使用”,”分割，默认为default。可使用http://.../turbine.stream?clu...{clusterConfig之一}访问。
**turbine.clusterNameExpression** ：
clusterNameExpression指定集群名称，默认表达式appName；此时：turbine.aggregator.clusterConfig需要配置想要监控的应用名称；
当clusterNameExpression: default时，turbine.aggregator.clusterConfig可以不写，因为默认就是default；
当clusterNameExpression: metadata[‘cluster’]时，假设想要监控的应用配置了eureka.instance.metadata-map.cluster: ABC，则需要配置，同时turbine.aggregator.clusterConfig: ABC
turbine会自动的从注册中心中获取需要监控的微服务，并聚合所有微服务中的 /hystrix.stream 数据。

1. 配置启动类

@SpringBootApplication
@EnableTurbine
@EnableHystrixDashboard**public class **TurbineApplication {
    **public static void **main(String[] args) {
        SpringApplication._run_(TurbineApplication.**class**, args);
    }
}
@EnableTurbine激活Turbine，@EnableHystrixDashBoard开启监控平台。

1. 测试验证

浏览器访问 [http://localhost:8086/hystrix](http://localhost:8086/hystrix)。并在页面url位置输入[http://localhost:8086/turbine.stream](http://localhost:8086/turbine.stream)即可查看多个微服务的聚合监控效果。
![](media/1648521281449-d3528518-5d3b-4200-8225-506f464e0395.png)

![](media/1648521282468-64962c11-89cb-4d6e-a3ec-a76d3d32042a.png)
## 8.4 熔断器的状态
熔断器有三个状态 CLOSED 、 OPEN 、 HALF_OPEN 熔断器默认关闭状态，当触发熔断后状态变更为 OPEN ,再等待到指定的时间，Hystrix会放请求检测服务是否开启，这期间熔断器会变为 HALF_OPEN 半开启状态，熔断探测服务可用则继续变更为 CLOSED 关闭熔断器。
![](media/1648521283298-5a17af9a-5f89-4137-a0f3-48fd0043882f.png)
**Closed**：关闭状态（断路器关闭），所有请求都正常访问。代理类维护了最近调用失败的次数，如果某次调用失败，则使失败次数加1。如果最近失败次数超过了在给定时间内允许失败的阈值，则代理类切换到断开(Open)状态。此时代理开启了一个超时时钟，当该时钟超过了该时间，则切换到半断开（Half-Open）状态。该超时时间的设定是给了系统一次机会来修正导致调用失败的错误。
**Open**：打开状态（断路器打开），所有请求都会被降级。Hystix会对请求情况计数，当一定时间内失败请求百分比达到阈值，则触发熔断，断路器会完全关闭。默认失败比例的阈值是50%，请求次数最少不低于20次。
**Half-Open**：半开状态，open状态不是永久的，打开后会进入休眠时间（默认是5S）。随后断路器会自动进入半开状态。此时会释放1次请求通过，若这个请求是健康的，则会关闭断路器，否则继续保持打开，再次进行5秒休眠计时。
### 8.4.1 熔断器状态切换演示
为了能够精确控制请求的成功或失败，在product-service的查询接口中加入一段逻辑：
@RequestMapping(value = **"/{id}"**, method = RequestMethod.**_GET_**)
    **public **ApiResult<Product> findById(@PathVariable Long id) **throws **InterruptedException {
        **if **(id != 1){
            **throw  new **RuntimeException(**"模拟故障"**);
        }
        Product product = **this**.**productService**.findById(id);
        product.setProductDesc(**"调用商品服务ip:" **+ **ip **+ **"端口:" **+ **port**);
        **return **ApiResult._success_(product);
    }

凡是参数id不为1的会请求失败。
调用端直接使用orderr-hystrix-rest模块即可，（**最好不用feign来进行演示，因为feign自身有很多默认的配置，可能会覆盖掉hystrix的配置**，不方便演示）。熔断器的默认触发阈值是20次请求，不好触发。休眠时间时5秒，时间太短，不易观察，为了测试方便，可以通过配置修改熔断策略：
**hystrix**:
  **command**:
    **default**:
      **circuitBreaker**:
        **requestVolumeThreshold**: 20 _#触发熔断的最小请求次数，默认20
        _**sleepWindowInMilliseconds**: 10000 _#熔断多少秒后去尝试请求
        _**errorThresholdPercentage**: 50 _#触发熔断的失败请求最小占比，默认50%_
我们准备两个请求窗口：
[http://localhost:8084/order/buy/1](http://localhost:8084/order/buy/1) ,肯定成功
[http://localhost:8084/order/buy/2](http://localhost:8084/order/buy/2)，肯定失败
当快速的访问第二个链接时（超过阈值）,就会触发熔断，断路器会断开，一切请求都会被降级处理，此时访问第一个链接，会发现返回结果也是失败。
![](media/1648521284482-907173fd-9ff8-45c4-a3ac-b8008e89e6ac.png)
## 8.5 熔断器的隔离策略
微服务使用Hystrix熔断器实现了服务的自动降级，让微服务具备自我保护的能力，提升了系统的稳定性，也较好的解决雪崩效应。其使用方式目前支持两种策略：

- 线程池隔离策略：使用一个线程池来存储当前的请求，线程池对请求作处理，设置任务返回处理超时时间，堆积的请求堆积入线程池队列。这种方式需要为每个依赖的服务申请线程池，有一定的资源消耗，好处是可以应对突发流量（流量洪峰来临时，处理不完可将数据存储到线程池队里慢慢处理）。
- 信号量隔离策略：使用一个原子计数器（或信号量）来记录当前有多少个线程在运行，请求来先判断计数器的数值，若超过设置的最大线程个数则丢弃改类型的新请求，若不超过则执行计数操作请求来计数器+1，请求返回计数器-1。这种方式是严格的控制线程且立即返回模式，无法应对突发流量（流量洪峰来临时，处理的线程超过数量，其他的请求会直接返回，不继续去请求依赖的服务）。

两种策略的对比如下：
![](media/1648521285378-8ec393da-d890-4b79-8b91-6571e3d45872.png)
策略切换配置：
hystrix.command.default.execution.isolation.strategy : 配置隔离策略，
ExecutionIsolationStrategy.SEMAPHORE 信号量隔离，
ExecutionIsolationStrategy.THREAD 线程池隔离。
hystrix.command.default.execution.isolation.maxConcurrentRequests: 最大信号量上限。

- Hystrix线程池配置介绍

# 核心线程数 默认值10
hystrix.threadpool.default.coreSize=10
# 最大线程数 默认值10 在1.5.9版本之前该值总是等于coreSize
hystrix.threadpool.default.maximumSize=10
# 阻塞队列大小 默认值-1表示使用同步队列 
hystrix.threadpool.default.maxQueueSize=-1
# 阻塞队列大小拒绝阈值 默认值为5 当maxQueueSize=-1时，不起作用
hystrix.threadpool.default.queueSizeRejectionThreshold=5
# 释放线程时间 min为单位 默认为1min，当最大线程数大于核心线程数的时
hystrix.threadpool.default.keepAliveTimeMinutes=1
# 是否允许maximumSize配置生效，默认值为false
hystrix.threadpool.default.allowMaximumSizeToDivergeFromCoreSize=true
## 8.6 课后作业
根据授课内容，动手完成课程中的案例实践，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 9 微服务网关概述
在微服务架构里面，不同的微服务一般会有不同的网络地址，客户端在访问这些微服务时必须记住几十甚至几百个地址，这对于客户端方来说太复杂也难以 维护。
![](media/1648521286830-bbdfc0fc-4190-45be-855e-8414cb1a810e.png)
如果让客户端直接与各个微服务通讯，可能会有很多问题：

- 客户端会请求多个不同的服务，需要维护不同的请求地址，增加开发难度；**客户端通常只能采用IP+端口的形式来访问服务（无网关的前提下）。**
- 在某些场景下存在跨域请求的问题； 
- 加大身份认证的难度，每个微服务需要独立认证。

因此，我们需要一个微服务网关，介于客户端与服务器之间的中间层，所有的外部请求都会先经过微服务网关。客户端只需要与网关交互，只知道一个网关地址即可，这样简化了开发还有以下优点：

- 易于监控 
- 易于认证 
- 减少了客户端与各个微服务之间的交互次数

![](media/1648521287590-7a85a21c-d515-4ab1-946c-634a68e9cd24.png)
## 9.1 服务网关的概念
### 9.1.1 什么是微服务网关
API网关是一个服务器，是系统对外的唯一入口。API网关封装了系统内部架构，为每个客户端提供一个定制的API。API网关方式的核心要点是，所有的客户端和消费端都通过统一的网关接入微服务，在网关层处理所有的非业务功能。通常，网关也是提供REST/HTTP的访问API。服务端通过API-GW注册和管理服务。
![](media/1648521288599-6d692785-a133-4876-9b96-ef18cccfffe3.png)
### 9.1.2 作用和应用场景
网关具有的职责，如身份验证、监控、负载均衡、缓存、请求分片与管理、静态响应处理。当然，最主要的职责还是与“外界联系”。
![](media/1648521289688-a0e9ee93-d554-4107-a4b6-9d04a0ada98b.png)
## 9.2 常见的API网关实现方式

- Kong

基于Nginx+Lua开发，性能高，稳定，有多个可用的插件(限流、鉴权等等)可以开箱即用。问题：只支持Http协议；二次开发，自由扩展困难；提供管理API，缺乏更易用的管控、配置方式。

- Zuul

Netflix开源，功能丰富，使用JAVA开发，易于二次开发；需要运行在web容器中，如Tomcat。问题：缺乏管控，无法动态配置；依赖组件较多；处理Http请求依赖的是Web容器，性能不如Nginx。

- Traefik

Go语言开发；轻量易用；提供大多数的功能：服务路由，负载均衡等等；提供WebUI 问题：二进制文件部署，二次开发难度大；UI更多的是监控，缺乏配置、管理能力。

- Spring Cloud Gateway

SpringCloud提供的网关服务。

- Nginx+lua实现

使用Nginx的反向代理和负载均衡可实现对api服务器的负载均衡及高可用。问题：自注册的问题和网关本身的扩展性。
## 9.3 基于Nginx的网关实现
### 9.3.1 Nginx介绍
Nginx("engine x")是一款是由俄罗斯的程序设计师Igor Sysoev所开发高性能的 Web和 反向代理服务器，也是一个 IMAP/POP3/SMTP 代理服务器。Nginx可以作为一个HTTP服务器进行网站的发布处理，在高连接并发的情况下，Nginx是Apache服务器不错的替代品。另外Nginx可以作为反向代理进行负载均衡的实现。
### 9.3.2 正向/反向代理

1. 正向代理

正向代理，"它代理的是客户端，代客户端发出请求"，是一个位于客户端和原始服务器(origin server)之间的服务器，为了从原始服务器取得内容，客户端向代理发送一个请求并指定目标(原始服务器)，然后代理向原始服务器转交请求并将获得的内容返回给客户端。客户端必须要进行一些特别的设置才能使用正向代理。
有一些网站我们无法访问，如国外的网站，或者说某些公司内部的网站，我们输入完整的url无法访问，这时候我们就可以使用正向代理，让可以访问这些url的代理服务器作为正向代理服务器，接收客户端请求，然后通过客户端的请求url去访问服务器，然后接收服务器的返回结果再回推给客户端，流程如下图。
![](media/1648521291587-4ffbf93a-51d6-40d1-ba81-8dbf3e498084.png)
在正向代理中，代理服务器有客户的访问信息，而服务器是不知道的，所以代理服务器可以隐藏客户信息，保护客户信息安全，同时通过正向代理，客户端也可以访问我们原来访问不了的网站，但客户端要知道准确地服务端url。

1. 反向代理

多个客户端给服务器发送的请求，Nginx服务器接收到之后，按照一定的规则分发给了后端的业务处理服务器进行处理了。此时请求的来源也就是客户端是明确的，但是请求具体由哪台服务器处理的并不明确了，Nginx扮演的就是一个反向代理角色。客户端是无感知代理的存在的，反向代理对外都是透明的，访问者并不知道自己访问的是一个代理。因为客户端不需要任何配置就可以访问。反向代理，"它代理的是服务端，代服务端接收请求"，主要用于服务器集群分布式部署的情况下，反向代理隐藏了服务器的信息。
当服务器不想让别人知道他的服务器地址时，或者服务器集群了很多台服务器，这时候客户如果都要用对应的准确url访问服务器的话太麻烦，或者说服务器希望对访问人数进行限制，安全识别等时。反向代理服务器就把这些服务器的请求全部整合，留给客户一个公用的网站，如公司的首页，淘宝首页等，客户访问时，把这些请求分派给不同的对应的服务器，流程如下图：
![](media/1648521293599-26cf856b-77c2-42ce-9d39-e53ea483be10.png)
在反向代理中，客户并不知道它访问的是哪个代理，所以访问信息都在反向代理服务器中，可以保证服务器内网的安全。同时反向代理服务器可以均衡地分派不同的访问请求给服务器，可以缓解服务器的压力，做到负载均衡。而Nginx就是比较常用的反向代理。
如果只是单纯的需要一个最基础的具备转发功能的网关，那么使用Ngnix是一个不错的选择。
### 9.3.3 演示环境准备
新建一个用于网关学习的工程gateway-demo，在工程下创建三个模块：eureka-server、order-service、product-service，详细操作步骤参考前面章节内容。在本机上安装nginx，找到nginx.exe运行即可。
![](media/1648521294763-882b80a8-185d-4243-a0ec-cc64a14bd352.png)
### 9.3.4 配置Nginx请求转发
进入目录conf，找到nginx.conf文件，添加如下配置：
   location /api-order{
            proxy_pass [http://127.0.0.1:8082/;](http://127.0.0.1:8082/;)
        }
        location /api-product{
            proxy_pass [http://127.0.0.1:8081/;](http://127.0.0.1:8081/;)
        }
重启nginx，分别在浏览器输入[http://localhost/api-product/product/2](http://localhost/api-product/product/2) 和 [http://localhost/api-order/order/1](http://localhost/api-order/order/1) 进行访问测试。
## 9.4 Spring Cloud Gateway简介
在SpringCloud微服务体系中，有个很重要的组件就是网关，在1.x版本中都是采用的Zuul网关；但在2.x版本中，zuul的升级一直跳票，SpringCloud最后自己研发了一个网关替代Zuul，那就是SpringCloud Gateway。
网上很多地方都说Zuul是阻塞的，Gateway是非阻塞的，这么说是不严谨的，准确的讲Zuul1.x是阻塞的，而在2.x的版本中，Zuul也是基于Netty，也是非阻塞的，如果一定要说性能，其实这个真没多大差距。
而官方出过一个测试项目，创建了一个benchmark的测试项目：[spring-cloud-gateway-bench](https://github.com/spencergibb/spring-cloud-gateway-bench)，其中对比了：Spring Cloud Gateway、Zuul1.x、Linkerd，见下表：
![](media/1648521296242-36b9bbd1-05c1-49c3-9312-83ee2d5fca14.png)
还有一点就是Gateway是**基于WebFlux**的。这里引出了WebFlux名词，**那什么是WebFlux？**
![](media/1648521300086-2cf11242-10bf-4b45-b592-8cc39e73a2cb.png)
左侧是传统的基于Servlet的**Spring Web MVC**框架，传统的Web框架，比如说：struts2，springmvc等都是基于Servlet API与Servlet容器基础之上运行的，在Servlet3.1之后才有了异步非阻塞的支持。右侧是5.0版本新引入的基于Reactive Streams的Spring WebFlux框架，从上到下依次是Router Functions，WebFlux，Reactive Streams三个新组件。
**Router Functions:** 对标**@Controller，@RequestMapping**等标准的Spring MVC注解，提供一套函数式风格的API，用于创建**Router，Handler**和**Filter**。
**WebFlux**: 核心组件，协调上下游各个组件提供响应式编程支持。
**Reactive Streams:** 一种支持背压（Backpressure）的异步数据流处理标准，主流实现有RxJava和Reactor，Spring WebFlux默认集成的是Reactor。
在Web容器的选择上，Spring WebFlux既支持像Tomcat，Jetty这样的的传统容器（前提是支持Servlet 3.1 Non-Blocking IO API），又支持像Netty，Undertow那样的异步容器。不管是何种容器，Spring WebFlux都会将其输入输出流适配成Flux<DataBuffer>格式，以便进行统一处理。
值得一提的是，除了新的Router Functions接口，Spring WebFlux同时支持使用老的Spring MVC注解声明Reactive Controller。和传统的MVC Controller不同，Reactive Controller操作的是非阻塞的ServerHttpRequest和ServerHttpResponse，而不再是Spring MVC里的HttpServletRequest和HttpServletResponse。
### 9.4.1 核心概念
![](media/1648521301710-6d36930b-ffef-4f06-beda-30e89e7c00cc.png)
**路由（route）**：路由是网关最基础的部分，路由信息由一个ID、一个目的URL、一组断言工厂和一组Filter组成。如果断言为真，则说明请求URL和配置的路由匹配。
**断言（predicates）**：Java8中的断言函数，Spring Cloud Gateway中的断言函数输入类型是 Spring5.0框架中的ServerWebExchange。Spring Cloud Gateway中的断言函数允许开发者去定义匹配来自Http Request中的任何信息，比如请求头和参数等。
**过滤器（filter）**：一个标准的Spring webFilter，Spring Cloud Gateway中的Filter分为两种类型，分别是Gateway Filter和Global Filter。过滤器Filter可以对请求和响应进行处理。
### 9.4.2 工作原理
![](media/1648521303044-22324efd-1197-44b6-a4c2-763443a04c84.png)
客户端向 Spring Cloud Gateway 发出请求。然后在 Gateway Handler Mapping 中找到与请求相匹配的路由，将其发送到 Gateway Web Handler。Handler 再通过指定的过滤器链来将请求发送到我们实际的服务执行业务逻辑，然后返回。过滤器之间用虚线分开是因为过滤器可能会在发送代理请求之前（“pre“）或之后（”post“）执行业务逻辑。Filter在”pre“类型的过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等，在“post”类型的过滤器中可以做响应内容、响应头的修改，日志的输出，流量监控等有着非常重要的作用。**核心逻辑就是路由转发，执行过滤器链。**在处理过程中，有一个重要的点就是将请求和路由进行匹配，这时候就需要用到predicate，它是决定了一个请求走哪一个路由。
## 9.5 GateWay入门案例
### 9.5.1 入门案例

1. 创建模块导入依赖

复用gateway-demo工程进行案例开发演示，在项目中添加新的模块gateway-server，并导入依赖：
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-gateway</**artifactId**>
</**dependency**>
注意SpringCloud Gateway使用的web框架为webflux，和SpringMVC不兼容。所以需要对父工程以及其他模块的依赖进行调整，具体操作为将父工程pom文件中的依赖项 spring-boot-starter-web 移除掉，然后在其他需要使用到的子模块中单独以入。

1. 配置启动类

@SpringBootApplication**public class **GatewayApplication {
    **public static void **main(String[] args) {
        SpringApplication._run_(GatewayApplication.**class**, args);
    }
}

1. 编写配置文件

**server**:
  **port**: 8080**spring**:
  **application**:
    **name**: gateway-server
  **cloud**:
    **gateway**:
      **routes**:
      - **id**: product-service
        **uri**: [http://127.0.0.1:8081](http://127.0.0.1:8081)
        **predicates**:
        - Path=/product/**
      - **id**: order-service
        **uri**: [http://127.0.0.1:8082](http://127.0.0.1:8082)
        **predicates**:
        - Path=/order/**

- id: 我们自定义的路由ID，保持唯一。
- uri:目标服务地址。
- predicates: ：路由条件，Predicate 接受一个输入参数，返回一个布尔值结果。该接口包含多种默认方法来将 Predicate 组合成其他复杂的逻辑（比如：与，或，非）。
- filters: 过滤规则，此处没有用到。

上面这段配置的意思是，配置了一个id为product-service的路由规则和一个id为order-service的路由规则，当访问网关请求地址以 product 开头时，会自动转发到地址： [http://127.0.0.1:8081/](http://127.0.0.1:8081/)  。配置完成启动项目即可在浏览器 访问进行测试，当我们访问地址 [http://localhost:8080/product/1](http://localhost:8080/product/1)  时会展示页面展示如下：
![](media/1648521303775-d4ff5273-8985-437a-8e0f-6536e90978be.png)
### 9.5.2 Gateway路由匹配规则
Spring Cloud Gateway 的功能很强大，前面我们只是使用了 predicates 进行了简单的条件匹配，其实 Spring Cloud Gataway 帮我们内置了很多 Predicates 功能。在 Spring Cloud Gateway 中 Spring 利用 Predicate 的特性实现了各种路由匹配规则，有通过 Header、请求参数等不同的条件来进行作为条件匹配到对应的路由。
![](media/1648521304812-b24ec101-f997-4d1b-9828-3f08baa17770.png)
在上图中，有很多类型的Predicate，比如说时间类型的Predicated（AfterRoutePredicateFactory BeforeRoutePredicateFactory BetweenRoutePredicateFactory），当只有满足特定时间要求的请求会进入到此predicate中，并交由router处理；cookie类型的CookieRoutePredicateFactory，指定的cookie满足正则匹配，才会进入此router;以及host、method、path、querparam、remoteaddr类型的predicate，每一种predicate都会对当前的客户端请求进行判断，是否满足当前的要求，如果满足则交给当前请求处理。如果有很多个Predicate，并且一个请求满足多个Predicate，则按照配置的顺序第一个生效。
#### 9.5.2.1 After Route Predicate Factory
After Route Predicate Factory使用的是时间作为匹配规则，只要当前时间大于设定时间，路由才会匹配请求。配置参考如下：
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: after_route
          **uri**: [http://www.ghostcloud.cn](http://www.ghostcloud.cn)
          **predicates**:
            - After=2021-12-25T14:33:47.789+08:00
这个路由规则会在东8区的2021-12-25 14:33:47后，将请求都转跳到www.ghostcloud.cn。
#### 9.5.2.2 Before Route Predicate Factory
Before Route Predicate Factory也是使用时间作为匹配规则，只要当前时间小于设定时间，路由才会匹配请求。配置参考如下：
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: before_route
          **uri**: [http://www.ghostcloud.cn](http://www.ghostcloud.cn)
          **predicates**:
            - Before=2021-12-25T14:33:47.789+08:00
这个路由规则会在东8区的2021-12-25 14:33:47前，将请求都转跳到www.ghostcloud.cn。
#### 9.5.2.3 Between Route Predicate Factory
Between Route Predicate Factory也是使用两个时间作为匹配规则，只要当前时间大于第一个设定时间，并小于第二个设定时间，路由才会匹配请求。配置参考如下：
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: between_route
          **uri**: [http://www.ghostcloud.cn](http://www.ghostcloud.cn)
          **predicates**:
            - Between=2021-12-25T14:33:47.789+08:00, 2021-12-26T14:33:47.789+08:00
这个路由规则会在东8区的2021-12-25 14:33:47到2021-12-26 14:33:47之间，将请求都转跳到ghostcloud。
#### 9.5.2.4 Cookie Route Predicate Factory
Cookie Route Predicate Factory使用的是cookie名字和正则表达式的value作为两个输入参数，请求的cookie需要匹配cookie名和符合其中value的正则。参考配置如下：
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: cookie_route
          **uri**: [http://www.ghostcloud.cn](http://www.ghostcloud.cn)
          **predicates**:
            - Cookie=cookiename, cookievalue
路由匹配请求存在cookie名为cookiename，cookie内容匹配cookievalue的，将请求转发到ghostcloud。
#### 9.5.2.5 Header Route Predicate Factory
Header Route Predicate Factory，与Cookie Route Predicate Factory类似，也是两个参数，一个header的name，一个是正则匹配的value。参考配置如下：
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: header_route
          **uri**: [http://www.ghostcloud.cn](http://www.ghostcloud.cn)
          **predicates**:
            - Header=X-Request-Id, \d+
路由匹配存在名为X-Request-Id，内容为数字的header的请求，将请求转发到ghostcloud。
#### 9.5.2.6 Host Route Predicate Factory
Host Route Predicate Factory使用的是host的列表作为参数，host使用Ant style匹配。参考配置如下：
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: host_route
          **uri**: [http://www.ghostcloud.cn](http://www.ghostcloud.cn)
          **predicates**:
            - Host=**.somehost.org,**.anotherhost.org
路由会匹配Host诸如：www.somehost.org 或 beta.somehost.org或www.anotherhost.org等请求。
#### 9.5.2.7 Method Route Predicate Factory
Method Route Predicate Factory是通过HTTP的method来匹配路由。参考配置如下：
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: method_route
          **uri**: [http://www.ghostcloud.cn](http://www.ghostcloud.cn)
          **predicates**:
            - Method=GET
路由会匹配到所有GET方法的请求。
#### 9.5.2.8 Path Route Predicate Factory
Path Route Predicate Factory使用的是path列表作为参数，使用Spring的PathMatcher匹配path，可以设置可选变量。参考配置如下：
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: host_route
          **uri**: [http://www.ghostcloud.cn](http://www.ghostcloud.cn)
          **predicates**:
            - Path=/foo/{segment},/bar/{segment}
上面路由可以匹配诸如：/foo/1 或 /foo/bar 或 /bar/baz等,其中的segment变量可以通过下面方式获取：
PathMatchInfo variables = exchange.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
Map<String, String> uriVariables = variables.getUriVariables();
String segment = uriVariables.get(**"segment"**);
在后续的GatewayFilter Factories就可以做对应的操作了。
#### 9.5.2.9 Query Route Predicate Factory
Query Route Predicate Factory可以通过一个或两个参数来匹配路由，一个是查询的name，一个是查询的正则value。参考配置如下：
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: query_route
          **uri**: [http://www.ghostcloud.cn](http://www.ghostcloud.cn)
          **predicates**:
            - Query=username
路由会匹配所有包含username查询参数的请求。查询参数也可使用正则表达式，比如 “user.“，此时凡包含请求参数 usern、userna、username等的请求都可以匹配。
#### 9.5.2.10 RemoteAddr Route Predicate Factory
RemoteAddr Route Predicate Factory通过无类别域间路由(IPv4 or IPv6)列表匹配路由。参考配置如下：
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: remoteaddr_route
          **uri**: [http://www.ghostcloud.cn](http://www.ghostcloud.cn)
          **predicates**:
            - RemoteAddr=192.168.1.1/24
上面路由就会匹配RemoteAddr（客户端地址）诸如192.168.1.10等请求。
RemoteAddr Route Predicate Factory默认情况下，使用的是请求的remote address。但是如果Spring Cloud Gateway是部署在其他的代理后面的，如Nginx，则Spring Cloud Gateway获取请求的remote address是其他代理的ip，而不是真实客户端的ip。
考虑到这种情况，可以自定义获取remote address的处理器RemoteAddressResolver。当然Spring Cloud Gateway也提供了基于X-Forwarded-For请求头的XForwardedRemoteAddressResolver。
XForwardedRemoteAddressResolver提供了两个静态方法获取它的实例： XForwardedRemoteAddressResolver::trustAll得到的RemoteAddressResolver总是获取X-Forwarded-For的第一个ip地址作为remote address，这种方式就比较容易被伪装的请求欺骗，模拟请求很容易通过设置初始的X-Forwarded-For头信息，就可以欺骗到gateway。
XForwardedRemoteAddressResolver::maxTrustedIndex得到的RemoteAddressResolver则会在X-Forwarded-For信息里面，从右到左选择信任最多maxTrustedIndex个ip，因为X-Forwarded-For是越往右是越接近gateway的代理机器ip，所以是越往右的ip，信任度是越高的。 那么如果前面只是挡了一层Nginx的话，如果只需要Nginx前面客户端的ip，则maxTrustedIndex取1，就可以比较安全地获取真实客户端ip。
### 9.5.3 动态路由
SpringCloud支持动态路由，即自动从注册中心获取服务列表并访问。具体操作步骤如下：

1. 添加注册中心依赖

在gateway-server模块的pom文件中添加注册中心依赖：
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-eureka-client</**artifactId**>
</**dependency**>

1. 修改配置文件

修改 application.yml 配置文件，添加eureka注册中心的相关配置，并修改访问映射的URL为服务名称：
**server**:
  **port**: 8080**spring**:
  **application**:
    **name**: gateway-server
  **cloud**:
    **gateway**:
      **routes**:
      - **id**: product-service
        **uri**: lb://product-service
        **predicates**:
        - Path=/product/**
      - **id**: order-service
        **uri**: lb://order-service
        **predicates**:
        - Path=/order/**
**eureka**:
  **client**:
    **service-url**:
      **defaultZone**: [http://127.0.0.1:8761/eureka/](http://127.0.0.1:8761/eureka/)
      **registry-fetch-interval-seconds**: 5 _# 获取服务列表的周期：5s
  _**instance**:
    **prefer-ip-address**: **true
    ip-address**: ${**spring.cloud.client.ip-address**}

- uri: uri以 lb: //开头（lb代表从注册中心获取服务），后面接的就是需要转发到的服务名称。
### 9.5.4 根据服务名称自动转发
如果我们有很多微服务需要通过网关进行转发，使用11.5.3小节介绍的配置方式会显得很麻烦，需要配置的路由规则非常多，为解决此问题，gateway网关提供了根据微服务名称自动转发功能，网关会从注册中心获取所有的服务列表，当请求发送到网关后，网关自动完成转发工作。
根据服务名称自动转发需要将配置改为如下方式：
**spring**:
  **application**:
    **name**: gateway-server
  **cloud**:
    **gateway**:
      **discovery**: _#配置自动根据服务名称转发
        _**locator**:
          **enabled**: **true **_#开启根据服务名称自动转发
          _**lower-case-service-id**: **true **_#微服务名称以小写形式呈现_
重启网关服务，在浏览器请求 [http://localhost:8080/product-service/product/1](http://localhost:8080/product-service/product/1) 查看效果如下图所示：
![](media/1648521305650-0a40c251-3bb9-4400-b308-28d2a87f5e48.png)
上图所示的访问路径添加上了服务名称。
### 9.5.5 重写转发路径
在SpringCloud Gateway中，路由转发是直接将匹配的路由path直接拼接到映射路径（URI）之后，那么在微服务开发中往往没有那么便利。这里就可以通过RewritePath机制来进行路径重写。

1. 案例改造

修改gateway-server的application.yml文件，将匹配路径改为/service-product/** （或者其他任意前缀，用于模拟日常开发中带了各种前缀的访问方式）。
重新启动网关，我们在浏览器访问[http://127.0.0.1:8080/service-product/product/1](http://127.0.0.1:8080/service-product/product/1) ，会抛出404。这是由于路由转发规则默认转发到商品微服务（ [http://127.0.0.1:8081/service-product/product/1](http://127.0.0.1:8081/service-product/product/1) ）路径上，而商品微服务又没有 service-product 对应的映射配置。

1. 解决方法

添加RewritePath重写转发路径，修改gateway-server的application.yml，添加重写规则。
**server**:
  **port**: 8080**spring**:
  **application**:
    **name**: gateway-server
  **cloud**:
    **gateway**:
      **routes**:
      - **id**: product-service
        **uri**: lb://product-service
        **predicates**:
        - Path=/service-product/**
        **filters**:
        - RewritePath=/service-product/(?<segment>.*),/$\{segment}
通过RewritePath配置重写转发的url，将/ service- product/(?<segment>.*)，重写为{segment}，然后转发到订单微服务。比如在网页上请求http://localhost:8080/ service- product/product/1，此时会将请求转发到[http://127.0.0.1:8081/product/1](http://127.0.0.1:8081/product/1)（ **值得注意的是在yml文档中此处 $ 要写成 $\ **）
## 9.6 Gateway过滤器
Spring Cloud Gateway除了具备请求路由功能之外，也支持对请求的过滤。通过过滤器可以修改HTTP请求的输入和输出，以达到在请求被路由前后完成某些特殊操作，比如身份验证、指标统计等。
### 9.6.1 过滤器概述

1. 过滤器的生命周期

Spring Cloud Gateway 的 Filter 的生命周期只有两个：“pre”和“post”。

- “pre”: 这种过滤器在请求被路由之前调用。我们可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等。
- “post”: 这种过滤器在路由到微服务以后执行。这种过滤器可用来为响应添加标准的 HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。

![](media/1648521306626-b9ff0224-d4c1-45a3-9e10-8d11c0ea1608.png)

1. 过滤器类型

Spring Cloud Gateway 的 Filter 从作用范围可分为另外两种GatewayFilter 与 GlobalFilter。

- GatewayFilter: 应用到单个路由或者一个分组的路由上。
- GlobalFilter: 应用到所有的路由上。
### 9.6.2 GatewayFilter
GatewayFilter，局部过滤器，是针对单个路由的过滤器。可以对访问的URL过滤，进行切面处理。在Spring Cloud Gateway中通过GatewayFilter的形式内置了很多不同类型的局部过滤器。这里简单将Spring Cloud Gateway内置的所有过滤器工厂整理成了一张表格，虽然不是很详细，但能作为速览使用。如下：

| **过滤器** | **作用** | **参数** |
| --- | --- | --- |
| AddRequestHeader | 为原始请求添加Header | Header的名称及值  |
| AddRequestParameter | 为原始请求添加请求参数 | 参数名称及值  |
| AddResponseHeader | 为原始响应添加Header | Header的名称及值  |
| DedupeResponseHeader | 剔除响应头中重复的值  | 需要去重的Header名 称及去重策略  |
| Hystrix | 为路由引入Hystrix的断路器保护 | HystrixCommand的名 称  |
| FallbackHeaders | 为fallbackUri的请求头中添加具 体的异常信息  | Header的名称  |
| PrefixPath | 为原始请求路径添加前缀  | 前缀路径 |
| PreserveHostHeader | 为请求添加一个 preserveHostHeader=true的属 性，路由过滤器会检查该属性以 决定是否要发送原始的Host | 无 |
| RequestRateLimiter | 用于对请求限流，限流算法为令 牌桶 | keyResolver、 rateLimiter、 statusCode、 denyEmptyKey、 emptyKeyStatus |
| RedirectTo | 将原始请求重定向到指定的URL  | http状态码及重定向的 url  |
| RemoveHopByHopHeadersFilter | 为原始请求删除IETF组织规定的 一系列Header | 默认就会启用，可以通 过配置指定仅删除哪些 Header |
| RemoveRequestHeader | 为原始请求删除某个Header | Header名称 |
| RemoveResponseHeader | 为原始响应删除某个Header | Header名称 |
| RewritePath | 重写原始的请求路径 | 原始路径正则表达式以 及重写后路径的正则表 达式 |
| RewriteResponseHeader | 重写原始响应中的某个Header  | Header名称，值的正 则表达式，重写后的值  |
| SaveSession | 在转发请求之前，强制执行 WebSession::save操作  | 无  |
| secureHeaders | 为原始响应添加一系列起安全作 用的响应头  | 无，支持修改这些安全响应头的值  |
| SetPath | 修改原始的请求路径 | 修改后的路径  |
| SetResponseHeader | 修改原始响应中某个Header的值 | Header名称，修改后的值  |
| SetStatus | 修改原始响应的状态码 | HTTP 状态码，可以是 数字，也可以是字符串  |
| StripPrefix | 用于截断原始请求的路径 | 使用数字表示要截断的 路径的数量 |
| Retry | 针对不同的响应进行重试  | retries、statuses、 methods、series |
| RequestSize | 设置允许接收最大请求包的大 小。如果请求包大小超过设置的 值，则返回 413 Payload Too Large | 请求包大小，单位为字 节，默认值为5M  |
| ModifyRequestBody | 在转发请求之前修改原始请求体 内容  | 修改后的请求体内容 |
| ModifyResponseBody | 修改原始响应体的内容  | 修改后的响应体内容 |

每个过滤器工厂都对应一个实现类，并且这些类的名称必须以 GatewayFilterFactory 结尾，这是 Spring Cloud Gateway的一个约定，例如 AddRequestHeader 对应的实现类为 AddRequestHeaderGatewayFilterFactory 。对于这些过滤器的使用方式可以参考官方文档。
### 9.6.3 GlobalFilter
GlobalFilter，全局过滤器，作用于所有路由，Spring Cloud Gateway 定义了Global Filter接口，用户可以自定义实现自己的Global Filter。通过全局过滤器可以实现对权限的统一校验，安全性验证等功能，并且全局过滤器也是程序员使用比较多的过滤器。
Spring Cloud Gateway内部也是通过一系列的内置全局过滤器对整个路由转发进行处理如下：
![](media/1648521307757-da85d738-d750-4417-9ae8-215ced039939.png)
#### 9.6.3.1 自定义全局过滤器
自定义全局过滤器需要实现GlobalFilter和Ordered接口，并且类上添加@Component注解，参考代码如下：
@Component**public class **TestCustomGlobalFilter **implements **GlobalFilter, Ordered {
    _/**
     * 执行过滤器中的业务逻辑
     */
    _@Override
    **public **Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.**_out_**.println(**"执行自定义的全局过滤器：TestCustomGlobalFilter"**);
        _//继续向下执行
        _**return **chain.filter(exchange);
    }

    _/**
     * 指定过滤器的执行顺序，返回值越小，执行优先级越高
     */
    _@Override
    **public int **getOrder() {
        **return **0;
    }
}
filter方法里面编写需要自定义的业务逻辑，getOrder方法定义过滤器的执行顺序，返回值越小，执行优先级越高。
从浏览器访问商品或订单服务，测试效果如下图所示：
![](media/1648521312630-e62b38d3-017e-4280-b836-4efb760035ea.png)
## 9.7 统一鉴权
可以通过自定义过滤器来完成统一的权限校验。
### 9.7.1 鉴权逻辑
开发中的鉴权逻辑：

- 当客户端第一次请求服务时，服务端对用户进行信息认证（登录）。
- 认证通过，将用户信息进行加密形成token，返回给客户端，作为登录凭证。
- 以后每次请求，客户端都携带认证的token。
- 服务端对token进行解密，判断是否有效。

![](media/1648521313219-107b1796-05ef-4c87-8844-48b0f27f5691.png)
如上图，对于验证用户是否已经登录鉴权的过程可以在网关层统一检验。检验的标准就是请求中是否携带token凭证以及token的正确性。
### 9.7.2 代码实现
下面的我们自定义一个GlobalFilter，去校验所有请求的请求参数中是否包含“access_token“，如何不包含请求参数”access_token“则不转发路由，否则执行正常的逻辑。
@Component**public class **LoginFilter **implements **GlobalFilter, Ordered {
    @Override
    **public **Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        _//上下文中获取请求参数
        _String access_token = exchange.getRequest().getQueryParams().getFirst(**"access_token"**);
        _//如果access_token为,则认证失败,结束转发
        _**if **(StringUtils._isEmpty_(access_token)) {
            System.**_out_**.println(**"access_token is empty..."**);
            _//设置响应状态码为认证失败
            _exchange.getResponse().setStatusCode(HttpStatus.**_UNAUTHORIZED_**);
            _//本次请求结束
            _**return **exchange.getResponse().setComplete();
        }
        **return **chain.filter(exchange);
    }

    @Override
    **public int **getOrder() {
        **return **-2;
    }
}
ServerWebExchange对象中包含请求与响应的上下文，可以从中获取请求参数以及设置响应信息。
## 9.8 网关限流
### 9.8.1 常见的限流算法
#### 9.8.1.1 固定窗口算法
固定窗口算法是最简单的一种限流实现方式，其本质是通过维护一个单位时间内的计数器，每次请求 计数器加1，当单位时间内计数器累加到大于设定的阈值，则之后的请求都被拒绝，直到单位时间已经过去，再将计数器重置为零。
比如我们规定，对于A接口来说，我们1分钟的访问次数不能超过100个。那么我们可以这么做：在一开始的时候，我们可以设置一个计数器counter，每当一个请求过来的时候，counter就加1，如果counter的值大于100并且该请求与第一个请求的间隔时间还在1分钟之内，那么说明请求数过多；如果该请求与第一个请求的间隔时间大于1分钟，且counter的值还在限流范围内，那么就重置 counter，具体算法的示意图如下：
![](media/1648521313633-2e22a472-be52-47c9-8147-26b94f119127.png)
这个算法虽然简单，但是有一个十分致命的问题，那就是临界问题，我们看下图：
![](media/1648521314087-df0423dc-7cd3-4e23-bb61-ff906d3ecd4d.png)
从上图中我们可以看到，假设有一个恶意用户，他在0:59时，瞬间发送了100个请求，并且1:00又瞬间发送了100个请求，那么其实这个用户在 1秒里面，瞬间发送了200个请求。我们刚才规定的是1分钟最多100个请求，也就是每秒钟最多1.7个请求，用户通过在时间窗口的重置节点处突发请求，可以瞬间超过我们的速率限制。用户有可能通过算法的这个漏洞，瞬间压垮我们的应用。
#### 9.8.1.2 滑动窗口算法
为了解决固定窗口算法的临界问题，可以将时间窗口划分成更小的时间窗口，然后随着时间的滑动删除相应的小窗口，而不是直接滑过一个大窗口，这就是滑动窗口算法。我们为每个小时间窗口都设置一个计数器，大时间窗口的总请求次数就是每个小时间窗口的计数器的和。如下图所示，我们的时间窗口是 5 秒，可以按秒进行划分，将其划分成 5 个小窗口，时间每过一秒，时间窗口就滑过一秒：
![](media/1648521315171-0cf158ae-81e8-423b-ab8c-e02ed77fa8eb.png)
每次处理请求时，都需要计算所有小时间窗口的计数器的和，考虑到性能问题，划分的小时间窗口不宜过多，譬如限流条件是每小时 N 个，可以按分钟划分为 60 个窗口，而不是按秒划分成 3600 个。当然如果不考虑性能问题，划分粒度越细，限流效果就越平滑。相反，如果划分粒度越粗，限流效果就越不精确，出现临界问题的可能性也就越大，当划分粒度为 1 时，滑动窗口算法就退化成了固定窗口算法。由于这两种算法都使用了计数器，所以也被称为** 计数器算法**（Counters）。
#### 9.8.1.3 漏桶算法
除了计数器算法，另一个很自然的限流思路是将所有的请求缓存到一个队列中，然后按某个固定的速度慢慢处理，这其实就是**漏桶算法**（Leaky Bucket）。漏桶算法假设将请求装到一个桶中，桶的容量为 M，当桶满时，请求被丢弃。在桶的底部有一个洞，桶中的请求像水一样按固定的速度（每秒 r 个）漏出来。我们用下面这个形象的图来表示漏桶算法：
![](media/1648521316022-24f8763b-084c-4b74-80d9-09ead3c39ebf.png)
漏桶算法可以通过一个队列来实现，如下图所示：
![](media/1648521316765-1c32c9c6-0d80-4d5d-87fb-3587a77bfabb.png)
当请求到达时，不直接处理请求，而是将其放入一个队列，然后另一个线程以固定的速率从队列中读取请求并处理，从而达到限流的目的。注意的是这个队列可以有不同的实现方式，比如设置请求的存活时间，或将队列改造成 PriorityQueue，根据请求的优先级排序而不是先进先出。当然队列也有满的时候，如果队列已经满了，那么请求只能被丢弃了。漏桶算法有一个缺陷，在处理突发流量时效率很低。
#### 9.8.1.4 令牌桶算法
令牌桶算法（Token Bucket）是目前应用最广泛的一种限流算法，它的基本思想由两部分组成：生成令牌 和 消费令牌。

- 生成令牌：假设有一个装令牌的桶，最多能装 M 个，然后按某个固定的速度（每秒 r 个）往桶中放入令牌，桶满时不再放入；
- 消费令牌：我们的每次请求都需要从桶中拿一个令牌才能放行，当桶中没有令牌时即触发限流，这时可以将请求放入一个缓冲队列中排队等待，或者直接拒绝。

令牌桶算法的图示如下：
![](media/1648521317170-68779af2-be92-4eea-9c81-fb98d74da002.png)
在上面的图中，我们将请求放在一个缓冲队列中，可以看出这一部分的逻辑和漏桶算法几乎一模一样，只不过在处理请求上，一个是以固定速率处理，一个是从桶中获取令牌后才处理。
仔细思考就会发现，令牌桶算法有一个很关键的问题，就是桶大小的设置，正是这个参数可以让令牌桶算法具备处理突发流量的能力。譬如将桶大小设置为 100，生成令牌的速度设置为每秒 10 个，那么在系统空闲一段时间的之后（桶中令牌一直没有消费，慢慢的会被装满），突然来了 50 个请求，这时系统可以直接按每秒 50 个的速度处理，随着桶中的令牌很快用完，处理速度又会慢慢降下来，和生成令牌速度趋于一致。这是令牌桶算法和漏桶算法最大的区别，漏桶算法无论来了多少请求，只会一直以每秒 10 个的速度进行处理。当然，处理突发流量虽然提高了系统性能，但也给系统带来了一定的压力，如果桶大小设置不合理，突发的大流量可能会直接压垮系统。
### 9.8.2 基于Filter的限流
SpringCloudGateway官方就提供了基于令牌桶的限流支持。基于其内置的过滤器工厂 RequestRateLimiterGatewayFilterFactory 实现。在过滤器工厂中是通过Redis和lua脚本结合的方式进行流量控制。
#### 9.8.2.1 环境配置
准备redis环境，安装redis，步骤省略。
新建gateway-redis-limit模块，在模块中引入gateway的起步依赖和redis的reactive依赖，配置如下：
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-gateway</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-eureka-client</**artifactId**>
</**dependency**>
_<!--健康检查监控端点-->_<**dependency**>
    <**groupId**>org.springframework.boot</**groupId**>
    <**artifactId**>spring-boot-starter-actuator</**artifactId**>
</**dependency**>_<!--Redis异步操作客户端-->_<**dependency**>
    <**groupId**>org.springframework.boot</**groupId**>
    <**artifactId**>spring-boot-starter-data-redis-reactive</**artifactId**>
</**dependency**>
引入Spring-boot-starter-data-redis-reactive，并完成redis配置，应用启动后Spring会自动生成ReactiveRedisTemplate，ReactiveRedisTemplate与RedisTemplate使用类似，但它提供的是异步的，响应式Redis交互方式。这里强调一下，响应式编程是异步的，ReactiveRedisTemplate发送Redis请求后不会阻塞线程，当前线程可以去执行其他任务。等到Redis响应数据返回后，ReactiveRedisTemplate再调度线程处理响应数据。响应式编程可以通过优雅的方式实现异步调用以及处理异步结果，正是它的最大的意义。
在本示例程序中Redis用于存放限流生成的数据，比如请求path当前的访问请求数、令牌数、最大容量等。
#### 9.8.2.2 修改配置文件
在application.yml配置文件中加入限流的配置，参考配置如下：
**server**:
  **port**: 8080**spring**:
  **application**:
    **name**: gateway-redis-limit
  **redis**:
    **host**: 192.168.10.204
    **port**: 6379
    **password**: 123456
  **cloud**:
    **gateway**:
      **routes**:
      - **id**: redis-limit
        **uri**: lb://product-service
        **order**: -1 _#如果需要routes与discovery两种配置方式同时使用,需要将该值设置为小于0的值,避免被discovery方式覆盖掉
        _**predicates**:
        - Path=/product/**
        **filters**:
        - **name**: RequestRateLimiter
          **args**:
            **key-resolver**: **'#{@pathKeyResolver}' **_#使用SpEL从容器中获取对象
            _**redis-rate-limiter**:
              **replenishRate**: 1 _# 令牌桶每秒填充平均速率
              _**burstCapacity**: 3 _# 令牌桶的上限
      _**discovery**:
        **locator**:
          **enabled**: **true
          lower-case-service-id**: **true
eureka**:
  **client**:
    **service-url**:
      **defaultZone**: [http://127.0.0.1:8761/eureka/](http://127.0.0.1:8761/eureka/)
      **registry-fetch-interval-seconds**: 5 _# 获取服务列表的周期：5s
  _**instance**:
    **prefer-ip-address**: **true
    ip-address**: ${**spring.cloud.client.ip-address**}
此处采用spring.cloud.gateway.routes 与 spring.cloud.gateway.discovery同时使用的方式进行配置，需要将**.routes.order配置为小于0的值，避免被discovery配置覆盖掉。
#### 9.8.2.3 实现KeyResolver接口
新建一个配置类KeyResolverConfiguration，里面实现key解析器的代码，注意方法面名需要和yml配置文件中的key-resolver的值相对应，参考代码如下：
_/**
 * 基于请求路径的限流
 * _**_@return
 _**_*/_@Bean**public **KeyResolver pathKeyResolver() {
    **return new **KeyResolver() {
        @Override
        **public **Mono<String> resolve(ServerWebExchange exchange) {
            **return **Mono._just_(exchange.getRequest().getPath().toString());
        }
    };
}
还可以配置基于请求参数的限流，参考代码如下：
_/**
 * 基于请求参数的限流
 */_@Bean**public **KeyResolver paramKeyResolver() {
    **return **exchange -> Mono._just_(exchange.getRequest().getQueryParams().getFirst(**"username"**));
}
**注意keyresolver的配置同时只能存在一个，否则会报错。**
#### 9.8.2.4 测试检验
通过浏览器访问[http://localhost:8080/product/1](http://localhost:8080/product/1)，通过快速刷新（快速按F5）的方式可以模拟高频请求，发挥一开始可以正常访问，但是当访问速度和次数提高之后会被限流，页面返回429错误码，参考下图。
![](media/1648521317883-941f13b1-cf86-4ec3-baa5-cb39e9e093b3.png)
同时通过redis命令行客户端可查看监控如下图所示：
![](media/1648521318441-4ef8558c-ed52-4dcb-81c2-e6106cad6e55.png)
通过reids的MONITOR可以监听redis的执行过程。具体操作:
1、输入：redis-cli -h 127.0.0.1 -p 6379 -a password
2、输入：monitor
大括号中就是我们的限流Key,这边是IP，此处因为经过虚拟网卡转发，所以显示192.168.10.1。 

- timestamp:存储的是当前时间的秒数，也就是System.currentTimeMillis() / 1000或者 Instant.now().getEpochSecond() 
- tokens:存储的是当前这秒钟的对应的可用的令牌数量 

Spring Cloud Gateway目前提供的限流还是相对比较简单的，在实际中我们的限流策略会有很多种情 况，比如：

- 对不同接口的限流 
- 被限流后的友好提示 

这些可以通过自定义RedisRateLimiter来实现自己的限流策略，这里我们不做讨论。
### 9.8.3 整合Hystrix熔断器
#### 9.8.3.1 新建模块引入依赖
创建gateway-hystrix模块，并添加如下依赖：
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-gateway</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-eureka-client</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-hystrix</**artifactId**>
</**dependency**>
#### 9.8.3.2 配置application.yml
**server**:
  **port**: 8080**spring**:
  **application**:
    **name**: gateway-hystrix
  **cloud**:
    **gateway**:
      **routes**:
      - **id**: redis-limit
        **uri**: lb://product-service
        **order**: -1 _#如果需要routes与discovery两种配置方式同时使用,需要将该值设置为小于0的值,避免被discovery方式覆盖掉
        _**predicates**:
        - Path=/product/**
        **filters**:
        - **name**: Hystrix _# 过滤器名称
          _**args**:
            **name**: fallback _#参数名称
            _**fallbackUri**: forward:/fallback
      **discovery**:
        **locator**:
          **enabled**: **true
          lower-case-service-id**: **true
hystrix**:
  **command**:
    **fallback**: _# 此处的名字需与上面Hystrix过滤器参数名称一致
      _**execution**:
        **isolation**:
          **thread**:
            **timeoutInMilliseconds**: 3000**eureka**:
  **client**:
    **service-url**:
      **defaultZone**: [http://127.0.0.1:8761/eureka/](http://127.0.0.1:8761/eureka/)
      **registry-fetch-interval-seconds**: 5 _# 获取服务列表的周期：5s
  _**instance**:
    **prefer-ip-address**: **true
    ip-address**: ${**spring.cloud.client.ip-address**}
本示例中只针对product-service的请求转发做了熔断配置，Hystrix过滤器源码在HystrixGatewayFilterFactory里面，为了更好的对演示效果进行展示，最好在product-service对应的查询接口中添加Thread.sleep(2000)，模拟网络延迟，并对熔断时间进行设置，即配置:
hystrix.command.fallback.execution.isolation.thread.timeoutInMilliseconds: 3000
**注意红色的“fallback”需要与过滤器中的args.name 值相对应**。
_假如我们在另一个应用程序中定义有默认的降级返回接口，也可在配置文件中定义fallbackUri转发到其他路径，参考配置如下：_
**spring**:
  **cloud**:
    **gateway**:
      **routes**:
        - **id**: product-service
          **uri**: lb://product-service
          **predicates**:
            - Path=//product/**
          **filters**:
            - **name**: Hystrix
              **args**:
                **name**: fallbackcmd
                **fallbackUri**: forward:/fallback
        - **id**: ingredients-fallback
          **uri**: http://localhost:9994
          **predicates**:
            - Path=/fallback

#### 9.8.3.3 创建fallback Controller
新建一个用于熔断之后的降级返回，参考代码如下：
@RestController**public class **DefaultFallbackController {
    @RequestMapping(value = **"/fallback"**, method = RequestMethod.**_GET_**)
    **public **String fallback() {
        System.**_out_**.println(**"====Gateway 网关启用了Hystrix 熔断器===="**);
        **return ****"被熔断了"**;
    }
}
#### 9.8.3.4 测试检验
通过浏览器访问 [http://localhost:8080/product/1](http://localhost:8080/product/1) ，通过F5快速刷新即可触发熔断效果，参考下图。
![](media/1648521318976-55440e62-43dd-47a5-a264-65d1f3947b30.png)
可以看到idea的控制台打印如下信息：
![](media/1648521320005-76d27056-d7cd-4f4e-b81a-53642b2385d9.png)
## 9.9 网关高可用
高可用HA（High Availability）是分布式系统架构设计中必须考虑的因素之一，它通常是指，通过设计减少系统不能提供服务的时间。我们都知道，单点是系统高可用的大敌，单点往往是系统高可用最大的风险和敌人，应该尽量在系统设计的过程中避免单点。方法论上，高可用保证的原则是“集群化”，或者叫“冗余”：只有一个单点，挂了服务会受影响；如果有冗余备份，挂了还有其他backup能够顶上。
![](media/1648521320348-80b34721-8907-4145-8a83-a1d272baa9d2.png)
我们实际使用 Spring Cloud Gateway 的方式如上图，不同的客户端使用不同的负载将请求分发到后端的 Gateway，Gateway 再通过HTTP调用后端服务，最后对外输出。因此为了保证 Gateway 的高可用性，前端可以同时启动多个 Gateway 实例进行负载，在 Gateway 的前端使用 Nginx 或者 F5 进行负载 转发以达到高可用性。
### 9.9.1 准备多个Gateway工程
为了演示方便，此处案例采用idea启动多个Gateway工程进行演示，参考配置如下：

1. pom依赖

<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-gateway</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-eureka-client</**artifactId**>
</**dependency**>

1. application.yml配置

**spring**:
  **application**:
    **name**: gateway-ha
  **cloud**:
    **gateway**:
      **discovery**: _#配置自动根据服务名称转发
        _**locator**:
          **enabled**: **true **_#开启根据服务名称自动转发
          _**lower-case-service-id**: **true **_#微服务名称以小写形式呈现_**eureka**:
  **client**:
    **service-url**:
      **defaultZone**: [http://127.0.0.1:8761/eureka/](http://127.0.0.1:8761/eureka/)
      **registry-fetch-interval-seconds**: 5 _# 获取服务列表的周期：5s
  _**instance**:
    **prefer-ip-address**: **true
    ip-address**: ${**spring.cloud.client.ip-address**}
---**spring**:
  **profiles**: gateway-ha1**server**:
  **port**: 9090
---**spring**:
  **profiles**: gateway-ha2**server**:
  **port**: 9091
### 9.9.2 配置Nginx
在Nginx配置文件中添加负载均衡配置，参考配置如下：
#配置多台服务器（这里只在一台服务器上的不同端口）
upstream gateway {
    server 127.0.0.1:9091;
    server 127.0.0.1:9090;
}
**上面的配置添加到server{}外面，下面这段配置需要写到server{}里面**
#请求转向定义的服务器列表,访问[http://127.0.0.1/](http://127.0.0.1/) 就会跳转到上面配置的服务器列表
location / {
    proxy_pass http://gateway;
}
在浏览器上通过访问[http://localhost/product-service/product/](http://localhost/product-service/product/) 请求的效果和之前是一样的。这次关闭一台网关服务器，还是可以支持部分请求的访问。
## 9.10 执行流程分析
Spring Cloud Gateway的核心处理流程主要分为如下几个步骤，流程图如下所示：
![](media/1648521321295-1ac74e40-9e54-45f5-9d71-a62a7b9f12ea.png)

- **ReactorHttpHandlerAdapter**：入口HttpServerRequest和HttpServerResponse转换，分别转换为ServerHttpRequest和ServerHttpResponse；
- **HttpWebHandlerAdapter**：构建组装网关请求的上下文；
- **DispatcherHandler**：所有请求的分发处理器，负责分发请求到对应的处理器；
- **RoutePredicateHandlerMapping**：路由断言处理映射器，用于路由查找，以及找到路由后返回对应的WebHandler，DispatcherHandler会依次遍历HandlerMapping进行处理；
- **FilteringWebHandler**：创建过滤器链，使用Filter链处理请求；

Spring Cloud Gateway 核心处理流程如上图所示，Gateway的客户端向 Spring Cloud Gateway 发送请求，请求首先进入ReactorHttpHandlerAdapter完成request与response的转换，然后被 HttpWebHandlerAdapter 进行提取组装成网关上下文，再然后网关的上下文会传递到 DispatcherHandler。DispatcherHandler 是所有请求的分发处理器， DispatcherHandler 主要负责分发请求对应的处理器。比如请求分发到对应的 RoutePredicateHandlerMapping （路由断言处理映射器）。路由断言处理映射器主要作用用于路由查找，以及找到路由后返回对应的 FilterWebHandler。FilterWebHandler 主要负责组装Filter链并调用Filter执行一系列的Filter处理，然后再把请求转到后端对应的代理服务处理，处理完毕之后将Response返回到Gateway客户端。
## 课后作业
1、动手完成使用Nginx作为网关的实践练习，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
2、动手完成Spring Cloud Gateway入门案例实践练习，并对Gateway提供的多种路由匹配规则进行一 一实践练习，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
3、动手完成Gateway自定义过滤器实现统一鉴权的实践练习，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
4、动手完成网关限流（Filter和Hystrix）实践练习，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
5、动手完成网关高可用Nginx+Gateway方式实践练习，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 10 微服务链路追踪
## 10.1 微服务架构下的问题
在大型系统的微服务化构建中，一个系统会被拆分成许多模块。这些模块负责不同的功能，组合成系统，最终可以提供丰富的功能。在这种架构中，一次请求往往需要涉及到多个服务。互联网应用构建在不同的软件模块集上，这些软件模块，有可能是由不同的团队开发、可能使用不同的编程语言来实现、有可能布在了几千台服务器，横跨多个不同的数据中心，也就意味着这种架构形式也会存在一些问题：

- 如何快速发现问题？ 
- 如何判断故障影响范围？ 
- 如何梳理服务依赖以及依赖的合理性？ 
- 如何分析链路性能问题以及实时容量规划？

微服务架构上通过业务来划分服务的，通过REST调用，对外暴露的一个接口，可能需要很多个服务协同才能完成这个接口功能，如果链路上任何一个服务出现问题或者网络超时，都会形成导致接口调用失败。随着业务的不断扩张，服务之间互相调用会越来越复杂。
![](media/1648521322169-da971222-b47e-4c7a-b958-2896ee887ea1.png)
随着服务的越来越多，对调用链的分析会越来越复杂。它们之间的调用关系也许如下：
![](media/1648521322934-bc7ba49c-6920-4c06-8b61-821b1968fd64.png)
分布式链路追踪（Distributed Tracing），就是将一次分布式请求还原成调用链路，进行日志记录，性能监控并将一次分布式请求的调用情况集中展示。比如各个服务节点上的耗时、请求具体到达哪台机器上、每个服务节点的请求状态等等。
目前业界比较流行的链路追踪系统如：Twitter的Zipkin，阿里的鹰眼，美团的Mtrace，大众点评的 cat等，大部分都是基于google发表的Dapper。Dapper阐述了分布式系统，特别是微服务架构中链路追踪的概念、数据表示、埋点、传递、收集、存储与展示等技术细节。
## 10.2 Sleuth概述
Spring Cloud Sleuth 主要功能就是在分布式系统中提供追踪解决方案，并且兼容支持了 zipkin，你只需要在pom文件中引入相应的依赖即可。
### 10.2.1 相关概念介绍
Spring Cloud Sleuth采用的是Google的开源项目Dapper的专业术语。

- Span：基本工作单元，例如，在一个新建的span中发送一个RPC等同于发送一个回应请求给RPC，span通过一个64位ID唯一标识，trace以另一个64位ID表示，span还有其他数据信息，比如摘要、时间戳事件、关键值注释(tags)、span的ID、以及进度ID(通常是IP地址) span在不断的启动和停止，同时记录了时间信息，当你创建了一个span，你必须在未来的某个时刻停止它。
- Trace：一系列spans组成的一个树状结构，例如，如果你正在跑一个分布式大数据工程，你可能需要创建一个trace。

![](media/1648521324102-1581ce2a-9f15-42d9-9d89-a8f152adc124.png)
_如上图所示，我们希望对整个登陆过程进行性能跟踪，那么对于每次执行__login()__的过程来说，就是我们_**_希望追踪的一个_**_trace__，而对于__dbAccess()__和__saveSession()__两个函数来说，则是一个trace中的两个span；而对于__dbAccess()__来说，则__saveObject()__函数则是其子span。然而对于上边的基本概念，我们描述是的_**_希望追踪的一个_**_trace__，为什么我们采用_**_希望追踪的_**_这样一个表述呢？这是因为，trace表述的是一次执行过程，是所有的span的集合。_

- Annotation：用来及时记录一个事件的存在，一些核心annotations用来定义一个请求的开始和结束。
- cs - Client Sent -客户端发起一个请求，这个annotion描述了这个span的开始
- sr - Server Received -服务端获得请求并准备开始处理它，如果将其sr减去cs时间戳便可得到网络延迟
- ss - Server Sent -注解表明请求处理的完成(当请求返回客户端)，如果ss减去sr时间戳便可得到服务端需要的处理请求时间
- cr - Client Received -表明span的结束，客户端成功接收到服务端的回复，如果cr减去cs时间戳便可得到客户端从服务端获取回复的所有所需时间。

将Span和Trace在一个系统中使用Zipkin注解的过程图形化：
![](media/1648521324743-88ed4ab3-6a1d-48fe-81ef-592ec22d26d7.png)
## 10.3 Sleuth入门
接下来通过之前的项目案例整合Sleuth，完成入门案例的编写：

1. 引入依赖

分别在gateway-server、order-service、product-service三个模块中添加以下依赖：
_<!--sleuth依赖-->_<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-sleuth</**artifactId**>
</**dependency**>

1. 修改配置文件

分别修改三个模块的application.yml文件，添加如下配置：
**logging**:
  **level**:
    **root**: INFO
    **org.springframework.web.servlet.DispatcherServlet**: DEBUG
    **org.springframework.cloud.sleuth**: DEBUG
启动微服务，调用之后，我们可以在控制台观察到sleuth的日志输出，如下图所示。
![](media/1648521325643-9f68064d-66e5-40da-9dbd-dee47688938f.png)
![](media/1648521327697-c6867456-1e53-47f0-b3e9-4b81dae00702.png)
![](media/1648521329375-4dafbafd-73d9-4ab5-9c07-21a8b1ce2ea9.png)
其中4111277e32c902d1是TraceId，后面跟着的是SpanId，依次调用有一个全局的TraceId，将调用链路串起来。仔细分析每个微服务的日志，不难看出请求的具体过程。
查看日志文件并不是一个很好的方法，当微服务越来越多日志文件也会越来越多，通过Zipkin可以将日志聚合，并进行可视化展示和全文检索。
## 10.4 Zipkin概述
Zipkin是Twitter的一个开源项目，它基于Google Dapper实现，它致力于收集服务的定时数据，以解决微服务架构中的延迟问题，包括数据的收集、存储、查找和展现。我们可以使用它来收集各个服务器上请求链路的跟踪数据，并通过它提供的REST API接口来辅助我们查询跟踪数据以实现对分布式系统的监控程序，从而及时地发现系统中出现的延迟升高问题并找出系统性能瓶颈的根源。除了面向开发的API接口之外，它也提供了方便的UI组件来帮助我们直观的搜索跟踪信息和分析请求链路明细，比如：可以查询某段时间内各用户请求的处理时间等。Zipkin提供了可插拔数据存储方式：InMemory、MySql、Cassandra以及Elasticsearch。
![](media/1648521329968-2a768a4e-63fb-451c-8292-a602c456e6ad.png)
上图展示了Zipkin的基础架构，它主要由4个核心组件构成：

- Collector：收集器组件，它主要用于处理从外部系统发送过来的跟踪信息，将这些信息转换为Zipkin内部处理的Span格式，以支持后续的存储、分析、展示等功能。
- Storage：存储组件，它主要对处理收集器接收到的跟踪信息，默认会将这些信息存储在内存中，我们也可以修改此存储策略，通过使用其他存储组件将跟踪信息存储到数据库中。
- RESTful API：API组件，它主要用来提供外部访问接口。比如给客户端展示跟踪信息，或是外接系统访问以实现监控等。
- Web UI：UI组件，基于API组件实现的上层应用。通过UI组件用户可以方便而有直观地查询和分析跟踪信息。

Zipkin分为两端，一个是Zipkin服务端，一个是Zipkin客户端，客户端也就是微服务的应用。客户端会配置服务端的URL地址，一旦发生服务间的调用的时候，会被配置在微服务里面的Sleuth的监听器监听，并生成相应的Trace 和Span信息发送给服务端。
发送的方式主要有两种，一种是HTTP报文的方式，还有一种是消息总线的方式如RabbitMQ。不论哪种方式，我们都需要：

- 一个Eureka服务注册中心，这里我们就用之前的eureka项目来当注册中心。
- 一个Zipkin服务端。
- 多个微服务，这些微服务中配置Zipkin客户端。
## 10.5 Zipkin Server的部署配置

1. 下载Zipkin Server

从Spring boot 2.0开始，官方就不再支持使用自建Zipkin Server的方式进行服务链路追踪，而是直接提供了编译好的jar包来给我们使用。可以从官方网站（[https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec](https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec) ）下载Zipkin的Web UI，我们这里下载的是 zipkin-server-2.12.9-exec.jar。

1. 启动Zipkin Server

在命令行输入java -jar zipkin-server-2.12.9-exec.jar启动Zipkin Server。
![](media/1648521330505-2a94ebef-94b9-4353-8f44-6fc3231a14eb.png)

- 默认Zipkin Server的请求端口为9411。
- Zipkin Server的启动参数可以通过官方提供的yml配置文件查找（[https://github.com/openzipkin/zipkin/blob/master/zipkin-server/src/main/resources/zipkin-server-shared.yml](https://github.com/openzipkin/zipkin/blob/master/zipkin-server/src/main/resources/zipkin-server-shared.yml) ）。
- 在浏览器输入[http://127.0.0.1:9411](http://127.0.0.1:9411) 即可进入到Zipkin Server的管理后台。

![](media/1648521330943-7749de44-8541-483a-9a38-e5a3c53686b2.png)
## 10.6 整合Sleuth&Zipkin
通过查看日志分析微服务的调用链路并不是一个很直观的方案，结合zipkin可以很直观地显示微服务之间的调用关系。

1. 客户端添加依赖

此处客户端泛指需要被追踪的微服务。
_<!--zipkin依赖-->_<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-zipkin</**artifactId**>
</**dependency**>

1. 修改客户端配置文件

在客户端配置文件的spring子项下添加如下配置：
**zipkin**:
  **base-url**: [http://127.0.0.1:9411/](http://127.0.0.1:9411/) _#zipkin server的请求地址
  _**sender**:
    **type**: _web __#请求方式,默认以http的方式向zipkin server发送追踪数据_**sleuth**:
  **sampler**:
    **probability**: 1.0 _#采样的百分比_
完整配置应该为spring.zipkin.base-url、spring.zipkin.sender.type、sleuth.sampler.probability。
指定了zipkin server的地址，下面制定需采样的百分比，默认为0.1，即10%，这里配置1，是记录全部 的sleuth信息，是为了收集到更多的数据（仅供测试用）。在分布式系统中，过于频繁的采样会影响系统性能，所以这里配置需要采用一个合适的值。

1. 测试检验

以此启动每个微服务，启动Zipkin Server。通过浏览器发送一次微服务请求。打开Zipkin Service控制台，我们可以根据条件追踪每次请求调用过程。
![](media/1648521332001-67fa3548-54cf-49d9-807a-c226da3b9509.png)
点击trace可以看到请求的细节，如下图所示：
![](media/1648521333616-7c1f4ecb-7b43-43b7-a264-04dfb53d5df0.png)
## 10.7 存储跟踪数据
Zipkin Server默认时间追踪数据信息保存到内存，这种方式不适合生产环境。因为一旦Service关闭重启或者服务崩溃，就会导致历史数据消失。Zipkin支持将追踪数据持久化到mysql数据库或者存储到 elasticsearch中。这里已mysql为例。
### 10.7.1 创建数据库表
可以从官网找到Zipkin Server持久mysql的数据库脚本。
/*
SQLyog Ultimate v11.33 (64 bit)
MySQL - 5.5.58 : Database - zipkin
*********************************************************************
*/
/*!40101 SET NAMES utf8 */**;**
/*!40101 SET SQL_MODE=''*/**;**
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */**;**
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */**;**
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */**;**
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */**;**
**CREATE** DATABASE /*!32312 IF NOT EXISTS*/`zipkin` /*!40100 DEFAULT CHARACTER SET utf8 */**;**
**USE** `zipkin`**;**
/*Table structure for table `zipkin_annotations` */
**DROP** **TABLE** **IF** **EXISTS** `zipkin_annotations`**;**
**CREATE** **TABLE** `zipkin_annotations` **(**
  `trace_id_high` **bigint****(**20**)** **NOT** **NULL** **DEFAULT** '0' **COMMENT** 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit'**,**
  `trace_id` **bigint****(**20**)** **NOT** **NULL** **COMMENT** 'coincides with zipkin_spans.trace_id'**,**
  `span_id` **bigint****(**20**)** **NOT** **NULL** **COMMENT** 'coincides with zipkin_spans.id'**,**
  `a_key` **varchar****(**255**)** **NOT** **NULL** **COMMENT** 'BinaryAnnotation.key or Annotation.value if type == -1'**,**
  `a_value` **blob** **COMMENT** 'BinaryAnnotation.value(), which must be smaller than 64KB'**,**
  `a_type` **int****(**11**)** **NOT** **NULL** **COMMENT** 'BinaryAnnotation.type() or -1 if Annotation'**,**
  `a_timestamp` **bigint****(**20**)** **DEFAULT** **NULL** **COMMENT** 'Used to implement TTL; Annotation.timestamp or zipkin_spans.timestamp'**,**
  `endpoint_ipv4` **int****(**11**)** **DEFAULT** **NULL** **COMMENT** 'Null when Binary/Annotation.endpoint is null'**,**
  `endpoint_ipv6` **binary****(**16**)** **DEFAULT** **NULL** **COMMENT** 'Null when Binary/Annotation.endpoint is null, or no IPv6 address'**,**
  `endpoint_port` **smallint****(**6**)** **DEFAULT** **NULL** **COMMENT** 'Null when Binary/Annotation.endpoint is null'**,**
  `endpoint_service_name` **varchar****(**255**)** **DEFAULT** **NULL** **COMMENT** 'Null when Binary/Annotation.endpoint is null'**,**
  **UNIQUE** **KEY** `trace_id_high` **(**`trace_id_high`**,**`trace_id`**,**`span_id`**,**`a_key`**,**`a_timestamp`**)** **COMMENT** 'Ignore insert on duplicate'**,**
  **KEY** `trace_id_high_2` **(**`trace_id_high`**,**`trace_id`**,**`span_id`**)** **COMMENT** 'for joining with zipkin_spans'**,**
  **KEY** `trace_id_high_3` **(**`trace_id_high`**,**`trace_id`**)** **COMMENT** 'for getTraces/ByIds'**,**
  **KEY** `endpoint_service_name` **(**`endpoint_service_name`**)** **COMMENT** 'for getTraces and getServiceNames'**,**
  **KEY** `a_type` **(**`a_type`**)** **COMMENT** 'for getTraces'**,**
  **KEY** `a_key` **(**`a_key`**)** **COMMENT** 'for getTraces'**,**
  **KEY** `trace_id` **(**`trace_id`**,**`span_id`**,**`a_key`**)** **COMMENT** 'for dependencies job'
**)** ENGINE**=**InnoDB **DEFAULT** CHARSET**=**utf8 ROW_FORMAT**=**COMPRESSED**;**
/*Data for the table `zipkin_annotations` */
/*Table structure for table `zipkin_dependencies` */
**DROP** **TABLE** **IF** **EXISTS** `zipkin_dependencies`**;**
**CREATE** **TABLE** `zipkin_dependencies` **(**
  `**day**` **date** **NOT** **NULL****,**
  `parent` **varchar****(**255**)** **NOT** **NULL****,**
  `child` **varchar****(**255**)** **NOT** **NULL****,**
  `call_count` **bigint****(**20**)** **DEFAULT** **NULL****,**
  **UNIQUE** **KEY** `**day**` **(**`**day**`**,**`parent`**,**`child`**)**
**)** ENGINE**=**InnoDB **DEFAULT** CHARSET**=**utf8 ROW_FORMAT**=**COMPRESSED**;**
/*Data for the table `zipkin_dependencies` */
/*Table structure for table `zipkin_spans` */
**DROP** **TABLE** **IF** **EXISTS** `zipkin_spans`**;**
**CREATE** **TABLE** `zipkin_spans` **(**
  `trace_id_high` **bigint****(**20**)** **NOT** **NULL** **DEFAULT** '0' **COMMENT** 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit'**,**
  `trace_id` **bigint****(**20**)** **NOT** **NULL****,**
  `id` **bigint****(**20**)** **NOT** **NULL****,**
  `name` **varchar****(**255**)** **NOT** **NULL****,**
  `parent_id` **bigint****(**20**)** **DEFAULT** **NULL****,**
  `debug` **bit****(**1**)** **DEFAULT** **NULL****,**
  `start_ts` **bigint****(**20**)** **DEFAULT** **NULL** **COMMENT** 'Span.timestamp(): epoch micros used for endTs query and to implement TTL'**,**
  `duration` **bigint****(**20**)** **DEFAULT** **NULL** **COMMENT** 'Span.duration(): micros used for minDuration and maxDuration query'**,**
  **UNIQUE** **KEY** `trace_id_high` **(**`trace_id_high`**,**`trace_id`**,**`id`**)** **COMMENT** 'ignore insert on duplicate'**,**
  **KEY** `trace_id_high_2` **(**`trace_id_high`**,**`trace_id`**,**`id`**)** **COMMENT** 'for joining with zipkin_annotations'**,**
  **KEY** `trace_id_high_3` **(**`trace_id_high`**,**`trace_id`**)** **COMMENT** 'for getTracesByIds'**,**
  **KEY** `name` **(**`name`**)** **COMMENT** 'for getTraces and getSpanNames'**,**
  **KEY** `start_ts` **(**`start_ts`**)** **COMMENT** 'for getTraces ordering and range'
**)** ENGINE**=**InnoDB **DEFAULT** CHARSET**=**utf8 ROW_FORMAT**=**COMPRESSED**;**
/*Data for the table `zipkin_spans` */
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */**;**
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */**;**
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */**;**
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */**;**
### 10.7.2 配置启动服务端
Zipkin Server启动参数配置项可参考官方文档，本示例启动参数如下：
java -jar zipkin-server-2.12.9-exec.jar --STORAGE_TYPE=mysql --MYSQL_HOST=192.168.10.204 --MYSQL_TCP_PORT=3306 --MYSQL_DB=mic-service-zipkin-db --MYSQL_USER=root --MYSQL_PASS=123456

- STORAGE_TYPE : 存储类型
- MYSQL_HOST：mysql主机地址
- MYSQL_TCP_PORT：mysql端口
- MYSQL_DB：mysql数据库名称
- MYSQL_USER：mysql用户名
- MYSQL_PASS ：mysql密码

配置好服务端之后，可以在浏览器请求几次，再回到数据库查看会发现数据已经持久化到mysql中。
![](media/1648521334581-4c5577f3-e58d-4671-9f69-02d9de045af9.png)
## 10.8 基于消息中间件收集数据
在默认情况下，Zipkin客户端和Server之间是使用HTTP请求的方式进行通信（即同步的请求方式），在网络波动，Server端异常等情况下可能存在信息收集不及时的问题。Zipkin支持与rabbitMQ整合完成异步消息传输。
加了MQ之后，通信过程如下图所示：
![](media/1648521335509-b65153ee-3fb6-4488-8d4d-a6f612f770f4.png)
### 10.8.1 RabbitMQ的安装与启动
安装步骤请参考官方文档。
安装成功后通过浏览器可访问RabbiMQ的管理端，[http://192.168.10.205:15672/#/](http://192.168.10.205:15672/#/) ，如下图所示：
![](media/1648521336206-15febf6d-1476-48b2-81fc-aee7914e193b.png)
### 10.8.2 启动Zipkin Server
启动命令参考如下：
java -jar zipkin-server-2.12.9-exec.jar --RABBIT_ADDRESSES=192.168.10.205:5672 --RABBIT_USER=admin --RABBIT_PASSWORD=123456

- RABBIT_ADDRESSES：指定RabbitMQ地址
- RABBIT_USER：用户名（默认guest）
- RABBIT_PASSWORD：密码（默认guest）

启动Zipkin Server之后，我们打开RabbitMQ的控制台可以看到多了一个Queue。
![](media/1648521337117-debd34ff-adba-44e8-a456-b9d54b0d07f0.png)
其中 zipkin 就是自动创建的Queue队列。
### 10.8.3 客户端配置

1. 引入依赖

在之前案例gateway-server、product-service、order-service的pom文件中引入以下依赖：
<**dependency**>
    <**groupId**>org.springframework.amqp</**groupId**>
    <**artifactId**>spring-rabbit</**artifactId**>
</**dependency**>
导入spring-rabbit依赖，是Spring提供的对rabbit的封装，客户端会根据配置自动的生产消息并发送到目标队列中。

1. 修改配置文件

对客户端服务的配置文件进行修改，参考如下配置：
**zipkin**:
  **sender**:
    **type**: _rabbit __#请求方式,采用rabbitmq异步发送数据_**sleuth**:
  **sampler**:
    **probability**: 1.0 _#采样的百分比_**rabbitmq**:
  **host**: 192.168.10.205
  **port**: 5672
  **username**: admin
  **password**: 123456
  **listener**: _#配置重试策略
    _**direct**:
      **retry**:
        **enabled**: **true
    simple**:
      **retry**:
        **enabled**: **true**

- 修改数据的上传方式，改为rabbit即可。
- 添加rabbitmq的相关配置。

**_spring.rabbitmq相关配置补充说明：_**
spring.rabbitmq.host: 默认localhost
spring.rabbitmq.port: 默认5672
spring.rabbitmq.username: 用户名
spring.rabbitmq.password: 密码
spring.rabbitmq.virtual-host: 连接到代理时用的虚拟主机
spring.rabbitmq.addresses: 连接到server的地址列表（以逗号分隔），先addresses后host
spring.rabbitmq.requested-heartbeat: 请求心跳超时时间，0为不指定，如果不指定时间单位默认为妙
spring.rabbitmq.publisher-confirms: 是否启用【发布确认】，默认false
spring.rabbitmq.publisher-returns: 是否启用【发布返回】，默认false
spring.rabbitmq.connection-timeout: 连接超时时间，单位毫秒，0表示永不超时

1. 测试

关闭Zipkin Server，并随意请求连接。打开rabbitmq管理后台可以看到，消息已经推送到rabbitmq。当Zipkin Server启动时，会自动的从rabbitmq获取消息并消费，展示追踪数据。
![](media/1648521338323-78005bd3-7b1c-4698-b7e8-590527e94a86.png)
通过增加MQ的方式，可以得到如下效果：

- 请求的耗时时间不会出现突然耗时特长的情况；
- 当ZipkinServer不可用时（比如关闭、网络不通等），追踪信息不会丢失，因为这些信息会保存在 Rabbitmq服务器上，直到Zipkin服务器可用时，再从Rabbitmq中取出这段时间的信息。
## 10.9 课后作业
完成课程内容中的案例实践，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 11 Spring Cloud Stream
在实际的企业开发中，消息中间件是至关重要的组件之一。消息中间件主要解决应用解耦，异步消息，流量削锋等问题，实现高性能，高可用，可伸缩和最终一致性架构。不同的中间件其实现方式，内部结构是不一样的。如常见的RabbitMQ和Kafka，由于这两个消息中间件的架构上的不同，像RabbitMQ有exchange，kafka有Topic，partitions分区，这些中间件的差异性导致我们实际项目开发给我们造成了一定的困扰，我们如果用了两个消息队列的其中一种，后面的业务需求，我想往另外一种消息队列进行迁移，这时候无疑就是一个灾难性的，一大堆东西都要重新推倒重新做，因为它跟我们的系统耦合了，这时候 Spring cloud Stream给我们提供了一种解耦合的方式。
## 11.1 概述
Spring Cloud Stream由一个中间件中立的核组成。应用通Spring Cloud Stream插入的input(相当于消费者consumer，它是从队列中接收消息的)和output(相当于生产者producer，它是从队列中发送消息的。)通道与外界交流。通道通过指定中间件的Binder实现与外部代理连接。业务开发者不再关注具体消息中间件，只需关注Binder对应用程序提供的抽象概念来使用消息中间件实现业务即可。
![](media/1648521339389-e7eade6a-eb47-4ac7-8b8e-737ff9a62f2b.png)
说明：最底层是消息服务，中间层是绑定层，绑定层和底层的消息服务进行绑定，顶层是消息生产者和消息消费者，顶层可以向绑定层生产消息和和获取消息消费。
## 11.2 核心概念
### 11.2.1 绑定器
Binder绑定器是Spring Cloud Stream中一个非常重要的概念。在没有绑定器这个概念的情况下，我们的Spring Boot应用要直接与消息中间件进行信息交互的时候，由于各消息中间件构建的初衷不同，它们的实现细节上会有较大的差异性，这使得我们实现的消息交互逻辑就会非常笨重，因为对具体的中间件实现细节有太重的依赖，当中间件有较大的变动升级、或是更换中间件的时候，我们就需要付出非常大的代价来实施。
通过定义绑定器作为中间层，实现了应用程序与消息中间件(Middleware)细节之间的隔离。通过向应用程序暴露统一的Channel通过，使得应用程序不需要再考虑各种不同的消息中间件的实现。当需要升级消息中间件，或者是更换其他消息中间件产品时，我们需要做的就是更换对应的Binder绑定器而不需要修改任何应用逻辑。甚至可以任意的改变中间件的类型而不需要修改一行代码。
通过配置把应用和spring cloud stream的binder绑定在一起，之后我们只需要修改binder的配置来达到动态修改topic、exchange、type等一系列信息而不需要修改一行代码。
### 11.2.2 发布/订阅模型
在Spring Cloud Stream中的消息通信方式遵循了发布-订阅模式，当一条消息被投递到消息中间件之后，它会通过共享的Topic主题进行广播，消息消费者在订阅的主题中收到它并触发自身的业务逻辑处理。这里所提到的Topic主题是Spring Cloud Stream中的一个抽象概念，用来代表发布共享消息给消费者的地方。在不同的消息中间件中，Topic可能对应着不同的概念，比如：在RabbitMQ中的它对应了Exchange、而在Kakfa中则对应了Kafka中的Topic。
![](media/1648521340009-789048e7-971d-4dbc-8aa6-fecbb940d0b5.png)
如上图是经典的Spring Cloud Stream的发布-订阅模型，生产者生产消息发布在shared topic（共享主题）上，然后消费者通过订阅这个topic来获取消息。其中topic对应于Spring Cloud Stream中的destinations（Kafka 的topic，RabbitMQ的 exchanges）。
框架自带的输入通道（input）接口是Sink，自带的输出通道(output)接口是Source，同时支持输入、输出通道的是Processor，后续通过案例进行讲解。
## 11.3 入门案例
### 11.3.1 演示环境准备
接下来的案例中将通过RabbitMQ作为消息中间件，完成Spring Cloud Stream 的入门案例，需要自行完成RabbitMQ的安装。
新建一个用于Spring Cloud Stream实践的父工程，并在引入相关依赖，参考前面的案例。
### 11.3.2 消息生产者
#### 11.3.2.1 创建生产者引入依赖
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-stream</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-stream-rabbit</**artifactId**>
</**dependency**>_<!--binder-rabbit依赖可以不用显示引入,它已经包含在spring-cloud-starter-stream-rabbit里面-->_<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-stream-binder-rabbit</**artifactId**>
</**dependency**>
spring-cloud-starter-stream-rabbit 里面已经包含了spring-cloud-stream-binder-rabbit，因此可以不显示的引入后者。
#### 11.3.2.2 配置application.yml
**spring**:
  **application**:
    **name**: producer-service
  **cloud**:
    **stream**:
      **bindings**:
        **output**:
          **destination**: stream-default _#如果使用rabbitmq,那么此处对应exchange的名字
          _**contentType**: text/plain
      **binders**:
        **defaultRabbit**:
          **type**: rabbit
  **rabbitmq**:
    **host**: 192.168.10.205
    **port**: 5672
    **username**: admin
    **password**: 123456**server**:
  **port**: 9001

- contentType：用于指定消息的类型，具体可以参考官方文档。
- destination：指定了消息发送的目的地，对应RabbitMQ，会发送到 exchange 是stream-default的所有消息队列中。
#### 11.3.2.3 定义binding
发送消息时需要定义一个接口，不同的是接口方法的返回对象是 MessageChannel，此处直接使用内置的接口Source：
**public interface **Source {
   String **_OUTPUT _**= **"output"**;
   @Output(Source.**_OUTPUT_**)
   MessageChannel output();
}
该接口位于org.springframework.cloud.stream.messaging包下，该接口声明了一个binding命名为“output”，这个binding声明了一个消息输出流，也就是消息的生产者。
#### 11.3.2.4 消息发送测试
定义一个消息发送类ProducerRunner，并于通过@EnableBinding注解与对应的binding关联起来。
@EnableBinding(Source.**class**)**public class **ProducerRunner **implements **CommandLineRunner {
    @Resource(name = **"output"**)
    **private **MessageChannel **output**;
    @Override
    **public void **run(String... args) {
        System.**_out_**.println(**"=======ProducerRunner#send()被执行======="**);
        **this**.**output**.send(MessageBuilder._withPayload_(**"hello spring cloud stream!"**).build());
    }
}
@EnableBinding(Source.class)用于指定消息发送通道接口。此处实现了org.springframework.boot.CommandLineRunner接口，目的在于让程序启动完成后自动执行run()方法。
通过浏览器访问rabbitmq的后台管理页面，可看到如下图所示结果：
![](media/1648521340959-c5f82340-7c2d-4a2a-880e-950022cf1566.png)
自动在rabbitmq中创建了一个名为stream-default的exchange。
#### 11.3.2.5 执行过程分析
![](media/1648521342593-01bb2912-6c88-40d5-afda-cbb41888bfaa.png)
如上图所示，output.send() 来自于名称为“output”的MessageChannel,对应的就是绑定器中的内置通道接口Source中定义的output对象，而Source接口中定义的名称为“output”的变量实际表示输出通道名称，对应配置文件中的bindings.output配置项。
### 11.3.3 消息消费者
#### 11.3.3.1 创建消费者引入依赖
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-stream</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-stream-rabbit</**artifactId**>
</**dependency**>
#### 11.3.3.2 配置application.yml
**spring**:
  **application**:
    **name**: consumer-service
  **cloud**:
    **stream**:
      **bindings**:
        **input**:
          **destination**: stream-default _#如果使用rabbitmq,那么此处对应exchange的名字
          _**contentType**: text/plain
      **binders**:
        **defaultRabbit**:
          **type**: rabbit
  **rabbitmq**:
    **host**: 192.168.10.205
    **port**: 5672
    **username**: admin
    **password**: 123456**server**:
  **port**: 9002

- destination：指定了消息获取的目的地，对应于MQ就是exchange。
#### 11.3.3.3 定义binding
使用内置的输入通道接口：
**public interface **Sink {
   String **_INPUT _**= **"input"**;
   @Input(Sink.**_INPUT_**)
   SubscribableChannel input();
}
注解@Input 对应的方法，需要返回 SubscribableChannel ，并且传入一个参数值，这样就声明了一个 binding 命名为“input”。
#### 11.3.3.4 消息接收测试
@EnableBinding(Sink.**class**)**public class **ConsumerListener {
    _/**
     * 监听binding为Sink.INPUT的消息
     */
    _@StreamListener(Sink.**_INPUT_**)
    **public void **input(Message<String> message) {
        System.**_out_**.println(**"=====consumer 收到消息：" **+ message.getPayload());
    }
}

- 定义一个类，[并添加注解@EnableBing(Sink.class)](mailto:并添加注解@EnableBing(Sink.class))，其中Sink 就是上述的接口，同时定义一个方法（此处是input）标明注解为 @StreamListener(Processor.INPUT)，方法参数为 Message 。
- 所有发送exchange为“stream-default”的MQ消息都会被投递到这个临时队列，并且触发上述的方法。

![](media/1648521343413-0bdb0ff6-92e6-4a30-9470-966d2d1dc013.png)
## 11.4 自定义消息通道
Spring Cloud Stream内置了两种接口，分别定义了binding为“input”的输入流，和“output”的输出流，而在我们实际使用中，往往是需要定义各种输入输出流。
示例程序直接在入门案例基础上进行修改。
### 11.4.1 生产者侧

1. 定义通道接口

**public interface **OrderOutputChannel {
    String **_OUTPUT _**= **"orderOutput"**;
    @Output(OrderOutputChannel.**_OUTPUT_**)
    MessageChannel orderOutput();
}
一个接口中，可以定义无数个输入输出流，可以根据实际业务情况划分。上述的接口，定义了一个订单输出binding。

1. 编写消息发送逻辑

@EnableBinding(OrderOutputChannel.**class**)**public class **OrderMessageSender {
    @Resource(name = **"orderOutput"**)
    **private **MessageChannel **orderOutput**;
    **public void **send(Object object) {
        **this**.**orderOutput**.send(MessageBuilder._withPayload_(object).build());
    }
}
在注解@EnableBinding中指定使用自定义的发送通道接口。

1. 修改配置文件

**spring**:
  **application**:
    **name**: producer-service
  **cloud**:
    **stream**:
      **bindings**:
        **orderOutput**:
          **destination**: stream-order
          **contentType**: text/plain        **output**:
          **destination**: stream-default _#如果使用rabbitmq,那么此处对应exchange的名字
          _**contentType**: text/plain
      **binders**:
        **defaultRabbit**:
          **type**: rabbit
在配置文件中添加自定义的通道“orderOutput”，并指定消息目的地“stream-order”。**注意此处的通道名称必须和接口中定义的名称相匹配。**

1. 编写发送测试代码

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.**class**)**public class **OrderMessageSenderTest {
    @Resource
    **private **OrderMessageSender **orderMessageSender**;
    @Test
    **public void **testSend(){
        **this**.**orderMessageSender**.send(**"==== send order info ===="**);
    }
}
### 11.4.2 消费者侧

1. 定义通道接口

**public interface **OrderInputChannel {
    String **_INPUT _**= **"orderInput"**;
    @Input(OrderInputChannel.**_INPUT_**)
    SubscribableChannel orderInput();
}

1. 编写消息监听器

@EnableBinding(OrderInputChannel.**class**)**public class **OrderMessageListener {
    @Resource(name = **"orderInput"**)
    **private **SubscribableChannel **orderInput**;

    @StreamListener(OrderInputChannel.**_INPUT_**)
    **public void **receive(Message<Object> message) {
        System.**_out_**.println(message.getPayload().toString());
    }
}
使用@StreamListener做监听的时候，需要指定OrderInputChannel.**_INPUT _**。

1. 修改配置文件

**spring**:
  **application**:
    **name**: consumer-service
  **cloud**:
    **stream**:
      **bindings**:
        **orderInput**:
          **destination**: stream-order
          **contentType**: text/plain        **input**:
          **destination**: stream-default _#如果使用rabbitmq,那么此处对应exchange的名字
          _**contentType**: text/plain

1. 进行测试

启动消费者程序，然后执行生产者侧的测试用例代码即可，结果参考下图：
![](media/1648521344082-c0c9ba33-1529-4f0f-814f-9e50a7ee52fe.png)
## 11.5 消息分组
通常在生产环境，我们的每个服务都不会以单节点的方式运行在生产环境，当同一个服务启动多个实例的时候，这些实例都会绑定到同一个消息通道的目标主题（Topic）上。默认情况下，当生产者发出一条消息到绑定通道上，这条消息会产生多个副本被每个消费者实例接收和处理，但是有些业务场景之下，我们希望生产者产生的消息只被其中一个实例消费，这个时候我们需要为这些消费者设置消费组来实现这样的功能。
![](media/1648521344699-1df3a6f9-31b8-4be2-889c-b056ba777d0f.png)
### 11.5.1 案例演示
在前面案例的基础上从consumer-service再复制一份消费者程序，或者按照前面章节介绍的方法，确保能同时启动两个相同的consumer-service实例。
此处采用复制的方式增加一个消费者实例，然后分别在两个消费者程序的配置文件中添加spring.cloud.stream.bindings.input.group 属性即可，参考如下：
**spring**:
  **application**:
    **name**: consumer-service
  **cloud**:
    **stream**:
      **bindings**:
        **orderInput**:
          **destination**: stream-order
          **contentType**: text/plain
          **group**: group-1        **input**:
          **destination**: stream-default _#如果使用rabbitmq,那么此处对应exchange的名字
          _**contentType**: text/plain
      **binders**:
        **defaultRabbit**:
          **type**: rabbit
  **rabbitmq**:
    **host**: 192.168.10.205
    **port**: 5672
    **username**: admin
    **password**: 123456**server**:
  **port**: 9003
在同一个group中的多个消费者只有一个可以获取到消息并消费。
## 11.6 消息分区
有一些场景需要满足，同一个特征的数据被同一个实例消费, 比如同一个id的传感器监测数据必须被同一个实例统计计算分析, 否则可能无法获取全部的数据。又比如部分异步任务，首次请求启动task，二次请求取消task，此场景就必须保证两次请求至同一实例。
![](media/1648521345523-0cfe3a6c-fd69-4417-bb8a-3430dcf3e7d8.png)
### 11.6.1 案例演示
在前面消息分组演示案例的基础上继续对生产者和2个消费者进行修改。

1. 修改生产者配置

**spring**:
  **application**:
    **name**: producer-service
  **cloud**:
    **stream**:
      **bindings**:
        **orderOutput**:
          **destination**: stream-order
          **contentType**: text/plain          **producer**:
            **partition-key-expression**: payload
            **partition-count**: 2        **output**:
          **destination**: stream-default _#如果使用rabbitmq,那么此处对应exchange的名字
          _**contentType**: text/plain
      **binders**:
        **defaultRabbit**:
          **type**: rabbit
通过 producer.partition-key-expression 参数指定分区键的表达式规则，用于区分每个消息被发送至对应分区的输出 channel。该表达式作用于传递给 MessageChannel 的 send 方法的参数，该参数实现 org.springframework.messaging.Message 接口的 GenericMessage 类。
如果 partition-key-expression 的值是 payload，将会使用所有放在 GenericMessage 中的数据作为分区数据。
producer.partitionCount：该参数指定了消息分区的数量。

- 修改发送消息的测试代码如下：

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.**class**)**public class **OrderMessageSenderTest {
    @Resource
    **private **OrderMessageSender **orderMessageSender**;
    @Test
    **public void **testSend() {
        **for **(**int **i = 0; i < 6; i++) {
            **this**.**orderMessageSender**.send(**"==== send order info ===="**);
        }
    }
}
连续6次发送相同的消息。

1. 修改消费者配置

**spring**:
  **application**:
    **name**: consumer-service
  **cloud**:
    **stream**:
      **bindings**:
        **orderInput**:
          **destination**: stream-order
          **contentType**: text/plain
          **group**: group-1
          **consumer**:
            **partitioned**: true        **input**:
          **destination**: stream-default _#如果使用rabbitmq,那么此处对应exchange的名字
          _**contentType**: text/plain
      **binders**:
        **defaultRabbit**:
          **type**: rabbit      **instance-count**: 2
      **instance-index**: 0
从上面的配置中，我们可以看到增加了这三个参数：
1. spring.cloud.stream.bindings._orderInput_.consumer.partitioned：通过该参数开启消费者分区功能； 
2. spring.cloud.stream.instance-count：该参数指定了当前消费者的总实例数量； 
3. spring.cloud.stream.instance-index：该参数设置当前实例的索引号，从0开始，最大值为 spring.cloud.stream.instance-count 参数减1。我们试验的时候需要启动多个实例，可以通过运行参数来为不同实例设置不同的索引值。
到这里消息分区配置就完成了，可以再次启动这两个应用，同时消费者启动多个，但需要注意的是要为消费者指定不同的实例索引号，这样当同一个消息被发给消费组时，可以发现只有一个消费实例在接收和处理这些相同的消息。
![](media/1648521346250-e7c3d397-0632-4975-b7f0-b88d89038c3c.png)
从上图可以看到相同的6条消息都被同一个消费者接收到。
## 11.7 课后作业
完成课程内容中的案例实践，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。
# 12 Spring Cloud Config
## 12.1 什么是配置中心
### 12.1.1 配置中心概述
对于传统的单体应用而言，常使用配置文件来管理所有配置，比如SpringBoot的application.yml文件， 但是在微服务架构中全部手动修改的话很麻烦而且不易维护。微服务的配置管理一般有以下需求：

- 集中配置管理，一个微服务架构中可能有成百上千个微服务，所以集中配置管理是很重要的。
- 不同环境不同配置，比如数据源配置在不同环境（开发，生产，测试）中是不同的。
- 运行期间可动态调整。例如，可根据各个微服务的负载情况，动态调整数据源连接池大小等。
- 配置修改后可自动更新。如配置内容发生变化，微服务可以自动更新配置。

综上所述对于微服务架构而言，一套统一的，通用的管理配置机制是不可缺少的总要组成部分。常见的做法就是通过配置服务器进行管理。
### 12.1.2 常见的配置中心
Spring Cloud Config为分布式系统中的外部配置提供服务器和客户端支持。
Apollo（阿波罗）是携程框架部门研发的分布式配置中心，能够集中化管理应用不同环境、不同集群的 配置，配置修改后能够实时推送到应用端，并且具备规范的权限、流程治理等特性，适用于微服务配置管理场景。
Disconf 专注于各种「分布式系统配置管理」的「通用组件」和「通用平台」, 提供统一的「配置管理 服务」包括 百度、滴滴出行、银联、网易、拉勾网、苏宁易购、顺丰科技 等知名互联网公司正在使用! 「disconf」在「2015 年度新增开源软件排名 TOP 100(OSC开源中国提供)」中排名第16强。
## 12.2 Spring Cloud Config简介
Spring Cloud Config项目是一个解决分布式系统的配置管理方案。它包含了Client和Server两个部分，Server提供配置文件的存储、以接口的形式将配置文件的内容提供出去，Client通过接口获取数据、并依据此数据初始化自己的应用。
![](media/1648521347123-b2e392b7-0562-425d-8692-5e91267feecd.png)
Spring Cloud Config为分布式系统中的外部配置提供服务器和客户端支持。使用Config Server，可以为所有环境中的应用程序管理其外部属性。它非常适合Spring应用，也可以使用在其他语言的应用上。随着应用程序通过从开发到测试和生产的部署流程，可以管理这些环境之间的配置，并确定应用程序具有迁移时需要运行的一切。服务器存储后端的默认实现使用git，因此它轻松支持标签版本的配置环境，以及可以访问用于管理内容的各种工具。
Spring Cloud Config服务端特性：

- HTTP，为外部配置提供基于资源的API（键值对，或者等价的YAML内容）
- 属性值的加密和解密（对称加密和非对称加密）
- 通过使用@EnableConfigServer在Spring boot应用中非常简单的嵌入。 

Config客户端的特性（特指Spring应用）:

- 绑定Config服务端，并使用远程的属性源初始化Spring环境。
- 属性值的加密和解密（对称加密和非对称加密）
## 12.3 Spring Cloud Config入门
Config Server是一个可横向扩展、集中式的配置服务器，它用于集中管理应用程序各个环境下的配置， 默认使用Git存储配置文件内容，也可以使用SVN存储，或者是本地文件存储。这里使用git作为学习的环境。
入门案例工程目录结构如下图所示：
![](media/1648521348162-4d94404c-32e6-422d-b0f1-0ec354bb5245.png)
### 12.3.1 在git上新建仓库
在git上新建仓库config-repository,将product-service工程的application.yml改名为product-service-dev.yml后上传，参考下图：
![](media/1648521349400-ec29eda4-959a-45f9-ac2d-339caf114834.png)
文件命名规则： 

- {application}-{profile}.yml 
- {application}-{profile}.properties 
- application为应用名称profile指的开发环境（用于区分开发环境，测试环境、生产环境等）
### 12.3.2 搭建服务端程序

1. 创建工程引入依赖

新建config-server模块，并添加如下依赖：
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-config-server</**artifactId**>
</**dependency**>

1. 配置application.yml

**server**:
  **port**: 9999**spring**:
  **application**:
    **name**: config-server
  **cloud**:
    **config**:
      **server**:
        **git**:
          **uri**: [http://git.ghostcloud.cn/anluming/config-repository.git](http://git.ghostcloud.cn/anluming/config-repository.git)
          **username**: anluming
          **password**: JLY123456

- 通过spring.cloud.config.server.git.uri : 配置git服务地址 
- 通过spring.cloud.config.server.git.username: 配置git用户名 
- 通过spring.cloud.config.server.git.password: 配置git密码
1. 配置启动类

@SpringBootApplication
@EnableConfigServer**public class **ConfigServerApplication {
    **public static void **main(String[] args) {
        SpringApplication._run_(ConfigServerApplication.**class**, args);
    }
}
@EnableConfigServer : 通过此注解开启配置中心服务端功能

1. 启动测试

启动config-server服务，在浏览器输入[http://localhost:9999/product-service-dev.yml](http://localhost:9999/product-service-dev.yml) ,就可访问到git服务器上的文件。
![](media/1648521350240-9629a2c3-82bd-45fd-909c-c546c879eb67.png)
### 12.3.3 改造客户端程序

1. 引入相关依赖

在客户端程序product-service中引入如下依赖：
_<!--config client所需依赖-->_<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-config</**artifactId**>
</**dependency**>

1. 修改配置文件

移除product-service中的application.yml文件，稍后改从config server中获取配置。

1. 添加bootstrap.yml配置文件

在product-service的resources目录下新建bootstrap.yml配置文件，在文件中写入如下配置：
**spring**:
  **cloud**:
    **config**:
      **uri**: http://localhost:9999
      **name**: product-service
      **profile**: pro
      **label**: master

- spring.cloud.config.uri：配置中心服务器地址；
- spring.cloud.config.name：配置文件名称的{application}部分；
- spring.cloud.config.profile：配置文件名称的{profile}部分；
- spring.cloud.config.label：git上分支名称。

bootstrap.yml配置文件比application.yml配置文件具有更高的加载优先级，启动应用时会检查此配置文件，在此文件中指定配置中心的服务地址，会自动的拉取所有应用配置并启用。

1. 测试验证

通过浏览器访问测试。
### 12.3.4 手动刷新配置数据
我们已经在客户端取到了配置中心的值，但当我们修改git上面的值时，服务端（Config Server） 能实时获取最新的值，但客户端（Config Client）读的是缓存，无法实时获取最新值。如下图所示：
![](media/1648521351312-ec272b11-e3b6-4421-b331-9e6b1b6f981a.png)
图左侧将git上product.name的值从“pear”修改为“pear-1”,图右侧config server将会同步进行更新。但是客户端程序product-service里面此时的product.name不会更新，除非重启程序。
SpringCloud已经为我们解决了这个问题，那就是客户端使用**post**去触发refresh，获取最新数据，需要依赖spring-boot-starter-actuator。

1. 客户端引入依赖

在product-service中引入如下依赖：
_<!--通过post请求刷新配置文件需要引入actuator依赖-->_<**dependency**>
    <**groupId**>org.springframework.boot</**groupId**>
    <**artifactId**>spring-boot-starter-actuator</**artifactId**>
</**dependency**>

1. 添加@RefreshScope注解

在需要刷新配置属性的类上面添加@RefreshScope注解，如下所示：
@RestController
@RequestMapping(**"/product"**)**@RefreshScope**
**public class **ProductController {
    @Value(**"${product.name}"**)
    **private **String **productName**;
    @GetMapping(value = **"/{productId}"**)
    **public **String find(@PathVariable Long productId) {
        **return ****"查询到商品【" **+ productId + **"】的信息"**;
    }
    @GetMapping(**"/name"**)
    **public **String findName() {
        **return ****"商品名称为：" **+ **this**.**productName**;
    }
}

1. 修改配置文件

修改product-service的bootstrap.yml配置文件，添加如下配置：
**management**:
  **endpoints**:
    **web**:
      **exposure**:
        **include**: refresh
通过该配置打开web监控访问端点。在**postman**中访问[http://localhost:9091/actuator/refresh](http://localhost:9091/actuator/refresh) ，使用**POST**方式提交数据，再次通过浏览器访问 [http://localhost:9091/product/name](http://localhost:9091/product/name) 发现数据已经更新。
![](media/1648521352499-e6d67bd8-a7c9-4d17-9fa0-6ef8f4dca0a8.png)
## 12.4 配置中心高可用
在之前的代码中，客户端都是直接调用配置中心的server端来获取配置文件信息。这样就存在了一个问题，客户端和服务端的耦合性太高，如果server端要做集群，客户端只能通过原始的方式来路由，server端改变IP地址的时候，客户端也需要修改配置，不符合springcloud服务治理的理念。springcloud提供了这样的解决方案，我们只需要将server端当做一个服务注册到eureka中，client端去 eureka中去获取配置中心server端的服务既可。
演示程序在前面案例基础上进行调整。
### 12.4.1 配置中心服务端改造

1. 引入依赖

在config-server模块中添加如下依赖：
_<!--config server高可用需要依赖eureka-->_<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-eureka-client</**artifactId**>
</**dependency**>

1. 修改配置文件

在config-server的配置文件添加eureka相关配置，参考如下：
**eureka**:
  **client**:
    **service-url**:
      **defaultZone**: http://localhost:8761/eureka/
  **instance**:
    **prefer-ip-address**: **true
    instance-id**: ${**spring.cloud.client.ip-address**}:${**server.port**}

1. 复制第二个配置中心实例

复制一个config-server实例，用于组成集群。

1. 启动测试

在config-server改造准备完成后，先启动eureka注册中心，在启动2个config-server实例，在浏览器中访问[http://localhost:8761/](http://localhost:8761/) 可看到两个配置中心实例都已经注册到注册中心，参考下图：
![](media/1648521354050-e683241b-465f-4d5b-b5e8-f35b3e89bf20.png)
### 12.4.2 客户端程序改造

1. 添加依赖

由于上一小节演示案例中product-service已经添加了相关依赖，所以此处无需引入新得依赖。参考依赖配置如下：
<**dependency**>
    <**groupId**>org.springframework.boot</**groupId**>
    <**artifactId**>spring-boot-starter-web</**artifactId**>
</**dependency**>
<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-netflix-eureka-client</**artifactId**>
</**dependency**>_<!--config client所需依赖-->_<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-config</**artifactId**>
</**dependency**>_<!--通过post请求刷新配置文件需要引入actuator依赖-->_<**dependency**>
    <**groupId**>org.springframework.boot</**groupId**>
    <**artifactId**>spring-boot-starter-actuator</**artifactId**>
</**dependency**>

1. 修改配置文件

配置中心高可用方式，客户端程序启动时会先从eureka注册中心获取配置中心的实例地址，然后再从配置中心获取配置数据进行初始化启动，因此需要对客户端product-service的bootstrap.yml文件进行修改，参考配置如下：
**spring**:
  **cloud**:
    **config**:
      **name**: product-service
      **profile**: pro
      **label**: master
      **discovery**:
        **enabled**: **true 
        service-id**: config-server
**management**:
  **endpoints**:
    **web**:
      **exposure**:
        **include**: refresh**eureka**:
  **client**:
    **service-url**:
      **defaultZone**: http://localhost:8761/eureka/
  **instance**:
    **prefer-ip-address**: **true
    instance-id**: ${**spring.cloud.client.ip-address**}:${**server.port**}

- spring.cloud.config.discovery.enabled：启用从注册中心获取配置中心。
- spring.cloud.config.discovery.service-id：配置中心服务的实例名称。
1. 启动测试
- 重复入门案例中演示的配置读取与刷新过程。
- 停掉其中一个配置中心服务实例，再次重复前一个演示过程。会发现配置中心实例挂掉一个之后，客户端程序依然能从另一个配置中心实例拉取配置数据正常启动，且能正常手动刷新配置。
## 12.5 消息总线BUS
在前面的案例里面，如果需要客户端获取到最新的配置信息需要执行refresh，我们可以利用webhook的机制每次提交代码发送请求来刷新客户端，当客户端越来越多的时候，需要每个客户端都执行一遍，这种方案就不太适合了，而消息总线可以完美解决这一问题。
在微服务架构中，通常会使用轻量级的消息代理来构建一个共用的消息主题来连接各个微服务实例，它广播的消息会被所有在注册中心的微服务实例监听和消费，也称消息总线。
Spring cloud bus被国内很多都翻译为消息总线，也挺形象的。大家可以将它理解为管理和传播所有分布式项目中的消息既可，其实本质是利用了MQ的广播机制在分布式的系统中传播消息，目前常用的有Kafka和RabbitMQ。利用bus的机制可以做很多的事情，其中配置中心客户端刷新就是典型的应用场景之一，我们用一张图来描述bus在配置中心使用的机制。
![](media/1648521355072-63421d42-f22d-4cd5-aad7-8a69a1f2d987.jpeg.jpg)
1、提交代码触发post请求给bus/refresh；
2、server端接收到请求并发送给Spring Cloud Bus；
3、Spring Cloud bus接到消息并通知给其它客户端；
4、其它客户端接收到通知，请求Server端获取最新配置；
5、全部客户端均获取到最新的配置。
配置中心与消息总线整合使用的示例将在上一小节演示程序基础上进行改造。
### 12.5.1 修改配置中心服务端

1. 添加依赖

在服务端程序config-server的pom文件中添加需要的依赖，参考如下：
_<!--通过post请求刷新配置文件需要引入actuator依赖-->_<**dependency**>
    <**groupId**>org.springframework.boot</**groupId**>
    <**artifactId**>spring-boot-starter-actuator</**artifactId**>
</**dependency**>_<!--消息总线与rabbitmq的依赖-->_<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-bus-amqp</**artifactId**>
</**dependency**>
因为需要从config-server端通过webhook触发配置更新消息的发送，因此需要引入actuator依赖，并且在配置中开启监控端点。

1. 修改配置

修改config-server的配置文件，参考如下所示：
**spring**:
  **application**:
    **name**: config-server
  **cloud**:
    **config**:
      **server**:
        **git**:
          **uri**: [http://git.ghostcloud.cn/anluming/config-repository.git](http://git.ghostcloud.cn/anluming/config-repository.git)
          **username**: anluming
          **password**: JLY123456  **rabbitmq**:
    **host**: 192.168.10.205
    **port**: 5672
    **username**: admin
    **password**: 123456
**eureka**:
  **client**:
    **service-url**:
      **defaultZone**: http://localhost:8761/eureka/
  **instance**:
    **prefer-ip-address**: **true
    instance-id**: ${**spring.cloud.client.ip-address**}:${**server.port**}**management**:
  **endpoints**:
    **web**:
      **exposure**:
        **include**: bus-refresh
配置中增加rabbitmq的配置项，并开启刷新配置的webhook。
### 12.5.2 修改客户端程序

1. 添加依赖

使用消息总线方式更新配置，客户端程序也需要引入相关依赖。在product-service的pom文件中添加如下依赖：
_<!--消息总线与rabbitmq的依赖-->_<**dependency**>
    <**groupId**>org.springframework.cloud</**groupId**>
    <**artifactId**>spring-cloud-starter-bus-amqp</**artifactId**>
</**dependency**>

1. 修改配置

修改product-service程序的配置文件，添加rabbitmq相关的配置，由于其配置是从远端读取的，因此需要到git上进行修改，参考下图所示：
![](media/1648521356050-d8b6029e-8951-4b69-810f-9f9321fcbeb5.png)
由于此时不需要从客户端通过webhook触发配置更新，因此可以去掉bootstrap.yml文件中的webhook端点配置，即移除management.endpoints.web.exposure.include 配置项。
启动eureka-server、config-server、product-service。当配置信息刷新后，只需要通过postman向config-server发送POST请求，即可刷新每个客户端的配置。
## 12.6 课后作业
完成课程内容中的案例实践，记录关键步骤、遇到的问题、解决方法，以word形式提交（要求附带关键截图，文档格式与本课件保持一致）。

