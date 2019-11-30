package searchengine;

/**
 *
 * @author Niro
 */
public class Website {

    private int id;
    private String content;

    Website(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public String[] getKeywords() {
        return this.content.split(" ");
    }

    public int getId() {
        return id;
    }
}
