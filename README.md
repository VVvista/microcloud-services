[toc]
### 1.项目开发环境
- jdk 1.8
- gradle 5.2.1
- springboot 2.0.0.RELEASE
- springCloudVersion Finchley.SR1

### 2.项目搭建
1.新建项目：idea -> new -> project：gradle
2.idea添加gradle插件：file -> settings ->plugins :gradle (便于执行gradle构建命令)
3.若使用本地gradle：file -> settings ->Build Tool -> gradle 

### 3.Gradle版本管理
#### 3.1 本地Gradle
1.本地安装Gradle并配置环境变量

2.idea中配置Gradle环境
file->settings->gradle：use local gradle distribution,设置Gradle Home

#### 3.2 Gradle-wrapper
Gradle版本更新非常快，同时为了gradle版本的统一管理。推荐使用wrapper管理版本。
`rootProject/gradle/wrapper` 目录下有两个文件：
- gradle-wrapper.jar：解析和运行gradle所需的jar包
- gradle-wrapper.properties：解析和运行gradle所需的配置文件

配置gradle-wrapper.properties文件
```
distributionBase=GRADLE_USER_HOME // gradle安装包解压文件存放位置
distributionPath=wrapper/dists    // 完整路径：$GRADLE_USER_HOME/wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-5.6.4-bin.zip    // gradle版本的下载地址
zipStoreBase=GRADLE_USER_HOME     // gradle安装包存放位置
zipStorePath=wrapper/dists        // 完整路径：$GRADLE_USER_HOME/wrapper/dists
```
注：GRADLE_USER_HOME 默认：~/.gradle

### 4.Gradle本地仓库
1.Gradle默认仓库
gradle下载的jar包默认存储在：~/.gradle/caches/modules-2/files-2.1
2.Gradle共用maven仓库
idea：file->settings->gradle 
将GRADLE_USER_HOME设置成maven仓库地址

### 5.项目配置
Gradle项目需要配置`settings.gradle`、`build.gradle`文件

#### 5.1  配置`settings.gradle`文件
`settings.gradle` 文件主要配置根项目和子项目，类似maven中的module
```
rootProject.name = 'microcloud-services'   // 根项目
include ':app', ':lib' // 子项目

include 'microcloud-api-getway:microcloud-zuul' // 子项目，注意路径
findProject(':microcloud-rount:microcloud-rount-ribbon')?.name = 'microcloud-rount-ribbon' // 子项目重新命名
```
`settings.gradle` 文件中还可以配置项目加载前、加载完成后的一些操作代码。

#### 5.2 配置`build.gradle`文件
`build.gradle`文件是Gradle的构建脚本，类似于maven中的`pom.xml`。
`build.gradle`文件中主要的语法块：
- buildscript{ }：gradle构建自身使用的资源。主要定义构建所使用的插件、jar和仓库等
- plugins：gradle构建所需的插件
- allprojects{ }：所有项目所需要配置声明
- subprojects{ }：子项目所需要配置声明

配置项示例可查看`build.gradle`文件。

##### 5.2.1 项目版本
项目属性主要有：rootProject、name、project、group、version、description、status等，详情可以查看`org.gradle.api.Project`类。
项目属性的赋值：1.直接赋值 2.`build.gradle`中定义  3.`gradle.properties`文件中设置属性参数，`build.gradle`中引用参数   4.自定义文件，`build.gradle`中引入文件及引用参数
```
// 1.直接赋值
group "com.kt.game" //已存在属性直接赋值即可
version = "1.0" 
description = "I Am A Gradle Test!"  

// 2.build.gradle中定义 
// build.gradle 
ext {
    projectGroup = 'com.kt.game'
    projectVersion = '1.0-SNAPSHOT'
    }
group "$projectGroup"
 
// 3.gradle.properties中设置参数
// gradle.properties
projectGroup=org.frameworkset.elasticsearchdemo
projectVersion=6.0.3
// build.gradle 
group "$projectGroup"
version "$projectVersion"

// 4.自定义文件
// version.gradle
ext {
    projectGroup = 'com.kt.game'
    projectVersion = '1.0-SNAPSHOT'
    }
// build.gradle 
apply from: 'version.gradle' // 引入自定义脚本
group "$projectGroup"
version "$projectVersion"
```
注：`gradle.properties`文件是内置的属性文件，gradle会自动加载该文件；自定义文件需要先在脚本中引入

