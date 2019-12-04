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
            "In this program we learned Java and other programming languages",
            "Coffee has a lot of nicknames java joe dirt mud brew cuppa daily grind lifeblood tar rocket fuel even worm dirt But have you ever considered why coffee is called java"
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

    private static int[][] sortResultsByNearness(int[][] phraseSearchResults) {
        int[][] sortedResults = phraseSearchResults.clone();

        for (int i = 0; i < sortedResults.length - 1; i++) {
            int minimumNearnessIndex = i;
            int minimumNearness = sortedResults[minimumNearnessIndex][1];

            for (int j = i; j < sortedResults.length; j++) {
                if (sortedResults[j][1] < minimumNearness) {
                    minimumNearness = sortedResults[j][1];
                    minimumNearnessIndex = j;
                }
            }

            // swap nearest to the front
            int[] t = sortedResults[i].clone();
            sortedResults[i] = sortedResults[minimumNearnessIndex].clone();
            sortedResults[minimumNearnessIndex] = t.clone();
        }

        return sortedResults;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // create a searchable table
        generateIndexTable();

        // prompt user for keywordSearch keyword
        System.out.print("Search: ");
        String userInput = sc.nextLine();

        String[] splitInput = userInput.split(" ");

        int[][] phraseSearchResult = indexTable.phraseSearch(userInput);
        int[][] sortedResults = sortResultsByNearness(phraseSearchResult);

        for (int i = 0; i < sortedResults.length; i++) {
            System.out.format("Page %d%n", sortedResults[i][0]);
        }
    }

}
