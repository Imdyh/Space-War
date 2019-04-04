package com.dyh.space;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Explode {
	public int enemy_x;	
	//y应为0，从上向下移动
	public int enemy_y;	
	public boolean isAlive=true;
	public int index=0;
	//爆炸
	public String[] explodeName={"e1.gif","e2.gif","e3.gif","e4.gif","e5.gif","e6.gif","e7.gif","e8.gif","e9.gif","e10.gif","e11.gif"};
	public Explode(int enemy_x, int enemy_y,boolean isAlive) {
		super();
		this.enemy_x = enemy_x;
		this.enemy_y = enemy_y;
		this.isAlive=isAlive;
		new GameSound("Explode06.mp3", false).start();
	}
	public void drawExplode(Graphics g){
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img = null;
		img=tk.getImage(Enemy.class.getClassLoader().getResource("image/"+explodeName[index]));
		g.drawImage(img, enemy_x, enemy_y,71*2,200,null);
		index++;
		if(index>explodeName.length-1){
			isAlive=false;
		}
	}
}
