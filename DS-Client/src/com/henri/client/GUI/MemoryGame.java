package com.henri.client.GUI;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Random;

/**
 * @author veerasam
 */
public class MemoryGame extends JFrame {

    /*Loading all 18 filenames */
    static String files[] = {"images-set1/after_effects_logo.PNG", "images-set1/android_logo.JPG",
            "images-set1/android_studio.JPG", "images-set1/apple_logo.JPG",
            "images-set1/bridge_logo.PNG", "images-set1/chrome_logo.JPG", "images-set1/dell_logo.JPG",
            "images-set1/edge_logo.JPG", "images-set1/firefox_logo.JPG", "images-set1/flash_logo.PNG",
            "images-set1/illustrator_logo.PNG", "images-set1/in_design_logo.PNG", "images-set1/IntelliJ_IDEA_Logo.JPG",
            "images-set1/lightroom_logo.PNG", "images-set1/photoshop_logo.PNG", "images-set1/premiere_pro_logo.PNG",
            "images-set1/react-native_logo.JPG", "images-set1/webstorm_logo.JPG"};
    static JButton buttons[];
    ImageIcon closedIcon;
    int numButtons;
    ImageIcon icons[];
    Timer myTimer;

    int numClicks = 0;
    int oddClickIndex = 0;
    int currentIndex = 0;

    public MemoryGame() {
        // Set the title.

        setTitle("Memory Game");

        // Specify an action for the close button.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a BorderLayout manager (should be chosen by the client (4x4, 6x6 or 6x4).
        setLayout(new GridLayout(4, 4));

        closedIcon = new StretchIcon("closed.png");
        numButtons = 16; //again depends on the chosen matrix
        buttons = new JButton[numButtons];
        icons = new ImageIcon[numButtons];
        for (int i = 0, j = 0; i < numButtons/2; i++) {
            icons[j] = new StretchIcon(files[i]);
            buttons[j] = new JButton("");
            buttons[j].addActionListener(new ImageButtonListener());
            buttons[j].setIcon(closedIcon);
            buttons[j].setBackground(Color.white);
            add(buttons[j++]);

            icons[j] = icons[j - 1];
            buttons[j] = new JButton("");
            buttons[j].addActionListener(new ImageButtonListener());
            buttons[j].setIcon(closedIcon);
            buttons[j].setBackground(Color.white);
            add(buttons[j++]);
        }

        // randomize icons array
        Random gen = new Random();
        for (int i = 0; i < numButtons; i++) {
            int rand = gen.nextInt(numButtons);
            ImageIcon temp = icons[i];
            icons[i] = icons[rand];
            icons[rand] = temp;
        }

        // Pack and display the window.
        pack();
        setVisible(true);

        myTimer = new Timer(1000, new TimerListener());
        // myTimer.start();
    }

    private class TimerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            buttons[currentIndex].setIcon(closedIcon);
            buttons[oddClickIndex].setIcon(closedIcon);
            myTimer.stop();
        }
    }

    private class ImageButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            // we are waiting for timer to pop - no user clicks accepted
            if (myTimer.isRunning())
                return;

            numClicks++;
            System.out.println(numClicks);

            // determine which button was clicked
            for (int i = 0; i < numButtons; i++) {
                if (e.getSource() == buttons[i]) {
                    buttons[i].setIcon(icons[i]);
                    currentIndex = i;
                }
            }

            // check for even click
            if (numClicks % 2 == 0) {
                // check whether same position is clicked twice!
                if (currentIndex == oddClickIndex) {
                    numClicks--;
                    return;
                }
                // are two images matching?
                if (icons[currentIndex] != icons[oddClickIndex]) {
                    // show images for 1 sec, before flipping back
                    myTimer.start();
                }
            } else {
                // we just record index for odd clicks
                oddClickIndex = currentIndex;
            }
        }
    }

    public static void main(String[] args) {
        new MemoryGame();
    }
}
