import mujica.io.codec.TowboatCharsetProvider;
import mujica.io.fs.ExtensionFileTypeDetector;
import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2023/12/12", name = "CDP")
module CDP {
    requires java.base;
    requires java.compiler;
    requires java.desktop;

    requires annotations;
    requires org.slf4j;
    requires io.netty.all;

    exports mujica.reflect.modifier;
    exports mujica.reflect.function;
    exports mujica.reflect.basic;
    exports mujica.reflect.bytecode;
    exports mujica.reflect.source;
    exports mujica.io.function;
    exports mujica.io.codec;
    exports mujica.io.buffer;
    exports mujica.io.hash;
    exports mujica.io.nest;
    exports mujica.ds;
    exports mujica.ds.generic;
    exports mujica.ds.generic.list;
    exports mujica.ds.generic.set;
    exports mujica.ds.generic.map;
    exports mujica.ds.generic.heap;
    exports mujica.ds.of_boolean;
    exports mujica.ds.of_boolean.list;
    exports mujica.ds.of_byte;
    exports mujica.ds.of_byte.list;
    exports mujica.ds.of_int;
    exports mujica.ds.of_int.list;
    exports mujica.ds.of_int.set;
    exports mujica.ds.of_int.map;
    exports mujica.ds.of_int.heap;
    exports mujica.ds.of_long;
    exports mujica.ds.of_long.list;
    exports mujica.ds.of_double;
    exports mujica.ds.of_double.list;
    exports mujica.text.number;
    exports mujica.text.format;
    exports mujica.text.collation;
    exports mujica.text.bnf;
    exports mujica.text.regex;
    exports mujica.algebra.discrete;
    exports mujica.algebra.prime;
    exports mujica.algebra.random;
    exports mujica.algebra.symbol;
    exports mujica.geometry;
    exports mujica.geometry.g2d;
    exports mujica.json.io;
    exports mujica.json.entity;
    exports mujica.netty.concurrent;

    provides java.nio.charset.spi.CharsetProvider with TowboatCharsetProvider;
    provides java.nio.file.spi.FileTypeDetector with ExtensionFileTypeDetector;
}
