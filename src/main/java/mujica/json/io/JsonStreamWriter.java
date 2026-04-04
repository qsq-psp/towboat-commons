package mujica.json.io;

import mujica.reflect.modifier.CodeHistory;

import java.io.Closeable;
import java.io.Flushable;

/**
 * Created on 2026/3/31.
 */
@CodeHistory(date = "2026/3/31")
public abstract class JsonStreamWriter extends JsonWriter implements Flushable, Closeable {
}
