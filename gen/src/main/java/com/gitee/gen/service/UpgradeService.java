package com.gitee.gen.service;

import com.gitee.gen.entity.ColumnInfo;
import com.gitee.gen.entity.SystemConfig;
import com.gitee.gen.mapper.UpgradeMapper;
import com.gitee.gen.util.SystemUtil;
import org.apache.ibatis.solon.annotation.Db;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.AppClassLoader;
import org.noear.solon.core.util.IoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;


/**
 * 升级
 *
 * @author tanghc
 */
@Component
public class UpgradeService {

    private static final Logger log = LoggerFactory.getLogger(UpgradeService.class);

    public static final String TABLE_DATASOURCE_CONFIG = "datasource_config";
    public static final String TABLE_TEMPLATE_CONFIG = "template_config";
    public static final String TABLE_TEMPLATE_GROUP = "template_group";
    public static final String TABLE_GENERATE_HISTORY = "generate_history";

    // 版本号，格式：xx.yy.zz，如：1.16.0,1.16.11
    private static final int CURRENT_VERSION = 200000;
    public static final String GEN_VERSION = "GEN_VERSION";
    private static final String DB_FILE_NAME = "gen.db";

    @Db
    private UpgradeMapper upgradeMapper;

    @Inject
    private SystemConfigService systemConfigService;

    @Inject("${dbms.enable:false}")
    private boolean useDbms;

    @Inject("${gen.db2.driverClassName}")
    private String driverClassName;

    @Inject("${dbms.database:gen}")
    private String dbName;

    public void init() {
        this.createTable("system_config");
        int oldVersion = getOldVersion();
        upgrade(oldVersion);
    }

