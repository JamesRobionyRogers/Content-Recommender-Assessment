package com.robionyrogers;

// Importing Libraries
import ecs100.*;                        // used for the GUI
import java.util.*;                     // general use 

import de.milchreis.uibooster.*;
import de.milchreis.uibooster.model.*;
import de.milchreis.uibooster.model.Form;
import de.milchreis.uibooster.model.FormElement;
import de.milchreis.uibooster.model.FormElementChangeListener;
import javax.swing.*;


import java.lang.Thread;
import java.awt.Color;                  // used for setting colours


/** GUI Driver Class runs the program and creates the GUI
 * GUI does not take any inital params
 * 
 * @author James Robiony-Rogers
 * @version 16th Sept 2021
 */
public class GUI {
    // Instance vairables
    private Collection cc;              // declairing the ContentCollection obj
    private Content content;            // used to store the current content obj
    private HashMap<Integer, Card> buttons = new HashMap<Integer, Card>();      //   
    private ArrayList<Card> contentCards = new ArrayList<Card>();               // ArrayList containing cards of content objs 
    private UiBooster booster = new UiBooster();


    // GUI: On screen button fields 
    private static final int FONT_SIZE = 18; 
    private static final int DISPLAY_WIDTH = UI.getCanvasWidth(); 
    private static final int DISPLAY_HEIGHT = UI.getCanvasHeight();  
    private static final String BG_COLOR = "#0D0D0D"; 

    // Screen state vairbales 
    private String GUIstate = "menu"; 
    

    /** Constructor: Initialising the UI by adding buttons, text fields etc */
    public GUI() {
        // Initialiseing and setting properties 
        this.cc = new Collection();     // initalising the collection obj 

        UI.initialise();                // initialising the UI
        this.addButtonsToList(); 
        this.setupGUI();

        // Adding mouse functionality
        UI.setMouseListener(this::doMouse);

        UI.setColor(Color.black);
        UI.setFontSize(FONT_SIZE);


        // UI.addButton("Quit", UI::quit); 

        UI.setDivider(0);       // Removing the GUI console


    }

    /** GUI component of the addCard method in the Collection class */
    public void addContentGUI() {
        final int MIN = 0; 
        final int MAX = 5; 
        final int INIT_VALUE = 0; 
        final int STEP = 1;
        ArrayList<String> formResults = new ArrayList<String>(); 
        boolean contine = true;         // used for conditions 

        Form form = this.booster.createForm("Add Content").addLabel("Add a new Movie/TV Show")
                    .addText("Name of the content:")
                    .addText("Director / Artist")
                    .addText("Genres:   *seperated by commas*")
                    .addSlider("Star Rating:", MIN, MAX, INIT_VALUE, STEP, STEP)
                    .addLabel("Click enter to continue")
                    .andWindow().setSize(500, 410).save()
                    .show();        // use .run() instead of show() to open the formBuilder without blocking.
        
        form.getElements().forEach(e -> {
            // e.getLable() : for getting the lable of the element
            // e.getValue() : for getting the value of the element returned in a FormElement obj
                        
            // Casting FormElement to string and storing value
            String result = e.getValue().toString();
           
            // Adding results of form to an ArrayList
            formResults.add(result); 
        });

        // Assign values from ArrayList to vairbales 
        String name = formResults.get(1); 
        String creator = formResults.get(2); 
        String genreText = formResults.get(3); 
        double rating = Double.parseDouble(formResults.get(4));

        // Processing grnreText to an ArrayList
        ArrayList<String> genres = new ArrayList<String>(Arrays.asList(genreText.split(" , ")));
        
        // Checking if the added content is valid 
        if (!checkContentNull(name, creator)) {
            // Adding the content to the collection
            this.cc.addContent(name, creator, genres, rating);
            UI.printMessage(" Content Added Successfully!");
        }

        else {
            UI.printMessage(" Content Failed to Add!"); 
        }

        
        
    }

