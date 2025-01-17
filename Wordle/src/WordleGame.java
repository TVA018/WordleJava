import java.util.*;
import static java.util.Map.entry;

public class WordleGame {
    private static final String FIRST_KB_ROW = "QWERTYUIOP", SECOND_KB_ROW = "ASDFGHJKL", THIRD_KB_ROW = "ZXCVBNM";
    static final HashMap<Character, Integer> KEYBOARD_INFO = new HashMap<>();

    static final String[] ANSI = {
            "\u001B[39m", //White
            "\u001B[37m", //Grey
            "\u001B[33m", //Yellow
            "\u001B[32m", //Green
    };

    private static final Scanner scanner = new Scanner(System.in);

    final String wordChosen;

    String[] wordsGuessed = {"", "", "", "", ""};
    private int currentGuess = 0;

    WordleGame() {
        this.wordChosen = WordList.List.get((int)(Math.random() * WordList.List.size()));

        for(int i = 0; i < FIRST_KB_ROW.length(); i++){
            KEYBOARD_INFO.put(FIRST_KB_ROW.charAt(i), 0);
        }

        for(int i = 0; i < SECOND_KB_ROW.length(); i++){
            KEYBOARD_INFO.put(SECOND_KB_ROW.charAt(i), 0);
        }

        for(int i = 0; i < THIRD_KB_ROW.length(); i++){
            KEYBOARD_INFO.put(THIRD_KB_ROW.charAt(i), 0);
        }
    }

    private void Render(){
        for(int i = 0; i < this.wordsGuessed.length; i++){
            if(i < currentGuess){
                String word = this.wordsGuessed[i];
                int[] colourCodes = matchString(word);

                for(int j = 0; j < word.length(); j++){

                    System.out.printf("[%s%s%s]", ANSI[colourCodes[j]], word.charAt(j), ANSI[0]);
                }
            } else {
                System.out.print("[ ]".repeat(this.wordsGuessed.length));
            }

            System.out.println();
        }

        System.out.println();

        for(int i = 0; i < FIRST_KB_ROW.length(); i++){
            char letter = FIRST_KB_ROW.charAt(i);
            int colourCode = KEYBOARD_INFO.get(letter);

            System.out.printf("%s%s ", ANSI[colourCode], letter);
        }

        System.out.println();

        for(int i = 0; i < SECOND_KB_ROW.length(); i++){
            char letter = SECOND_KB_ROW.charAt(i);
            int colourCode = KEYBOARD_INFO.get(letter);

            System.out.printf(" %s%s", ANSI[colourCode], letter);
        }

        System.out.print("\n ");

        for(int i = 0; i < THIRD_KB_ROW.length(); i++){
            char letter = THIRD_KB_ROW.charAt(i);
            int colourCode = KEYBOARD_INFO.get(letter);

            System.out.printf(" %s%s", ANSI[colourCode], letter);
        }

        System.out.println("\n");
    }

    private int[] matchString(String input){
        int[] codes = {1, 1, 1, 1, 1};

        //Set keyboard inputs
        for(int i = 0; i < input.length(); i++){
            if(KEYBOARD_INFO.get(input.charAt(i)) == 0){
                KEYBOARD_INFO.replace(input.charAt(i), 1);
            }
        }

        //Check for perfect matches
        ArrayList<String> perfectMatches = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            if(this.wordChosen.charAt(i) == input.charAt(i)){
                codes[i] = 3; //Perfect match
                KEYBOARD_INFO.replace(input.charAt(i), 3);
                perfectMatches.add(String.valueOf(input.charAt(i)));
            }
        }

        //Check for partial matches (in the word, but not in the right spot)
        for(int i = 0; i < 5; i++){
            if(codes[i] != 3){
                char letter = input.charAt(i);

                for(int j = 0; j < 5; j++){
                    if(this.wordChosen.charAt(j) == letter && codes[j] < 2){
                        codes[i] = 2;
                        if(KEYBOARD_INFO.get(letter) < 2){
                            KEYBOARD_INFO.replace(letter, 2);
                        }
                        break;
                    }
                }
            }
        }

        return codes;
    }

    private static String getValidInput(){
        System.out.print("Please enter a 5-letter word.\n> ");
        String input = scanner.nextLine().toUpperCase();

        while (input.length() != 5 || !WordList.List.contains(input)){
            if(input.length() != 5){
                System.out.print("Please enter a 5-letter word.\n> ");
            } else {
                System.out.print("Word not found, please pick a different word.\n> ");
            }

            input = scanner.nextLine().toUpperCase();
        }

        return input;
    }

    void Play(){
        boolean gameWon = false;
        currentGuess = 0;

        //Typing STAIN as a guess when the wordChosen is TAWNY breaks it for some reason, test later
        //System.out.println(this.wordChosen);

        this.Render();
        while (currentGuess < 5 && !gameWon){
            this.wordsGuessed[currentGuess] = getValidInput();

            if(this.wordsGuessed[currentGuess].equals(this.wordChosen)){
                gameWon = true;
            }

            currentGuess += 1;
            this.Render();
        }

        if(gameWon){
            System.out.println("Congratulations, you won!");
        } else {
            System.out.println("You lost, the word was " + this.wordChosen);
        }
    }
}
