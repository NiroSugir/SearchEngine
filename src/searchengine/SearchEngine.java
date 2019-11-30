package searchengine;

/**
 *
 * @author Niro
 */
public class SearchEngine {

    final private static IndexTable indexTable = new IndexTable();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

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

        System.out.println(
                indexTable
        );

        // target: a searchable table of keywords that contains the page the
        //      word appears on as well as the position on the page
    }

}
