package mujica.io.stream;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created on 2025/4/25.
 */
public interface BufferingPolicyOwner {

    @NotNull
    BufferingPolicy getPolicy();

    void setPolicyDelayed(@NotNull BufferingPolicy policy);

    void setPolicyImmediately(@NotNull BufferingPolicy policy) throws IOException;
}
