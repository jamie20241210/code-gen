package com.gitee.gen.controller;

import com.gitee.gen.common.Action;
import com.gitee.gen.common.Result;
import com.gitee.gen.config.DbTypeConfig;
import com.gitee.gen.entity.DatasourceConfig;
import com.gitee.gen.gen.DBConnect;
import com.gitee.gen.gen.GeneratorConfig;
import com.gitee.gen.gen.SQLService;
import com.gitee.gen.gen.SQLServiceFactory;
import com.gitee.gen.gen.TableDefinition;
import com.gitee.gen.service.DatasourceConfigService;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tanghc
 */
@Controller
@Mapping("datasource")
public class DatasourceConfigController {

    @Inject
    private DatasourceConfigService datasourceConfigService;

    @Mapping("/add")
    public Result add(DatasourceConfig datasourceConfig) {
        datasourceConfigService.insert(datasourceConfig);
        return Action.ok();
    }

    @Mapping("/list")
    public Result list() {
        List<DatasourceConfig> datasourceConfigList = datasourceConfigService.listAll();
        return Action.ok(datasourceConfigList);
    }

    @Mapping("/update")
    public Result update(DatasourceConfig datasourceConfig) {
        datasourceConfigService.update(datasourceConfig);
        return Action.ok();
    }

    @Mapping("/del")
    public Result del(DatasourceConfig datasourceConfig) {
        datasourceConfigService.delete(datasourceConfig);
        return Action.ok();
    }

    @Mapping("/table/{id}")
    public Result listTable(@Path("id") int id) {
        DatasourceConfig dataSourceConfig = datasourceConfigService.getById(id);
        GeneratorConfig generatorConfig = GeneratorConfig.build(dataSourceConfig);
        SQLService service = SQLServiceFactory.build(generatorConfig);
        List<TableDefinition> list = service.getTableSelector(generatorConfig).getSimpleTableDefinitions();
        return Action.ok(list);
    }


    @Mapping("/test")
    public Result test(DatasourceConfig datasourceConfig) {
        String error = DBConnect.testConnection(GeneratorConfig.build(datasourceConfig));
        if (error != null) {
            return Action.err(error);
        }
        return Action.ok();
    }

    @Mapping("/dbtype")
    public Result dbType(DatasourceConfig datasourceConfig) {
        List<DbTypeShow> dbTypeShowList = DbTypeConfig.getInstance()
                .getConnectConfigMap()
                .entrySet()
                .stream()
                .map(entry -> new DbTypeShow(entry.getValue().getName(), entry.getKey()))
                .collect(Collectors.toList());
        return Action.ok(dbTypeShowList);
    }

    private static class DbTypeShow {
        private String label;
        private Integer dbType;

        public DbTypeShow(String label, Integer dbType) {
            this.label = label;
            this.dbType = dbType;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Integer getDbType() {
            return dbType;
        }

        public void setDbType(Integer dbType) {
            this.dbType = dbType;
        }
    }


}
