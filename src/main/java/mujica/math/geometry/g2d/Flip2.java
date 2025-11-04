package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created on 2022/6/27.
 */
@CodeHistory(date = "2022/6/27", project = "Ultramarine")
public class Flip2 extends Translation2 implements FlipOption2 {

    private static final long serialVersionUID = 0xdd0221d9348f764cL;
    
    public int op;
    
    public Flip2() {
        super();
    }
    
    public Flip2(double tx, double ty, int op) {
        super(tx, ty);
        setFlipOp(op);
    }
    
    public Flip2(Flip2 that) {
        super(that);
        setFlipOp(that.op);
    }

    @Override
    @NotNull
    public Flip2 duplicate() {
        return new Flip2(this);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // always healthy; pass
    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    public boolean isFlipX() {
        return (X & op) != 0;
    }

    public boolean isFlipY() {
        return (Y & op) != 0;
    }

    public boolean isPermute() {
        return (PERMUTE & op) != 0;
    }

    private static final boolean[] FLIPPED = {
            false, true, true, false, true, false, false, true
    };

    public boolean isFlipped() {
        return FLIPPED[MASK & op];
    }

    @Override
    public double calculateDeterminant() {
        return isFlipped() ? -1.0 : 1.0;
    }

    public void setFlipOp(int op) {
        this.op = op;
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        setFlipOp(0);
        return super.reset();
    }

    @Override
    public void transform0(@NotNull Point src, @NotNull Point dst) {
        switch (MASK & op) {
            default: // no op, op = 0
                dst.setPoint(tx + src.x, ty + src.y); // same as super.transform()
                break;
            case X:
                dst.setPoint(tx - src.x, ty + src.y);
                break;
            case Y:
                dst.setPoint(tx + src.x, ty - src.y);
                break;
            case X | Y:
                dst.setPoint(tx - src.x, ty - src.y);
                break;
            case PERMUTE:
                dst.setPoint(tx + src.y, ty + src.x);
                break;
            case X | PERMUTE:
                dst.setPoint(tx + src.y, ty - src.x);
                break;
            case Y | PERMUTE:
                dst.setPoint(tx - src.y, ty + src.x);
                break;
            case X | Y | PERMUTE:
                dst.setPoint(tx - src.y, ty - src.x);
                break;
        }
    }

    @Override
    public void transform(@NotNull HalfPlane src, @NotNull HalfPlane dst) {
        switch (MASK & op) {
            default: // no op, op = 0
                dst.setHalfPlane(src.a, src.b, src.c - tx * src.a - ty * src.b);
                break;
            case X:
                dst.setHalfPlane(-src.a, src.b, src.c + tx * src.a - ty * src.b);
                break;
            case Y:
                dst.setHalfPlane(src.a, -src.b, src.c - tx * src.a + ty * src.b);
                break;
            case X | Y:
                dst.setHalfPlane(-src.a, -src.b, src.c + tx * src.a + ty * src.b);
                break;
            case PERMUTE:
                dst.setHalfPlane(src.b, src.a, src.c - tx * src.b - ty * src.a);
                break;
            case X | PERMUTE:
                dst.setHalfPlane(src.b, -src.a, src.c - tx * src.b + ty * src.a);
                break;
            case Y | PERMUTE:
                dst.setHalfPlane(-src.b, src.a, src.c + tx * src.b - ty * src.a);
                break;
            case X | Y | PERMUTE:
                dst.setHalfPlane(-src.b, -src.a, src.c + tx * src.b + ty * src.a);
                break;
        }
    }

    @NotNull
    @Override
    public GeometryOperationResult setToInverse() {
        switch (MASK & op) {
            default: // no op, op = 0
                return super.setToInverse();
            case X:
                setTranslation(tx, -ty);
                break;
            case Y:
                setTranslation(-tx, ty);
                break;
            case X | Y:
                break;
            case PERMUTE:
                setTranslation(-ty, -tx);
                break;
            case X | PERMUTE:
                setTranslation(ty, -tx);
                setFlipOp(Y | PERMUTE);
                break;
            case Y | PERMUTE:
                setTranslation(-ty, tx);
                setFlipOp(X | PERMUTE);
                break;
            case X | Y | PERMUTE:
                setTranslation(ty, tx);
                break;
        }
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    public Flip2 invert() {
        switch (MASK & op) {
            default: // no op, op = 0
                return new Flip2(-tx, -ty, op);
            case X:
                return new Flip2(tx, -ty, op);
            case Y:
                return new Flip2(-tx, ty, op);
            case X | Y:
                return new Flip2(this);
            case PERMUTE:
                return new Flip2(-ty, -tx, op);
            case X | PERMUTE:
                return new Flip2(ty, -tx, Y | PERMUTE);
            case Y | PERMUTE:
                return new Flip2(-ty, tx, X | PERMUTE);
            case X | Y | PERMUTE:
                return new Flip2(ty, tx, op);
        }
    }

    @Override
    @NotNull
    public GeometryOperationResult inverseTransform(@NotNull Point src, @NotNull Point dst) {
        switch (MASK & op) {
            default: // no op, op = 0
                dst.setPoint(src.x - tx, src.y - ty); // same as super.inverseTransform()
                break;
            case X:
                dst.setPoint(tx - src.x, src.y - ty);
                break;
            case Y:
                dst.setPoint(src.x - tx, ty - src.y);
                break;
            case X | Y:
                dst.setPoint(tx - src.x, ty - src.y); // same as transform()
                break;
            case PERMUTE:
                dst.setPoint(src.y - ty, src.x - tx);
                break;
            case X | PERMUTE:
                dst.setPoint(ty - src.y, src.x - tx);
                break;
            case Y | PERMUTE:
                dst.setPoint(src.y - ty, tx - src.x);
                break;
            case X | Y | PERMUTE:
                dst.setPoint(ty - src.y, tx - src.x);
                break;
        }
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    public double vectorComponent(int index) {
        switch (index) {
            case 0: // mxx
                if (isPermute()) {
                    return 0.0;
                } else if (isFlipX()) {
                    return -1.0;
                } else {
                    return 1.0;
                }
            case 1: // myx
                if (isPermute()) {
                    if (isFlipY()) {
                        return -1.0;
                    } else {
                        return 1.0;
                    }
                } else {
                    return 0.0;
                }
            case 2:
                return tx;
            case 3: // mxy
                if (isPermute()) {
                    if (isFlipX()) {
                        return -1.0;
                    } else {
                        return 1.0;
                    }
                } else {
                    return 0.0;
                }
            case 4: // myy
                if (isPermute()) {
                    return 0.0;
                } else if (isFlipY()) {
                    return -1.0;
                } else {
                    return 1.0;
                }
            case 5:
                return ty;
            case 6:
            case 7:
                return 0.0;
            case 8:
                return 1.0;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private static final String[] OP_NAMES = {"I", "X", "Y", "XY", "P", "XP", "YP", "XYP"};

    public String flipOpToString() {
        return OP_NAMES[MASK & op];
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f, %s)", tx, ty, flipOpToString());
    }
}
