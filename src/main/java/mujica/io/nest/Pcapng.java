package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;

@CodeHistory(date = "2020/12/26", project = "webbiton")
@CodeHistory(date = "2025/10/9")
@ReferencePage(title = "PCAP Now Generic (pcapng) Capture File Format", href = "https://ietf-opsawg-wg.github.io/draft-ietf-opsawg-pcap/draft-ietf-opsawg-pcapng.html")
public interface Pcapng {

    String SUFFIX = "pcapng";

    String DOT_SUFFIX = "." + SUFFIX;

    interface BlockType {

        int SECTION_HEADER =                0x0a0d0d0a;

        int INTERFACE_DESCRIPTION =         0x00000001;

        @Deprecated
        int PACKET =                        0x00000002;

        int SIMPLE_PACKET =                 0x00000003;

        int NAME_RESOLUTION =               0x00000004;

        int INTERFACE_STATISTIC =           0x00000005;

        int ENHANCED_PACKET =               0x00000006;

        int IRIG_TIMESTAMP =                0x00000007;

        int ARINC_INFORMATION =             0x00000008;

        int SYSTEMD_JOURNAL_EXPORT =        0x00000009;

        int DECRYPTION_SECRETS =            0x0000000a;

        int LOG_SUBJECT =                   0x806d0001;

        int LOG_THREAD =                    0x806d0002;

        int LOG_TEXT =                      0x806d0003;

        int LOG_JSON =                      0x806d0004;

        int LOG_HTML =                      0x806d0005;

        int LOG_JAVA_OBJECT =               0x806d0006;
    }

    /**
     * 2 bytes : option code or {@link Option#END_OF_OPTION}
     * 2 bytes : option length of value without padding
     * variable : option value, padded to 4 bytes
     */
    interface Option {

        /**
         * zero length, [1, 1]
         */
        int END_OF_OPTION =                 0x0000;

        /**
         * variable length UTF-8 string, [0, +]
         */
        int COMMENT =                       0x0001;
    }

    /**
     * 4 bytes : block type = {@link BlockType#SECTION_HEADER}
     * 4 bytes : block total length
     * 4 bytes : byte order magic = {@link SectionHeader#MAGIC}
     * 2 bytes : major version = 1
     * 2 bytes : minor version = 0
     * 8 bytes : section length
     * variable : options
     * 4 bytes : block total length
     */
    interface SectionHeader {

        int MAGIC =                         0x1a2b3c4d;

        /**
         * variable length UTF-8 string, [0, 1]
         */
        int OPTION_HARDWARE =               0x0002;

        /**
         * variable length UTF-8 string, [0, 1]
         */
        int OPTION_OS =                     0x0003;

        /**
         * variable length UTF-8 string, [0, 1]
         */
        int OPTION_USER_APPLICATION =       0x0004;
    }

    /**
     * 4 bytes : block type = {@link BlockType#INTERFACE_DESCRIPTION}
     * 4 bytes : block total length
     * 2 bytes : link type = {@link LinkType}
     * 2 bytes : reserved = 0
     * 4 bytes : snap length (zero indicates no limit)
     * variable : options
     * 4 bytes : block total length
     */
    interface InterfaceDescription {

        /**
         * variable length UTF-8 string, [0, 1]
         * for example :
         * eth0
         * \Device\NPF_{AD1CE675-96D0-47C5-ADD0-2504B9126B68}
         */
        int OPTION_NAME =                   0x0002;

        /**
         * variable length UTF-8 string, [0, 1]
         * for example :
         * Wi-Fi
         * Local Area Connection
         * Wireless Network Connection
         * First Ethernet Interface
         */
        int OPTION_DESCRIPTION =            0x0003;

        /**
         * 8 bytes (address and subnet mask), [0, +]
         */
        int OPTION_IPV4_ADDRESS =           0x0004;

        /**
         * 17 bytes (address and prefix length), [0, +]
         */
        int OPTION_IPV6_ADDRESS =           0x0005;

        /**
         * 6 bytes, [0, 1]
         */
        int OPTION_MAC_ADDRESS =            0x0006;

        /**
         * 8 bytes, [0, 1]
         */
        int OPTION_EUI_ADDRESS =            0x0007;

        /**
         * 8 bytes, [0, 1]
         */
        int OPTION_SPEED =                  0x0008;

        /**
         * 1 byte, [0, 1]
         */
        int OPTION_TIME_RESOLUTION =        0x0009;

        /**
         * 4 bytes, [0, 1]
         */
        int OPTION_TIME_ZONE =              0x000a;

        /**
         * libpcap string or BPF bytecode, [0, 1]
         */
        int OPTION_FILTER =                 0x000b;

        /**
         * variable length UTF-8 string, [0, 1]
         */
        int OPTION_OS =                     0x000c;

        /**
         * variable length UTF-8 string, [0, 1]
         */
        int OPTION_HARDWARE =               0x000f;

        /**
         * variable length UTF-8 string, [0, 1]
         */
        int OPTION_IANA_TIME_ZONE_NAME =    0x0012;
    }

    interface LinkType {

        int ETHERNET = 1;

        int RAW = 101;

        int IPV4 = 228;

        int IPV6 = 229;
    }

    /**
     * 4 bytes : block type = {@link BlockType#ENHANCED_PACKET}
     * 4 bytes : block total length
     * 4 bytes : interface ID
     * 4 bytes : timestamp (high)
     * 4 bytes : timestamp (low)
     * 4 bytes : capture packet length
     * 4 bytes : original packet length
     * variable : packet data, padded to 4 bytes
     * variable : options
     * 4 bytes : block total length
     */
    interface EnhancedPacket {

        /**
         * 4 bytes, [0, 1]
         * {@link #FLAG_INBOUND}
         * {@link #FLAG_OUTBOUND}
         * {@link #FLAG_UNICAST}
         * {@link #FLAG_MULTICAST}
         * {@link #FLAG_BROADCAST}
         * {@link #FLAG_PROMISCUOUS}
         */
        int OPTION_FLAGS =              0x0002;

        /**
         * variable length, [0, +]
         */
        int OPTION_HASH =               0x0003;

        /**
         * 8 bytes, [0, 1]
         */
        int OPTION_DROP_COUNT =         0x0004;

        /**
         * 8 bytes, [0, 1]
         */
        int OPTION_PACKET_ID =          0x0005;

        /**
         * 8 bytes, [0, 1]
         */
        int OPTION_QUEUE =              0x0006;

        int MASK_PACKET_DIRECTION =     0x00000003;

        int FLAG_INBOUND =              0x00000001;

        int FLAG_OUTBOUND =             0x00000002;

        int MASK_RECEPTION_TYPE =       0x0000001c;

        int FLAG_UNICAST =              0x00000004;

        int FLAG_MULTICAST =            0x00000008;

        int FLAG_BROADCAST =            0x0000000c;

        int FLAG_PROMISCUOUS =          0x00000010;
    }
} 
