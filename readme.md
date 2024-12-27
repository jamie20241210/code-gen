# code-gen

一款代码生成工具，可自定义模板生成不同的代码，支持MySQL、Oracle、SQL Server、PostgreSQL。

- 只需要一个Java8环境，下载后即可运行使用。
- 步骤简单，只需配置一个数据源，然后勾选模板即可生成代码。
- 默认提供了通用的实体类、mybatis接口、mybatis配置文件模板，可以快速开发mybatis应用。

> 用到的技术：[Solon](https://solon.noear.org/) + Mybatis + Vue

## 使用步骤

### 方式1：本地zip部署

- 前往[发行版页面](https://gitee.com/durcframework/code-gen/releases)，下载最新版本zip文件
- 解压zip，如果是Mac/Linux操作系统，运行`run.sh`文件启动，Windows操作系统双击`run.bat`启动
- 浏览器访问`http://localhost:6969/`

默认端口是6969，更改端口号按如下方式：

打开`conf/app.yml`，修改`server.port`值

或者

指定JVM参数`-Dserver.port=6666`

> 升级：后续升级覆盖gen.jar和dist文件夹即可

### 方式2：docker运行

- 下载公共镜像

`docker pull registry.cn-hangzhou.aliyuncs.com/tanghc/gen:latest`

下载完毕后，执行

```shell
docker run --name gen --restart=always \
  -p 6969:6969 \
  -v /opt/gen/:/opt/gen/ \
  -v /opt/gen/conf/:/gen/conf/ \
  -v /opt/gen/ext:/gen/ext \
  -d registry.cn-hangzhou.aliyuncs.com/tanghc/gen:latest
```

浏览器访问`http://ip:6969/`


### 本地构建镜像


clone代码，然后执行`docker-build.sh`脚本

执行

```shell
docker run --name gen --restart=always \
  -p 6969:6969 \
  -v /opt/gen/:/opt/gen/ \
  -v /opt/gen/conf/:/gen/conf/ \
  -v /opt/gen/ext:/gen/ext \
  -d <镜像ID>
```

## 其它数据库支持


默认支持mysql数据库，如果要支持其它数据库，如Oracle，步骤如下：

- docker

1. 将数据库驱动放到`/opt/gen/ext`下
2. 重启docker

- 本地运行

1. 将数据库驱动放到`gen/ext`下
2. 设置环境变量JAVA_HOME，指向java安装目录
3. 编辑`run.sh`文件，添加启动参数：`-Djava.ext.dirs=$JAVA_HOME/jre/lib/ext:./ext`

添加后如下：

`java -Djava.ext.dirs=$JAVA_HOME/jre/lib/ext:./ext -Dsolon.config.add=./conf/app.yml -Duser.timezone=Asia/Shanghai -jar -Xms64m -Xmx64m gen.jar`

执行`sh run.sh`启动


## 使用Mysql存储

默认使用SQLITE3存储，如果要使用mysql存储，需要做如下配置

- 创建数据库, [SQL文件](https://gitee.com/durcframework/code-gen/blob/master/db/mysql.sql)
- 打开app.yml文件，docker环境下在`/opt/gen/conf`下新建一个`app.yml`文件
- 添加/修改如下配置

```yaml
dbms:
  # 设置为true
  enable: true
  # 设置数据库地址，库名，连接账号
  host: localhost:3306
  database: gen
  username: root
  password: root
```

重启服务

## 其它

- [快速搭建SpringBoot+Mybatis应用](https://gitee.com/durcframework/code-gen/wikis/pages?sort_id=2478942&doc_id=27724)
- [更多模板](https://gitee.com/durcframework/code-gen/wikis/pages?sort_id=2979234&doc_id=27724)
- [代码生成器原理](https://gitee.com/durcframework/code-gen/wikis/pages?sort_id=3287812&doc_id=27724)

## 工程说明

- front：前端vue
- gen：后端服务
- db：数据库初始化文件
- script：辅助脚本

## 本地开发

- 运行`gen`下的`com.gitee.gen.App`（solon-web工程）
- 运行`front`下的前端项目，详见：[readme](./front/README.md)


## 参与贡献

欢迎贡献代码，完善功能，PR请提交到`pr`分支

## 自主构建

> 需要安装Maven3，Java8

- 自动构建[推荐]：

Mac/Linux系统可直接执行`build.sh`进行构建，构建结果在`dist/gen`文件夹下。

- 手动构建：
  
    > 前提：安装nodejs12，Java8，maven3

    `cd front`
    
    - 执行`npm install`
    - 执行`npm run build:prod`
    
    `cd ..`
    
    - 执行`mvn clean package`，生成结果在`target/gen`目录
    - 浏览器访问`http://localhost:6969/`
## 构建多平台

  - 安装多平台buildx : `docker buildx create --use --name m1_builder`

  - 查看并启动 builder 实例：`docker buildx inspect --bootstrap` 

  - 一次打包多平台镜像并推送
    - 打包多平台镜像并推送：`docker buildx build -t registry.cn-hangzhou.aliyuncs.com/tanghc/gen --platform=linux/amd64,linux/arm64/v8 . --push`
  - 或打包保存在本地
    - 打包x64平台镜像在本地：`docker buildx build -t registry.cn-hangzhou.aliyuncs.com/tanghc/gen --platform=linux/amd64 -o type=docker .`
    - 打包arm64平台镜像在本地：`docker buildx build -t registry.cn-hangzhou.aliyuncs.com/tanghc/gen --platform=linux/arm64/v8 -o type=docker .`
  - 快速运行：`docker  run -d -p 6969:6969 registry.cn-hangzhou.aliyuncs.com/tanghc/gen`

## 效果图

![模板编辑](https://foruda.gitee.com/images/1707207449352876614/1384dfef_332975.png "gen-tpl.png")

![代码生成](https://images.gitee.com/uploads/images/2020/0724/180853_df66e76d_332975.png "gen7.png")

![生成结果](https://images.gitee.com/uploads/images/2020/0731/085506_9d66201f_332975.png "gen8.png")
