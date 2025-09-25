import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2023/12/12", name = "CDP")
module CDP {
    requires java.base;

    requires annotations;
    requires io.netty.all;

    exports org.slf4j;

    exports mujica.reflect;
    exports mujica.reflect.modifier;
    exports mujica.reflect.function;
    exports mujica.reflect.meta;
    exports mujica.io.function;
    exports mujica.io.view;
    exports mujica.io.stream;
    exports mujica.io.buffer;
    exports mujica.io.hash;
    exports mujica.io.charset;
    exports mujica.io.fs;
    exports mujica.ds;
    exports mujica.ds.generic;
    exports mujica.ds.generic.list;
    exports mujica.ds.generic.set;
    exports mujica.ds.generic.heap;
    exports mujica.ds.of_boolean;
    exports mujica.ds.of_int;
    exports mujica.ds.of_int.list;
    exports mujica.ds.of_int.set;
    exports mujica.ds.of_int.map;
    exports mujica.ds.of_int.heap;
    exports mujica.ds.of_long;
    exports mujica.ds.of_double;
    exports mujica.text.number;
    exports mujica.text.escape;
    exports mujica.text.word;
    exports mujica.text.format;
    exports mujica.text.pattern;
    exports mujica.text.collation;
    exports mujica.math.algebra;
    exports mujica.math.algebra.discrete;
    exports mujica.math.algebra.prime;
    exports mujica.math.algebra.random;
    exports mujica.math.algebra.symbol;
    exports mujica.math.geometry;
    exports mujica.math.geometry.g2d;

    provides java.nio.charset.spi.CharsetProvider with mujica.io.charset.TowboatCharsetProvider;
}
