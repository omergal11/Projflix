package com.example.netflixandroid;

import android.content.Context;

import com.example.netflix.R;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private Context context;
    public ConfigManager(Context context) {
        this.context = context;
    }
//read properties
    public Properties loadConfig() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.config);
            properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public String getIpAddress() {
        Properties properties = loadConfig();
        return properties.getProperty("ip_address");
    }

    public String getPort() {
        Properties properties = loadConfig();
        return properties.getProperty("port");
    }
    public String getSecretData() {
        Properties properties = loadConfig();
        return properties.getProperty("jwt_secret");
    }
}