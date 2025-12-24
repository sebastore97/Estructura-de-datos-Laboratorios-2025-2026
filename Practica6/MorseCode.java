import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MorseCode {

    EDBinaryNode<Character> morseRoot = null;

    public MorseCode() {

        morseRoot = new EDBinaryNode<>(null);
    }

    public MorseCode(String fileName) {
        morseRoot= new EDBinaryNode<>(null);
        readFileInfo(fileName);
    }

    private void readFileInfo(String filename) {
        Scanner input = null;
        try {
            input = new Scanner(new File(filename));
        } catch (FileNotFoundException exception) {
            System.out.println("File not found!");
        }

        while (input.hasNextLine()) {
            String cars = input.next();
            char car = cars.charAt(0);
            String code = input.next();
            insert(car, code);
        }
        input.close();
    }

    public void insert(char car, String code) {
        insert(morseRoot, car, code);
    }

    private void insert(EDBinaryNode<Character> currentNode, char car, String code) {
        if (code.length() == 0) {
            currentNode.setData(car);
            return;
        }

        char c = code.charAt(0);

        if (c == '.') {
            if (!currentNode.hasLeft())
                currentNode.setLeft(new EDBinaryNode<>(null));
            insert(currentNode.left(), car, code.substring(1));
        } else {
            if (!currentNode.hasRight())
                currentNode.setRight(new EDBinaryNode<>(null));
            insert(currentNode.right(), car, code.substring(1));
        }
    }

    public String inorderPrint() {
        return inorderPrint(morseRoot);
    }

    private String inorderPrint(EDBinaryNode<Character> currentNode) {
        if (currentNode == null)
            return "";

        String secuence = "";
        secuence += inorderPrint(currentNode.left());

        if (currentNode.data() != null)
            secuence += currentNode.data();

        secuence += inorderPrint(currentNode.right());

        return secuence;
    }

    public int size() {
        return size(morseRoot);
    }

    private int size(EDBinaryNode<Character> currentNode) {
        if (currentNode == null)
            return 0;

        int cont = 1;

        if (currentNode.isLeaf())
            return cont;

        if (currentNode.hasLeft())
            cont += size(currentNode.left());

        if (currentNode.hasRight())
            cont += size(currentNode.right());

        return cont;
    }

    //-------------- Version más corta del método size() -------------------------------------------------------------
//    private int size(EDBinaryNode<Character> node) {
//            if (node == null)
//                return 0;
//
//        return size(node.left()) + size(node.right()) + 1;
//    }



    public boolean isExtended() {
        return isExtended(morseRoot);
    }

    private boolean isExtended(EDBinaryNode<Character> currentNode) {
        if (currentNode == null)
            return true;

        if (currentNode.isLeaf())
            return true;

        if (!currentNode.hasLeft() || !currentNode.hasRight())
            return false;

        return isExtended(currentNode.left()) && isExtended(currentNode.right());
    }


    public Set<Character> nonExtendedNodes () {
        Set<Character> set = new HashSet<>();
        nonExtendedNodes(morseRoot, set);

        return set;
    }

    private void nonExtendedNodes (EDBinaryNode<Character> currentNode, Set<Character> set) {
        if (currentNode != null) {
            if (currentNode.hasRight() && !currentNode.hasLeft() || currentNode.hasLeft() && !currentNode.hasRight()) {
                if (currentNode.data() != null)
                    set.add(currentNode.data());
            }
        } else
            return;

        nonExtendedNodes(currentNode.left(), set);
        nonExtendedNodes(currentNode.right(), set);
    }

    public String decode(String codetext) {
        String[] codes = codetext.split(" ");
        String result = "";

        for (String code : codes)
            result += decode(morseRoot, code);

        return result;
    }

    private char decode(EDBinaryNode<Character> currentNode, String codetext) {
        if (codetext.length() == 0)
            return currentNode.data();

        char c = codetext.charAt(0);

        if (c == '.')
            return decode(currentNode.left(), codetext.substring(1));
        else
            return decode(currentNode.right(), codetext.substring(1));
    }


    public String encode(String text) {
        String result = "";

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c != ' ') {
                String morseCode = encode(morseRoot, c);
                if (morseCode != null)
                    result += morseCode + " ";
            }
        }

       return result.substring(0, result.length() - 1);
    }

    private String encode(EDBinaryNode<Character> currentNode, char character) {
        if (currentNode == null)
            return null;

        if (currentNode.data() != null)
            if (currentNode.data().equals(character))
                return "";

        String left = encode(currentNode.left(), character);

        if (left != null) {
            return "." + left;
        } else {
            String right = encode(currentNode.right(), character);
            if (right != null)
                return "-" + right;
        }

        return null;
    }

}
