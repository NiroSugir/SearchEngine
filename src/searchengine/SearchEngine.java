package searchengine;

import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Niro
 */
public class SearchEngine {

    final private static IndexTable indexTable = new IndexTable();

    public static void generateIndexTable() {
        String[] html = {
            "In this course we will learn the Java programming language",
            "In this program we learned Java and other programming languages"
        };

        Website[] websites = new Website[html.length];

        // index table retrieves keywords and positions of each word from each 
        // page and adds that info as columns in the table
        for (int i = 0; i < html.length; i++) {
            // create a website object out of html text
            websites[i] = new Website(i + 1, html[i]);

            // add website objects to an index table
            indexTable.addWebsite(websites[i]);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // create a searchable table
        generateIndexTable();

        // prompt user for search keyword
        System.out.print("Search: ");
        String userInput = sc.nextLine();

        String[] splitInput = userInput.split(" ");

        System.out.println(Arrays.toString(indexTable.search(splitInput[0])));

    }

}