##### 5.2.2 Gradle插件
`build.gradle`中有两种引入插件的方式：DSL语法、buildscript块
1.DSL语法
```
plugins {
    id «plugin id»                                            
    id «plugin id» version «plugin version» [apply «false»]   
}
```
可以使用` pluginManagement {} `块对插件进行管理
```
pluginManagement {// 必须置于脚本首行
    plugins {
    }
    resolutionStrategy {
    }
    repositories {
    }
}
```
2.buildscript块
```
apply plugin: '<pluginid>'
```
可以在` buildscript {} `中定义插件的版本和下载地址等。
```
buildscript {
    repositories { // 仓库
    }

    dependencies { // 依赖jar
    }
}
apply plugin:'<pluginid>'
```
官网：https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl

* 内置插件
Grade有很多内置的插件，内置插件无需配置依赖路径：
1.编译、测试插件（Java、Groovy、Scala、War 等）
2.代码分析插件（Checkstyle、FindBugs、Sonar 等）
3.IDE插件（Eclipse、IDEA等）
```
apply plugin: 'java'  // 导入 java插件
```
java插件是Gradle核心插件，提供了一系列的任务支持构建、编译、测试：执行命令可在idea->Gradle->Tasks->build中查找

* 第三方插件
Gradle中引入第三方插件，需要配置依赖路径:
```
buildscript {
    repositories { // 仓库
        mavenLocal() // 本地仓库
        maven { url "http://maven.aliyun.com/nexus/content/groups/public/" } // 自定义地址
        mavenCentral() // maven中央仓库
        jcenter()  // 插件仓库，导入的插件将会在仓库中寻找并下载
    }

    dependencies { // 依赖jar
        classpath 'io.spring.gradle:dependency-management-plugin:1.0.9.RELEASE' //版本依赖管理插件
    }
}
apply plugin: 'io.spring.dependency-management'// 引入插件
```
Gradle插件查询：https://plugins.gradle.org/

##### 5.2.3 依赖
脚本运行或项目所需的依赖均在 `dependencies{}` 中声明
* 声明依赖
```
#build.gradle
dependencies {
    <configuration name> <dependencies>
}
```
* 添加依赖
常用的依赖关系：
(1) implementation(compile)：编译时依赖（src/main/java）
(2) runtimeOnly(runtime)：运行时依赖（src/main/java）
(3) testImplementation(testCompile)：测试时编译时依赖（src/main/test）编译时的依赖
(4) testRuntimeOnly(testRuntime)：仅在测试用例运行时依赖（src/main/java）执行时的依赖
(5) archives：发布构件时依赖，如jar包等
default：默认依赖配置
... 
`<dependencies>` 规则与maven中相同： `group:name:version`
除了上述常用的依赖项，gradle还添加一些额外的依赖项：
compileOnly、compileClasspath、runtimeClasspath、testCompileOnly等
官网：https://docs.gradle.org/current/userguide/java_plugin.html

```
 dependencies {
     //setting.gradle文件中 include ':childProject'
     compile project(":microcloud-server") // 子项目依赖
     implementation 'ch.qos.logback:logback-classic:1.2.2'// 编译依赖
     testImplementation group: 'junit', name: 'junit', version: '5.12' // 测试依赖
 }
```
* 动态依赖jar包版本
```
dependencies {
    /* 选择1以上任意一个版本，这使发生版本冲突的几率变小*/
    compile group: 'org.slf4j',name: 'slf4j-api',version:'1.+'
   /* 选择最新的版本，避免直接指定版本号 */
    compile group: 'org.slf4j',name: 'slf4j-api',version:'latest.integration' }
```

* 依赖下载仓库
下载依赖的仓库在 `repositories{}` 中声明
```
repositories {
    mavenLocal() 
    maven { url "file://E:/githubrepo/releases" } // 本地仓库地址
    maven { url "http://mvnrepository.com/" } // 远程仓库 
    /* 
     * 公司仓库，可能需要验证
     * 不推荐直接将用户名密码写在build.gradle中
     * 可以写在~/.gradle/gradle.properties中，再使用
     */
    maven {
        url "<you_company_resp_url>"
        credentials {
            username 'your_username'
            password 'your_password'
        }
    }
    mavenCentral()
    jcenter()
 
}
```