    /** Displays all of the Content in the collection */
    public void viewAllContentGUI() {
        ArrayList<Content> collection = this.cc.getCollection(); 
        int cntCardID = 0;                  // index of the content in the collection 
        int row = 0;
        this.GUIstate = "view content";     // used in the doMouse method 

        this.clearGUI();
        for(Content content : collection) {
            // Option 2: Displaying to the GUI 
            // Initialiseing the card 
            
            Card crd = new Card(cntCardID, content.getName(), "content");   // creating a content card 
            crd.addContentObj(content);

            // Setting the properties for the content card
            final int PADDING = 20;                                 // card pading
            int buttonWidth = (DISPLAY_WIDTH / 2) - (4 * PADDING);  // button width
            int cardWidth = buttonWidth + (2 * PADDING);            // card width
            int cardHeight = 200;                                   // card height

            // Assigning the x & y values
            if (cntCardID % 2 == 0 && cntCardID != 0) {row++;}       // assigning new row - adding to y pos

            int cardX = cardWidth * (cntCardID%2);                   // creating the posX of the card 
            int cardY = cardHeight * row;                            // creating the posY of the card

            // NOTE: we now have the cards (x & y) values A.K.A positional values

            // Adding the card's positional values to the obj
            crd.addCard(cardX, cardY, cardWidth, cardHeight);

            this.drawButton(crd);

            // Adding the card to the contentCards list
            this.contentCards.add(crd);

            cntCardID++;         // adding to the index to make the rows work. 
        }

    }

    /** Finds the content that the user wants to rate and changes the rating */
    public void rateContentGUI() {
        // Change to view all screen 
        this.viewAllContentGUI();

        // Setting variables for the box
        final int MIN = 0;
        final int MAX = 5;
        final int INIT_VALUE = 0;
        final int STEP = 1;
        ArrayList<String> allTitles = new ArrayList<String>(Arrays.asList("Choose a title:"));
        ArrayList<String> formResults = new ArrayList<String>();
        ArrayList<Content> collection = this.cc.getCollection(); 

        // Adding all titles to the ArrayList 
        for (Content cnt : collection) {
            allTitles.add(cnt.getName());
        }

        // Open dialog box and revieve input 
        Form form = this.booster.createForm("Change Rating of a Title").addLabel("Change the rating of a title in the collection")
                    // .addText("Name of the content:")
                    .addSelection("Title:", allTitles)
                    .addSlider("Rating:", MIN, MAX, INIT_VALUE, STEP, STEP)
                    .andWindow().setSize(500, 250).save()
                    .show();        // use .run() instead of show() to open the formBuilder without blocking.
        
        form.getElements().forEach(e -> {
            // e.getLable() : for getting the lable of the element
            // e.getValue() : for getting the value of the element returned in a FormElement obj
                        
            // Casting FormElement to string and storing value
            String result = e.getValue().toString();
           
            // Adding results of form to an ArrayList
            formResults.add(result); 
        });

        // Processing input 
        String userTitle = formResults.get(1); 
        double userRating = Double.parseDouble(formResults.get(2)); 
        

        // Itterating through the collection finding the matching content 
        for (Content cnt : collection) {
            
            // CHecking for the matching name 
            if (cnt.getName().equalsIgnoreCase(userTitle)) {
                // Changing the user's rating of the title 
                cnt.changeRating(userRating);
            }
        } 

        // Refresh the contect screen
        this.viewAllContentGUI();

        
    } 

    // FIXME: finish off this method 
    public void findContentGUI() {
        // Getting user input 
        String title = UI.askString("Content Name: "); 
        double rating = UI.askDouble("Changed Rating: "); 

        // Itterate through the collection of content checking for the name 
        for (Content cnt : this.cc.getCollection()) {
            if (cnt.getName().equalsIgnoreCase(title)) {
                this.cc.changeRating(cnt.getName(), rating);
            }
        }
    }

    /** Visualy sets up the GUI */
    public void setupGUI() {                // TESTING: Could use this as part of a Sprint
        // Background
        this.setColour(BG_COLOR);
        UI.fillRect(0, 0, 10000, 10000);      // FIXME: (drawing and clearing the backgroud) drawing the background colour 

        // Card Properties 
        final int PADDING = 40;                                     // card pading 
        int buttonWidth = (DISPLAY_WIDTH / 2) - (4 * PADDING);      // button width 
        int cardWidth = buttonWidth + (2 * PADDING);                // card width 
        int cardHeight = 100;                                       // card height 
        int row = 0;
        
        // Itterating through and drawing the cards 
        for (int btnIndex : this.buttons.keySet()) { 
            // Creating Card obj instance  
            Card card = buttons.get(btnIndex); 

            // Assigning the x & y values for the card
            if (btnIndex % 2 == 0 && btnIndex != 0) {row++;}        // assigning new row - adding to y pos

            int cardX = cardWidth * (btnIndex%2);                   // creating the posX of the card 
            int cardY = cardHeight * row;                           // creating the posY of the card 
            
            // NOTE: we now have the cards (x & y) values A.K.A positional values 

            // Adding the cards positional values to the obj
            card.addCard(cardX, cardY, cardWidth, cardHeight);  
            
            // Drawing the card (button inside the card) to the GUI
            this.drawButton(card);

            // Removing the GUI console
            UI.setDivider(0);
        }

    }

