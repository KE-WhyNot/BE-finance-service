package com.youthfi.finance.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "kis")
public class KisApiProperties {
    private List<KisKey> keys;

    public static class KisKey {

        private String appkey;

        private String appsecret;

        public String getAppkey() { return appkey; }

        public String getAppsecret() { return appsecret; }

        public void setAppkey(String appkey) { this.appkey = appkey; }

        public void setAppsecret(String appsecret) { this.appsecret = appsecret; }

    }

    public List<KisKey> getKeys() {

        if (keys == null || keys.isEmpty()) {
            List<KisKey> envKeys = loadKeysFromEnvironment();
            if (!envKeys.isEmpty()) {
                this.keys = envKeys;
            }
        }
        return keys;
    }

    public void setKeys(List<KisKey> keys) { this.keys = keys; }

    private List<KisKey> loadKeysFromEnvironment() {
        List<KisKey> result = new ArrayList<>();

        addIfPresent(result, 1);
        addIfPresent(result, 2);

        return result;
    }

    private void addIfPresent(List<KisKey> accumulator, int index) {
        String appkey = getEnv("KIS_API_KEY_" + index + "_APPKEY");
        String appsecret = getEnv("KIS_API_KEY_" + index + "_APPSECRET");
        if (appkey != null && appsecret != null) {
            KisKey key = new KisKey();
            key.setAppkey(appkey);
            key.setAppsecret(appsecret);
            accumulator.add(key);
        }
    }

    private String getEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }

}