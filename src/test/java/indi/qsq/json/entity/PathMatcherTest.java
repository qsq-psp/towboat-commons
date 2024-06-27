package indi.qsq.json.entity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2022/7/30.
 */
public class PathMatcherTest {

    @Test
    public void testBuildKeyIndex() {
        final PathTree tree = new PathTree();
        tree.build("{\"a\":[0,0],\"b\":[0,3],\"c\":[1,2]}");
        assertEquals(new PathDestination.AtKey("b"), tree.get(0, 3));
        assertEquals(new PathDestination.AtKey("c"), tree.get(1, 2));
        assertEquals(new PathDestination.AtKey("a"), tree.get(0, 0));
        assertNull(tree.get());
        assertNull(tree.get(0));
        assertNull(tree.get(5));
        assertNull(tree.get(0, 0, 7));
        assertNull(tree.get(1, 1, 1));
        assertNull(tree.get(8, 8, 4, 8));
    }

    @Test
    public void testBuildKeyKey() {
        final PathTree tree = new PathTree();
        tree.build("{\"+\":[\"am\",\"is\",\"are\"],\"-\":[\"are\",\"are\",\"are\"],\".\":[\"am\",\"pm\"]}");
        assertEquals(new PathDestination.AtKey("+"), tree.get("am", "is", "are"));
        assertEquals(new PathDestination.AtKey("-"), tree.get("are", "are", "are"));
        assertEquals(new PathDestination.AtKey("."), tree.get("am", "pm"));
        assertNull(tree.get());
        assertNull(tree.get("+"));
        assertNull(tree.get("am"));
        assertNull(tree.get("is"));
        assertNull(tree.get("are"));
        assertNull(tree.get("pm"));
        assertNull(tree.get("am", "am", "pm"));
        assertNull(tree.get("am", "are", "is"));
        assertNull(tree.get("are", "are"));
        assertNull(tree.get("am", "pm", "ac", "dc"));
    }

    @Test
    public void testBuildKeyMix() {
        final PathTree tree = new PathTree();
        tree.build("{\"agrarian\":[\"logistics\",3,0,\"madder\"],\"aneurine\":[\"maroon\",5],\"mechanoid\":[\"assassination\",\"mipmap\"],\"monastery\":[2,2],\"camouflage\":[2,\"ornamental\",\"chiffon\"]}");
        assertEquals(new PathDestination.AtKey("agrarian"), tree.get("logistics", 3, 0, "madder"));
        assertEquals(new PathDestination.AtKey("aneurine"), tree.get("maroon", 5));
        assertEquals(new PathDestination.AtKey("mechanoid"), tree.get("assassination", "mipmap"));
        assertEquals(new PathDestination.AtKey("monastery"), tree.get(2, 2));
        assertEquals(new PathDestination.AtKey("camouflage"), tree.get(2, "ornamental", "chiffon"));
        assertNull(tree.get());
        assertNull(tree.get("logistics"));
        assertNull(tree.get("logistics", 0, 3, "madder"));
        assertNull(tree.get(3, 0, "madder"));
        assertNull(tree.get(5, "maroon"));
        assertNull(tree.get("assassination"));
        assertNull(tree.get("assassination", 5));
        assertNull(tree.get("maroon"));
        assertNull(tree.get("mipmap", 0, 1));
        assertNull(tree.get(0, 2, 2));
        assertNull(tree.get(2, 2, 0));
        assertNull(tree.get("ornamental", "chiffon"));
        assertNull(tree.get("ornamental", "chiffon", 2, 2));
    }

    @Test
    public void testBuildIndexIndex() {
        final PathTree tree = new PathTree();
        tree.build("[[1,1],[1,2],[2,2],[2,4],[3,0]]");
        assertEquals(new PathDestination.AtIndex(0), tree.get(1, 1));
        assertEquals(new PathDestination.AtIndex(1), tree.get(1, 2));
        assertEquals(new PathDestination.AtIndex(2), tree.get(2, 2));
        assertEquals(new PathDestination.AtIndex(3), tree.get(2, 4));
        assertEquals(new PathDestination.AtIndex(4), tree.get(3, 0));
        assertNull(tree.get());
        assertNull(tree.get(0));
        assertNull(tree.get(0, 0));
        assertNull(tree.get(1, 0));
        assertNull(tree.get(1, 1, 1));
        assertNull(tree.get(4, 2));
        assertNull(tree.get(2, 1));
        assertNull(tree.get(0, 3));
    }

