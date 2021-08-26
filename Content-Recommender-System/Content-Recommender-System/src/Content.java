// Importing Libraries 


/** Content.java: Obj class
 * a Content contains an id, name, creator, genre and rating 
 * 
 * @author James Robiony-Rogers
 * @version 1.0 - 26th August 2021 */
public class Content {
    // Defining fields of the class
    private int id;                 // ID od the content (used in the Hashmap)
    private String name;            // Name of the piece of content 
    private String creator;         // Name of the creator
    private String[] genre;           // Genre the piece of content is under 
    private double rating;          // The users rating for the piece of content

    /**
     * Construtor of the Content obj
     * 
     * @param id      (int) - contents id number
     * @param name    (string) - name of the content
     * @param creator (string) - name of the content creator
     * @param genre   (string) - genre the content is under
     * @param rating (double) - rating of the content   */
    public Content(int id, String name, String creator, String[] genre, double rating) {
        // Assigning the obj's details 
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.genre = genre;
        this.rating = rating;
    }

    /** Running an instance of the GUI
     * @param args (String[]) - Standard */
    public static void main(String[] args) throws Exception {
        // new GUI();          // running the driver when this file is executed 
    }
}
