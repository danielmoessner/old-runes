package sample.java3d.simple3d;

import javafx.geometry.Point3D;

public class StringVector {

    private String x;
    private String y;
    private String z;

    // Constructors
    StringVector(String x, String y, String z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    StringVector() {
        this("x", "y", "z");
    }

    // swaps the given values. In the order of this StringVector
    private double apply(String s, double x, double y, double z) {
        switch (s) {
            case "x":
                return x;
            case "-x":
                return -x;
            case "y":
                return y;
            case "-y":
                return -y;
            case "z":
                return z;
            case "-z":
                return -z;
            default:
                System.out.println("error");
                return 0;
        }
    }

    public double applyX(double x, double y, double z) {
        return apply(this.x, x, y, z);
    }

    public double applyY(double x, double y, double z) {
        return apply(this.y, x, y, z);
    }

    public double applyZ(double x, double y, double z) {
        return apply(this.z, x, y, z);
    }

    public double applyX(Point3D p) {
        return applyX(p.getX(), p.getY(), p.getZ());
    }

    public double applyY(Point3D p) {
        return applyY(p.getX(), p.getY(), p.getZ());
    }

    public double applyZ(Point3D p) {
        return applyZ(p.getX(), p.getY(), p.getZ());
    }

    // Cross Product
    public StringVector specialCrossProduct(int x, int y, int z) {
        if ((x != 0 && Math.abs(x) != 1) || (y != 0 && Math.abs(y) != 1) || (z != 0 && Math.abs(z) != 1) || (Math.abs(x + y + z) != 1))
            return null;

        int ax = getNumber(this.x);
        int ay = getNumber(this.y);
        int az = getNumber(this.z);

        if (Math.abs(x) == 1) {
            return new StringVector(
                    this.x,
                    getString(az * x - ax * z),
                    getString(ax * y - ay * x)
            );
        } else if (Math.abs(y) == 1) {
            return new StringVector(
                    getString(ay * z - az * y),
                    this.y,
                    getString(ax * y - ay * x)
            );
        } else {
            return new StringVector(
                    getString(ay * z - az * y),
                    getString(az * x - ax * z),
                    this.z
            );
        }
    }

    public StringVector specialCrossProduct(Point3D p) {
        return specialCrossProduct((int) p.getX(), (int) p.getY(), (int) p.getZ());
    }

    // Convert String/int to int/String
    private String getString(int k) {
        switch (k) {
            case 1:
                return "x";
            case -1:
                return "-x";
            case 2:
                return "y";
            case -2:
                return "-y";
            case 3:
                return "z";
            case -3:
                return "-z";
            default:
                return "";
        }
    }

    private int getNumber(String k) {
        switch (k) {
            case "x":
                return 1;
            case "-x":
                return -1;
            case "y":
                return 2;
            case "-y":
                return -2;
            case "z":
                return 3;
            case "-z":
                return -3;
            default:
                return 0;
        }
    }

    // Getter and Setter
    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getZ() {
        return z;
    }

    //
    public String toString() {
        return "StringVector: [x = " + x + ", y = " + y + ", z = " + z + "]";
    }

}
