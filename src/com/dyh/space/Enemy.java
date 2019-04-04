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
	//x�������,����
	public int enemy_x;	
	//yӦΪ0�����������ƶ�
	public int enemy_y;	
	//x�����ƶ��ٶ�
	public int speed_x;	
	//y�����ƶ��ٶ�
	public int speed_y;	
	//�л��߶�
	public int enemy_width=SpaceMain.WIDTH/15;
	//�л����
	public int enemy_height=SpaceMain.WIDTH/15;
	//ɱ����
	public int killAbility;	
	//�Ƿ���
	public boolean isAlive;
	//��Ӧ��ֵ
	public char value;//ABCDEFGH...
	//�л�ͼƬ����
	public int index;
	Random random=new Random();
	//�л��״��ƶ�����
	public int director;
	//�л�ͼƬ
	public String[] imgName={"enemy01.gif","enemy02.png","enemy03.gif"};
	//���캯��
	public Enemy(int speed_x, int speed_y, int killAbility, boolean isAlive,char value) {
		super();
		this.enemy_x = random.nextInt(SpaceMain.WIDTH-enemy_width-3)+1;
		this.enemy_y = -enemy_height;
		//��ÿ���л�������һ��ͼƬ
		index=random.nextInt(3);
		//�״��ƶ�����
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
	//���Ƶл�
	public void drawEnemy(Graphics g){
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage(Enemy.class.getClassLoader().getResource("image/"+imgName[index]));
		g.drawImage(img, enemy_x, enemy_y,enemy_width,enemy_height,null);
		//����Ӧ��ֵ����
		g.setColor(Color.GREEN);
		g.setFont(new Font("΢���ź�", Font.BOLD,55));
		//�����л�����������ĸ
		g.drawString(value+"", enemy_x+enemy_width/2-18, enemy_y+enemy_height/2);
		move();
	}
	//�ƶ��л�
	public void move(){
		//�ж��Ƿ��ཻ
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
	//��ȡ�л�����
	public Rectangle enemyRectang(){
		return new Rectangle(enemy_x, enemy_y, enemy_width, enemy_height);
	}
}
