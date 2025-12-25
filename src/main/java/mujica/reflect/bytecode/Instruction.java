package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/13")
@ReferencePage(title = "JVMS12 Instructions", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-6.html#jvms-6.5")
public class Instruction extends ClassFileNodeAdapter {

    @NotNull
    final CodeAttributeInfo parent;

    final int instructionIndex;

    public Instruction(@NotNull CodeAttributeInfo parent, int index) {
        super();
        this.parent = parent;
        this.instructionIndex = index;
    }

    @Override
    public int groupCount() {
        final int opcode = 0xff & parent.code.get(instructionIndex);
        if (opcode == Opcode.TABLESWITCH || opcode == Opcode.LOOKUPSWITCH || opcode == Opcode.WIDE) {
            return 1;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public Class<? extends ClassFileNode> getGroup(int groupIndex) {
        final int opcode = 0xff & parent.code.get(instructionIndex);
        if (opcode == Opcode.TABLESWITCH || opcode == Opcode.LOOKUPSWITCH) {
            if (groupIndex == 0) {
                return SwitchCase.class;
            }
        } else if (opcode == Opcode.WIDE) {
            if (groupIndex == 0) {
                return Instruction.class;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        if (group == SwitchCase.class) {
            return 2;
        } else if (group == Instruction.class) {
            return 1;
        } else {
            return 0;
        }
    }
}
