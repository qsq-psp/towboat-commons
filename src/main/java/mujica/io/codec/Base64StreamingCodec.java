package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;

/**
 * +--first octet--+-second octet--+--third octet--+
 * |7 6 5 4 3 2 1 0|7 6 5 4 3 2 1 0|7 6 5 4 3 2 1 0|
 * +-----------+---+-------+-------+---+-----------+
 * |5 4 3 2 1 0|5 4 3 2 1 0|5 4 3 2 1 0|5 4 3 2 1 0|
 * +--1.index--+--2.index--+--3.index--+--4.index--+
 */
@CodeHistory(date = "2025/4/16")
@ReferencePage(title = "RFC4648: The Base16, Base32, and Base64 Data Encodings", href = "https://www.rfc-editor.org/rfc/inline-errata/rfc4648.html")
public interface Base64StreamingCodec extends BufferingPolicyOwner {

    int getBuffer();
}
