package mujica.json.provided.netty;

import io.netty.channel.nio.NioEventLoop;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/23.
 */
public class EventExecutorGroupTransformer implements JsonContextTransformer<EventExecutorGroup> {

    public static final EventExecutorGroupTransformer INSTANCE = new EventExecutorGroupTransformer();

    @Override
    public void transform(EventExecutorGroup group, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("class");
            out.stringValue(group.getClass().getName());
            out.stringKey("shuttingDown");
            out.booleanValue(group.isShuttingDown());
            out.stringKey("shutDown");
            out.booleanValue(group.isShutdown());
            out.stringKey("terminated");
            out.booleanValue(group.isTerminated());
        }
        if (group instanceof EventExecutor) {
            EventExecutor executor = (EventExecutor) group;
            out.stringKey("in");
            out.booleanValue(executor.inEventLoop());
            if (executor instanceof SingleThreadEventExecutor) {
                SingleThreadEventExecutor singleThreadEventExecutor = (SingleThreadEventExecutor) executor;
                out.stringKey("pendingTasks");
                out.numberValue(singleThreadEventExecutor.pendingTasks());
                out.stringKey("threadProperties");
                ThreadPropertiesTransformer.INSTANCE.transform(singleThreadEventExecutor.threadProperties(), out, context);
                if (singleThreadEventExecutor instanceof NioEventLoop) {
                    NioEventLoop nioEventLoop = (NioEventLoop) singleThreadEventExecutor;
                    out.stringKey("ioRatio");
                    out.numberValue(nioEventLoop.getIoRatio());
                    out.stringKey("registeredChannels");
                    out.numberValue(nioEventLoop.registeredChannels());
                }
            }
        }
        if (group instanceof MultithreadEventExecutorGroup) {
            MultithreadEventExecutorGroup executorGroup = (MultithreadEventExecutorGroup) group;
            out.stringKey("executorCount");
            out.numberValue(executorGroup.executorCount());
        }
        out.closeObject();
    }
}
