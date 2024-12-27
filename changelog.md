# changelog

## 2.0.5

- 新增`${table.columnCount}`变量,返回表字段数量

## 2.0.4

- 使用jdkhttp

## 2.0.3

- 连接方式管理放在配置文件中
- solon更新到2.8.5


```yaml
# 连接方式管理, {HOST},{PORT},{DB_NAME} 表示占位符
connect:
  "1":
    name: MySQL
    driver: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://{HOST}:{PORT}/{DB_NAME}?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    serviceName: com.gitee.gen.gen.mysql.MySqlService
  "2":
    name: Oracle
    driver: oracle.jdbc.driver.OracleDriver
    url: jdbc:oracle:thin:@{HOST}:{PORT}{DB_NAME}
    serviceName: com.gitee.gen.gen.oracle.OracleService
  "3":
    name: "SQL Server"
    driver: com.microsoft.sqlserver.jdbc.SQLServerDriver
    url: jdbc:sqlserver://{HOST}:{PORT};DatabaseName={DB_NAME};trustServerCertificate=true
    serviceName: com.gitee.gen.gen.sqlserver.SqlServerService
  "4":
    name: PostgreSQL
    driver: org.postgresql.Driver
    url: jdbc:postgresql://{HOST}:{PORT}/{DB_NAME}
    serviceName: com.gitee.gen.gen.postgresql.PostgreSqlService
  "5":
    name: "达梦"
    driver: dm.jdbc.driver.DmDriver
    url: jdbc:dm://{HOST}:{PORT}/{DB_NAME}
    serviceName: com.gitee.gen.gen.dm.DmService
```

## 2.0.2

- 支持其它数据库代码生成，具体参考项目首页说明

## 2.0.1

- 新增`${context.dbNameCamel}` 数据库名驼峰，如：shopOrder
- 新增`${context.dbNameClean}` 数据库名驼峰，如：shoporder

## 2.0.0

- 【新增】底层框架改为solon，启动更快，内存更小（64M）
- 【新增】类型配置功能
- 【优化】统一变量命名

1.x升级到2.0

将`gen.jar`、`conf`、`lib`、`dist`、`run.sh`放入到gen目录下并覆盖

非MySql数据库需要将驱动包（如ojdbc6.jar）放到`gen/lib`下

```text
如果使用内置数据库（gen.db）则不需要改动，可以把启动内存改成64M

如果使用MYSQL数据库

打开conf/app.yml

注释sqlite3数据库连接，打开下面的mysql连接
```

执行`run.sh`启动

## 1.6.3