##### 5.2.4 jar包冲突
jar包冲突时，gradle自动使用最高版本的jar。但支持排除指定jar、排除所有jar、jar包冲突时报异常、版本冲突时使用指定jar等操作
* 排除指定的jar 
```
// 方式1：在 dependencies{} 中排除依赖jar
dependencies {
        compile (group: 'org.hibernate', name: 'hibernate-core', version: '3.6.3.Final'){
                // module 是 jar 的 name,group是必选项，module可选
               exclude group:"org.slf4j" , module:"slf4j-api"  } }
// 方式2：在 configurations{} 中排除依赖jar
configurations {
    // 排除 logback 依赖
    implementation.exclude group:"org.slf4j" , module:"slf4j-api"

}
```
* 排除所有依赖jar
```
dependencies {
     compile (group: 'org.hibernate', name: 'hibernate-core', version: '3.6.3.Final'){
     // transitive默认为true ，设为false编译会报错，需要手动添加相应依赖jar包
        transitive=false   }}
```
* 关闭gradle版本冲突处理机制
```
configurations.all{
    resolutionStrategy{
        //  gradle不处理版本冲突
        failOnVersionConflict()  
    }
}
```
* 强行指定版本号
```
configurations.all{
    resolutionStrategy{
        force 'org.slf4j:slf4j-api:1.7.24'  
    }
}
```

### 6.依赖管理
Gradle中引用 `dependency-management` 插件实现对依赖的版本管理
#### 6.1 添加`dependency-management` 插件
```
buildscript {
    repositories {
        maven { url 'https://repo.spring.io/plugins-snapshot' }
    }
    dependencies {
        classpath 'io.spring.gradle:dependency-management-plugin:1.0.7.BUILD-SNAPSHOT'
    }
}

apply plugin: "io.spring.dependency-management"

```

#### 6.2 依赖管理配置
使用`dependencyManagement{ }`声明依赖版本，有两种方式：
1.使用插件的DSL实现直接配置管理
2.导入需要的maven boms
```
# 使用DSL管理
dependencyManagement {
    dependencies {
        dependency 'org.springframework:spring-core:5.0.3.RELEASE'
    }
}

dependencies {
    compile 'org.springframework:spring-core'
}

```
```
#导入maven boms
dependencyManagement {
     imports {
          mavenBom 'io.spring.platform:platform-bom:1.0.1.RELEASE'
     }
}

dependencies {
     compile 'org.springframework.integration:spring-integration-core'
}
```
官网：https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/

### 7.springboot插件
springboot插件实现对gradle的支持，类似于maven中的`org.springframework.boot.spring-boot-maven-plugin`插件
```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "org.springframework.boot:spring-boot-gradle-plugin:2.2.7.RELEASE"
  }
}

apply plugin: "org.springframework.boot"

```
官网：https://plugins.gradle.org/plugin/org.springframework.boot
https://docs.spring.io/spring-boot/docs/2.0.0.M2/gradle-plugin//reference/html/

### 8.java插件
* 项目结构
java项目默认的目录结构与maven中相同，同时支持修改和添加目录结构
```
JavaGradle
└─src
    ├─main
    │  ├─java
    │  └─resources
    └─test
        ├─java
        └─resources
``` 
可以在`sourceSets{ }`中修改Java源码位置
```
# 修改默认目录
sourceSets{
    main{
        java{
            srcDir 'src/java'
        }
        
        resources{
            srcDir 'src/resources'
        }
        
    }
}
```
* java版本
```
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
```
* Java任务
java插件添加了一些用于项目构建的任务，任务之间具有依赖性，执行某个任务默认先执行依赖的构建任务
:compileJava
:processResources
:classes
:jar
:assemble
:compileTestJava
:processTestResources
:testClasses
:test
:check
:build
任务所依赖的任务也会在构建过程中显示出来
官网：https://docs.gradle.org/current/userguide/java_plugin.html

### 9.maven-publish插件
maven-publish插件支持将项目发布到maven本地仓库、远程仓库，同时也可以自定要发布到maven中的内容
```
plugins {
    `maven-publish`
}
publishing {
    publications {
        create<MavenPublication>("myLibrary") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "myRepo"
            url = uri("file://${buildDir}/repo")
        }
    }
}
```
maven-publish插件定义的任务有：
：generatePomFileForPubNamePublication — GenerateMavenPom
：publishPubNamePublicationToRepoNameRepository — PublishToMavenRepository
：publishPubNamePublicationToMavenLocal — PublishToMavenLocal
：publish
：publishToMavenLocal
官网：https://docs.gradle.org/current/userguide/publishing_maven.html

