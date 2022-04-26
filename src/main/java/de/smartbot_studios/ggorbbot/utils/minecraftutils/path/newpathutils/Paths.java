package de.smartbot_studios.ggorbbot.utils.minecraftutils.path.newpathutils;

import java.util.LinkedList;
import java.util.Random;

public class Paths {

    private static Paths instance;
    private static final LinkedList<Path> paths = new LinkedList<>();

    private Paths() {
        //todo mehr paths erstellen und so wie hier adden
        paths.add(p1());
    }

    public static Path randomOHPath() {
        if(instance == null) instance = new Paths();

        if(paths.size() == 1) return paths.get(0);
        else return paths.get(new Random().nextInt(paths.size() - 1));
    }

    public static Path p1() {
        return new Path() {
            @Override
            public void path() {
                goTo(new Point(167, -26));
                goTo(new Point(170, -41));
            }
        };
    }
}
