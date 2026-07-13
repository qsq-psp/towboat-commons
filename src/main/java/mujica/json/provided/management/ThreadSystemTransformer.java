package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ThreadMXBean;

/**
 * Created on 2026/5/19.
 */
public class ThreadSystemTransformer implements JsonContextTransformer<ThreadMXBean> {

    public static final ThreadSystemTransformer INSTANCE = new ThreadSystemTransformer();

    static final FastString PEAK_COUNT = new FastString("peakCount");

    static final FastString TOTAL_COUNT = new FastString("totalCount");

    static final FastString DAEMON_COUNT = new FastString("daemonCount");

    static final FastString TID = new FastString("threadID");

    static final FastString CPU_TIME = new FastString("cpuTime");

    static final FastString USER_TIME = new FastString("userTime");

    @Override
    public void transform(@NotNull ThreadMXBean in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(BufferPoolTransformer.COUNT);
            out.numberValue(in.getThreadCount());
            out.key(PEAK_COUNT);
            out.numberValue(in.getPeakThreadCount());
            out.key(TOTAL_COUNT);
            out.numberValue(in.getTotalStartedThreadCount());
            out.key(DAEMON_COUNT);
            out.numberValue(in.getDaemonThreadCount());
            out.key(TID);
            out.arrayValue(in.getAllThreadIds());
            try {
                long time = in.getCurrentThreadCpuTime(); // ns
                out.key(CPU_TIME);
                out.numberValue(time);
            } catch (UnsupportedOperationException e) {
                if (context != null) {
                    context.getLogger().debug("{}", in, e);
                }
            }
            try {
                long time = in.getCurrentThreadUserTime(); // ns
                out.key(USER_TIME);
                out.numberValue(time);
            } catch (UnsupportedOperationException e) {
                if (context != null) {
                    context.getLogger().debug("{}", in, e);
                }
            }
        }
        out.closeObject();
    }
}
