package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;

/**
 *             1          2          3
 *  01234567 89012345 67890123 45678901 23456789
 * +--------+--------+--------+--------+--------+
 * |< 1 >< 2| >< 3 ><|.4 >< 5.|>< 6 ><.|7 >< 8 >|
 * +--------+--------+--------+--------+--------+
 *                                         <===> 8th character
 *                                   <====> 7th character
 *                              <===> 6th character
 *                        <====> 5th character
 *                  <====> 4th character
 *             <===> 3rd character
 *       <====> 2nd character
 *  <===> 1st character
 */
@CodeHistory(date = "2025/4/28")
@ReferencePage(title = "RFC4648: The Base16, Base32, and Base64 Data Encodings", href = "https://www.rfc-editor.org/rfc/inline-errata/rfc4648.html")
public interface Base32StreamingCodec extends BufferingPolicyOwner {

    int MESSAGE_STEP = Byte.SIZE; // 8
    int CODE_STEP = 5;
    int START_SHIFT = MESSAGE_STEP * CODE_STEP; // 40

    long getBuffer();
}
