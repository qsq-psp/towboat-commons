package mujica.ds.primitive;

/**
 * Created on 2025/6/16.
 */
public interface RealContext extends NumericContext {

    void sqrt(int srcVariable, int dstVariable);

    void cbrt(int srcVariable, int dstVariable);

    void pow(int srcVariable, int dstVariable);

    void sin(int srcVariable, int dstVariable);

    void cos(int srcVariable, int dstVariable);

    void tan(int srcVariable, int dstVariable);
}
