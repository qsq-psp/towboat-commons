package mujica.json.entity;

import mujica.ds.of_int.list.CopyOnResizeIntList;
import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2023/4/29", project = "Ultramarine", name = "StructureChecker")
@CodeHistory(date = "2026/4/2")
public class StructureCheckAdapter<H extends JsonHandler> extends JsonHandlerAdapter<H> implements StructureChecked {

    protected final CopyOnResizeIntList stack = new CopyOnResizeIntList(null);

    public StructureCheckAdapter(H h) {
        super(h);
        stack.offerLast(STATE_START);
    }

    public void reset() {
        stack.clear();
        stack.offerLast(STATE_START);
    }
}
