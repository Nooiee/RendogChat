package com.nooiee.rendogchat.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    public static boolean BasicFalse = false;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/rendogrpg_config.json");

    public static class Config {
        public String chatTabName = "Tab";
        public List<Condition> conditions = new ArrayList<>();

        public static class Condition {
            public String mode; // 포함 or 제외
            public String text1;
            public String text2;
            public String text3;

            public Condition(String mode, String text1, String text2, String text3) {
                this.mode = mode;
                this.text1 = text1;
                this.text2 = text2;
                this.text3 = text3;
            }
        }
    }

    private static Config config;

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                config = GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                e.printStackTrace();
                config = new Config();
            }
        } else {
            config = new Config();
            saveConfig();
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getConfig() {
        return config;
    }

    // chatTabName 접근자 메서드
    public static String getChatTabName() {
        return config.chatTabName;
    }

    // chatTabName 설정자 메서드
    public static void setChatTabName(String name) {
        if (config != null) {
            config.chatTabName = name;
            saveConfig(); // 설정 변경 후 자동으로 저장
        }
    }
}
