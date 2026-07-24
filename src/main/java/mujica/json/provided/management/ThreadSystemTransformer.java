package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.ThreadMXBean;

@CodeHistory(date = "2026/5/19")
public class ThreadSystemTransformer implements JsonContextTransformer<ThreadMXBean> {

    public static final ThreadSystemTransformer INSTANCE = new ThreadSystemTransformer();

    static final FastString PEAK_COUNT = new FastString("peakCount");

    static final FastString TOTAL_COUNT = new FastString("totalCount");

    static final FastString DAEMON_COUNT = new FastString("daemonCount");

    static final FastString TID = new FastString("threadID");

    static final FastString CPU_TIME = new FastString("cpuTime");

    static final FastString USER_TIME = new FastString("userTime");

    @Override
    public void transform(@NotNull ThreadMXBean bean, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(BufferPoolTransformer.COUNT);
            out.numberValue(bean.getThreadCount());
            out.key(PEAK_COUNT);
            out.numberValue(bean.getPeakThreadCount());
            out.key(TOTAL_COUNT);
            out.numberValue(bean.getTotalStartedThreadCount());
            out.key(DAEMON_COUNT);
            out.numberValue(bean.getDaemonThreadCount());
            out.key(TID);
            out.arrayValue(bean.getAllThreadIds());
            try {
                long time = bean.getCurrentThreadCpuTime(); // ns
                out.key(CPU_TIME);
                out.numberValue(time);
            } catch (UnsupportedOperationException e) {
                if (context != null) {
                    context.getLogger().debug("{}", bean, e);
                }
            }
            try {
                long time = bean.getCurrentThreadUserTime(); // ns
                out.key(USER_TIME);
                out.numberValue(time);
            } catch (UnsupportedOperationException e) {
                if (context != null) {
                    context.getLogger().debug("{}", bean, e);
                }
            }
        }
        out.closeObject();
    }
}
