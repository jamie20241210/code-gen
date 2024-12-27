package com.gitee.gen.service;

import com.gitee.gen.config.ConnectConfig;
import com.gitee.gen.config.DbTypeConfig;
import com.gitee.gen.entity.DatasourceConfig;
import com.gitee.gen.mapper.DatasourceConfigMapper;
import org.apache.ibatis.solon.annotation.Db;
import org.noear.solon.annotation.Component;

import java.util.List;

/**
 * @author tanghc
 */
@Component
public class DatasourceConfigService {

    @Db
    private DatasourceConfigMapper datasourceConfigMapper;

    public DatasourceConfig getById(int id) {
        return datasourceConfigMapper.getById(id);
    }

    public List<DatasourceConfig> listAll() {
        return datasourceConfigMapper.listAll();
    }

    public void insert(DatasourceConfig templateConfig) {
        templateConfig.setIsDeleted(0);
        ConnectConfig connectConfig = DbTypeConfig.getInstance().getConnectConfig(templateConfig.getDbType());
        if (connectConfig != null) {
            templateConfig.setDriverClass(connectConfig.getDriver());
        }
        datasourceConfigMapper.insert(templateConfig);
    }

    public void update(DatasourceConfig templateConfig) {
        datasourceConfigMapper.update(templateConfig);
    }

    public void delete(DatasourceConfig templateConfig) {
        datasourceConfigMapper.delete(templateConfig);
    }
}
