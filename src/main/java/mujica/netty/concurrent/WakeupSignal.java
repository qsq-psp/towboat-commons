package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

@CodeHistory(date = "2022/7/28", project = "infrastructure")
@CodeHistory(date = "2025/12/26")
class WakeupSignal<T> extends PhantomReference<T> {

    /**
     * @param referent when it is null, the Reference will not enqueue automatically, but you can enqueue it by calling enqueue()
     * @param queue when it is null, the Reference will not enqueue, no Exception is thrown
     */
    WakeupSignal(@Nullable T referent, @Nullable ReferenceQueue<? super T> queue) {
        super(referent, queue);
    }
}
