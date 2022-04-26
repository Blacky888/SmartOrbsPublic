package de.smartbot_studios.ggorbbot.utils.minecraftutils.path.newpathutils;

public class Vec2d {

    private double x, y;

    public Vec2d() {}

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return the Vec2d from p to p1
     */
    public static Vec2d fromPoints(Point p, Point p1) {
        return new Vec2d(p1.getX() - p.getX(), p1.getY() - p.getY());
    }


    /**
     *
     * @param f the value the length of the Vec2d is going to be multiplied with
     * @return Vec2d multiplied with f
     */
    public Vec2d stretch(double f) {
        return new Vec2d(this.x * f, this.y * f);
    }

    /**
     *
     * @return Vec2d with length 1
     */
    public Vec2d getUnitVec() {
        return stretch(1 / getLength());
    }

    /**
     *
     * @return Vec2d being orthogonal on the left side in moving-direction
     */
    public Vec2d getLeftsidedOrthogonalVec() {
        return new Vec2d(
                (-1) * MathUtils.getPrefix(this.y),
                this.x / Math.abs(this.y)
        );
    }

    /**
     *
     * @param v Vec2d to be summed up
     * @return the sum of both Vec2ds
     */
    public Vec2d sum(Vec2d v) {
        this.x += v.getX();
        this.y += v.getY();
        return this;
    }

    /**
     *
     * @param v Vec2d to be subbed
     * @return difference of bot Vec2ds
     */
    public Vec2d sub(Vec2d v) {
        this.x -= v.getX();
        this.y -= v.getY();
        return this;
    }


    /**
     *
     * @return Scalarproduct of bot Vec2ds
     */
    public double getScalarProdukt(Vec2d v) {
        return this.x * v.getX() + this.y * v.getY();
    }

    /**
     *
     * @param v the Vec2d the angle should be found to
     * @return the angle between both Vec2ds
     */
    public double getAngleToVec(Vec2d v) {
        return Math.toDegrees(Math.acos(getScalarProdukt(v) / (getLength() * v.getLength())));
    }

    /**
     *
     * @return the length of the Vec2d
     */
    public double getLength() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point toPoint() {
        return new Point(this.x, this.y);
    }
}
