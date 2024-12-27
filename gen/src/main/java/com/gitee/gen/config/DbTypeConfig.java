package com.gitee.gen.config;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

import java.util.Map;

/**
 * @author 六如
 */
@Configuration
public class DbTypeConfig {

    @Inject("${connect}")
    private Map<Integer, ConnectConfig> connectConfigMap;

    public static DbTypeConfig getInstance() {
        return Solon.context().getBean(DbTypeConfig.class);
    }

    public ConnectConfig getConnectConfig(Integer type) {
        return connectConfigMap.get(type);
    }

    public Map<Integer, ConnectConfig> getConnectConfigMap() {
        return connectConfigMap;
    }

    public void setConnectConfigMap(Map<Integer, ConnectConfig> connectConfigMap) {
        this.connectConfigMap = connectConfigMap;
    }
}
