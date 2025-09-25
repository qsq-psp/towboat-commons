package mujica.io.stream;

import mujica.io.view.DataViewOwner;

/**
 * Created on 2025/4/16.
 * According to RFC4648 The Base16, Base32, and Base64 Data Encodings https://www.rfc-editor.org/rfc/inline-errata/rfc4648.html
 * +--first octet--+-second octet--+--third octet--+
 * |7 6 5 4 3 2 1 0|7 6 5 4 3 2 1 0|7 6 5 4 3 2 1 0|
 * +-----------+---+-------+-------+---+-----------+
 * |5 4 3 2 1 0|5 4 3 2 1 0|5 4 3 2 1 0|5 4 3 2 1 0|
 * +--1.index--+--2.index--+--3.index--+--4.index--+
 */
public interface Base64StreamingCodec extends DataViewOwner, BufferingPolicyOwner {

    int getBuffer();
}
