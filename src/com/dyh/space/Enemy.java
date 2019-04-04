package com.dyh.space;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

import com.dyh.main.SpaceMain;

public class Enemy {
	//x随机生成,坐标
	public int enemy_x;	
	//y应为0，从上向下移动
	public int enemy_y;	
	//x方向移动速度
	public int speed_x;	
	//y方向移动速度
	public int speed_y;	
	//敌机高度
	public int enemy_width=SpaceMain.WIDTH/15;
	//敌机宽度
	public int enemy_height=SpaceMain.WIDTH/15;
	//杀伤力
	public int killAbility;	
	//是否存活
	public boolean isAlive;
	//对应的值
	public char value;//ABCDEFGH...
	//敌机图片索引
	public int index;
	Random random=new Random();
	//敌机首次移动方向
	public int director;
	//敌机图片
	public String[] imgName={"enemy01.gif","enemy02.png","enemy03.gif"};
	//构造函数
	public Enemy(int speed_x, int speed_y, int killAbility, boolean isAlive,char value) {
		super();
		this.enemy_x = random.nextInt(SpaceMain.WIDTH-enemy_width-3)+1;
		this.enemy_y = -enemy_height;
		//给每个敌机随机获得一个图片
		index=random.nextInt(3);
		//首次移动方向
		director=random.nextInt(2);
		if(director==0){
			speed_x=-speed_x;
		}
		this.speed_x = speed_x;
		this.speed_y = speed_y;
		this.killAbility = killAbility;
		this.isAlive = isAlive;
		this.value=value;
		
	}
	//绘制敌机
	public void drawEnemy(Graphics g){
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage(Enemy.class.getClassLoader().getResource("image/"+imgName[index]));
		g.drawImage(img, enemy_x, enemy_y,enemy_width,enemy_height,null);
		//将对应的值画出
		g.setColor(Color.GREEN);
		g.setFont(new Font("微软雅黑", Font.BOLD,55));
		//画出敌机锁所带的字母
		g.drawString(value+"", enemy_x+enemy_width/2-18, enemy_y+enemy_height/2);
		move();
	}
	//移动敌机
	public void move(){
		//判断是否相交
		if(enemyRectang().intersects(SpaceMain.heroRectang())){
			isAlive=false;
			SpaceMain.explodes.add(new Explode(enemy_x, enemy_y, true));
			SpaceMain.hero_blood-=killAbility;
		}
		enemy_x+=speed_x;
		if(enemy_x>=SpaceMain.WIDTH-enemy_width){
			speed_x=-speed_x;
		}
		if(enemy_x<=0){
			speed_x=-speed_x;
		}
		enemy_y+=speed_y;
		if(enemy_y>SpaceMain.HEIGHT){
			SpaceMain.hero_blood-=killAbility;
			isAlive=false;
		}
	}
	//获取敌机矩形
	public Rectangle enemyRectang(){
		return new Rectangle(enemy_x, enemy_y, enemy_width, enemy_height);
	}
}
