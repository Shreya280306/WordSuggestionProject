
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WordSearchFunctions {
    WordSearchNode root = new WordSearchNode();

    WordSearchFunctions(WordSearchNode givenRoot){
        root = givenRoot;
    }
    public static void insertingNodeInTrie(WordSearchNode root, String str){
        WordSearchNode current = root;
        for(int i=0; i<str.length(); i++){
            char ch = str.charAt(i);
            if(current.children[ch - 'a'] == null){
                WordSearchNode newNode = new WordSearchNode(ch);
                current.children[ch - 'a'] = newNode;
            }
            current = current.children[ch - 'a'];
        }
        current.isEndOfWord = true;
    }

    public static WordSearchNode searchingSubstring(WordSearchNode root, String str) {
        WordSearchNode current = root;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (current.children[ch - 'a'] != null) {
                current = current.children[ch - 'a'];
            }
            else{
                return null;
            }
        }
        return current;
    }

    public static void insertSuggestions(WordSearchNode root, String substring, String[] suggestions){
        WordSearchNode lastNode = searchingSubstring(root, substring);
        if(lastNode != null) {
            lastNode.suggestions = suggestions;
        }
    }

    public static String[] getSuggestions(WordSearchNode root, String substring){
        WordSearchNode lastNode = searchingSubstring(root, substring);
        if(lastNode == null){
            return null;
        }
        return lastNode.suggestions;
    }

    public static void readWordsFromFile1(WordSearchNode root, String filePath1){
        try(BufferedReader br = new BufferedReader(new FileReader(filePath1))){
            String word;
            while((word = br.readLine()) != null){
                word = word.trim();
                if(!word.isEmpty()){
                    insertingNodeInTrie(root, word.toLowerCase());
                }
            }
        }

        catch(IOException e){
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void readSuggestionsFromFile2(WordSearchNode root, String filePath2){
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(filePath2))){
            while((line = br.readLine()) != null){
                String[] cells = line.split(",");
                if(cells.length == 0) continue;
                String substring = cells[0].trim();
                String[] suggestions = new String[5];
                for(int i=0; i<5; i++){
                    if(i+1 < cells.length){
                        suggestions[i] = cells[i+1].trim();
                    }
                    else{
                        suggestions[i] = "";
                    }
                }
                insertSuggestions(root,substring, suggestions);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

