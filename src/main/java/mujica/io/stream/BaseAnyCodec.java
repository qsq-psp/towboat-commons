package mujica.io.stream;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;

@CodeHistory(date = "2025/5/6")
public abstract class BaseAnyCodec implements Serializable {

    private static final long serialVersionUID = 0x18f681b47db88bddL;

    @NotNull
    protected final BaseAnyCodecSpec spec;

    protected BaseAnyCodec(@NotNull BaseAnyCodecSpec spec) {
        super();
        this.spec = spec;
    }

    public abstract boolean moreOctet();

    public abstract boolean moreCode();

    public abstract void encodeIn(byte octet);

    public abstract void encodeStop();

    public abstract byte encodeOut();

    public abstract void decodeIn(byte code) throws IOException;

    public abstract void decodeStop();

    public abstract byte decodeOut();
}