    @Test
    public void testBuildIndexKey() {
        final PathTree tree = new PathTree();
        tree.build("[[\"oxygen\"],[\"clergyman\"],[\"coercion\"],[\"you\",\"I\",\"they\"],[\"you\",\"me\",\"them\"]]");
        assertEquals(new PathDestination.AtIndex(0), tree.get("oxygen"));
        assertEquals(new PathDestination.AtIndex(1), tree.get("clergyman"));
        assertEquals(new PathDestination.AtIndex(2), tree.get("coercion"));
        assertEquals(new PathDestination.AtIndex(3), tree.get("you", "I", "they"));
        assertEquals(new PathDestination.AtIndex(4), tree.get("you", "me", "them"));
        assertNull(tree.get());
        assertNull(tree.get("penultimate"));
        assertNull(tree.get("you", "oxygen"));
        assertNull(tree.get("I", "clergyman"));
        assertNull(tree.get("me", "coercion"));
        assertNull(tree.get("you", "they"));
        assertNull(tree.get("you", "them"));
        assertNull(tree.get("I", "they"));
        assertNull(tree.get("me", "them"));
        assertNull(tree.get("they", "I", "you"));
        assertNull(tree.get("them", "me"));
    }

    @Test
    public void testBuildIndexMix() {
        final PathTree tree = new PathTree();
        tree.build("[[0,\"contaminant\"],[1,\"cornerstone\"],[\"portraiture\"],[2,\"detergent\"],[\"diatom\"],[0,3],[0,\"raccoon\"],[2,\"regiment\",\"emerald\"]]");
        assertEquals(new PathDestination.AtIndex(0), tree.get(0, "contaminant"));
        assertEquals(new PathDestination.AtIndex(1), tree.get(1, "cornerstone"));
        assertEquals(new PathDestination.AtIndex(2), tree.get("portraiture"));
        assertEquals(new PathDestination.AtIndex(3), tree.get(2, "detergent"));
        assertEquals(new PathDestination.AtIndex(4), tree.get("diatom"));
        assertEquals(new PathDestination.AtIndex(5), tree.get(0, 3));
        assertEquals(new PathDestination.AtIndex(6), tree.get(0, "raccoon"));
        assertEquals(new PathDestination.AtIndex(7), tree.get(2, "regiment", "emerald"));
        assertNull(tree.get());
        assertNull(tree.get(0));
        assertNull(tree.get(1));
        assertNull(tree.get(2));
        assertNull(tree.get(3));
        assertNull(tree.get("contaminant"));
        assertNull(tree.get("cornerstone"));
        assertNull(tree.get(0, "portraiture"));
        assertNull(tree.get(1, "detergent"));
        assertNull(tree.get(2, "diatom"));
        assertNull(tree.get(3, 3));
        assertNull(tree.get("raccoon", 0));
        assertNull(tree.get("regiment", 2, "emerald"));
    }

    @Test
    public void testBuildMixMix() {
        final PathTree tree = new PathTree();
        tree.build("{}");
        tree.build("[]");
        tree.build("{\"hopper\":[0,\"wavefront\"],\"inhale\":[\"java\"],\"tulip\":[\"java\",\"script\"],\"root\":[]}");
        tree.build("[[\"java\",\"class\",\"jar\"],[4,0,2],[0,\"lactose\"],[\"zenith\"]]");
        assertEquals(new PathDestination.AtKey("root"), tree.get());
        assertEquals(new PathDestination.AtKey("hopper"), tree.get(0, "wavefront"));
        assertEquals(new PathDestination.AtKey("inhale"), tree.get("java"));
        assertEquals(new PathDestination.AtKey("tulip"), tree.get("java", "script"));
        assertEquals(new PathDestination.AtIndex(0), tree.get("java", "class", "jar"));
        assertEquals(new PathDestination.AtIndex(1), tree.get(4, 0, 2));
        assertEquals(new PathDestination.AtIndex(2), tree.get(0, "lactose"));
        assertEquals(new PathDestination.AtIndex(3), tree.get("zenith"));
        assertNull(tree.get(0));
        assertNull(tree.get(4));
        assertNull(tree.get("wavefront"));
        assertNull(tree.get("wavefront", 0));
        assertNull(tree.get("lactose", 0));
        assertNull(tree.get("lactose", 0, 2));
        assertNull(tree.get("root"));
        assertNull(tree.get("javascript"));
    }

