package searchengine;

import java.util.Arrays;

/**
 *
 * @author Niro
 */
public class IndexTable {

    final private int COLUMNS = 2;

    private int maximumWordDistance;

    // row of keywords with columns of Keyword || Page ID || Keyword Index
    private String[][] table;
    private Website[] websites;

    IndexTable() {
        this.websites = new Website[0];
        this.table = new String[0][COLUMNS];

        this.maximumWordDistance = 0;
    }

    // adds keywords to the index table. Can only be called after website it 
    // added. does not validate if this page has already been indexed
    private void indexWebsite(Website website) {
        String[] keywords = website.getKeywords();
        String pageId = "" + website.getId();

        // update the maximum word distance to the distance between the first and
        // last word
        this.maximumWordDistance = keywords.length > this.maximumWordDistance ? keywords.length : this.maximumWordDistance;

        // resize the table to accomodate the keywords on this page
        String[][] newTable = new String[this.table.length + keywords.length][COLUMNS];

        // copy old table content to new table
        for (int i = 0; i < this.table.length; i++) {
            for (int j = 0; j < this.table[i].length; j++) {
                newTable[i][j] = this.table[i][j];
            }
        }

        // add keywords to the end of the new table
        for (int i = 0; i < keywords.length; i++) {
            int rowNumber = i + this.table.length;
            int keywordIndex = i + 1;

            newTable[rowNumber][0] = keywords[i];
            newTable[rowNumber][1] = pageId + "-" + (keywordIndex);
        }

        this.table = newTable;
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < this.table.length; i++) {
            output += String.format("%d: %s%n", i, Arrays.toString(this.table[i]));
        }

        output += "\n";

        return output;
    }

    public void addWebsite(Website website) {
        // make sure the websites are all unique
        for (int i = 0; i < this.websites.length; i++) {
            // already parsed
            if (this.websites[i].getId() == website.getId()) {
                return;
            }
        }

        // increase size of the array;
        Website[] newList = new Website[this.websites.length + 1];

        // copy content to the new list
        for (int i = 0; i < this.websites.length; i++) {
            newList[i] = this.websites[i];
        }

        // add website to the bottom
        newList[this.websites.length] = website;

        // set the new list as the list of websites
        this.websites = newList;

        // index the site by adding the keywords found on this page to the table
        this.indexWebsite(website);
    }

    public String[] keywordSearch(String keyword) {
        // determine the size of the return array by finding the number of times
        // that keyword appears in the index
        int numberOfOccurances = 0;
        for (int i = 0; i < this.table.length; i++) {
            if (this.table[i][0].equalsIgnoreCase(keyword)) {
                numberOfOccurances++;
            }
        }

        // create the return array and set the first element to the keyword
        // being searched
        String returnArray[] = new String[numberOfOccurances + 1];
        returnArray[0] = keyword;

        // counter to keep track of where the next page entry should go. starts
        // at 1 because 0 is reserved for the keywordSearch term (eg: keyword)
        int returnIndex = 1;
        for (int i = 0; i < this.table.length; i++) {
            if (this.table[i][0].equalsIgnoreCase(keyword)) {
                returnArray[returnIndex] = this.table[i][1];
                returnIndex++;
            }
        }

        return returnArray;
    }

    public int[][] phraseSearch(String phrase) {
        String[] keywords = phrase.split(" ");
        String[][] keywordSearchResults = new String[keywords.length][];

        // keywordSearch all keywords
        for (int i = 0; i < keywords.length; i++) {
            keywordSearchResults[i] = this.keywordSearch(keywords[i]);
        }

        // array of results with the nearness of words
        int[][] phraseResults = new int[keywordSearchResults[0].length - 1][2];

        // go through every result for the first keyword
        // each subsequent keword must appear exactly one position after
        //      the previous one to be a phrase.
        for (int i = 1; i < keywordSearchResults[0].length; i++) {
            // convert keyword location entry back to original page and position
            // integers
            String[] splitOfPrimaryKeyword = keywordSearchResults[0][i].split("-");
            int pageOfPrimaryKeyword = Integer.parseInt(splitOfPrimaryKeyword[0]);
            int positionOfPrimaryKeyword = Integer.parseInt(splitOfPrimaryKeyword[1]);

            phraseResults[i - 1][0] = pageOfPrimaryKeyword;
            phraseResults[i - 1][1] = nearnessOfWordsInPhrase(keywordSearchResults, pageOfPrimaryKeyword, positionOfPrimaryKeyword);
        }

        return phraseResults;
    }

    private int nearnessOfWordsInPhrase(String[][] keywordSearchResults, int pageOfPrimaryKeyword, int positionOfPrimaryKeyword) {
        // track the collective distance between the words
        int nearnessOfWordsInPhrase = 0;

        for (int j = 1; j < keywordSearchResults.length; j++) {
            int minimumNearnessOfSubsequentKeywords = this.maximumWordDistance + 1;

            for (int k = 1; k < keywordSearchResults[j].length; k++) {
                String[] splitOfThisKeyword = keywordSearchResults[j][k].split("-");
                int pageOfThisKeyword = Integer.parseInt(splitOfThisKeyword[0]);
                int positionOfThisKeyword = Integer.parseInt(splitOfThisKeyword[1]);

                // on the same page, calculate the nearness o
                if (pageOfPrimaryKeyword == pageOfThisKeyword && positionOfThisKeyword > positionOfPrimaryKeyword) {
                    int nearness = positionOfThisKeyword - positionOfPrimaryKeyword - 1;

                    if (nearness < minimumNearnessOfSubsequentKeywords) {
                        minimumNearnessOfSubsequentKeywords = nearness;
                    }
                }
            }

            nearnessOfWordsInPhrase += minimumNearnessOfSubsequentKeywords;
        }

        return nearnessOfWordsInPhrase;
    }
}
