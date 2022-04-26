package de.smartbot_studios.ggorbbot.utils.javautils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class RequireNotNull {

    private Map<String, Object> requireNotNullList = new HashMap<>();

    public RequireNotNull() {}

    public void requireNotNull(String name, Object object) {
        this.requireNotNullList.put(name, object);
    }

    public boolean check() {
        AtomicBoolean toReturn = new AtomicBoolean(true);
        requireNotNullList.forEach((name, object) -> {
            if(object == null) toReturn.set(false);
        });
        return toReturn.get();
    }

    public List<String> getNulls() {
        List<String> toReturn = new ArrayList<>();
        requireNotNullList.forEach((name, object) -> {
            if(object == null) {
                toReturn.add(name);
            }
        });
        return toReturn;
    }
}