    /** Draws button with text on it usign some params  
     * 
     * @param cardX (int) - card x value 
     * @param cardY (int) - card y value 
     * @param buttonWidth (int) - button width 
     * @param btnText (String) - the text on the button
     */
    private void drawButton(Card card) {  // int cardX, int cardY, int buttonWidth, int buttonHeight, String btnText, String type
        // Assigning the general button properties
        int cardX = card.cardX;
        int cardY = card.cardY;
        String btnText = card.btnText;
        String type = card.type; 

        // Menu button
        if (type.equalsIgnoreCase("menu")) { 
            // Menu button properties      
            final int PADDING = 40; 
            final int BORDER = 4; 
            final int FONTSIZE = 20; 
            final int FONTPADDING = 28;
            int buttonWidth = (DISPLAY_WIDTH / 2) - (4 * PADDING);
            int buttonHeight = 50; 
            

            // Setting the button positional values
            int btnPosX = cardX + PADDING; 
            int btnPosY = cardY + PADDING; 

            // Assigning the buttons posional values for the doMouse method 
            card.addBtn(btnPosX, btnPosY, buttonWidth, buttonHeight);

            // Drawing the button
            // Button Border
            this.setColour("#870000");
            UI.fillRect(btnPosX, btnPosY, buttonWidth, buttonHeight);

            // Setting the new positional values for the inner button
            btnPosX = btnPosX + BORDER;
            btnPosY = btnPosY + BORDER; 

            // Solid Button Inner 
            this.setColour("#E30032");
            UI.fillRect(btnPosX, btnPosY, (buttonWidth - (2 * BORDER)), (buttonHeight - (2 * BORDER))); 

            // Printing text on the buttons 
            this.setColour("#ffffff");
            UI.setFontSize(FONTSIZE);
            UI.drawString(btnText, (btnPosX + 10), (btnPosY + FONTPADDING)); 
        } 
        // Content button 
        if (type.equalsIgnoreCase("content")) { 
            // Menu button properties
            final int PADDING = 20;
            final int BORDER = 4;
            final int RATING_FONT = 30; 
            final int TITLE_FONT = 20;
            final int HEADER_FONT = 16; 
            final int BODY_FONT = 12; 
            int fontPadding = 28;
            int buttonWidth = (DISPLAY_WIDTH / 2) - (4 * PADDING);
            int buttonHeight = 180;

            // Setting the button positional values
            int btnPosX = cardX + PADDING;
            int btnPosY = cardY + PADDING;

            // Assigning the buttons posional values for the doMouse method
            card.addBtn(btnPosX, btnPosY, buttonWidth, buttonHeight);

            // Drawing the button 
            // Button Border
            this.setColour("#870000");
            UI.fillRect(btnPosX, btnPosY, buttonWidth, buttonHeight);

            // Setting the new positional values for the inner button
            btnPosX = btnPosX + BORDER;
            btnPosY = btnPosY + BORDER;

            // Solid Button Inner
            this.setColour("#E30032");
            UI.fillRect(btnPosX, btnPosY, (buttonWidth - (2 * BORDER)), (buttonHeight - (2 * BORDER)));

            // Printing text on the buttons 
            this.setColour("#ffffff");

            // Content name 
            UI.setFontSize(TITLE_FONT);
            UI.drawString(card.cnt.getName(), (btnPosX + 10), (btnPosY + fontPadding));
            fontPadding += TITLE_FONT + 5; 

            // Creator name
            UI.setFontSize(HEADER_FONT);
            UI.drawString(card.cnt.getCreator(), (btnPosX + 10), (btnPosY + fontPadding));
            fontPadding += HEADER_FONT + 15;

            // Genre heading  
            UI.setFontSize(TITLE_FONT);
            UI.drawString("Genres:", (btnPosX + 10), (btnPosY + fontPadding));
            fontPadding += TITLE_FONT;
            // Genre text
            UI.setFontSize(BODY_FONT);
            String genreText = String.join(", ", card.cnt.getGenres());
            UI.drawString(genreText, (btnPosX + 10), (btnPosY + fontPadding));
            fontPadding += BODY_FONT;

            // Rating:
            String imgSrc = "C:/Users/James Robiony-Rogers/OneDrive - Onslow College/2021 Yr 13/13DTC/2021-08-00 - Project Management/recommender/src/main/java/com/robionyrogers/img/gold-star.png";
            UI.drawImage(imgSrc, (btnPosX + 10), (btnPosY + fontPadding), 50, 50);
            fontPadding += BODY_FONT + 25;
            // Rating text
            String ratingText = String.valueOf(card.cnt.getRating()); 
            UI.setFontSize(RATING_FONT);
            UI.drawString(ratingText, (btnPosX + 75), (btnPosY + fontPadding));
        }
    }

