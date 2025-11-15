import java.util.Scanner;

public class Main {
    static void runningInCMD(){
        WordSearchNode root = new WordSearchNode();
        WordSearchFunctions trie = new WordSearchFunctions(root);
        Scanner sc = new Scanner(System.in);

//for setting up the trie
        String filePath1 = "C:\\Users\\shrey\\Downloads\\file with words.csv";
        WordSearchFunctions.readWordsFromFile1(root, filePath1);
        String filePath2 = "C:\\Users\\shrey\\Downloads\\file with substring suggestions.csv";
        WordSearchFunctions.readSuggestionsFromFile2(root, filePath2);

//for accessing the elements of the trie
        System.out.println("Enter the substring you want suggestions for: ");
        String substring = sc.nextLine();
        String[] suggestedWords = WordSearchFunctions.getSuggestions(root, substring);
        if(suggestedWords == null){
            System.out.println("This substring doesn't exist in the library! ");
            return;
        }
        System.out.println("Here are the suggestions for this substring ");
        for(int i=0; i<5; i++){
            if(!suggestedWords[i].isEmpty()) {
                System.out.println(suggestedWords[i]);
            }
        }
        System.out.println("Does any suggestion match the word you were typing? (yes/no)");
        String answer = sc.nextLine();
        if(answer.equals("yes") || answer.equals("Yes")){
            System.out.println("Enter the number of the word that matched (1-5)");
            int num = sc.nextInt();
            sc.nextLine();//Clears the next line

            WordSearchNode node = WordSearchFunctions.searchingSubstring(root, substring);
            if(node != null) {
                node.freqArray[num - 1] += 1;
                System.out.println("Thanks for your response!");
            }
        }
        else{
            System.out.println("Okay! What was the word that you were typing? ");
            String fullWord = sc.nextLine();
            WordSearchNode node = WordSearchFunctions.searchingSubstring(root, substring);
            int index = 0;
            if(node != null) {
                int least = node.freqArray[0];
                for (int i = 0; i < 5; i++) {
                    if (node.freqArray[i] < least) {
                        least = node.freqArray[i];
                        index = i;
                    }
                }
                node.suggestions[index] = fullWord;
                node.suggestions = suggestedWords;
                System.out.println("Okay! Library has been updated according to your preference");
            }
        }
        System.out.println("Do you want to check for another word?(yes/no)");
        String output = sc.nextLine();
        if(output.equalsIgnoreCase("yes")){
            runningInCMD();
        }
        else{
            System.out.println("Ok! Exiting");
        }
    }
    public static void main(String[] args) {
        runningInCMD();
    }
}