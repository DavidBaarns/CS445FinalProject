package cs445finalproject;

/**
 * *************************************************************
 * file: Block.java
 * author: David Baarns, Joshua Yi, Tim Shannon, Jack Zhang, Brian Bauer
 * class: CS 445 – Computer Graphics
 *
 * assignment: Final Project
 * date last modified: 5/6/2018
 *
 * purpose: Creates structure for housing block data
 * 
 *
 ***************************************************************
 */

//package openglapp;

public class Block {
	private boolean IsActive;
	private BlockType Type;
	private float x,y,z;
	
	public enum BlockType{
		BlockType_Grass(0),
		BlockType_Sand(1),
		BlockType_Water(2),
		BlockType_Dirt(3),
		BlockType_Stone(4),
		BlockType_Bedrock(5); // [DEFAULT]
		private int BlockID;
		BlockType(int i){ BlockID = i;}
		public int GetID(){return BlockID;}
		public void SetID(int i){BlockID = i;}
	}
	public Block(BlockType type){Type = type;}
	public void setCoords(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public boolean IsActive() {return IsActive;}
	public void SetActive(boolean active){IsActive=active;}
	public int GetID(){return Type.GetID();}
}