    // TODO: create a method that draws an "X" for the top right/left of the program so the user can exit the program 

    /** Checks if the card has been clicked */
    public boolean checkButtonClick(double mouseX, double mouseY, Card crd) {
        // Assigning vairables for area checking        
        int btnX = crd.btnPosX;
        int btnY = crd.btnPosY;
        int btnW = crd.buttonWidth; 
        int btnH = crd.buttonHeight; 
        
        // Checking if mouse click is within the btn container 
        if( (mouseX >= btnX) && (mouseX <= (btnX + btnW)) && 
            (mouseY >= btnY) && (mouseY <= (btnY + btnH)) ) {
                // User is within the container
                return true;    
        }

        // User is outside the container
        return false; 
    }

    /** Checks for user mouse input on the GUI
     * @param action
     * @param x
     * @param y
     */
    public void doMouse(String action, double x, double y) {
        // checking what screen it should be checking 
        if(this.GUIstate.equals("menu")) {

            // Checking for mouse click 
            if (action.equals("clicked")) {
                
                // Itterating through the buttons 
                for (int btnIndex : this.buttons.keySet()) {

                    Card btn = this.buttons.get(btnIndex);

                    // Checking if the (x, y) of the mouse click is within a button container
                    if(checkButtonClick(x, y, btn)) {
                        String btnClicked = btn.btnText;   // DEBUGGING: 

                        if (btn.btnText.equalsIgnoreCase("Add Content")) {
                            this.addContentGUI();
                        }

                        else if (btn.btnText.equalsIgnoreCase("View All Content")) {
                            this.viewAllContentGUI();
                        }

                        else if (btn.btnText.equalsIgnoreCase("Find Content")) {
                            this.findContentGUI();
                        }

                        else if (btn.btnText.equalsIgnoreCase("Change Rating")) {
                            this.rateContentGUI();
                        }

                        else if (btn.btnText.equalsIgnoreCase("Quit")) {
                            UI.quit();
                        }
                    }
                } 
            }
        
        }    
        else if (this.GUIstate.equals("view content")) {
            // Exit view content on mouse click
            if (action.equals("clicked")) {
                this.setupGUI(); 
                this.GUIstate = "menu"; 
            }
        }

        String pause = this.GUIstate; 
        
    } 

    private boolean checkContentNull(String name, String creator) {

        // Checking name 
        if (name.equals("")) {
            return true; 
        }

        // Checking the creator 
        if (creator.equals("")) {
            return true; 
        }

        return false; 
    }

    /** Used for adding entries for buttons to the HashMap (or maybe ArrayList) */
    private void addButtonsToList() {
        // Adding on screen buttons
        this.buttons.put(0, new Card(0, "Add Content", "menu"));
        this.buttons.put(1, new Card(1, "View All Content", "menu"));
        this.buttons.put(2, new Card(2, "Find Content", "menu"));
        this.buttons.put(3, new Card(3, "Change Rating", "menu")); 
        this.buttons.put(4, new Card(4, "Quit", "menu"));
    }

    /** Sets the color using a hex code */
    private void setColour(String hex) {
        Color col = Color.decode(hex);
        UI.setColor(col);
    }

    /** Fills GUI with the background colour */
    private void clearGUI() {
        this.setColour(BG_COLOR);
        UI.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT); 
    }

    /** DEBUGGING: method */
    private void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        new GUI(); 
    }
}



// NOTE: Sort out the buttons (buttons dont all work)
//          Finish off rateContentGUI()