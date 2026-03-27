package mujica.ds.generic.list;

import mujica.algebra.random.RandomContext;
import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.ComparableComparator;
import mujica.ds.of_int.PublicIntSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@CodeHistory(date = "2026/2/12")
public class ListSortingAlgorithmTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListSortingAlgorithmTest.class);

    private static final int REPEAT_COUNT = 103;

    private static final int MAIN_LENGTH = 278;

    private static final int ASIDE_LENGTH = 19;

    private static final int STRING_LENGTH = 5;

    private final RandomContext rc = new RandomContext();

    @NotNull
    private String objectListToString(@NotNull HashMap<Object, Integer> map, @NotNull List<Object> list) {
        final int size = list.size();
        final int[] array = new int[size];
        for (int index = 0; index < size; index++) {
            array[index] = Objects.requireNonNullElse(map.get(list.get(index)), -1);
        }
        return Arrays.toString(array);
    }

    private void checkObject(@NotNull Function<Comparator<Object>, SortingAlgorithm<List<Object>>> constructor) {
        final HashMap<Object, Integer> indexMap = new HashMap<>();
        final Comparator<Object> comparator = Comparator.comparingInt(indexMap::get);
        final SortingAlgorithm<List<Object>> algorithm = constructor.apply(comparator);
        Assert.assertEquals(MonotonicityDirection.ASCENDING, algorithm.monotonicity());
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            indexMap.clear();
            int length = rc.nextInt(2, MAIN_LENGTH);
            for (int index = 0; index < length; index++) {
                indexMap.put(new Object(), index);
            }
            List<Object> list = new ArrayList<>(indexMap.keySet());
            if (rc.nextBoolean()) {
                indexMap.put(null, indexMap.remove(list.set(rc.nextInt(list.size()), null)));
            }
            rc.shuffleList(list);
            algorithm.sort(list);
            try {
                for (int index = 0; index < length; index++) {
                    Assert.assertEquals(index, indexMap.get(list.get(index)).intValue());
                }
            } catch (AssertionError e) {
                LOGGER.error("{}", objectListToString(indexMap, list));
                throw e;
            }
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            indexMap.clear();
            int prefixLength = rc.nextInt(ASIDE_LENGTH);
            int prefixAndMainLength = prefixLength + rc.nextInt(2, MAIN_LENGTH);
            int totalLength = prefixAndMainLength + rc.nextInt(ASIDE_LENGTH);
            for (int index = 0; index < totalLength; index++) {
                indexMap.put(new Object(), index);
            }
            List<Object> list = new ArrayList<>(indexMap.keySet());
            if (rc.nextBoolean()) {
                indexMap.put(null, indexMap.remove(list.set(rc.nextInt(list.size()), null)));
            }
            rc.shuffleList(list);
            algorithm.sort(list, prefixLength, prefixAndMainLength);
            try {
                for (int index = prefixLength + 1; index < prefixAndMainLength; index++) {
                    Assert.assertTrue(comparator.compare(list.get(index - 1), list.get(index)) < 0);
                }
            } catch (AssertionError e) {
                LOGGER.error("{}", objectListToString(indexMap, list));
                throw e;
            }
        }
    }

    @Test
    public void checkObject() {
        checkObject(ListInsertionSort::new);
        checkObject(ListMergeSort::new);
        checkObject(ListSelectionSort::new);
    }

    private void checkString(@NotNull SortingAlgorithm<List<String>> algorithm) {
        Assert.assertEquals(MonotonicityDirection.ASCENDING, algorithm.monotonicity());
        final HashMap<String, PublicIntSlot> counter = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            counter.clear();
            int prefixLength = rc.nextInt(ASIDE_LENGTH);
            int prefixAndMainLength = prefixLength + rc.nextInt(2, MAIN_LENGTH);
            int totalLength = prefixAndMainLength + rc.nextInt(ASIDE_LENGTH);
            List<String> list = new ArrayList<>(totalLength);
            for (int index = 0; index < totalLength; index++) {
                String string = rc.nextString(STRING_LENGTH);
                list.add(string);
                PublicIntSlot.increase(counter, string);
            }
            algorithm.sort(list, prefixLength, prefixAndMainLength);
            try {
                for (int index = prefixLength + 1; index < prefixAndMainLength; index++) {
                    Assert.assertTrue(list.get(index - 1).compareTo(list.get(index)) <= 0);
                }
                for (String string : list) {
                    PublicIntSlot.decrease(counter, string);
                }
                for (PublicIntSlot value : counter.values()) {
                    Assert.assertEquals(0, value.getInt());
                }
            } catch (AssertionError e) {
                LOGGER.error("{}", list);
                throw e;
            }
        }
    }

    @Test
    public void checkString() {
        final ComparableComparator<String> comparator = new ComparableComparator<>();
        checkString(new ListInsertionSort<>(comparator));
        checkString(new ListMergeSort<>(comparator));
        checkString(new ListSelectionSort<>(comparator));
    }

    private static final TimeUnit[] TIME_UNIT = TimeUnit.values();

    @Nullable
    private TimeUnit nextTimeUnit() {
        final int length = TIME_UNIT.length;
        int index = rc.nextInt(length + 1);
        if (index == length) {
            return null;
        } else {
            return TIME_UNIT[index];
        }
    }

    @NotNull
    private List<TimeUnit> nextTimeUnitList(int length) {
        final List<TimeUnit> list;
        switch (rc.nextInt() & 0xf) {
            case 0:
                list = new Vector<>();
                break;
            case 1:
                list = new LinkedList<>();
                break;
            case 2:
                list = new CopyOnWriteArrayList<>();
                break;
            case 3:
                list = new TruncateList<>();
                break;
            default:
                list = new ArrayList<>(length);
                break;
        }
        for (int index = 0; index < length; index++) {
            list.add(nextTimeUnit());
        }
        return list;
    }

    private static final Comparator<TimeUnit> TIME_UNIT_COMPARATOR = Comparator.comparingInt(unit -> {
        if (unit == null) {
            return -1;
        } else {
            return unit.ordinal();
        }
    });

    private void checkTimeUnit(@NotNull SortingAlgorithm<List<TimeUnit>> algorithm) {
        Assert.assertEquals(MonotonicityDirection.ASCENDING, algorithm.monotonicity());
        final HashMap<TimeUnit, PublicIntSlot> counter = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            counter.clear();
            int prefixLength = rc.nextInt(ASIDE_LENGTH);
            int prefixAndMainLength = prefixLength + rc.nextInt(2, MAIN_LENGTH);
            List<TimeUnit> list = nextTimeUnitList(prefixAndMainLength + rc.nextInt(ASIDE_LENGTH));
            for (TimeUnit timeUnit : list) {
                PublicIntSlot.increase(counter, timeUnit);
            }
            algorithm.sort(list, prefixLength, prefixAndMainLength);
            try {
                for (int index = prefixLength + 1; index < prefixAndMainLength; index++) {
                    Assert.assertTrue(TIME_UNIT_COMPARATOR.compare(list.get(index - 1), list.get(index)) <= 0);
                }
                for (TimeUnit timeUnit : list) {
                    PublicIntSlot.decrease(counter, timeUnit);
                }
                for (PublicIntSlot value : counter.values()) {
                    Assert.assertEquals(0, value.getInt());
                }
            } catch (AssertionError e) {
                LOGGER.error("{}", list);
                throw e;
            }
        }
    }

    @Test
    public void checkTimeUnit() {
        checkTimeUnit(new ListInsertionSort<>(TIME_UNIT_COMPARATOR));
        checkTimeUnit(new ListMergeSort<>(TIME_UNIT_COMPARATOR));
        checkTimeUnit(new ListSelectionSort<>(TIME_UNIT_COMPARATOR));
    }

    private static final String[] ZONE_ID = TimeZone.getAvailableIDs();

    static {
        LOGGER.info("ZONE_ID.length = {}", ZONE_ID.length);
    }

    @Nullable
    private TimeZone nextTimeZone() {
        switch (rc.nextInt() & 0x7) {
            case 0:
                return null;
            case 1:
                return TimeZone.getDefault();
            default:
                return TimeZone.getTimeZone(ZONE_ID[rc.nextInt(ZONE_ID.length)]);
        }
    }

    @NotNull
    private List<TimeZone> nextTimeZoneList(int length) {
        final List<TimeZone> list;
        switch (rc.nextInt() & 0xf) {
            case 0:
                list = new Vector<>();
                break;
            case 1:
                list = new LinkedList<>();
                break;
            case 2:
                list = new CopyOnWriteArrayList<>();
                break;
            case 3:
                list = new TruncateList<>(length);
                break;
            default:
                list = new ArrayList<>();
                break;
        }
        for (int index = 0; index < length; index++) {
            list.add(nextTimeZone());
        }
        return list;
    }

    @NotNull
    private static String timeZoneListToString(@NotNull List<TimeZone> list) {
        final int size = list.size();
        final int[] rawOffsets = new int[size];
        for (int index = 0; index < size; index++) {
            TimeZone timeZone = list.get(index);
            if (timeZone != null) {
                rawOffsets[index] = timeZone.getRawOffset();
            } // else zero
        }
        return Arrays.toString(rawOffsets);
    }

    private static final Comparator<TimeZone> TIME_ZONE_COMPARATOR = Comparator.comparingInt(zone -> {
        if (zone == null) {
            return 0;
        } else {
            return zone.getRawOffset();
        }
    });

    private void checkTimeZone(@NotNull SortingAlgorithm<List<TimeZone>> algorithm) {
        Assert.assertEquals(MonotonicityDirection.ASCENDING, algorithm.monotonicity());
        final HashMap<TimeZone, PublicIntSlot> counter = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            counter.clear();
            int prefixLength = rc.nextInt(ASIDE_LENGTH);
            int mainEnd = prefixLength + rc.nextInt(2, MAIN_LENGTH);
            List<TimeZone> list = nextTimeZoneList(mainEnd + rc.nextInt(ASIDE_LENGTH));
            for (TimeZone timeZone : list) {
                PublicIntSlot.increase(counter, timeZone);
            }
            algorithm.sort(list, prefixLength, mainEnd);
            try {
                for (int index = prefixLength + 1; index < mainEnd; index++) {
                    Assert.assertTrue(TIME_ZONE_COMPARATOR.compare(list.get(index - 1), list.get(index)) <= 0);
                }
                for (TimeZone timeZone : list) {
                    PublicIntSlot.decrease(counter, timeZone);
                }
                for (PublicIntSlot value : counter.values()) {
                    Assert.assertEquals(0, value.getInt());
                }
            } catch (AssertionError e) {
                LOGGER.error("{}", timeZoneListToString(list));
                throw e;
            }
        }
    }

    @Test
    public void checkTimeZone() {
        checkTimeZone(new ListInsertionSort<>(TIME_ZONE_COMPARATOR));
        checkTimeZone(new ListMergeSort<>(TIME_ZONE_COMPARATOR));
        checkTimeZone(new ListSelectionSort<>(TIME_ZONE_COMPARATOR));
    }

    private static final Locale[] LOCALE = Locale.getAvailableLocales();

    static {
        LOGGER.info("LOCALE.length = {}", LOCALE.length);
    }

    @NotNull
    private List<Locale> nextLocaleList() {
        final int modulo = LOCALE.length;
        final int length = rc.nextInt(2, modulo);
        final List<Locale> list;
        switch (rc.nextInt() & 0xf) {
            case 0:
                list = new Vector<>(length);
                break;
            case 1:
                list = new LinkedList<>();
                break;
            case 2:
                list = new CopyOnWriteArrayList<>();
                break;
            case 3:
                list = new TruncateList<>(length);
                break;
            default:
                list = new ArrayList<>(length);
                break;
        }
        int srcIndex = rc.nextInt(modulo);
        for (int dstIndex = 0; dstIndex < length; dstIndex++) {
            list.add(LOCALE[srcIndex++ % modulo]);
        }
        rc.shuffleList(list);
        return list;
    }

    private static final Comparator<Locale> LOCALE_COMPARATOR = Comparator.comparing(Locale::getLanguage)
            .thenComparing(Locale::getCountry).thenComparing(Locale::getScript).thenComparing(Locale::getVariant);

    private void checkLocale(@NotNull SortingAlgorithm<List<Locale>> algorithm) {
        Assert.assertEquals(MonotonicityDirection.ASCENDING, algorithm.monotonicity());
        final HashMap<Locale, PublicIntSlot> counter = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            counter.clear();
            List<Locale> list = nextLocaleList();
            for (Locale locale : list) {
                PublicIntSlot.increase(counter, locale);
            }
            algorithm.sort(list);
            try {
                for (int index = list.size() - 1; index > 0; index--) {
                    Assert.assertTrue(LOCALE_COMPARATOR.compare(list.get(index - 1), list.get(index)) <= 0);
                }
                for (Locale locale : list) {
                    PublicIntSlot.decrease(counter, locale);
                }
                for (PublicIntSlot value : counter.values()) {
                    Assert.assertEquals(0, value.getInt());
                }
            } catch (AssertionError e) {
                LOGGER.error("{}", list);
                throw e;
            }
        }
    }

    @Test
    public void checkLocale() {
        checkLocale(new ListInsertionSort<>(LOCALE_COMPARATOR));
        checkLocale(new ListMergeSort<>(LOCALE_COMPARATOR));
        checkLocale(new ListSelectionSort<>(LOCALE_COMPARATOR));
    }
}
