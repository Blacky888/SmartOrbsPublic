package de.smartbot_studios.ggorbbot.utils.javautils;

import java.util.HashMap;
import java.util.Map;

public class ToStringHelper {

    private Map<String, Object> map = new HashMap<>();
    private String name = "name";

    public ToStringHelper() {}

    public ToStringHelper withName(String name) {
        this.name = name;
        return this;
    }

    public ToStringHelper addProperty(String name, Object value) {
        map.put(name, value);
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("={");

        String[] comparable = new String[map.entrySet().size() * 2];

        int i = 0;
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            StringBuilder builder = new StringBuilder();
            builder.append(entry.getKey()).append("=").append(entry.getValue());
            comparable[i] = builder.toString();
            i++;
            comparable[i] = ", ";
            i++;
        }

        comparable[comparable.length - 1] = "";

        for (String value : comparable) {
            stringBuilder.append(value);
        }
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
