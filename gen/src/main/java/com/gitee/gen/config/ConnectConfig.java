package com.gitee.gen.config;

/**
 * @author 六如
 */
public class ConnectConfig {

    private String name;
    private String driver;
    private String url;
    private String serviceName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "ConnectConfig{" +
                "name='" + name + '\'' +
                ", driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
