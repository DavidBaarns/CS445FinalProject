/**
 * *************************************************************
 * file: cs445FinalProject.java
 * author: David Baarns, 
 * class: CS 445 – Computer Graphics
 *
 * assignment: Final Project
 * date last modified: 
 *
 * purpose:
 *
 ***************************************************************
 */
package cs445finalproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class cs445FinalProject {
public float x, y, z;

 
 
    public static void main(String[] args) {
        cs445FinalProject basic = new cs445FinalProject();

        basic.start();

    }

    // Calls method to create the windows, iniealize openGL,
    // and render
    private void start() {
        try {
            createWindow();
            initGL();
            render();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Creates the new window and keyboard.
    private void createWindow() throws LWJGLException {
        Display.setFullscreen(false);

        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle("Final Project");
        Display.create();
        //Keyboard.create();

    }

    // Initializes openGL, sets window size and color
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glOrtho(0, 640, 0, 480, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    // This method prints instructions, reads the coordinates.txt file,
    // and handles the logic to color and draw each shape as needed.
    private void render() throws IOException {
        
        while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            {
               
                }
                Display.update();
                Display.sync(60);
            }
        }

        //Display.destroy();

    }
