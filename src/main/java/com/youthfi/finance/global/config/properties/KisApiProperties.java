package com.youthfi.finance.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "kis")
public class KisApiProperties {
    private List<KisKey> keys;

    public static class KisKey {
        private String appkey;
        private String appsecret;
        public String getAppkey() { return appkey; }
        public void setAppkey(String appkey) { this.appkey = appkey; }
        public String getAppsecret() { return appsecret; }
        public void setAppsecret(String appsecret) { this.appsecret = appsecret; }
    }

    public List<KisKey> getKeys() { return keys; }
    public void setKeys(List<KisKey> keys) { this.keys = keys; }
}