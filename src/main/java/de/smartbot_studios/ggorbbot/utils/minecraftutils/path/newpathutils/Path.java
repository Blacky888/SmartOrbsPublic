package de.smartbot_studios.ggorbbot.utils.minecraftutils.path.newpathutils;

import java.util.LinkedList;

public abstract class Path {

    private boolean executing;

    private final LinkedList<Point> points = new LinkedList<>();

    public Path() {}

    public void goTo(Point p) {
        if (executing) return;

        this.points.add(p);
    }

    public boolean hasNext() {
        return !this.points.isEmpty();
    }

    public Point getNextPoint() {
        return this.points.poll();
    }

    public void clear() {
        if(executing) return;

        this.points.clear();
    }

    public void curvePercent(Point beginning, Point target, double smoothness, Curve c) {
        if (executing) return;

        smoothness = Math.abs(smoothness);
        if(!(smoothness <= 1)) return;
        smoothness *= Integer.MAX_VALUE;
        int curve;
        System.out.println(c);
        if (c.equals(Curve.LEFT)) curve = 1;
        else curve = -1;
        System.out.println(curve);

        Vec2d way = Vec2d.fromPoints(beginning, target);
        Vec2d orth = way.getLeftsidedOrthogonalVec().getUnitVec().stretch(smoothness).stretch(curve);
        Point midpoint = way.stretch(0.5).sum(beginning.toVec()).sum(orth).toPoint();
        System.out.println(orth.getX() + " " + orth.getY());
        System.out.println(midpoint.getX() + " " + midpoint.getY());

        Vec2d r = Vec2d.fromPoints(midpoint, target);

        Circle circle = new Circle(midpoint, r.getLength());

        double angle = circle.getAngle(beginning);
        double angle1 = circle.getAngle(target);

        if(curve == 1) {
            circle.getPoints(angle, angle1).forEach(this::goTo);
        } else circle.getPoints(angle1, angle).forEach(this::goTo);
    }

    public void curve(Point beginning, Point target, int smoothness, Curve c) {
        if (executing) return;

        smoothness = Math.abs(smoothness);
        int curve;
        System.out.println(c);
        if (c.equals(Curve.LEFT)) curve = 1;
        else curve = -1;
        System.out.println(curve);

        Vec2d way = Vec2d.fromPoints(beginning, target);
        Vec2d orth = way.getLeftsidedOrthogonalVec().getUnitVec().stretch(smoothness).stretch(curve);
        Point midpoint = way.stretch(0.5).sum(beginning.toVec()).sum(orth).toPoint();
        System.out.println(orth.getX() + " " + orth.getY());
        System.out.println(midpoint.getX() + " " + midpoint.getY());

        Vec2d r = Vec2d.fromPoints(midpoint, target);

        Circle circle = new Circle(midpoint, r.getLength());

        double angle = circle.getAngle(beginning);
        double angle1 = circle.getAngle(target);

        if(curve == 1) {
            circle.getPoints(angle, angle1).forEach(this::goTo);
        } else circle.getPoints(angle1, angle).forEach(this::goTo);
    }

    public void build() {
        clear();
        path();

        executing = true;
    }

    public abstract void path();
}
