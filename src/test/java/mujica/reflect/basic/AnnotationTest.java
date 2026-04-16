package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;

@CodeHistory(date = "2022/10/19", project = "nettyon", name = "AnnotationInheritanceRepetitionTest")
@CodeHistory(date = "2026/4/10")
public class AnnotationTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @interface Mark {

        int data();
    }

    @Mark(data = 1)
    static class Marked {

        @Mark(data = 2)
        public void foo() {}

        @Mark(data = 3)
        public void bar() {}
    }

    static class MarkedChild extends Marked {

        @Override
        public void foo() {}
    }

    @Test
    public void caseMarked() throws ReflectiveOperationException {
        Mark mark;
        mark = Marked.class.getAnnotation(Mark.class);
        Assert.assertNotNull(mark);
        Assert.assertEquals(1, mark.data());
        mark = MarkedChild.class.getAnnotation(Mark.class);
        Assert.assertNull(mark);
        mark = Marked.class.getDeclaredMethod("foo").getAnnotation(Mark.class);
        Assert.assertNotNull(mark);
        Assert.assertEquals(2, mark.data());
        mark = MarkedChild.class.getDeclaredMethod("foo").getAnnotation(Mark.class);
        Assert.assertNull(mark);
        mark = MarkedChild.class.getMethod("bar").getAnnotation(Mark.class);
        Assert.assertNotNull(mark);
        Assert.assertEquals(3, mark.data());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Inherited
    @interface InheritedMark {

        int data();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Repeatable(RepeatedMark.class)
    @interface RepeatingMark {

        int data();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @interface RepeatedMark {

        RepeatingMark[] value() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Inherited
    @Repeatable(RepeatedInheritedMark.class)
    @interface RepeatingInheritedMark {

        int data();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Inherited
    @interface RepeatedInheritedMark {

        RepeatingInheritedMark[] value() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Inherited
    @Repeatable(RepeatedItemInheritedMark.class)
    @interface RepeatingItemInheritedMark {

        int data();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Inherited
    @interface RepeatedItemInheritedMark {

        RepeatingItemInheritedMark[] value() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Inherited
    @Repeatable(RepeatedListInheritedMark.class)
    @interface RepeatingListInheritedMark {

        int data();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Inherited
    @interface RepeatedListInheritedMark {

        RepeatingListInheritedMark[] value() default {};
    }
}
