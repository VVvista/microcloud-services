[toc]
## microcloud-services:
这是一个基于gradle构建工具的spring cloud微服务框架，包含了服务注册、配置管理、断路器、负载均衡、路由、服务管理。

#### 1.项目开发环境
- jdk 1.8
- scala 2.11
- gradle 5.6.4
- springboot 1.5.9.RELEASE
- springCloudVersion Edgware.RELEASE

#### 2.组件说明
- 服务注册/发布：eureka
- 配置中心: config
- 消息总线：amqp -> bus
- 负载均衡：feign / ribbon
- 路由网关：zull
- 链路追踪： zipkin
#### 3.项目结构
Root project 'microcloud-services'  - 根目录
|    \--- Project ':microcloud-api-getway:microcloud-zuul' - 服务网关
|    \--- Project ':microcloud-call-chain:microcloud-zipkin-server' - 链路追踪器
|    \--- Project ':microcloud-config:microcloud-config-reader' - 集中配置管理服务端
|    \--- Project ':microcloud-config:microcloud-config-reader-bus' - 消息总线
|    \--- Project ':microcloud-config:microcloud-config-server' - 集中配置管理
|    \--- Project ':microcloud-rount:microcloud-rount-fegin' - 负载均衡器
|    \--- Project ':microcloud-rount:microcloud-rount-ribbon' - 负载均衡器
|    \--- Project ':microcloud-server:microcloud-eureka-server' - 服务注册中心
|    \--- Project ':microcloud-server:microcloud-eureka-server-client' - 服务提供者

#### 4.项目配置
##### 4.1  配置`settings.gradle`
在 `settings.gradle` 文件中配置根项目和子项目，类似maven中的module
```
rootProject.name = 'microcloud-services'
include 'microcloud-api-getway:microcloud-zuul' // 注意路径
```
##### 4.2 自定义变量
方法一：创建`gradle.properties`文件。
在`gradle.properties`文件中定义`build.gradle`所需的变量 ，如：依赖jar的版本号。gradle构建时会自动加载`gradle.properties`文件，无需额外配置
注意：变量名不要跟project或task中的变量名相同。
```
#gradle.properties
springBootVersion=1.5.9.RELEASE
springCloudVersion=Edgware.RELEASE
#build.gradle 变量的调用
${springBootVersion}
```
方法二：自定义任意文件。
自定义任意文件，如：创建`other.gradle`，并在`build.gradle`中引入`other.gradle`。文件中可以定义任意项，包含变量、task等
```
#other.gradle
ext{
springBootVersion='1.5.9.RELEASE'
springCloudVersion='Edgware.RELEASE'
}
#build.gradle
apply from: 'version.gradle'
```
方式三：在`build.gradle`直接定义
```
#build.gradle
ext{
springBootVersion='1.5.9.RELEASE'
springCloudVersion='Edgware.RELEASE'
}
```
##### 4.3 配置`build.gradle`
gradle的构建脚本为`build.gradle`，类似于maven中的`pom.xml`。
`build.gradle`文件主要定义的语法块：
- buildscript：gradle构建自身使用的资源。主要定义构建所使用的插件、jar和仓库等
- plugins：gradle构建所需的插件
- allprojects：所有项目所需要配置声明
- subprojects：子项目所需要配置声明
具体的配置项可查看项目中的`build.gradle`文件。

