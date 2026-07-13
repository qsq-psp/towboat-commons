package mujica.json.provided.netty;

import io.netty.channel.nio.NioEventLoop;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/23")
public class EventExecutorGroupTransformer implements JsonContextTransformer<EventExecutorGroup> {

    public static final EventExecutorGroupTransformer INSTANCE = new EventExecutorGroupTransformer();

    @Override
    public void transform(EventExecutorGroup group, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("class");
            out.stringValue(group.getClass().getName());
            out.key("shuttingDown");
            out.booleanValue(group.isShuttingDown());
            out.key("shutDown");
            out.booleanValue(group.isShutdown());
            out.key("terminated");
            out.booleanValue(group.isTerminated());
        }
        if (group instanceof EventExecutor) {
            EventExecutor executor = (EventExecutor) group;
            out.key("in");
            out.booleanValue(executor.inEventLoop());
            if (executor instanceof SingleThreadEventExecutor) {
                SingleThreadEventExecutor singleThreadEventExecutor = (SingleThreadEventExecutor) executor;
                out.key("pendingTasks");
                out.numberValue(singleThreadEventExecutor.pendingTasks());
                out.key("threadProperties");
                ThreadPropertiesTransformer.INSTANCE.transform(singleThreadEventExecutor.threadProperties(), out, context);
                if (singleThreadEventExecutor instanceof NioEventLoop) {
                    NioEventLoop nioEventLoop = (NioEventLoop) singleThreadEventExecutor;
                    out.key("ioRatio");
                    out.numberValue(nioEventLoop.getIoRatio());
                    out.key("registeredChannels");
                    out.numberValue(nioEventLoop.registeredChannels());
                }
            }
        }
        if (group instanceof MultithreadEventExecutorGroup) {
            MultithreadEventExecutorGroup executorGroup = (MultithreadEventExecutorGroup) group;
            out.key("executorCount");
            out.numberValue(executorGroup.executorCount());
        }
        out.closeObject();
    }
}
