
// Importing Librarys 
import java.util.HashMap; 
import java.util.ArrayList;
import java.util.*;

/**
 * Collection is a support class for the GUI A collection contains a range of
 * Content obj ans stores them Collection does not take any initial parameters
 * 
 * @author James Robiony-Rogers
 * @version 1.0 - 31st August 2021
 */
public class Collection {
    // Defigning the fields of the class
    private HashMap<Integer, Content> map; // initialising the collection map TESTING:
    private ArrayList<Content> collection; // initialising the collection ArrayList
    private int currContentID; //

    /** Constructor: initialising the collection & adding content */
    public Collection() {
        // Initialise the collection
        this.map = new HashMap<Integer, Content>(); // TESTING:
        this.collection = new ArrayList<Content>();

        // Creating Content objs
        Content c1 = new Content(1, "Lupin", "George Kay", new ArrayList<String>(Arrays.asList("Crime", "Mystery")),
                4.1);
        Content c2 = new Content(2, "Ozark", "Bill Bubuque", new ArrayList<String>(Arrays.asList("Crime", "Thriller")),
                4.5);
        Content c3 = new Content(3, "Narcos", "Chris Brancato",
                new ArrayList<String>(Arrays.asList("Crime", "Thriller")), 3.5);

        // Adding Content to the collection
        this.map.put(1, c1); // TESTING:
        this.collection.add(c1);
        this.map.put(2, c2); // TESTING:
        this.collection.add(c2);
        this.map.put(3, c3); // TESTING:
        this.collection.add(c3);

        this.currContentID = 3;
    }

    /**
     * Getter Method: Returning the collection
     * 
     * @return ArrayList<Content> - returns the collection of Content objs
     */
    public ArrayList<Content> getCollection() {
        return this.collection;
    }

    /**
     * Lets the user add a Content obj to the collection
     * 
     * @param String (name) - name of the content 
     * @param String (creator) - name of the creator
     * @param ArrayList<String> (genres) - an ArrayList of the contents genres 
     * @param double (rating) - the rating the user gave it between 0.0 and 5.0 
     * @return boolean - if the content name is already in the collection*/
    public boolean addContent(String name, String creator, ArrayList<String> genres, double rating) {
        boolean inCollection = false;

        // Boundary Checking: Duplicate name FIXME: There are instnces where there could be two songs/movies with the same name, maybe compear objs and check if the same
        // be two song/movies with the same name, maybe 
        for (Content c : this.collection) { // itterate through collection
            if (c.getName().equalsIgnoreCase(name)) { // check if name of content already exists
                inCollection = true; // stating the name alredy exists in the collection
            }
        }

        // Adding content if passes checks
        if (!inCollection) {
            this.currContentID++; // creating the new id for the content
            Content content = new Content(this.currContentID, name, creator, genres, rating); // creating card obj
            this.collection.add(content); // adding card obj to collection
        }

        return inCollection;
    }

    public static void main(String[] args) {
        Collection c = new Collection(); // TESTING: creating a collection obj for testing purposes

        // TODO: Test the addContent() method with the String[] array or if ArrayList
        // needs to be used
        c.addContent("Nyxia", "Scott Reintgen", new ArrayList<String>(Arrays.asList("Crime", "Thriller")), 5);

    }
}
