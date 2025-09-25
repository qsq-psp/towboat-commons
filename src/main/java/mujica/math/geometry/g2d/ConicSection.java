package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2024/2/17", project = "Ultramarine")
public abstract class ConicSection<P extends Point, Q extends Point> extends OriginJordan2<P> {

    @NotNull
    public final Q v;

    public ConicSection(
            @NotNull @Name(value = "origin", language = "en") @Name(value = "中心点", language = "zh") P o,
            @NotNull @Name(value = "vertex", language = "en") @Name(value = "其中一个顶点", language = "zh") Q v
    ) {
        super(o);
        this.v = v;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        o.checkHealth(consumer);
        checkPositive("origin-vertex distance", Point.squareNorm(o, v), consumer);
    }

    @Override
    public boolean isHealthy() {
        return o.isHealthy() && Point.squareNorm(o, v) > 0.0;
    }

    @Name(value = "离心率", language = "zh")
    public double eccentricity() {
        return Double.NaN;
    }

    @Name(value = "焦准距", language = "zh")
    public double focalParameter() {
        return Double.NaN;
    }

    @Name(value = "半正焦弦", language = "zh")
    @Name(value = "半通径", language = "zh")
    public double semiLatusRectum() {
        return Double.NaN;
    }

    @Name(value = "焦点数量", language = "zh")
    public int focusCount() {
        return 0;
    }

    @Name(value = "焦点", language = "zh")
    public void getFocus(int index, @NotNull Point point) {
        throw new IndexOutOfBoundsException();
    }

    @Name(value = "准线数量", language = "zh")
    public int directrixCount() {
        return 0;
    }

    @Name(value = "准线", language = "zh")
    public void getDirectrix(int index, @NotNull HalfPlane halfPlane) {
        throw new IndexOutOfBoundsException();
    }

    @Name(value = "渐近线数量", language = "zh")
    public int asymptoteCount() {
        return 0;
    }

    @Name(value = "渐近线", language = "zh")
    public void getAsymptote(int index, @NotNull HalfPlane halfPlane) {
        throw new IndexOutOfBoundsException();
    }

    @Name(value = "对称轴数量", language = "zh")
    public int symmetryAxisCount() {
        return 0;
    }

    @Name(value = "对称轴", language = "zh")
    public void getSymmetryAxis(int index, @NotNull HalfPlane halfPlane) {
        throw new IndexOutOfBoundsException();
    }

    @Name(value = "顶点数量", language = "zh")
    public int symmetryPointCount() {
        return 0;
    }

    @Name(value = "顶点", language = "zh")
    @Name(value = "对称轴与曲线的交点", language = "zh")
    public void getSymmetryPoint(int index, @NotNull Point point) {
        throw new IndexOutOfBoundsException();
    }

    @Name(value = "二次曲线的参数数量", language = "zh")
    @Override
    public int vectorLength() {
        return 6;
    }

    @Name(value = "二次曲线的参数", language = "zh")
    @Override
    public double vectorComponent(int index) {
        if (index < 6) {
            return Double.NaN;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Name(value = "真近点角转化为焦半径", language = "zh")
    @Name(value = "以一个焦点为极点的极坐标表达式", language = "zh")
    public double trueAnomalyToFocalRadius(@NotNull Direction2 anomaly) {
        return semiLatusRectum() / (1 + eccentricity() * anomaly.cos());
    }

    @Name(value = "真近点角转化为切线斜率", language = "zh")
    @NotNull
    public GeometryOperationResult trueAnomalyToTangent(@NotNull Direction2 anomaly, @NotNull Direction2 tangent) {
        return GeometryOperationResult.UNKNOWN;
    }

    @Name(value = "真近点角转化为曲率", language = "zh")
    public double trueAnomalyToCurvature(@NotNull Direction2 anomaly) {
        return Double.NaN;
    }

    @Name(value = "真近点角转化为曲率半径", language = "zh")
    public double trueAnomalyToCurvatureRadius(@NotNull Direction2 anomaly) {
        return 1.0 / trueAnomalyToCurvature(anomaly);
    }

    @Name(value = "真近点角转化为曲线上的点", language = "zh")
    public void trueAnomalyToPoint(@NotNull Direction2 anomaly, @NotNull Point point) {
        final double radius = trueAnomalyToFocalRadius(anomaly);
        if (radius == radius) {
            Tangent direction = new Tangent();
            direction.setToVector(v);
            direction.add(anomaly);
            direction.toPoint(radius, point);
        } else {
            point.invalidate();
        }
    }

    @NotNull
    @Override
    public GeometryOperationResult reset() {
        o.reset();
        v.setPoint(1.0, 0.0);
        return GeometryOperationResult.UNKNOWN;
    }

    @NotNull
    @Override
    public GeometryOperationResult invalidate() {
        o.invalidate();
        v.invalidate();
        return GeometryOperationResult.UNKNOWN;
    }

    @NotNull
    @Override
    public GeometryOperationResult normalize() {
        final double a = v.euclidNorm();
        if (a > 0.0) {
            o.reset();
            v.setPoint(a, 0.0);
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }
}