##### 4.4 子项目配置
本项目使用spring-cloud框架，spring-cloud针对各项服务有对应的组件和配置规则。
spring-cloud的组件大致的配置规则为：创建Moudle -> 配置`build.gradle`文件 -> 配置yml文件 -> 创建主启动类 -> 编写业务类
下面以 `microcloud-eureka-server-client`模块为例叙述，其他模块类似。
1.`microcloud-eureka-server-client`模块
2.配置`build.gradle`文件
```
#项目描述
description '服务注册中心'
#项目依赖
dependencies {
    compile('org.springframework.cloud:spring-cloud-starter-eureka-server')
}
```
3.配置resource/application.yml
注意：各组件的配置文件名及配置参数存在不同，具体配置可见官网
```
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
server:
  port: 8763
spring:
  application:
    name: eureka-demo-client
```
4.创建主启动类
```
@SpringBootApplication
@EnableDiscoveryClient
public class EurekaDemoClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaDemoClientApplication.class, args);
    }
}
```
5.编写业务类
```
@RestController
public class EurekaControllerApplication {

    @Value("${server.port}")
    private int port;

    @RequestMapping("hi")
    public String hi(@RequestParam(required = false) String name) {
        if (name == null)
            name = "Anonymous";
        return "hi " + name + ", my port=" + port;
    }
}
```
#### 5.构建脚本配置说明
##### 5.1 gradle版本
在进行项目引入时，本地环境可以搭建gradle环境，使用gradle-wrapper插件进行gradle版本管理，同时也避免了项目成员间gradle版本不一致的问题
在根目录的 `gradle/wrapper` 目录下有两个文件：
- gradle-wrapper.jar：解析和运行gradle所需的jar包
- gradle-wrapper.properties：解析和运行gradle所需的配置文件
gradle构建项目中会去properties文件中查找gradle运行的环境，如果没有找到gradle运行所需的版本会自定下载，如果存在指定版本，直接运行。
```
distributionBase=GRADLE_USER_HOME
# 解压缩后存放的相对路径
distributionPath=wrapper/dists
# 获取gradle的下载地址正式项目所用版本
distributionUrl=https\://services.gradle.org/distributions/gradle-5.6.4-all.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```
##### 5.2 依赖插件及项目版本号
以Java插件为例
```
#build.gradle 
apply plugin: 'java'
sourceCompatibility = 1.8 // Java版本号
#项目名、版本号可以统一配置
group 'com.kt.game'// 项目group
version '1.1-SNAPSHOT' // 项目版本号

```
##### 5.3 指定maven仓库
gradle使用 `repositories{ }` 声明maven仓库
```
 repositories {
      mavenLocal() // 本地仓库
      maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' } // 自定义地址
      mavenCentral() // maven中央仓库
} 
```
##### 5.4 依赖声明
1.依赖声明：`dependencies{}`
- 依赖jar声明
```
 dependencies {
     implementation 'ch.qos.logback:logback-classic:1.2.2'// 编译依赖，但不会传递给子类
     testImplementation group: 'junit', name: 'junit', version: '4.12' // 测试依赖
     compile 'org.springframework.boot:spring-boot-gradle-plugin:2.0.0.RELEASE' // 编译依赖
}
```
- 项目依赖
```
#build.gradle
dependencies {
    compile project(":microcloud-server") 
}
```

2.jar版本号
- 定义版本号
为了方便管理在`build.gradle`或自定义文件中定义变量进行统一管理，具体操作同gradle中的变量定义和引用，不再叙述
- 使用最新版本
`version:latest.integration`会选择最新的版本，避免直接指定版本号
```
dependencies {
    compile group: 'org.slf4j',name: 'slf4j-api',version:'latest.integration' }
```
3.jar包冲突
项目中出现jar包冲突时，gradle自动使用最高版本的jar。但支持排除指定jar、排除所有jar、jar包冲突时报异常、版本冲突时使用指定jar等操作
- 排除指定的jar 
```
#方式一：在 dependencies{}中排除依赖jar
dependencies {
        compile (group: 'org.hibernate', name: 'hibernate-core', version: '3.6.3.Final'){
                // module 是 jar 的 name,group是必选项，module可选
               exclude group:"org.slf4j" , module:"slf4j-api"  } }
#方式2：在 configurations{}中排除依赖jar
configurations {
    // 排除 logback 依赖
    implementation.exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
}
```
- 排除所有依赖jar
```
dependencies {
     compile (group: 'org.hibernate', name: 'hibernate-core', version: '3.6.3.Final'){
     // transitive默认为true ，设为false编译会报错，需要手动添加相应依赖jar包
        transitive=false   }}
```
- 关闭gradle版本冲突处理机制
```
configurations.all{
    resolutionStrategy{
        //  gradle不处理版本冲突
        failOnVersionConflict()  }}
```
- 强行指定版本号
```
configurations.all{
    resolutionStrategy{
        force 'org.slf4j:slf4j-api:1.7.24'  }}
```
####  6.不同环境的编译
gradle项目中可能有多个编译环境，如：test、dev、pro等。gradle可以在编译时指定环境参数实现加载不同的文件
1.在`src/profile/$env`目录下创建profile.gradle
2.在`build.gradle`中添加配置
```
def env = System.getProperty("env") ?: "dev"
sourceSets {
    main {
        resources {
            srcDirs = ["src/main/resources", "src/main/profile/$env"]
        }
    }
}

apply from: "src/main/profile/$env/profile.gradle
```
项目编译时指定环境,如:`gradle build -Denv=prod`
####  7.项目编译打包
##### 7.1 项目编译
使用idea中的gradle工具直接执行build相关的编译打包命令即可。打包文件在项目的`build`目录下
##### 7.2 项目依赖打包
gradle执行jar命令打包后的文件仅包含项目源文件，不包含定义的依赖jar。如果需要将依赖一起打包，可以使用application 插件或编写task任务。
方式1：添加shadowJar插件
方式2：添加application插件，执行distZip、distTar命令执行（引入插件后，可在idea的gradle工具中直接执行）
```
apply plugin: 'application'
mainClassName = 'com.lion.admin.server.AdminServerApplication' // 主启动类
```
方式3：自定义打包,jar命令执行
```
jar {
    manifest {
        attributes 'Manifest-Version': 1.0 //jar版本，可选
        attributes 'Main-Class': 'com.orgine.ApplicationRun' //启动类全路径,可选
    }

    from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
    into('assets') {
        from 'assets'
    }
}
```




