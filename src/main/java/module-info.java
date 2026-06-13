import mujica.io.codec.TowboatCharsetProvider;
import mujica.io.fs.ExtensionFileTypeDetector;
import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2023/12/12", name = "CDP")
module CRYCHIC {
    requires java.base;
    requires java.compiler;
    requires java.desktop;
    requires java.management;

    requires annotations;
    requires org.slf4j;
    requires io.netty.all;

    exports mujica.reflect.modifier;
    exports mujica.reflect.function;
    exports mujica.reflect.basic;
    exports mujica.reflect.bytecode;
    exports mujica.reflect.source;
    exports mujica.io.function;
    exports mujica.io.stream;
    exports mujica.io.buffer;
    exports mujica.io.hash;
    exports mujica.io.codec;
    exports mujica.io.compress;
    exports mujica.io.nest;
    exports mujica.io.fs;
    exports mujica.ds;
    exports mujica.ds.generic;
    exports mujica.ds.generic.list;
    exports mujica.ds.generic.set;
    exports mujica.ds.generic.map;
    exports mujica.ds.generic.heap;
    exports mujica.ds.bit;
    exports mujica.ds.bit.list;
    exports mujica.ds.i8;
    exports mujica.ds.i8.list;
    exports mujica.ds.i8.view;
    exports mujica.ds.i8.run;
    exports mujica.ds.of_char.sequence;
    exports mujica.ds.of_char.filter;
    exports mujica.ds.of_char.sanitizer;
    exports mujica.ds.of_char.collation;
    exports mujica.ds.of_char.number;
    exports mujica.ds.of_char.format;
    exports mujica.ds.of_char.bnf;
    exports mujica.ds.of_char.regex;
    exports mujica.ds.of_int;
    exports mujica.ds.of_int.list;
    exports mujica.ds.of_int.set;
    exports mujica.ds.of_int.map;
    exports mujica.ds.of_int.heap;
    exports mujica.ds.of_long;
    exports mujica.ds.of_long.list;
    exports mujica.ds.f64;
    exports mujica.ds.f64.list;
    exports mujica.algebra.discrete;
    exports mujica.algebra.prime;
    exports mujica.algebra.random;
    exports mujica.geometry;
    exports mujica.geometry.g2d;
    exports mujica.json.handler;
    exports mujica.json.io;
    exports mujica.json.modifier;
    exports mujica.json.reflect;
    exports mujica.json.provided;
    exports mujica.netty.concurrent;
    exports mujica.netty.mysql;

    provides java.nio.charset.spi.CharsetProvider with TowboatCharsetProvider;
    provides java.nio.file.spi.FileTypeDetector with ExtensionFileTypeDetector.Cached;
}
