public class WordSearchNode {
    char ch;
    boolean isEndOfWord;
    String[] suggestions;
    WordSearchNode[] children;
    int[] freqArray;

    WordSearchNode(){
        ch = '\0'; //ch is null
        isEndOfWord = false;
        suggestions = null;
        children = new WordSearchNode[26];
        freqArray = new int[5];
    }

    WordSearchNode(char givenCharacter){
        ch = givenCharacter;
        isEndOfWord = false;
        suggestions = null;
        children = new WordSearchNode[26];
        freqArray = new int[5];
    }

//    public String[] wordsFromSubstring(String substring){
//        if (substring.equals("ap")) {
//            suggestions = new String[]{"apart", "appear", "apply", "approach", "approve"};
//        }
//        else if (substring.equals("app")) {
//            suggestions = new String[]{"appoint", "applause", "appliance", "appreciate", "appetite"};
//        }
//        else if (substring.equals("appl")) {
//            suggestions = new String[]{"apply", "apple", "applause", "application", "appliance"};
//        }
//        else if (substring.equals("apple")) {
//            suggestions = new String[]{"apple", "applesauce", "applewood", "applejack", "applepie"};
//        }
//        else {
//            suggestions = new String[]{};
//        }
//        return suggestions;
//    }
}
