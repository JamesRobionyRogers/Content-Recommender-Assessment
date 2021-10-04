package com.robionyrogers;

/**
 * Card.java: Obj class a
 * 
 * @author James Robiony-Rogers
 * @version 1.0 - 23rd September 2021
 */
public class Card {
    // Defining the card properties
    public int id; 

    public int cardX = 0;
    public int cardY = 0; 
    public int cardWidth = 100; 
    public int cardHeight = 30; 
    // Button properties
    public int btnPosX;
    public int btnPosY;
    public int buttonWidth; 
    public int buttonHeight; 
    public String btnText; 
    public String type; 
    public Content cnt;    

    /**
     * 
     * @param id
     * @param cardX
     * @param cardY
     * @param buttonWidth
     * @param buttonHeight
     * @param btnText
     * @param cardType
     */
    public Card(int id, int cardX, int cardY, int buttonWidth, int buttonHeight, String btnText, String cardType) {
        // Assigning the obj's details
        this.id = id;
        this.cardX = cardX;
        this.cardY = cardY;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight; 
        this.btnText = btnText;
        this.type = cardType; 
    }

    /**
     * 
     * @param id
     * @param btnText
     * @param cardType
     */
    public Card(int id, String btnText, String cardType) {
        // Assigning the obj's details
        this.id = id;
        this.btnText = btnText;
        this.type = cardType;
    }

    /** Assigning the positional values for the button within the card */
    public void addBtn(int btnPosX, int btnPosY, int btnWidth, int btnHeight) {
        this.btnPosX = btnPosX;
        this.btnPosY = btnPosY;
        this.buttonWidth = btnWidth;
        this.buttonHeight = btnHeight;
    }

    /** Assigning the positonal values of the card */ 
    public void addCard(int cardX, int cardY, int cardWidth, int cardHeight) {
        this.cardX = cardX;
        this.cardY = cardY;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
    }

    public void addContentObj(Content cnt) {
        this.cnt = cnt;
    }


    /** Running an instance of the GUI
     * @param args (String[]) - Standard */
    public static void main(String[] args) throws Exception {
        new GUI(); // running the driver when this file is executed

    }
}
