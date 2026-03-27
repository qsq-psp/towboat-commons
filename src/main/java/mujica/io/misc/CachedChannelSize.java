package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

@CodeHistory(date = "2026/2/23")
public class CachedChannelSize implements CachedDataSize<SeekableByteChannel> {

    @NotNull
    private final SeekableByteChannel channel;

    private transient long value;

    public CachedChannelSize(@NotNull SeekableByteChannel channel) throws IOException {
        super();
        this.channel = channel;
        updateCache();
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public void setLong(long newValue) {
        value = newValue;
    }

    @NotNull
    @Override
    public SeekableByteChannel getTarget() {
        return channel;
    }

    @NotNull
    @Override
    public CachedChannelSize updateCache() throws IOException {
        value = channel.size();
        return this;
    }

    @NotNull
    @Override
    public String toString() {
        return "CachedChannelSize[channel = " + channel + ", value = " + value + "]";
    }
}
