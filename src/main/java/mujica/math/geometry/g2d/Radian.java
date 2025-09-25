package mujica.math.geometry.g2d;

import mujica.reflect.modifier.CodeHistory;

/**
 * Created in existence on 2018/7/9, named Radian.
 * Created in coo on 2020/2/27, named MoveAngle.
 * Created on 2022/6/26.
 */
@CodeHistory(date = "2025/3/2")
public class Radian extends Direction2 {

    private static final long serialVersionUID = 0x157626619ecce8c2L;

    public double rad;

    public Radian() {
        super();
    }

    public Radian(double rad) {
        super();
        this.rad = rad;
    }

    @Override
    public double getRadian() {
        return rad;
    }

    @Override
    public void setRadian(double value) {
        rad = value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(rad);
    }

    @Override
    public String toString() {
        return String.format("%.4frad", rad);
    }
}
