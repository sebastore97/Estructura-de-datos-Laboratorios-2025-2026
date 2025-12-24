import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MorseCodeTest {
    static HashMap<Character, String> codes = new HashMap<>();
    final static List<Character> nodosBase= Arrays.asList('U', 'R');

    private void readFileInfo(String filename) throws FileNotFoundException {
        Scanner input = null;
        try {
            input = new Scanner(new File(filename));
        } catch (FileNotFoundException exception) {
            System.out.println("File not found!");
        }

        while (input.hasNextLine()) {
            String cars = input.next();
            String code = input.next();
            codes.put(cars.charAt(0), code);
        }
        input.close();
    }

    private String text2code(String text) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c != ' ') {
                String cod = codes.get(c);
                System.out.println("cod= " + cod);
                res.append(cod);
                res.append(' ');
            }
        }
        res.deleteCharAt(res.length() - 1); //remove last white space
        return res.toString();
    }

    @org.junit.Test
    public void inorderTest () throws FileNotFoundException {
        readFileInfo("morseCodes.txt");
        MorseCode morse = new MorseCode("morseCodes.txt");
        String res = morse.inorderPrint();
        String expected = "HSVIFUELRAPWJBDXNCKYTZGQMO";
        System.out.println("expected inorder sequence: " + expected);
        System.out.println("obtained inorder sequence: " + res);
        assertEquals(expected, res);
    }

    @org.junit.Test
    public void sizeTest () throws FileNotFoundException {
        readFileInfo("morseCodes.txt");
        MorseCode morse = new MorseCode();
        int expectedSize = 1;
        int size = morse.size();
        assertEquals(expectedSize, size);

        morse = new MorseCode("morseCodes.txt");
        size = morse.size();
        String expected = "HSVIFUELRAPWJBDXNCKYTZGQMO";
        expectedSize = expected.length();
        System.out.println("expected size: " + (expectedSize+1));
        System.out.println("obtained size: " + size);
        assertEquals(expectedSize+1, size);
    }


    @org.junit.Test
    public void isExtendedTest() throws FileNotFoundException {
        readFileInfo("morseCodes.txt");
        MorseCode morse = new MorseCode();
        boolean isExtended = morse.isExtended();
        assertTrue(isExtended);

        morse = new MorseCode("morseCodes.txt");
        isExtended = morse.isExtended();
        assertEquals(false, isExtended);
    }


    @org.junit.Test
    public void nonExtendedNodesTest() throws FileNotFoundException {
        System.out.println("Checking nonExtendedNodes");
        readFileInfo("morseCodes.txt");

        MorseCode morse = new MorseCode("morseCodes.txt");
        Set<Character> expected = new HashSet<>(nodosBase);

        Set<Character> obtained = morse.nonExtendedNodes();
        System.out.println("    esperado: "+expected);
        System.out.println("    obtenido: "+obtained);
        assertTrue(expected.equals(obtained));
    }

    @org.junit.Test
    public void decodingTest() throws FileNotFoundException {
        readFileInfo("morseCodes.txt");
        MorseCode mc = new MorseCode("morseCodes.txt");

        String res = mc.inorderPrint();
        String expected = "HSVIFUELRAPWJBDXNCKYTZGQMO";
        System.out.println("expected inorder sequence: " + expected);
        System.out.println("obtained inorder sequence: " + res);
        assertEquals(expected, res);

        // sos decode
        System.out.println("Decode Test 1");
        String str = "... --- ...";
        System.out.println("str = " + str);
        System.out.println("str should decode to: SOS");
        res = mc.decode(str);
        System.out.println("decode(str) = " + res);
        assertEquals("SOS", res);
        //testResults("sos", mc.decode(str));

        // abcdef...xyz decode
        System.out.println("Decode Test 2");
        str = ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -."
                + " --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --..";
        System.out.println("str = " + str);
        System.out.println("str should decode to: ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        res = mc.decode(str);
        System.out.println("decode(str) = " + res);
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", res);
        //testResults("abcdefghijklmnopqrstuvwxyz", mc.decode(str));

        // helpmeobiwanyouremyonlyhope decode
        System.out.println("Decode Test 3");
        String text = "HELPMEYOUAREMYONLYHOPE";
        str = text2code(text);
        //str = ".... . .-.. .--. -- . --- -... .. .-- .- -. -.-- --- ..-"
        //      + " .-. . -- -.-- --- -. .-.. -.-- .... --- .--. .";
        System.out.println("str = " + str);
        System.out.println("str should decode to: HELPMEYOUAREMYONLYHOPE");
        //String expected="helpmeyouaremyonlyhope"
        res = mc.decode(str);
        System.out.println("decode(str) = " + res);
        assertEquals(text, res);
    }

    @org.junit.Test
    public void encodingTest() throws FileNotFoundException {
        readFileInfo("morseCodes.txt");
        MorseCode mc = new MorseCode("morseCodes.txt");

        String res = mc.inorderPrint();
        String expected = "HSVIFUELRAPWJBDXNCKYTZGQMO";
        System.out.println("expected inorder sequence: " + expected);
        System.out.println("obtained inorder sequence: " + res);
        assertEquals(expected, res);

        System.out.println("\n Encode test 1");
        for (char c : codes.keySet()) {
            String str = "";
            str += c;
            //System.out.println("str = " + c);
            String cod = codes.get(c);
            System.out.println(c+" should encode to: " + cod);
            res = mc.encode(str);
            System.out.println("Obtained encode("+c+") = " + res);
            assertEquals(cod, res);
        }

        System.out.println("\n Encode test 2");
        String str = "SOS";
        //System.out.println("str = " + str);
        System.out.println(str+" should encode to: ... --- ...");
        expected = "... --- ...";
        res = mc.encode(str);
        //System.out.println("encode(str) = " + res);
        System.out.println("Obtained encode("+str+") = " + res);
        assertEquals(expected, res);

        System.out.println("\n Encode test 3");
        str = "PLEASE HELP ME";
        expected = text2code(str);
        //System.out.println("str = " + str);
        System.out.println(str+" should encode to: " + expected);
        res = mc.encode(str);
        //System.out.println("encode(str) = " + res);
        System.out.println("Obtained encode("+str+") = " + res);
        assertEquals(expected, res);
    }

}