- 字段长度信息增加支持pgsql [pr](https://gitee.com/durcframework/code-gen/pulls/32)

## 1.6.2

- 【优化】修改字符串格式化使用中文括号导致乱码 [pr](https://gitee.com/durcframework/code-gen/pulls/30)

## 1.6.1

- 【优化】达梦数据库类型映射java类型改为使用DmTypeFormatter [PR](https://gitee.com/durcframework/code-gen/pulls/26)
- 【优化】解决PostgreSql表同时存在主键与唯一索引造成生成的列重复出现 [PR](https://gitee.com/durcframework/code-gen/pulls/27)
- 【优化】mysql增加column的排序 [PR](https://gitee.com/durcframework/code-gen/pulls/29)、[#I5Z2FF](https://gitee.com/durcframework/code-gen/issues/I5Z2FF)、[#I4RUJP](https://gitee.com/durcframework/code-gen/issues/I4RUJP)

## 1.6.0

本次更新主要来自[PR](https://gitee.com/durcframework/code-gen/pulls/24)，再次感谢这位小伙伴的分享。

- 【新增】生成代码按钮 增加在新标签页打开
- 【新增】数据源配置 新增分组名称（可为空）；选择数据源下拉框 根据分组名称进行分组
- 【新增】增加Java类名驼峰转横杠的变量
- 【新增】模板编辑页面监听Ctrl+S按键，可快捷保存
- 【新增】新增模板复制功能
- 【新增】新增数据库描述字段（可为空），选择数据源时增加描述字段的展示
- 【新增】模板的文件目录可以使用变量；生成的压缩包内文件带‘.’的包名有层级
- 【优化】生成代码时 切换模板下拉框 清除先前被选中的模板
- 【优化】前端 配置数据源 去掉数据库描述不能为空的限制
- 【优化】前端代码编辑框增加代码样式选择，增加快捷键提示，美化搜索和替换框

## 1.5.9

- 支持达梦数据库 [pr](https://gitee.com/durcframework/code-gen/pulls/22)

## 1.5.8

- 修复oracle数据源时，生成代码页面加载不到表列表问题 [pr](https://gitee.com/durcframework/code-gen/pulls/18)  [#I4AIPV](https://gitee.com/durcframework/code-gen/issues/I4AIPV)

## 1.5.7

- sqlite在mac m1架构下无法运行问题。
- 修复连接Oracle数据库生成代码时，查询字段sql报错问题

## 1.5.6

- 【新增】常用变量
```
${context.randomInt}：int随机数
${context.randomLong}：long随机数
${context.randomBoolean}：boolean随机
${context.uuid}：生成uuid
${context.nextId}：生成唯一id，基于雪花算法
```

- 【新增】新增velocity变量， `${column.isNullable}`：字段是否可空,返回boolean [#I4QAMO](https://gitee.com/durcframework/code-gen/issues/I4QAMO)
- 【新增】新增velocity变量， `${column.columnNameLF}`：表中字段名首字母小写 [#I4QJJY](https://gitee.com/durcframework/code-gen/issues/I4QJJY)
- 【优化】多个模板组切换问题 [#I4QFDO](https://gitee.com/durcframework/code-gen/issues/I4QFDO)
- 【修复】修复升级1.4.0表名重复问题 [#I4LQJW](https://gitee.com/durcframework/code-gen/issues/I4LQJW)

## 1.5.5

- 优化窗口关闭

## 1.5.4

- 模板新增${column.label}变量

## 1.5.3

1. 前端：修复修改数据源时候不管什么数据库都带出oracle数据库
2. 前端：新增oracle类型数据库数据库角色可以为空
3. 前端：修复数据库类型为oracle数据库时候测试连接服务器字段展示undefined问题
4. 前后端：新增author作者名配置，方便模板中插入作者
5. 后端：修复postgresql数据库表如果没有设置主键无法获取列数组问题

## 1.5.2

- 支持`SqlServer`字段长度信息（`${column.maxLength}`，`${column.scale}`）
- 修复没有主键无法生成问题

## 1.5.1

- 表字段信息，增加字段长度信息（`${column.maxLength}`，`${column.scale}`），目前只支持 mysql 和 oracle. [PR](https://gitee.com/durcframework/code-gen/pulls/12)

## 1.5.0

**注意：** 

> 从1.5.0开始前端打包后的文件不再放到`resource/public`下，而是单独放到外面`gen/view`下。
> 理论上前端打包后的文件也属于编译后的文件，同class文件一样，不能提交到git，git只存放源码，这样在合并PR的时候会减少文件的冲突。

- 支持oracle服务名和SID连接 [pr](https://gitee.com/durcframework/code-gen/pulls/11)
- 支持模板指定目录名称 by Mario Luo
- 支持模板导入导出 by Mario Luo
- 优化表前缀删除功能 by Mario Luo
- 调整静态文件编译目录

## 1.4.16

- 修复表名前缀替换问题 [pr](https://gitee.com/durcframework/code-gen/pulls/9/files)

## 1.4.15

- 修复PostgreSQL的numeric字段映射问题

## 1.4.14

- 修复`不支持“variant”数据类型`错误 [#I3CSLO](https://gitee.com/durcframework/code-gen/issues/I3CSLO)
- 修复数据库名字带点.生成错误问题 [#I2EBRL](https://gitee.com/durcframework/code-gen/issues/I2EBRL)

## 1.4.13

- 优化类型转换

## 1.4.12

- PostgreSQL数据源可指定schema（模板中可使用：`${table.schema}`）

## 1.4.11

- 优化模板管理交互

## 1.4.10

- 修复PostgreSQL数值类型映射问题

## 1.4.9

- 新增`${context.delPrefix}变量（删除前缀）`

## 1.4.8

- 支持PostgreSQL的jsonb类型

## 1.4.7

- 修复PostgreSQL生成多个相同字段问题
- 优化PostgreSQL精度类型
- 优化Mysql表查询 [#I3C57N](https://gitee.com/durcframework/code-gen/issues/I3C57N)
- 修复可以看到删除掉的模板 [#I3AX6E](https://gitee.com/durcframework/code-gen/issues/I3AX6E)


## 1.4.6

- 模板新增时间变量

```
${context.datetime}：日期时间，年月日时分秒
${context.date}：日期，年月日
${context.time}：时间，时分秒
```

## 1.4.5

- 可以格式化xml代码（application.properties中设置`gen.format-xml=true`）

## 1.4.4

- jtds驱动替换成sqljdbc4

## 1.4.3

- 【优化】模板编辑页面滚动条下拉可固定右侧变量区域
- 【优化】模板编辑页面保存按钮优化

## 1.4.2

- 修复oracle下同一库不同用户无法显示表问题
- 修复模板名称重复问题
- 数据源下拉框显示用户名
- 修复${table.hasBigDecimalField}不生效问题

## 1.4.1

- 修复数据库字段单个单词全部大写的情况下，实体字段会全部大写
- 修复oracle的number类型映射问题
- 生成代码页面自动选择上一次数据源
- 修复新建模版template_config里group_name会丢失问题

## 1.4.0

- 增加模板组，可以根据需求选择不同的组的模板
- 可以给数据源配置默认的包名、删除前缀、以及模板组
- 表名检索忽略大小写
- 新增生成历史，可以对历史配置再次生成代码

## 1.3.5

- 修复mybatis的jdbcType隐射BIGINT错误
- 修复oracle隐射number类型错误

## 1.3.4

- 修复Mysql表名为关键字生成报错问题
- 优化交互

## 1.3.3

- 修复生成结果无法过滤文件错误

## 1.3.2

- 优化启动脚本

## 1.3.1

- 修复筛选表格后全选选中所有数据BUG

## 1.3.0

- 支持C#变量，可生成C#文件
- 支持表过滤
- 支持数据源复制

## 1.2.2

- 优化模板编辑界面

## 1.2.1

- 修复保存模板时的错误
- 模板编辑器新增行号
- 新增admin-vue-CRUD模板 [doc](https://gitee.com/durcframework/code-gen/wikis/pages?sort_id=2688205&doc_id=27724)

## 1.2.0

- 支持PostgreSQL

## 1.1.4

- 修改mysql驱动为com.mysql.cj.jdbc.Driver
- 修改mysql jdbc连接串,增加serverTimezone=Asia/Shanghai
- 修复数据库名(dbName)包含'-'的异常.如(platform-cloud)

## 1.1.3

- 新增${context.dbName}数据库名称变量

## 1.1.2

- 调整生成结果树状展示

## 1.1.1

- 优化下划线字段转驼峰字段算法 [I1PDHW](https://gitee.com/durcframework/code-gen/issues/I1PDHW)

## 1.1.0

- 结果页面可下载代码
- 优化Connection连接数
- 修复测试连接问题

## 1.0.2

- 优化交互

## 1.0.1

- 新增复制代码功能

## 1.0.0

- 重构前端页面，采用elementUI
- 优化了生成步骤
- 优化了模板编辑页面
