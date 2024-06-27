package indi.um.json.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2023/5/2.
 */
public class ProtoObject extends JsonObject {

    private static final long serialVersionUID = 0xFBF6AE3280CFD161L;

    private volatile JsonObject prototype;

    @Override
    public JsonObject getPrototype() {
        return prototype;
    }

    public void setPrototype(@NotNull JsonObject prototype) {
        if (this.prototype == prototype) {
            return;
        }
        while (true) {
            if (setPrototype(this.getRootPrototype(), prototype.getRootPrototype(), prototype)) {
                break;
            }
        }
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private boolean setPrototype(JsonObject oldRoot, JsonObject newRoot, JsonObject prototype) {
        final int compare = Integer.compare(System.identityHashCode(oldRoot), System.identityHashCode(newRoot));
        if (compare < 0) {
            synchronized (oldRoot) {
                synchronized (newRoot) {
                    if (this.getRootPrototype() == oldRoot && prototype.getRootPrototype() == newRoot) {
                        this.prototype = prototype;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else if (compare > 0) {
            synchronized (newRoot) {
                synchronized (oldRoot) {
                    if (this.getRootPrototype() == oldRoot && prototype.getRootPrototype() == newRoot) {
                        this.prototype = prototype;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else if (oldRoot == newRoot) {
            throw new IllegalArgumentException("Prototype loop");
        } else {
            synchronized (JsonObject.class) {
                synchronized (oldRoot) {
                    synchronized (newRoot) {
                        if (this.getRootPrototype() == oldRoot && prototype.getRootPrototype() == newRoot) {
                            this.prototype = prototype;
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
    }

    public boolean hasOwnProperty(@NotNull String name) {
        return super.containsKey(name);
    }

    @Override
    public boolean containsKey(@NotNull Object name) {
        return super.containsKey(name) || prototype != null && prototype.containsKey(name);
    }

    @Override
    public Object get(@NotNull Object name) {
        if (super.containsKey(name)) {
            return super.get(name);
        } else if (prototype != null) {
            return prototype.get(name);
        } else {
            return null;
        }
    }

    public boolean set(@NotNull String name, @Nullable Object value) {
        super.put(name, value);
        return true;
    }

    @Override
    public Object put(@NotNull String name, @Nullable Object value) {
        if (super.containsKey(name)) {
            return super.put(name, value);
        } else {
            super.put(name, value);
            if (prototype != null) {
                return prototype.get(name);
            } else {
                return null;
            }
        }
    }
}
