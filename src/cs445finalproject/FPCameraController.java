package cs445finalproject;

/**
 * *************************************************************
 * file: FPCameraController.java author: David Baarns, Joshua Yi, Tim Shannon,
 * Jack Zhang, Brian Bauer class: CS 445 – Computer Graphics
 *
 * assignment: Final Project date last modified: 5/29/2018
 *
 * purpose: Creates camera and cube, creates key bindings to:
 *      move around,
 *      Generate random chunks, 
 *      increase and decrease chunk size,
 *      toggle hollow chunks
 *
 *
 ***************************************************************
 */
//package openglapp;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;

public class FPCameraController {

    //3d vector to store the camera's position in
    private Vector3Float position = null;
    private Vector3Float lPosition = null;
    private int CHUNK_SIZE = 30;
    private boolean Hollow = false;
    //the rotation around the Y axis of the camera
    private float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Chunk c = new Chunk(0, 0, 0, CHUNK_SIZE, Hollow);
    private Chunk d;
    public FPCameraController(float x, float y, float z) {
        //instantiate position Vector3f to the x y z params.
        position = new Vector3Float(x, y, z);
        lPosition = new Vector3Float(x, y, z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
    }
    //increment the camera's current yaw rotation

    public void yaw(float amount) {
        //increment the yaw by the amount param
        yaw += amount;
    }
    //increment the camera's current yaw rotation

    public void pitch(float amount) {
        //increment the pitch by the amount param
        pitch -= amount;
    }
    //moves the camera forward relative to its current rotation (yaw)

    public void walkForward(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
    }
    //moves the camera backward relative to its current rotation (yaw)

    public void walkBackwards(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
    }
    //strafes the camera left relative to its current rotation (yaw)

    public void strafeLeft(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw - 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw - 90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    //strafes the camera right relative to its current rotation (yaw)

    public void strafeRight(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw + 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw + 90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    //moves the camera up relative to its current rotation (yaw)

    public void moveUp(float distance) {
        position.y -= distance;
    }
    //moves the camera down

    public void moveDown(float distance) {
        position.y += distance;
    }
    //translates and rotate the matrix so that it looks through the camera
    //this does basically what gluLookAt() does

    public void lookThrough() {
        //roatate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
    }

    public void gameLoop() {
        FPCameraController camera = new FPCameraController(20, -70, -2);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.19f;
        float movementSpeed = 1.0f;
        //hide the mouse
        Mouse.setGrabbed(true);

        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            time = Sys.getTime();
            lastTime = time;
            //distance in mouse movement
            //from the last getDX() call.
            dx = Mouse.getDX();
            //distance in mouse movement
            //from the last getDY() call.
            dy = Mouse.getDY();
            //controll camera yaw from x movement fromt the mouse
            camera.yaw(dx * mouseSensitivity);
            //controll camera pitch from y movement fromt the mouse
            camera.pitch(dy * mouseSensitivity);
            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer
            if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
            {
                camera.walkForward(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
            {
                camera.walkBackwards(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left {
            {
                camera.strafeLeft(movementSpeed);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right {
            {
                camera.strafeRight(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))//move up {
            {
                camera.moveUp(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_R))//Random {
            {
                c = null;
                System.gc();
                System.runFinalization();
                c = new Chunk(0, 0, 0, CHUNK_SIZE, Hollow);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_T))//Random Top {
            {

                c.rebuildMesh(0, 0, 0);

            }

            while (Keyboard.next()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_H) { // Toggle Hollow mode
                    if (Keyboard.getEventKeyState()) {
                        Hollow = !Hollow;
                        if (Hollow) {
                           d = c;
                            System.out.println("Hollow toggle: [ON] The next generated chunk will be hollow.");
                        } else {
                            System.out.println("Hollow toggle: [OFF] The next generated chunk will be filled in.");
                        }
                        
                    }
                }

                if (Keyboard.getEventKey() == Keyboard.KEY_EQUALS) { //Increase Chunk Size 
                    if (Keyboard.getEventKeyState()) {
                        if (CHUNK_SIZE == 120) {
                            System.out.println("Maximum chunk size");
                        } else {
                            c = null;
                            System.gc();
                            System.runFinalization();
                            CHUNK_SIZE += 30;
                            c = new Chunk(0, 0, 0, CHUNK_SIZE, Hollow);
                        }
                    }
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_MINUS) {//Decrease Chunk Size {
                    if (Keyboard.getEventKeyState()) {
                        if (CHUNK_SIZE == 30) {
                            System.out.println("Minimum chunk size");
                        } else {
                            c = null;
                            System.gc();
                            System.runFinalization();
                            CHUNK_SIZE -= 30;
                            c = new Chunk(0, 0, 0, CHUNK_SIZE, Hollow);
                        }
                    }
                }
            }
            
            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            //you would draw your scene here.
            c.render();
            //draw the buffer to the screen
            Display.update();
            Display.sync(60);
        }

        Display.destroy();
    }

    private void render() {
        try {
            //glEnable(GL_DEPTH_TEST);
            glBegin(GL_QUADS);
            glColor3f(2.0f, 0.0f, 2.0f);
            glVertex3f(1.0f, -2.0f, -1.0f);
            glVertex3f(-1.0f, -2.0f, -1.0f);
            glVertex3f(-1.0f, 2.0f, -1.0f);
            glVertex3f(1.0f, 2.0f, -1.0f);
            glEnd();
        } catch (Exception e) {
        }
    }
}
