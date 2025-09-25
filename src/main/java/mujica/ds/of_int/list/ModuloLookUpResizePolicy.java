package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/6/30")
public class ModuloLookUpResizePolicy extends LookUpResizePolicy {

    private static final long serialVersionUID = 0x1bf5a339f6be2cbaL;

    protected ModuloLookUpResizePolicy(@NotNull int[] array, boolean isPrime, boolean isQuadraticFull) {
        super(array, isPrime, isQuadraticFull);
    }

    protected ModuloLookUpResizePolicy(@NotNull int[] array) {
        super(array);
    }

    @Override
    public int nextCapacity(int currentCapacity) {
        final int modulo = array[0];
        final int index = currentCapacity % modulo;
        assert array[index] == currentCapacity || array[modulo] == currentCapacity;
        return array[index + 1];
    }

    private static final int[] A_59 = {
            59, 827, 887, 947, 1774, 2011, 2543, 3134, 3371, 4139, 5438, 6619, 6679, 7211, 7507, 7862, 8158, 8867, 9871,
            9931, 10463, 11054, 11527, 11587, 12119, 13654, 14422, 14423, 15014, 15959, 16078, 16787, 17614, 17851,
            17911, 17971, 18503, 18622, 18859, 18919, 18979, 20219, 22639, 22699, 23054, 23291, 24767, 25771, 26126,
            27779, 28547, 28607, 29611, 29671, 30203, 30971, 31267, 31327, 31387, 31859
    };

    public static final ModuloLookUpResizePolicy INSTANCE_59 = new ModuloLookUpResizePolicy(A_59, false, true);

    private static final int[] A_67 = {
            67, 1006, 1543, 1678, 3019, 3623, 3691, 4027, 4363, 4766, 5102, 5438, 5774, 6043, 6379, 6782, 7118, 7454,
            7723, 8059, 8462, 8731, 9067, 9403, 9739, 10343, 11014, 11551, 11686, 12022, 13966, 13967, 14303, 14438,
            15511, 15646, 15647, 16519, 17123, 17191, 17326, 17327, 17462, 18803, 19139, 19207, 19543, 19678, 20014,
            20551, 20686, 21491, 21559, 21694, 23102, 23371, 23774, 24043, 24379, 24782, 26927, 27598, 27934, 29878,
            29879, 29947, 30886, 31891
    };

    public static final ModuloLookUpResizePolicy INSTANCE_67 = new ModuloLookUpResizePolicy(A_67, false, true);
}