    public void initDatabase() {
        if (useDbms) {
            log.info("使用DBMS，跳过sqlit3文件初始化");
            return;
        }
        File dbFile = getDbFile();
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (InputStream inputStream = AppClassLoader.getSystemResourceAsStream("gen_init.db")) {
                Path path = dbFile.toPath();
                log.info("初始化数据库文件, path={}", path);
                OutputStream outputStream = Files.newOutputStream(path);
                IoUtil.transferTo(inputStream, outputStream);
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("初始化数据库失败", e);
            }
        }
    }

    private File getDbFile() {
        String localDbPath = getLocalDbPath();
        log.info("SQLite3 Database file:{}", localDbPath);
        return new File(localDbPath);
    }

    public static String getLocalDbPath() {
        String dbPath = Solon.cfg().get("LOCAL_DB", "");
        if (Utils.isNotBlank(dbPath)) {
            return dbPath.replace("${user.home}", SystemUtil.getUserHome());
        } else {
            return SystemUtil.getBinPath() + "/" + DB_FILE_NAME;
        }
    }

    private int getOldVersion() {
        return systemConfigService.getValue(GEN_VERSION)
                .map(Integer::parseInt)
                .orElse(0);
    }

    private void writeNewVersion() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfigKey(GEN_VERSION);
        systemConfig.setConfigValue(String.valueOf(CURRENT_VERSION));
        systemConfig.setRemark("版本号");
        systemConfigService.saveOrUpdate(systemConfig);
    }

    /**
     * 升级
     */
    public void upgrade(int oldVersion) {
        if (oldVersion == 0) {
            upgradeV1_4_0();
            upgradeV1_4_12();
            upgradeV1_4_17();
            upgradeV1_5_2();
        }
        doUpgrade(oldVersion);
        writeNewVersion();
    }

    private void doUpgrade(int oldVersion) {
        upgradeV1_6_0(oldVersion);
        upgradeV2_0_0(oldVersion);
    }

    private void upgradeV2_0_0(int oldVersion) {
        if (oldVersion < 200000) {
            createTable("type_config", () -> {
                runSql("insert into type_config(db_type, base_type, box_type) values\n" +
                        "('bit', 'boolean', 'Boolean')\n" +
                        ",('boolean', 'boolean', 'Boolean')\n" +
                        ",('tinyint', 'int', 'Integer')\n" +
                        ",('smallint', 'int', 'Integer')\n" +
                        ",('int', 'int', 'Integer')\n" +
                        ",('bigint', 'long', 'Long')\n" +
                        ",('float', 'float', 'Float')\n" +
                        ",('double', 'double', 'Double')\n" +
                        ",('decimal', 'BigDecimal', 'BigDecimal')\n" +
                        ",('varchar', 'String', 'String')\n" +
                        ",('datetime', 'Date', 'Date')\n" +
                        ",('date', 'Date', 'Date')\n" +
                        ",('blob', 'String', 'String')\n" +
                        ",('jsonb', 'String', 'String')");
            });
        }
    }

    private void upgradeV1_6_0(int oldVersion) {
        if (oldVersion < 101600) {
            this.addColumn(TABLE_DATASOURCE_CONFIG, "db_desc", "varchar(64)");
            this.addColumn(TABLE_DATASOURCE_CONFIG, "db_group_name", "varchar(64)");
        }
    }

    private void upgradeV1_4_17() {
        this.addColumn(TABLE_TEMPLATE_CONFIG, "folder", "varchar(64)");
    }

    private void upgradeV1_4_12() {
        this.addColumn(TABLE_DATASOURCE_CONFIG, "schema_name", "varchar(100)");
    }

    /**
     * 升级v1.4.0
     */
    private void upgradeV1_4_0() {
        this.createTable(TABLE_GENERATE_HISTORY);
        boolean isCreate = this.createTable(TABLE_TEMPLATE_GROUP);
        if (isCreate) {
            runSql("INSERT INTO `template_group` (`id`, `group_name`, `is_deleted`) VALUES (1,'default',0)");
        }

        this.addColumn(TABLE_DATASOURCE_CONFIG, "package_name", "varchar(100)");
        this.addColumn(TABLE_DATASOURCE_CONFIG, "del_prefix", "varchar(200)");
        this.addColumn(TABLE_DATASOURCE_CONFIG, "group_id", "int");

        this.addColumn(TABLE_TEMPLATE_CONFIG, "group_id", "int");
        this.addColumn(TABLE_TEMPLATE_CONFIG, "group_name", "varchar(100)");
        runSql("update template_config set group_id=1,group_name='default' where group_id IS NULL");
    }

    /**
     * 升级v1.5.2
     * 1、前端：修复修改数据源时候不管什么数据库都带出oracle数据库
     * 2、前端：新增oracle类型数据库 数据库角色可以为空
     * 3、前端：修复数据库类型为oracle数据库时候测试连接服务器字段展示undefined问题
     * 4、前后端：新增author作者名配置，方便模板中插入作者
     * 5、后端：修复postgresql数据库表如果没有设置主键无法获取列数组问题
     */
    private void upgradeV1_5_2() {
        this.addColumn(TABLE_DATASOURCE_CONFIG, "author", "varchar(255)");
    }

    private void runSql(String sql) {
        upgradeMapper.runSql(sql);
    }

    /**
     * 添加表字段
     *
     * @param tableName  表名
     * @param columnName 字段名
     * @param type       字段类型，如：varchar(128),text,integer
     * @return 返回true，插入成功
     */
    public boolean addColumn(String tableName, String columnName, String type) {
        if (!isColumnExist(tableName, columnName)) {
            try {
                if (isMysql()) {
                    upgradeMapper.addColumnMysql(tableName, columnName, type);
                } else if (isDm()) {
                    upgradeMapper.addColumnDm(tableName, columnName, type);
                } else {
                    upgradeMapper.addColumn(tableName, columnName, type);
                }
            } catch (Exception e) {
                log.error("add column error, tableName={}, columnName={}, type={}",
                        tableName, columnName, type, e);
            }
            return true;
        }
        return false;
    }

    /**
     * 创建表
     *
     * @param tableName 表名
     * @return 创建成功返回true
     */
    public boolean createTable(String tableName, Runnable afterCreated) {
        if (!isTableExist(tableName)) {
            String sql = this.loadDDL(tableName);
            upgradeMapper.runSql(sql);
            if (afterCreated != null) {
                afterCreated.run();
            }
            return true;
        }
        return false;
    }

    public boolean createTable(String tableName) {
        return createTable(tableName, null);
    }

    private String loadDDL(String tableName) {
        String tmp_dm = "ddl_%s_dm.txt";
        String tmp_mysql = "ddl_%s_mysql.txt";
        String tmp_sqlite = "ddl_%s_sqlite.txt";
        String tmp = isDm() ? tmp_dm : (isMysql() ? tmp_mysql : tmp_sqlite);
        String filename = "upgrade/" + String.format(tmp, tableName);
        InputStream inputStream = AppClassLoader.getSystemResourceAsStream(filename);
        if (inputStream == null) {
            throw new RuntimeException("找不到文件：" + filename);
        }
        try {
            return IoUtil.transferToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("打开文件出错", e);
        }
    }

    /**
     * 判断列是否存在
     *
     * @param tableName  表名
     * @param columnName 列名
     * @return true：存在
     */
    public boolean isColumnExist(String tableName, String columnName) {
        List<ColumnInfo> columnInfoList = isDm() ? upgradeMapper.listColumnInfoDm(tableName) :
                (isMysql() ? upgradeMapper.listColumnInfoMysql(tableName, dbName) : upgradeMapper.listColumnInfo(tableName));
        return columnInfoList
                .stream()
                .anyMatch(columnInfo -> Objects.equals(columnInfo.getName(), columnName));
    }

    /**
     * 表是否存在
     *
     * @param tableName
     * @return
     */
    public boolean isTableExist(String tableName) {
        List<String> tableNameList;
        if (isMysql()) {
            tableNameList = upgradeMapper.listTableNameMysql();
        } else if (isDm()) {
            tableNameList = upgradeMapper.listTableNameDm();
        } else {
            tableNameList = upgradeMapper.listTableName();
        }
        return tableNameList != null && tableNameList.contains(tableName);
    }

    private boolean isMysql() {
        return useDbms && this.driverClassName.contains("mysql");
    }

    private boolean isDm() {
        return useDbms && this.driverClassName.contains("dm");
    }

}
