package mujica.ds.slot;

import mujica.ds.i32.list.CapacityPolicyProvider;
import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/7/7")
public interface SlotListAllocator<S, A> extends SlotArrayAllocator<S, A>, CapacityPolicyProvider {}
