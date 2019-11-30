package searchengine;

/**
 *
 * @author Niro
 */
public class IndexTable {

    private Website[] websites;

    IndexTable() {
        this.websites = new Website[0];
    }

    public void addWebsite(Website website) {
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
    }

}