###  10.打包
#### 10.1 项目编译
使用idea中的gradle工具直接执行build相关的编译打包命令即可，生成的文件在 `build` 目录下
#### 10.2 可执行jar包
gradle执行jar命令打包后的文件仅包含项目源文件，不包含定义的依赖jar。如果需要将依赖一起打包，可以使用application 插件或编写task任务。

1.自定义打包
在`build.gradle`文件中添加jar闭包，执行 `gradle jar`，生成文件在`build/libs` 目录下
```
jar {
    manifest {// 配置META-INF/MANIFEST.MF文件
        attributes 'Manifest-Version': 1.0 //jar版本，可选
        attributes 'Main-Class': 'com.orgine.ApplicationRun' //启动类全路径,可选
    }

    from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } } // 收集compile类依赖
}
```

2.shadowJar插件
在`shadowJar`闭包中指定需要打入的依赖jar，执行 shadowJar task
```
plugins {
    id 'com.github.johnrengelman.shadow' version '1.2.3'
}
shadowJar {
    // 只把指定的依赖打入到fat jar中
    dependencies {
        // 这个依赖不一定是compile阶段的依赖
        include (dependency('com.google.guava:guava:16.0.1'))   
        include (dependency('com.datastax.cassandra:cassandra-driver-core:2.2.0-rc3'))
    }
    relocate 'com.google', 'com.google.gridx'// 将com.google包替换成com.google.gridx包名
}
```
参考：https://www.zybuluo.com/xtccc/note/317832
官网：https://imperceptiblethoughts.com/shadow/getting-started/#default-java-groovy-tasks

3.application插件
`build.gradle`中引入application插件，执行distZip、distTar命令，生成的文件在 `build/distribution` 目录下
```
apply plugin: 'application'
mainClassName = 'com.lion.admin.server.AdminServerApplication' // 主启动类
```

###  11.不同环境的编译
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

###  12.搭建springCloud项目
microcloud-services:
基于gradle构建工具的spring cloud微服务框架，包含了服务注册、配置管理、断路器、负载均衡、路由、服务管理。

#### 12.1 项目开发环境
- jdk 1.8
- gradle 5.2.1
- springboot 2.0.0.RELEASE
- springCloudVersion Finchley.SR1

#### 12.2 组件说明
- 服务注册/发布：eureka
- 配置中心: config
- 消息总线：amqp -> bus
- 负载均衡：feign / ribbon
- 路由网关：zull

#### 12.3 项目结构
Root project 'microcloud-services'  - 根目录
|    \--- Project ':microcloud-api-getway:microcloud-zuul' - 服务网关
|    \--- Project ':microcloud-config:microcloud-config-reader' - 集中配置管理服务端
|    \--- Project ':microcloud-config:microcloud-config-reader-bus' - 消息总线
|    \--- Project ':microcloud-config:microcloud-config-server' - 集中配置管理
|    \--- Project ':microcloud-rount:microcloud-rount-fegin' - 负载均衡器
|    \--- Project ':microcloud-rount:microcloud-rount-ribbon' - 负载均衡器
|    \--- Project ':microcloud-server:microcloud-eureka-server' - 服务注册中心
|    \--- Project ':microcloud-server:microcloud-eureka-server-client' - 服务提供者

#### 12.4 项目配置
本项目使用spring-cloud框架，spring-cloud针对各项服务有对应的组件和配置规则。
spring-cloud的组件大致的配置规则为：创建Moudle -> 配置`build.gradle`文件 -> 配置yml文件 -> 创建主启动类 -> 编写业务类

下面以 `microcloud-eureka-server-client`模块为例叙述，其他模块类似。
* 1.`microcloud-eureka-server-client`模块
创建`microcloud-eureka-server-client` module
* 2.配置`build.gradle`文件
```
// #项目描述
description '服务注册中心'
// #项目依赖
dependencies {
    compile('org.springframework.cloud:spring-cloud-starter-eureka-server')
}
```
* 3.配置resource/application.yml
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
* 4.创建主启动类
```
@SpringBootApplication
@EnableDiscoveryClient
public class EurekaDemoClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaDemoClientApplication.class, args);
    }
}
```
* 5.编写业务类
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

