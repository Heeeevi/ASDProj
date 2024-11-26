import java.util.*;

public class Main {
    public static void main(String[] args){
        do {
            TTT game = new TTT();
            game.play();
            System.out.println("Do you want to play again? Y/N");
            Scanner sc = new Scanner(System.in);
            char ans = sc.nextLine().charAt(0);
            if (ans != 'Y' && ans != 'y'){
                System.out.println("good bye HAHAHA!");
                System.exit(0);
            }
        } while(true);
    }
}
