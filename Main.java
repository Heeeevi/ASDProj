/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231102 - Ahmed Miftag Ghifari
 * 2 - 5026231103 - Eric Vincentius Jaolis
 * 3 - 5026231156 - Hafiyyuddin Ahmad
 */

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
