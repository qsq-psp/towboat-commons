package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;

@CodeHistory(date = "2022/5/8")
public interface StateStack {

    @ReferencePage(title = "CanvasRenderingContext2D.prototype.save()", href = "https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/save")
    void save();

    @ReferencePage(title = "CanvasRenderingContext2D.prototype.restore()", href = "https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/restore")
    void restore() throws IllegalStateException;

    @ReferencePage(title = "CanvasRenderingContext2D.prototype.reset()", href = "https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/reset")
    void reset();
}
