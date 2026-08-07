package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;
import mujica.text.sanitizer.CharSequenceAppender;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/3/4")
public class Bracket {

    @NotNull
    public final String left, right;

    public Bracket(@NotNull String left, @NotNull String right) {
        super();
        this.left = left;
        this.right = right;
    }

    public Bracket(@NotNull String both) {
        this(both, both);
    }

    @Override
    public int hashCode() {
        return left.hashCode() * 223 + right.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Bracket)) {
            return false;
        }
        final Bracket that = (Bracket) obj;
        return this.left.equals(that.left) && this.right.equals(that.right);
    }

    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Bracket[left = ");
        CharSequenceAppender.Json.ESSENTIAL.append(left, sb);
        sb.append(", right = ");
        CharSequenceAppender.Json.ESSENTIAL.append(right, sb);
        return sb.append("]").toString();
    }
}
