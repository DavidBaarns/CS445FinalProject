/**
 * *************************************************************
 * file: Chunk.java
 * author: David Baarns, Joshua Yi, Tim Shannon, Jack Zhang, Brian Bauer
 * class: CS 445 – Computer Graphics
 *
 * assignment: Final Project
 * date last modified: 5/6/2018
 *
 * purpose: Creates a Chunk 30 x 30 and determines block types
 * and placement, then renders it.
 *
 *
 ***************************************************************
 */
package cs445finalproject;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Chunk {

    static int CHUNK_SIZE;
    static final int CUBE_LENGTH = 2;
    static final float persistenceMin = 0.04f;
    static final float persistenceMax = 0.12f;

    private int VBOVertexHandle;
    private int VBOTextureHandle;
    private int VBOColorHandle;
    private Texture texture;

    private Block[][][] Blocks;
    private int StartX, StartY, StartZ;
    private Random r;

    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;

    public void render() {
        initLightArrays();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition); //sets our light’s position
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);//sets our specular light
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);//sets our diffuse light
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);//sets our ambient light
        glEnable(GL_LIGHTING);//enables our lighting
        glEnable(GL_LIGHT0);//enables light0

        glPushMatrix();
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glColorPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBindTexture(GL_TEXTURE_2D, 1);
        glTexCoordPointer(2, GL_FLOAT, 0, 0L);
        glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
    }

    private void initLightArrays() {
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0.0f).put(0.0f).put(-26.0f).put(1.0f).flip();
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(3.0f).put(3.0f).put(3.0f).put(0.0f).flip();
    }

    public void delete() {

    }

    public void rebuildMesh(float startX, float startY, float startZ) {
        Random random = new Random();
        int sandXMin = random.nextInt(15);
        int sandXMax = random.nextInt(15) + 100;
        int sandZMin = random.nextInt(15);
        int sandZMax = random.nextInt(15) + 100;

        int waterXMin = random.nextInt(15);
        int waterXMax = random.nextInt(15) + 75;
        int waterZMin = random.nextInt(15);
        int waterZMax = random.nextInt(15) + 75;

        float persistence = 0;
        persistence = (random.nextFloat() % persistenceMax) + persistenceMin;

        int seed = (int) (50 * random.nextFloat());

        SimplexNoise noise = new SimplexNoise(CHUNK_SIZE, persistence, seed);

        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();

        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);

        for (float x = 0; x < CHUNK_SIZE; x++) {
            for (float z = 0; z < CHUNK_SIZE; z++) {
                for (float y = 0; y < CHUNK_SIZE; y++) {
                    //generate height from simplex noise
                    int height = (int) (startY + Math.abs((int) (CHUNK_SIZE * noise.getNoise((int) x, (int) z))) * CUBE_LENGTH) + 15;

                    if (y >= height) {
                        break;
                    }
                    if (y == 0) {
                        Blocks[(int) x][(int) y][(int) z] = new Block(Block.BlockType.BlockType_Bedrock);
                    }
                    if (y>=14 && y< height-1 && y>0){
                        
                        Blocks[(int) x][(int) y][(int) z] = new Block(Block.BlockType.BlockType_Dirt);
                    }
                    // Top layer. Decide between water, grass, and sand
                    if (y == height - 1) {
                        {
                            if (x >= waterXMin && x <= waterXMax && z >= waterZMin && z <= waterZMax && y == 14) {
                                Blocks[(int) x][(int) y][(int) z] = new Block(Block.BlockType.BlockType_Water);
                            } else if (x >= sandXMin && x <= sandXMax && z >= sandZMin && z <= sandZMax && y == 14) {
                                Blocks[(int) x][(int) y][(int) z] = new Block(Block.BlockType.BlockType_Sand);
                            }
                            else{
                                 Blocks[(int) x][(int) y][(int) z] = new Block(Block.BlockType.BlockType_Grass);
                            }

                            
                        }

//                        r = new Random();
//                        float temp = r.nextFloat();
//                        if (temp > 0.76f) {
                        //Blocks[(int) x][(int) y][(int) z] = new Block(Block.BlockType.BlockType_Sand);
//                        } else if (temp > 0.53f) {
//
//                            Blocks[(int) x][(int) y][(int) z] = new Block(Block.BlockType.BlockType_Water);
//                        } else {
//
//                            Blocks[(int) x][(int) y][(int) z] = new Block(Block.BlockType.BlockType_Grass);
//                        }
                    }
                    

//                    if (x >= waterXMin && x <= waterXMax && z >= waterZMin && z <= waterZMax && y == 15) {
//                        Blocks[(int) x][(int) y][(int) z] = new Block(Block.BlockType.BlockType_Water);
//                    } else if (x >= sandXMin && x <= sandXMax && z >= sandZMin && z <= sandZMax && y == 15) {
//                        Blocks[(int) x][(int) y][(int) z] = new Block(Block.BlockType.BlockType_Sand);
//                    }
                    VertexPositionData.put(createCube(-(float) (startX + x * CUBE_LENGTH), (float) (y * CUBE_LENGTH + (int) (CHUNK_SIZE * .8)), -(float) (startZ + z * CUBE_LENGTH)));

                    VertexColorData.put(createCubeVertexCol(getCubeColor(
                            Blocks[(int) x][(int) y][(int) z])));

                    VertexTextureData.put(createTexCube((float) 0,
                            (float) 0, Blocks[(int) (x)][(int) (y)][(int) (z)]));
                }
            }
        }

        VertexColorData.flip();
        VertexPositionData.flip();
        VertexTextureData.flip();
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static float[] createTexCube(float x, float y, Block block) {
        float offset = (1024f / 16) / 1024f;

        switch (block.GetID()) {
            case 0: {	// Grass
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 3, y + offset * 10,
                    x + offset * 2, y + offset * 10,
                    x + offset * 2, y + offset * 9,
                    x + offset * 3, y + offset * 9,
                    // TOP!
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // FRONT QUAD
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // BACK QUAD
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    // LEFT QUAD
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1,
                    // RIGHT QUAD
                    x + offset * 3, y + offset * 0,
                    x + offset * 4, y + offset * 0,
                    x + offset * 4, y + offset * 1,
                    x + offset * 3, y + offset * 1};
            }

            case 1: {	// Sand
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 3, y + offset * 2,
                    // TOP!
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 3, y + offset * 2,
                    // FRONT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 3, y + offset * 2,
                    // BACK QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 3, y + offset * 2,
                    // LEFT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 3, y + offset * 2,
                    // RIGHT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 3, y + offset * 2};
            }

            case 2: {	// Water
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 15, y + offset * 12,
                    x + offset * 16, y + offset * 12,
                    x + offset * 16, y + offset * 13,
                    x + offset * 15, y + offset * 13,
                    // TOP!
                    x + offset * 15, y + offset * 12,
                    x + offset * 16, y + offset * 12,
                    x + offset * 16, y + offset * 13,
                    x + offset * 15, y + offset * 13,
                    // FRONT QUAD
                    x + offset * 15, y + offset * 12,
                    x + offset * 16, y + offset * 12,
                    x + offset * 16, y + offset * 13,
                    x + offset * 15, y + offset * 13,
                    // BACK QUAD
                    x + offset * 15, y + offset * 12,
                    x + offset * 16, y + offset * 12,
                    x + offset * 16, y + offset * 13,
                    x + offset * 15, y + offset * 13,
                    // LEFT QUAD
                    x + offset * 15, y + offset * 12,
                    x + offset * 16, y + offset * 12,
                    x + offset * 16, y + offset * 13,
                    x + offset * 15, y + offset * 13,
                    // RIGHT QUAD
                    x + offset * 15, y + offset * 12,
                    x + offset * 16, y + offset * 12,
                    x + offset * 16, y + offset * 13,
                    x + offset * 15, y + offset * 13};
            }

            case 3: {	// Dirt
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // TOP!
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // FRONT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // BACK QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // LEFT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // RIGHT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0};
            }

            case 4: {	// Stone
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 0, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    // TOP!
                    x + offset * 0, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    // FRONT QUAD
                    x + offset * 0, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    // BACK QUAD
                    x + offset * 0, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    // LEFT QUAD
                    x + offset * 0, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    // RIGHT QUAD
                    x + offset * 0, y + offset * 1,
                    x + offset * 1, y + offset * 1,
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2};
            }

            case 5: {	// Bedrock 
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    // TOP!
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    // FRONT QUAD
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    // BACK QUAD
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    // LEFT QUAD
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2,
                    // RIGHT QUAD
                    x + offset * 1, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 2,
                    x + offset * 1, y + offset * 2};
            }
            case 6: { // Lapis Stone
                return new float[]{
                    //
                    x + offset * 1, y + offset * 10,
                    x + offset * 0, y + offset * 10,
                    x + offset * 0, y + offset * 11,
                    x + offset * 1, y + offset * 11,
                    //
                    x + offset * 1, y + offset * 10,
                    x + offset * 0, y + offset * 10,
                    x + offset * 0, y + offset * 11,
                    x + offset * 1, y + offset * 11,
                    //  
                    x + offset * 1, y + offset * 10,
                    x + offset * 0, y + offset * 10,
                    x + offset * 0, y + offset * 11,
                    x + offset * 1, y + offset * 11,
                    //
                    x + offset * 1, y + offset * 10,
                    x + offset * 0, y + offset * 10,
                    x + offset * 0, y + offset * 11,
                    x + offset * 1, y + offset * 11,
                    //
                    x + offset * 1, y + offset * 10,
                    x + offset * 0, y + offset * 10,
                    x + offset * 0, y + offset * 11,
                    x + offset * 1, y + offset * 11,
                    //
                    x + offset * 1, y + offset * 10,
                    x + offset * 0, y + offset * 10,
                    x + offset * 0, y + offset * 11,
                    x + offset * 1, y + offset * 11,};
            }
            case 7: { // Gold
                return new float[]{
                    //
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    x + offset * 0, y + offset * 3,
                    x + offset * 1, y + offset * 3,
                    //
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    x + offset * 0, y + offset * 3,
                    x + offset * 1, y + offset * 3,
                    //
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    x + offset * 0, y + offset * 3,
                    x + offset * 1, y + offset * 3,
                    //
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    x + offset * 0, y + offset * 3,
                    x + offset * 1, y + offset * 3,
                    //
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    x + offset * 0, y + offset * 3,
                    x + offset * 1, y + offset * 3,
                    //
                    x + offset * 1, y + offset * 2,
                    x + offset * 0, y + offset * 2,
                    x + offset * 0, y + offset * 3,
                    x + offset * 1, y + offset * 3,};
            }
            case 8: { // Diamond
                return new float[]{
                    //
                    x + offset * 3, y + offset * 3,
                    x + offset * 2, y + offset * 3,
                    x + offset * 2, y + offset * 4,
                    x + offset * 3, y + offset * 4,
                    //
                    x + offset * 3, y + offset * 3,
                    x + offset * 2, y + offset * 3,
                    x + offset * 2, y + offset * 4,
                    x + offset * 3, y + offset * 4,
                    //
                    x + offset * 3, y + offset * 3,
                    x + offset * 2, y + offset * 3,
                    x + offset * 2, y + offset * 4,
                    x + offset * 3, y + offset * 4,
                    //
                    x + offset * 3, y + offset * 3,
                    x + offset * 2, y + offset * 3,
                    x + offset * 2, y + offset * 4,
                    x + offset * 3, y + offset * 4,
                    //
                    x + offset * 3, y + offset * 3,
                    x + offset * 2, y + offset * 3,
                    x + offset * 2, y + offset * 4,
                    x + offset * 3, y + offset * 4,
                    //
                    x + offset * 3, y + offset * 3,
                    x + offset * 2, y + offset * 3,
                    x + offset * 2, y + offset * 4,
                    x + offset * 3, y + offset * 4,};
            }
            default: {	// Dirt [Default]
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // TOP!
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // FRONT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // BACK QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // LEFT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0,
                    // RIGHT QUAD
                    x + offset * 3, y + offset * 1,
                    x + offset * 2, y + offset * 1,
                    x + offset * 2, y + offset * 0,
                    x + offset * 3, y + offset * 0};
            }
        } // End Switch
    }

    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        return cubeColors;
    }

    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[]{
            // TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            // BOTTOM QUAD
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // FRONT QUAD
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            // BACK QUAD
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // LEFT QUAD
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            // RIGHT QUAD
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z};
    }

    private float[] getCubeColor(Block block) {
        return new float[]{1, 1, 1};
    }

    public Chunk(int startX, int startY, int startZ, int size) {
        CHUNK_SIZE = size;

        try {
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("cs445finalproject/terrain.png"));
        } catch (Exception e) {
            System.out.print("ER-ROAR!");
        }

        r = new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    float temp = r.nextFloat();
//                    if(temp > 0.8f){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
//                    }
//                    else if(temp > 0.6f && (y >= 1 || y <= 5)){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
//                    }
//                    else if(temp > 0.4f  && (y >= 1 || y <= 3)){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
//                    }
//                    else if(temp > 0.2f && (y >= 1 || y <= 6)){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
//                    }
//                    else if(temp > 0.1f && (y == 3 || y <= 10)){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
//                    }
//                    else{
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);
//                    }
                    if (temp > 0.45) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    } else if (temp > 0.1) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);

                    } else if (temp > 0.05) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Lapis);
                    } else if (temp >= 0.01) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Gold);
                    } else if (temp >= 0) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Diamond);
                    }
                }
            }
        }

        VBOVertexHandle = glGenBuffers();
        VBOColorHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();

        StartX = startX;
        StartY = startY;
        StartZ = startZ;

        rebuildMesh(startX, startY, startZ);
    }
}
