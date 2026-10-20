package mujica.ds.sort;

import mujica.reflect.modifier.CodeHistory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@CodeHistory(date = "2026/9/21")
@RunWith(Suite.class)
@Suite.SuiteClasses({
        S8SortTest.class,
        S32SortTest.class,
        S64SortTest.class
})
public class SortTest {}
