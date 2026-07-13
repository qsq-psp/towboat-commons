package mujica.ds.sort;

import mujica.ds.slot.Arithmetic;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;

@CodeHistory(date = "2025/12/19", name = "SelectTwoIntMeanAsPivot")
@CodeHistory(date = "2025/12/24", name = "SelectTwoLongMeanAsPivot")
@CodeHistory(date = "2026/7/12")
public class SelectTwoMeanAsPivot<S, A> implements SelectPivot<S, A> {

    @NotNull
    final SlotArrayAllocator<S, A> allocator;

    @NotNull
    final Arithmetic<S> arithmetic;

    @NotNull
    final RoundingMode roundingMode;

    @NotNull
    final S first, second;

    public SelectTwoMeanAsPivot(@NotNull SlotArrayAllocator<S, A> allocator, @NotNull Arithmetic<S> arithmetic, @NotNull RoundingMode roundingMode) {
        super();
        this.allocator = allocator;
        this.arithmetic = arithmetic;
        this.roundingMode = roundingMode;
        this.first = allocator.newSlot();
        this.second = allocator.newSlot();
    }

    public SelectTwoMeanAsPivot(@NotNull SlotArrayAllocator<S, A> allocator, @NotNull Arithmetic<S> arithmetic) {
        this(allocator, arithmetic, RoundingMode.DOWN);
    }

    @Override
    public void selectPivot(@NotNull A target, int startIndex, int endIndex, @NotNull S out) {
        if (endIndex - startIndex >= 2) {
            allocator.load(target, startIndex, first);
            allocator.load(target, startIndex + 1, first);
            arithmetic.mean(first, second, out, roundingMode);
        } else {
            assert startIndex < endIndex;
            allocator.load(target, startIndex, out);
        }
    }
}
