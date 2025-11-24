package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/11/8")
public class NestResourceKeys {

    public static final NestResourceKey.Gzip GZIP_EXTRA = new NestResourceKey.Gzip("extra");

    public static final NestResourceKey.Gzip GZIP_COMMENT = new NestResourceKey.Gzip("comment");

    public static final NestResourceKey.GzipMain GZIP_UNNAMED_MAIN = new NestResourceKey.GzipMain("unnamed-main");

    public static final NestResourceKey.Gzip GZIP_MAIN_TRAILING = new NestResourceKey.Gzip("main-trailing");

    public static final NestResourceKey.Gzip GZIP_FILE_TRAILING = new NestResourceKey.Gzip("file-trailing");
}
