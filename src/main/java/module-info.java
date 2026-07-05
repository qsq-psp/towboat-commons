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
    exports mujica.ds.any;
    exports mujica.ds.any.list;
    exports mujica.ds.any.set;
    exports mujica.ds.any.map;
    exports mujica.ds.any.heap;
    exports mujica.ds.bit;
    exports mujica.ds.bit.list;
    exports mujica.ds.i8;
    exports mujica.ds.i8.list;
    exports mujica.ds.i8.view;
    exports mujica.ds.i8.run;
    exports mujica.ds.text.sequence;
    exports mujica.ds.text.filter;
    exports mujica.ds.text.sanitizer;
    exports mujica.ds.text.collation;
    exports mujica.ds.text.number;
    exports mujica.ds.text.format;
    exports mujica.ds.text.bnf;
    exports mujica.ds.text.regex;
    exports mujica.ds.i32;
    exports mujica.ds.i32.list;
    exports mujica.ds.i32.set;
    exports mujica.ds.i32.map;
    exports mujica.ds.i32.heap;
    exports mujica.ds.i64;
    exports mujica.ds.i64.list;
    exports mujica.ds.f32;
    exports mujica.ds.f64;
    exports mujica.ds.slot;
    exports mujica.algebra.discrete;
    exports mujica.algebra.prime;
    exports mujica.algebra.random;
    exports mujica.geometry;
    exports mujica.geometry.g2d;
    exports mujica.json.container;
    exports mujica.json.handler;
    exports mujica.json.io;
    exports mujica.json.modifier;
    exports mujica.json.reflect;
    exports mujica.netty.concurrent;
    exports mujica.netty.mysql;

    provides java.nio.charset.spi.CharsetProvider with TowboatCharsetProvider;
    provides java.nio.file.spi.FileTypeDetector with ExtensionFileTypeDetector.Cached;
}
