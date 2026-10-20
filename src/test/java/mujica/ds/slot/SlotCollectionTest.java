package mujica.ds.slot;

import mujica.ds.bit.BitSlotCollectionTest;
import mujica.ds.i32.I32SlotCollectionTest;
import mujica.ds.i32.S32SlotCollectionTest;
import mujica.ds.i64.I64SlotCollectionTest;
import mujica.ds.i8.I8SlotCollectionTest;
import mujica.reflect.modifier.CodeHistory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@CodeHistory(date = "2026/9/20")
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BitSlotCollectionTest.class,
        I8SlotCollectionTest.class,
        I32SlotCollectionTest.class, S32SlotCollectionTest.class,
        I64SlotCollectionTest.class
})
public class SlotCollectionTest {}
