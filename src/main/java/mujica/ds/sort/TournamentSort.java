package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/3/4", name = "ListTournamentSort")
@CodeHistory(date = "2026/7/21")
@ReferencePage(title = "锦标赛排序", href = "https://oi-wiki.org/basic/tournament-sort/")
public class TournamentSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    A auxiliaryArray;

    public TournamentSort(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        return 0;
    }

    @Override
    public long sort(@NotNull A target) {
        return sort(target, 0, comparator.length(target));
    }
}
