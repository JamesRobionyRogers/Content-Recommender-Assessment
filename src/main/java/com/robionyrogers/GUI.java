package com.robionyrogers;

// Importing Libraries
import ecs100.*;                        // used for the GUI
import java.util.*;                     // general use 

import de.milchreis.uibooster.*;
// import de.milchreis.uibooster.model.*;
import de.milchreis.uibooster.model.Form;
// import de.milchreis.uibooster.model.FormElement;
// import de.milchreis.uibooster.model.FormElementChangeListener;
// import javax.swing.*;


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
    private Collection cc;                                          // declairing the ContentCollection obj
    private ArrayList<Card> buttons = new ArrayList<Card>();       
    private ArrayList<Card> contentCards = new ArrayList<Card>();   // ArrayList containing cards of content objs 
    private Card exitButton = new Card(0, "quit", "quit");
    private Card backButton = new Card(0, "back", "back");
    // View all content fields 
    private ArrayList<Card> navBtns = new ArrayList<Card>();
    private int page = 1; 
    private UiBooster booster = new UiBooster();


    // GUI: On screen button fields 
    private static final int FONT_SIZE = 18; 
    private static final int DISPLAY_WIDTH = UI.getCanvasWidth() + 20; 
    private static final int DISPLAY_HEIGHT = UI.getCanvasHeight();  
    private static final String BG_COLOR = "#0D0D0D"; 

    // Screen state vairbales 
    private String GUIstate = "menu"; 
    

    /** Constructor: Initialising the UI by adding buttons, text fields etc */
    public GUI() {
        // Initialiseing and setting properties 
        this.cc = new Collection();     // initalising the collection obj 

        UI.initialise();                // initialising the UI
        this.addMenuButtons(); 
        this.setupMenu();
        this.updateContentCards();

        // Adding mouse functionality
        UI.setMouseListener(this::doMouse);

        // Setting default values for the UI
        UI.setColor(Color.black);
        UI.setFontSize(FONT_SIZE);
        UI.setDivider(0);               // removing the GUI console

        // DEBUGGING: 
        this.setColour("#FFFFFF");
        UI.drawLine(DISPLAY_WIDTH, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
    }

    /** GUI component of the addCard method in the Collection class */
    public void addContentGUI() {
        final int MIN = 0; 
        final int MAX = 5; 
        final int INIT_VALUE = 0; 
        final int STEP = 1;
        ArrayList<String> formResults = new ArrayList<String>(); 

        UiBooster formBooster = new UiBooster();
        Form form = formBooster.createForm("Add Content").addLabel("Add a new Movie/TV Show")
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
        String name = formResults.get(1).strip(); 
        String creator = formResults.get(2).strip(); 
        String genreText = formResults.get(3).strip(); 
        double rating = Double.parseDouble(formResults.get(4));

        // Processing grnreText to an ArrayList
        ArrayList<String> genres = new ArrayList<String>(Arrays.asList(genreText.split(", ")));
        
        // Checking if the added content is valid 
        if (!checkContentNull(name, creator)) {
            // Adding the content to the collection
            this.cc.addContent(name, creator, genres, rating);
            UI.printMessage(" Content Added Successfully!");
        }

        else {
            UI.printMessage("Content Failed to Add!"); 
        }
        
    }

    /** Displays all of the Content in the collection */
    public void viewAllContentGUI() {
        // Fields 
        int pageNum = this.page; 
        int cardIndx = 0; 

        int cardWidth = 100; 
        int cardHeight = 50; 
        int padding = 20; 
        
        // Setting up the nav buttons
        Card nextPage = new Card(0, "Next Page", "nav");
        Card prevPage = new Card(1, "Prev Page", "nav");
        nextPage.addCard((DISPLAY_WIDTH - cardWidth), (DISPLAY_HEIGHT - cardHeight), cardWidth, cardHeight);
        prevPage.addCard(0, (DISPLAY_HEIGHT - cardHeight), cardWidth, cardHeight);
        
        // Adding the nav buttons to the navBtns list     : = new ArrayList<Card>(Arrays.asList(nextPage, prevPage));
        if (this.navBtns.size() < 2) {
            this.navBtns.add(nextPage);
            this.navBtns.add(prevPage);
        }
        

        // Setting GUI state 
        this.GUIstate = "view content";     // used in the doMouse method 

        // Reset the GUI 
        this.setupGUI();

        // Updating the contnet cards
        this.updateContentCards();

        // Setting up the cards vairables
        Card crd = this.contentCards.get(pageNum); 
        String initialType = crd.type;

        crd.type = "single content";

        // Drawing card to the screen 
        this.drawButton(crd);

        // Draw the page number 
        this.drawPageNumber();

        // Resetting the card type
        crd.type = initialType;

        // Printing out the navigation buttons
        for (Card nav : this.navBtns) {
            this.drawButton(nav);
        }
                    
    }

    private void updateContentCards() {
        ArrayList<Content> collection = this.cc.getCollection();
        int cntCardID = 0; // index of the content in the collection
        int row = 0;
        boolean inList = false;
        // Creating the Content Card        - createContentCard(); 
        for(Content content : collection) {
            inList = false;     // resetting the inList var each time
            // Initialiseing the card 
            Card crd = new Card(cntCardID, content.getName(), "content");   // creating a content card 
            crd.addContentObj(content);

            // Setting the properties for the content card
            final int PADDING = 20;                                 // card pading
            int cardWidth = DISPLAY_WIDTH / 2;                      // card width
            int buttonWidth = cardWidth - (2 * PADDING);            // button width
            int cardHeight = 200;                                   // card height

            // Assigning the x & y values
            if (cntCardID % 2 == 0 && cntCardID != 0) {row++;}       // assigning new row - adding to y pos

            int cardX = cardWidth * (cntCardID%2);                   // creating the posX of the card 
            int cardY = cardHeight * row;                            // creating the posY of the card

            // NOTE: we now have the cards (x & y) values A.K.A positional values

            // Adding the card's positional values to the obj
            crd.addCard(cardX, cardY, cardWidth, cardHeight);

            // Checking if the Card obj is already inside the contentCards ArrayList 
            for (Card tempCard : this.contentCards) {
                if (crd.cnt.equals(tempCard.cnt)) {
                    inList = true;     
                } 
            }

            // If the Card isnt in the List, then adding it to the list 
            if (!inList) {
                this.contentCards.add(crd);
                
            }
            
            cntCardID++;        // adding to the index to make the rows work.
            
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
                    .addLabel("Click enter to continue")
                    .andWindow().setSize(500, 290).save()
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

    public void findContentGUI() {
        ArrayList<String> allTitles = new ArrayList<String>(Arrays.asList("Choose a title:"));
        ArrayList<String> formResults = new ArrayList<String>();
        ArrayList<Content> collection = this.cc.getCollection();
        this.GUIstate = "view content";

        // Adding all titles to the ArrayList
        for (Content cnt : collection) {
            allTitles.add(cnt.getName());
        }

        // Open dialog box and revieve input
        Form form = this.booster.createForm("Display Title")
                .addLabel("Display The Infomation of a Title")
                .addSelection("Title:", allTitles)
                .addLabel("Click enter to continue")
                .andWindow().setSize(500, 190).save()
                .show(); // use .run() instead of show() to open the formBuilder without blocking.

        // Storing input from the form 
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

        // Itterating through the collection finding the matching content
        this.updateContentCards();
        for (Card btn : this.contentCards) {

            // Checking for the matching name
            if (btn.cnt.getName().equalsIgnoreCase(userTitle)) {
                // Storing initial type value of card
                String initialType = btn.type;

                btn.type = "single content";


                this.setupGUI();
                this.drawButton(btn);

                // Resetting the card type 
                btn.type = initialType; 
            }
        }
    }

    public void getRecommendationsGUI() {
        // Get recommendations based off of a title in the collection

        // Setting vairbales
        ArrayList<String> allTitles = new ArrayList<String>(Arrays.asList("Choose a title:"));
        ArrayList<String> formResults = new ArrayList<String>();
        ArrayList<Card> similarContent = new ArrayList<Card>();
        ArrayList<Content> collection = this.cc.getCollection();
        Card selectedCard = new Card(); 

        // Selected title's information 
        String creator;
        ArrayList<String> genres; 

        // Setting the GUI's state
        this.GUIstate = "recommendation";        

        // Adding all titles to the ArrayList
        for (Content cnt : collection) {
            allTitles.add(cnt.getName());
        }

        // Printing loading dialog to the GUI 
        this.clearGUI();                // clearing GUI before printing
        this.setColour("#FFFFFF"); 
        UI.setFontSize(50); 
        UI.drawString("Recommendations Loading...", 40, ((DISPLAY_HEIGHT / 2)- 50)); 

        // Open dialog box and revieve input
        Form form = this.booster.createForm("Display Title")
                .addLabel("Display The Infomation of a Title")
                .addSelection("Title:", allTitles)
                .addLabel("Click enter to continue")
                .andWindow().setSize(500, 190).save()
                .show(); // use .run() instead of show() to open the formBuilder without blocking.

        // Storing input from the form 
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

        // Finding the contentCard assoceaed to the title 
        this.updateContentCards();
        for (Card crd : this.contentCards) {
            if (crd.cnt.getName().equals(userTitle)) {
                selectedCard = crd; 
            }
        }

        // Extracting the selected content's infomation
        creator = selectedCard.cnt.getCreator();
        genres = selectedCard.cnt.getGenres();

        // Adding similar content to the collection 
        for (Card crd : this.contentCards) {
            if (crd.cnt.getCreator().equalsIgnoreCase(creator)) {
                similarContent.add(crd); 
            }
            // Refrains form adding content twice: Content w the same creator cant then be double added for having the same genres 
            else {
                // Checking for similar generes 
                for (String g : crd.cnt.getGenres()) {
                    if (genres.contains(g)) {
                        similarContent.add(crd); 
                    }
                }
            }
        }

        // Printing out the similar content cards
        this.setupGUI();
        for (Card crd : similarContent) {
            this.drawButton(crd);
        }
    }

    /** Displaying the credits to the GUI */
    public void displayCreditsGUI() {
        // Setting up the GUI 
        this.setupGUI();
        this.GUIstate = "view content";

        // Setting up the text 
        UI.setFontSize(30);
        this.setColour("#ffffff");

        // Setting up Card
        int crdPosX = 20;
        int crdPosY = 35;
        int border = 10;
        int padding = 20;
        int cardWidth = DISPLAY_WIDTH - (2*padding); 
        int cardHeight = DISPLAY_HEIGHT - (2*padding)-20; 
         
        // Button Border
        this.setColour("#870000");
        UI.fillRect(crdPosX, crdPosY, cardWidth, cardHeight);

        // Setting the new positional values for the inner button
        crdPosX = crdPosX + border;
        crdPosY = crdPosY + border;

        // Solid Button Inner
        this.setColour("#E30032");
        UI.fillRect(crdPosX, crdPosY, (cardWidth - (2 * border)), (cardHeight - (2 * border)));

        // Crediting  
        int xValue = 50;
        this.setColour("#FFFFFF");
        UI.drawString("13DTC Project Management Internal (AS91901) 2021", xValue-18, 85);

        UI.drawString("ECS100 Library Provided by Victoria University", xValue, 200);  
        UI.drawString("UiBooster Library Provided by Milchreis (GitHub)", xValue, 250);
        UI.drawString("GitHub Copilot Provided by GitHub", xValue, 300);

        UI.drawString("Created & Designed By: James Robiony-Rogers", xValue, 425);
    }

    /** Visualy sets up the GUI */
    public void setupMenu() {            
        // Background
        this.setupGUI();

        // Card Properties 
        final int PADDING = 40;                            // card pading 
        int cardWidth = DISPLAY_WIDTH / 2;                 // card width
        int buttonWidth = cardWidth - (2 * PADDING);       // button width 
        
        int cardHeight = 100;                                       // card height 
        int row = 0;
        
        // Itterating through and drawing the menu cards 
        for (Card card : this.buttons) {
            int btnIndex = card.id; 

            // Assigning the x & y values for the card
            if (btnIndex % 2 == 0 && btnIndex != 0) {row++;}        // assigning new row - adding to y pos

            int cardX = cardWidth * (btnIndex%2);                   // creating the posX of the card 
            int cardY = cardHeight * row;                           // creating the posY of the card 
            
            // NOTE: we now have the cards (x & y) values A.K.A positional values 

            // Adding the cards positional values to the obj
            card.addCard(cardX, cardY, cardWidth, cardHeight);  
            
            // Drawing the card (button inside the card) to the GUI
            this.drawButton(card);
        }
    }

    // Clears and draws the exit button
    public void setupGUI() { 
        this.clearGUI();            // erasing all the drwaring on the GUI 
        this.drawExitButton();      // drawing the exit button
        this.drawBackButton();      // drawing the back button
        UI.printMessage("");        // clearing the message bar
        UI.setDivider(0);           // removing the GUI console
    }

    /** Draws button with text on it usign some params  
     * @param card (Card obj) - extracts the fields and draws the cards */
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
            int buttonWidth = ((DISPLAY_WIDTH / 2) - (2 * PADDING));
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
        // viewAllContent() nav buttons
        if (type.equalsIgnoreCase("nav")) {
            // Menu button properties
            final int FONTSIZE = 18;
            final int FONTPADDING = 28;
            int buttonWidth = 100;
            int buttonHeight = FONTSIZE + FONTPADDING;

            // Setting the button positional values
            int btnPosX = cardX;
            int btnPosY = cardY;

            // Assigning the buttons posional values for the doMouse method
            card.addBtn(btnPosX, btnPosY, buttonWidth, buttonHeight);

            // Drawing the button
            // Button Border TESTING: 
            this.setColour("#870000");
            UI.fillRect(btnPosX, btnPosY, buttonWidth, buttonHeight);

            // Printing text on the buttons
            this.setColour("#ffffff");
            UI.setFontSize(FONTSIZE);
            UI.drawString(btnText, (btnPosX + 10), (btnPosY + FONTPADDING));
        }
        
        // Content button 
        if (type.equalsIgnoreCase("content")) { 
            // Menu button properties
            final int PADDING = 20;
            final int EXIT_PADDING = 20;
            final int BORDER = 4;
            final int RATING_FONT = 30; 
            final int TITLE_FONT = 20;
            final int HEADER_FONT = 16; 
            final int BODY_FONT = 12; 
            final int IMG_SIZE = 50;
            int fontPadding = 28;
            int cardWidth = DISPLAY_WIDTH / 2; // card width
            int buttonWidth = cardWidth - (2 * PADDING); // button width
            int buttonHeight = 180;

            // Setting the button positional values
            int btnPosX = cardX + PADDING;
            int btnPosY = cardY + PADDING + EXIT_PADDING;

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
            UI.drawImage(imgSrc, (btnPosX + 10), (btnPosY + fontPadding), IMG_SIZE, IMG_SIZE);
            fontPadding += BODY_FONT + 25;
            // Rating text
            String ratingText = String.valueOf(card.cnt.getRating()); 
            UI.setFontSize(RATING_FONT);
            UI.drawString(ratingText, (btnPosX + (IMG_SIZE + 25)), (btnPosY + fontPadding));
        }
        // Single Content button
        if (type.equalsIgnoreCase("single content")) {
            // Menu button properties
            final int PADDING = 20;
            final int BORDER = 4;
            final int RATING_FONT = 30;
            final int TITLE_FONT = 40;
            final int HEADER_FONT = 32;
            final int BODY_FONT = 24;
            final int IMG_SIZE = 70;
            int fontPadding = 40; // 28

            int buttonWidth = DISPLAY_WIDTH - (2 * PADDING);
            int buttonHeight = 300;
            cardX = 0;
            cardY = 20;

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
            UI.drawImage(imgSrc, (btnPosX + 10), (btnPosY + fontPadding), IMG_SIZE, IMG_SIZE);
            fontPadding += BODY_FONT + 25;
            // Rating text
            String ratingText = String.valueOf(card.cnt.getRating());
            UI.setFontSize(RATING_FONT);
            UI.drawString(ratingText, (btnPosX + (IMG_SIZE + 25)), (btnPosY + fontPadding));
        }
    }

    /** Draws a cross in the top rght corner of the screen used as an exit button  */
    public void drawExitButton() {
        // Setting vairables: 
        final int SIZE = 30;    // width and height 
        int posX = DISPLAY_WIDTH - SIZE;
        int posY = SIZE; 

        // Setting colour and font sizes
        this.setColour("#FFFFFF");      // white colour 
        UI.setFontSize(SIZE);

        // Drawing the X in the top right hand corner 
        UI.drawString("×", posX, posY);

        // Adding properties to the obj
        this.exitButton.addCard(posX, 0, SIZE, SIZE);
        this.exitButton.addBtn(posX, 0, SIZE, SIZE);
    } 

    /** Draws a backwards sign in the top left corner of the screen used as a back button */
    public void drawBackButton() {
        // Setting vairables: 
        final int SIZE = 30;    // width and height 
        int posX = 15;
        int posY = SIZE-4; 

        // Setting colour and font sizes
        this.setColour("#FFFFFF");      // white colour 
        UI.setFontSize(SIZE);

        // Drawing the X in the top right hand corner 
        UI.drawString("‹", posX, posY);

        // Adding properties to the obj
        this.backButton.addCard(posX, 0, SIZE, SIZE);
        this.backButton.addBtn(posX, 0, SIZE, SIZE);
    }
    
    /** Draw the page number to the top middle of the GUI */
    public void drawPageNumber() {
        // Setting vairables: 
        final int SIZE = 18;    // width and height 
        int posX = DISPLAY_WIDTH/2 - SIZE;
        int posY = 25; 

        // Setting colour and font sizes
        this.setColour("#FFFFFF");      // white colour 
        UI.setFontSize(SIZE);

        // Drawing the X in the top right hand corner 
        UI.drawString("Page: " + String.valueOf(this.page), posX, posY);
    }

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
        // Checking what screen it should be checking 
        if(this.GUIstate.equals("menu")) {

            // Checking for mouse click 
            if (action.equals("clicked")) {
                
                // Itterating through the buttons 
                for (Card btn : this.buttons) {

                    // Checking if the (x, y) of the mouse click is within a button container
                    if(checkButtonClick(x, y, btn)) {
                        // String btnClicked = btn.btnText;   // DEBUGGING: 

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

                        else if (btn.btnText.equals("Recommendations")) {
                            this.getRecommendationsGUI();
                        }

                        else if (btn.btnText.equals("Credits")) {
                            this.displayCreditsGUI();
                        }

                        else if (btn.btnText.equalsIgnoreCase("Quit")) {
                            UI.quit();
                        }
                    }
                }
                
                if(checkButtonClick(x, y, this.exitButton)) {
                    UI.quit();
                }
                

            }
        
        }    
        // View Content Screen
        else if (this.GUIstate.equals("view content")) {
            // Exit view content on mouse click
            if (action.equals("clicked")) {
                // Next and Previous pages checking: 
                for (Card nav : this.navBtns) {
                    if (checkButtonClick(x, y, nav)) {
                        int contentAmount = this.contentCards.size(); 

                        if (nav.btnText.equalsIgnoreCase("Next Page")) {
                            // Add one to the page number if
                            if (this.page < contentAmount-1) {   // boundary checking 
                                this.page++; 
                                 
                                // Redraw the page
                                this.setupGUI();
                                this.viewAllContentGUI();
                            }
                            
                            else {
                                // Send a message to the user that they are on the last page
                                // UI.printMessage("You are on the last page");

                                // Send the user to the first page 
                                this.page = 1;

                                // Redraw the page
                                this.setupGUI();
                                this.viewAllContentGUI();
                            }
                        }

                        else if (nav.btnText.equalsIgnoreCase("Prev Page")) {
                            // Minus one from the page number 
                            if (this.page > 1) {
                                this.page--;
                                
                                // Redraw the page 
                                this.setupGUI();
                                this.viewAllContentGUI();
                            }

                            else {
                                // Send a message that the user is on the first page
                                // UI.printMessage("You are on the first page");

                                // Send the user to the last page 
                                this.page = contentAmount-1;

                                // Redraw the page
                                this.setupGUI();
                                this.viewAllContentGUI();
                            }
                        }
                    }
                }

                // Quit checking 
                if (checkButtonClick(x, y, this.exitButton)) {
                    UI.quit();
                }

                // Checking if a user has clicked on the back button 
                if (checkButtonClick(x, y, this.backButton)) {
                    this.GUIstate = "menu";
                    this.setupGUI();
                }

            }
        }
        // Recommendations Screen
        else if (this.GUIstate.equals("recommendation")) {
            // Exit view content on mouse click
            if (action.equals("clicked")) {
                // Navigating back to the menu 
                this.setupMenu();
                this.GUIstate = "menu";
            }
        }

        // Quiting the program if x is clicked
        if (action.equals("clicked")) {
            if (checkButtonClick(x, y, this.exitButton)) {
                UI.quit();
            }

            // navigating back to the menu when back button is clicked
            if (checkButtonClick(x, y, this.backButton)) {
                this.GUIstate = "menu";
                // this.setupGUI();
                this.setupMenu();
            }
        }
        
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

    /** Used for adding entries for buttons to the ArrayList */
    private void addMenuButtons() {        
        this.buttons.add(new Card(0, "Add Content", "menu"));
        this.buttons.add(new Card(1, "View All Content", "menu"));
        this.buttons.add(new Card(2, "Find Content", "menu"));
        this.buttons.add(new Card(3, "Change Rating", "menu")); 
        this.buttons.add(new Card(4, "Recommendations", "menu"));
        this.buttons.add(new Card(6, "Credits", "menu"));       
        this.buttons.add(new Card(7, "Quit", "menu"));

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
        // Credit: https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java  (user: joshua) & GitHub Copilot 
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



/** Credits: 
 *      UiBooster:          https://github.com/Milchreis/UiBooster/blob/master/README.md#form-dialog 
 *      ECS100:             ECS100 Documentation
 *      GitHub Copilot:     via VS Code
 * 
 * 
 */ 
