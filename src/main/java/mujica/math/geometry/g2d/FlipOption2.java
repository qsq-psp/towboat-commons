package mujica.math.geometry.g2d;

/**
 * Created on 2022/7/1.
 */
public interface FlipOption2 {

    int X               = 0x01;
    int Y               = 0x02;
    int PERMUTE         = 0x04;
    int MASK            = X | Y | PERMUTE;
}
