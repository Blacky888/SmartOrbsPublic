package de.smartbot_studios.ggorbbot.utils.javautils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Config {

    private File configFile;

    private JsonObject config = new JsonObject();

    public Config(File configFile) {
        this.configFile = configFile;
        try {
            config = new JsonParser().parse(new BufferedReader(new FileReader(configFile))).getAsJsonObject();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    public static Config loadConfig(File configFile, boolean createIfNotExisting) {
        if(!configFile.exists()) {
            if(createIfNotExisting) {
                configFile.getParentFile().mkdirs();
                try {
                    configFile.createNewFile();
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonConfig = gson.toJson(new JsonObject());
                    FileWriter writer;
                    try {
                        writer = new FileWriter(configFile);
                        writer.write(jsonConfig);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                return new Config(configFile);
            } else {
                return null;
            }
        } else return new Config(configFile);
    }

    public static Config loadConfig(String configfile, boolean createIfNotExisting) {
        return loadConfig(new File(configfile), createIfNotExisting);
    }

    public JsonObject getConfig() {
        try {
            JsonObject build = new JsonParser().parse(new BufferedReader(new FileReader(configFile))).getAsJsonObject();
            config.entrySet().forEach(element -> {
                build.add(element.getKey(), element.getValue());
                config = build;
            });
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return config;
    }

    public void saveConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = gson.toJson(config);
        FileWriter writer;
        try {
            writer = new FileWriter(configFile);
            writer.write(jsonConfig);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
