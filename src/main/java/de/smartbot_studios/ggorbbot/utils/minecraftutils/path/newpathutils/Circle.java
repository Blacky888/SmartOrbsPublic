package de.smartbot_studios.ggorbbot.utils.minecraftutils.path.newpathutils;

import java.util.LinkedList;

public class Circle {

    private Point midpoint;
    private double r;
    public Circle(Point midpoint, double r) {
        this.midpoint = midpoint;
        this.r = Math.abs(r);
    }


    /**
     *
     * @param p the new midpoint of the circle
     */
    public void move(Point p) {
        this.midpoint = p;
    }

    /**
     *
     * @param r the new r of the circle
     */
    public void setR(double r) {
        this.r = Math.abs(r);
    }

    /**
     *
     * @return the scope of the circle
     */
    public double getScope() {
        return 2 * Math.PI * this.r;
    }

    /**
     *
     * @param p the point the angle should be found to
     * @return the angle for the p
     */
    public double getAngle(Point p) {
        double angle = Math.toDegrees(
                Math.acos((p.getX() - this.midpoint.getX()) / this.r) * MathUtils.getPrefix(p.getY() - this.midpoint.getY())
        );
        return angle < 0 ? angle + 360: angle;
    }


    /**
     *
     * @param angle the angle of the point
     * @return
     */
    public Point getPoint(double angle) {
        double rad = Math.toRadians(angle);
        double x = MathUtils.round(midpoint.getX() + r * Math.cos(rad), 2);
        double y = MathUtils.round(midpoint.getY() + r * Math.sin(rad), 2);
        return new Point(x, y);
    }

    /**
     *
     * @return List of points between the two given angles
     */
    public LinkedList<Point> getPoints(double angle1, double angle2) {
        LinkedList<Point> toReturn = new LinkedList<>();

        Vec2d v = Vec2d.fromPoints(this.midpoint, getPoint(angle1));
        Vec2d v1 = Vec2d.fromPoints(this.midpoint, getPoint(angle2));

        double a = v.getAngleToVec(v1);
        double props = a / 360;
        double s = getScope() * props;
        int amount = (int) (2 * s);
        double angle = a / amount;

        for (int i = 0; i <= amount; i++) {
            toReturn.add(getPoint(angle1 + i * angle));
        }

        return toReturn;
    }
}
