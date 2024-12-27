package com.gitee.gen.gen;

import com.gitee.gen.config.ConnectConfig;
import com.gitee.gen.config.DbTypeConfig;
import org.noear.solon.core.util.ClassUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SQLServiceFactory {


    private static final Map<Integer, SQLService> SERVICE_CONFIG = new ConcurrentHashMap<>(16);

    public static SQLService build(GeneratorConfig generatorConfig) {
        Integer dbType = generatorConfig.getDbType();
        return SERVICE_CONFIG.computeIfAbsent(dbType, k -> {
            ConnectConfig connectConfig = DbTypeConfig.getInstance().getConnectConfig(dbType);
            String className = connectConfig.getServiceName();
            Class<?> aClass = ClassUtil.loadClass(className);
            if (aClass == null) {
                throw new RuntimeException("找不到数据库服务类:" + className);
            }
            return ClassUtil.newInstance(aClass);
        });
    }

}
