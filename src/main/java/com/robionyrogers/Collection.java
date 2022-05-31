package com.robionyrogers;

// Importing Librarys 
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
    private ArrayList<Content> collection; // initialising the collection ArrayList
    private int currContentID; //

    /** Constructor: initialising the collection & adding content */
    public Collection() {
        // Initialise the collection
        this.collection = new ArrayList<Content>();

        // Adding Content to the collection
        this.collection.add(new Content(1, "Lupin", "George Kay", new ArrayList<String>(Arrays.asList("Crime", "Mystery")), 4.1));
        this.collection.add(new Content(2, "Ozark", "Bill Bubuque", new ArrayList<String>(Arrays.asList("Crime", "Thriller")), 4.5));
        this.collection.add(new Content(3, "Narcos", "Chris Brancato", new ArrayList<String>(Arrays.asList("Crime", "Thriller")), 3.5));
        this.collection.add(new Content(4, "Nyxia", "Scott Reintgen", new ArrayList<String>(Arrays.asList("Thriller")), 5)); 
        this.collection.add(new Content(5, "YSIV", "Logic", new ArrayList<String>(Arrays.asList("Rap, Hip-Hop")), 4.2)); 

        this.currContentID = this.collection.size();
    }

    /**
     * Getter Method: Returning the collection
     * 
     * @return ArrayList<Content> - returns the collection of Content objs
     */
    public ArrayList<Content> getCollection() {
        return this.collection;
    }

    public void changeRating(String cntName, double rating) {
        // Itterate through the colletion checking for the same conent 
        for (Content cnt : this.getCollection()) {
            // Checking if the name matches 
            if (cnt.getName().equalsIgnoreCase(cntName)) {
                // Chnaging the rating 
                this.collection.get(cnt.getID()); 
            }
        }
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

        // Boundary Checking: Duplicate name FIXME: There are instnces where there could be two songs/movies with the same name, maybe compear objs and check if the same be two song/movies with the same name, maybe 
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
        new GUI();  // runs the GUI file 
    }
}