    @Test
    public void testPutAndBuild() {
        final PathTree tree = new PathTree();
        assertTrue(tree.put(new PathDestination.AtIndex(2), 0, 7));
        assertTrue(tree.put(new PathDestination.AtIndex(1), "base", 7));
        assertTrue(tree.put(new PathDestination.AtKey("radix"), "base", "hardware"));
        assertTrue(tree.put(new PathDestination.AtKey("bread"), "interface", "hardware"));
        assertTrue(tree.put(new PathDestination.AtKey("print"), "type", 30));
        tree.build("[[\"type\",20],[\"base\",\"hardware\"],[\"base\",\"basic\"]]");
        tree.build("{\"HTML\":[0,7],\"CPL\":[\"type\",30]}");
        assertEquals(new PathDestination.AtKey("HTML"), tree.get(0, 7));
        assertEquals(new PathDestination.AtIndex(1), tree.get("base", 7));
        assertEquals(new PathDestination.AtIndex(1), tree.get("base", "hardware"));
        assertEquals(new PathDestination.AtKey("bread"), tree.get("interface", "hardware"));
        assertEquals(new PathDestination.AtKey("CPL"), tree.get("type", 30));
        assertEquals(new PathDestination.AtIndex(0), tree.get("type", 20));
        assertEquals(new PathDestination.AtIndex(2), tree.get("base", "basic"));
    }

    @Test
    public void testBuildAndPut() {
        final PathTree tree = new PathTree();
        tree.build("[[\"youth\",4,5],[\"null\",0,0,2],[\"spring\",9],[\"east\",1,1,2]]");
        tree.build("{\"exit\":[\"null\",0,1,4],\"terminate\":[\"test\",3],\"escape\":[\"east\",1,1,0,8],\"vanish\":[\"spring\",2,0]}");
        assertTrue(tree.put(new PathDestination.AtIndex(300), "null", 0));
        assertTrue(tree.put(new PathDestination.AtIndex(320), "null", 0, 0));
        assertTrue(tree.put(new PathDestination.AtIndex(340), "test", 2));
        assertTrue(tree.put(new PathDestination.AtIndex(360), "test", 3));
        assertTrue(tree.put(new PathDestination.AtIndex(380), "test", 4));
        assertTrue(tree.put(new PathDestination.AtIndex(400), "spring", 0, 2));
        assertTrue(tree.put(new PathDestination.AtKey("vast"), "youth", 4, 5));
        assertTrue(tree.put(new PathDestination.AtKey("bold"), "spring", 8));
        assertTrue(tree.put(new PathDestination.AtKey("cyan"), "east", 1, 1, 2));
        assertEquals(new PathDestination.AtKey("vast"), tree.get("youth", 4, 5));
        assertEquals(new PathDestination.AtIndex(1), tree.get("null", 0, 0, 2));
        assertEquals(new PathDestination.AtIndex(2), tree.get("spring", 9));
        assertEquals(new PathDestination.AtKey("cyan"), tree.get("east", 1, 1, 2));
        assertEquals(new PathDestination.AtKey("exit"), tree.get("null", 0, 1, 4));
        assertEquals(new PathDestination.AtIndex(360), tree.get("test", 3));
        assertEquals(new PathDestination.AtKey("escape"), tree.get("east", 1, 1, 0, 8));
        assertEquals(new PathDestination.AtKey("vanish"), tree.get("spring", 2, 0));
    }

    @Test
    public void testTransformToObject() {
        final PathTree tree = new PathTree();
        tree.build("{\"$\":[\"$\"]}");
        assertEquals("{}", PathMatcher.ToObjectConsumer.transform(tree, "{}"));
    }
}
