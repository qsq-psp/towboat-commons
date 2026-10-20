package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.concurrent.Semaphore;

@CodeHistory(date = "2022/4/22", project = "LeetInAction", name = "ImageWaiter")
@CodeHistory(date = "2026/10/10")
public class SemaphoreImageObserver implements ImageObserver {

    @NotNull
    final Semaphore semaphore;

    public SemaphoreImageObserver(@NotNull Semaphore semaphore) {
        super();
        this.semaphore = semaphore;
    }

    @Override
    public boolean imageUpdate(@NotNull Image image, int flags, int x, int y, int w, int h) {
        semaphore.release();
        return false;
    }
}
