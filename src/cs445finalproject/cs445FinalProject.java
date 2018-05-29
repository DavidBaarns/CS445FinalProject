/**
 * *************************************************************
 * file: cs445FinalProject.java
 * author: David Baarns, Joshua Yi, Tim Shannon, Jack Zhang, Brian Bauer
 * class: CS 445 – Computer Graphics
 *
 * assignment: Final Project
 * date last modified: 5/29/2018
 *
 * purpose: Create openGL window and render
 *
 * ADDITIONAL FEATURES
 * Press R to spawn new random chunk
 * Press T to spawn new random top layers
 * Press +/= key to increase chunk size by 30 (Up to 120x120)
 * Press -/_ key to decrease chunk size by 30 (Down to 30x30)
 * Press H to toggle Hollow mode. When turned on, the next chunk
 *      generated (via R key, T, +/= key or -/_ key) will be
 *      completely hollow.
 ***************************************************************
 */
package cs445finalproject;

import java.io.IOException;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import java.nio.FloatBuffer;

public class cs445FinalProject {

    private FPCameraController fp;
    private DisplayMode displayMode;
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;

    public static void main(String[] args) {
        cs445FinalProject basic = new cs445FinalProject();
        basic.start();

    }


// Calls method to create the windows, iniealize openGL,
// and render
    public void start() {
        try {
            createWindow();
            initGL();
            fp = new FPCameraController(0f, 0f, 0f);
            fp.gameLoop();//render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Creates the new window and keyboard.
    private void createWindow() throws LWJGLException {
        Display.setFullscreen(false);
        DisplayMode d[]
                = Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640
                    && d[i].getHeight() == 480
                    && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("Final Project");
        Display.create();
    }

// Initializes openGL, sets window size and color
    private void initGL() {

        glClearColor(0.05f, 0.75f, 0.91f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);
        GLU.gluPerspective(100.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

       
    }
}
