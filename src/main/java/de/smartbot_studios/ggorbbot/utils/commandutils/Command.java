package de.smartbot_studios.ggorbbot.utils.commandutils;

public abstract class Command {
    private String name;

    public Command(String name) {
        this.name = name;
    }

    public abstract void execute(String[] args);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
