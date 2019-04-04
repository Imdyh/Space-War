package com.dyh.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;
import com.dyh.space.CloseWindow;
import com.dyh.space.Enemy;
import com.dyh.space.Explode;
import com.dyh.space.GameSound;
import com.dyh.space.GameState;
import com.dyh.space.KeyDown;

public class SpaceMain extends Frame{
	
	private static final long serialVersionUID = 1364966797254398912L;
	public static Dimension screensize =Toolkit.getDefaultToolkit().getScreenSize();
	public static int WIDTH=(int)screensize.getWidth();
	public static int HEIGHT=(int)screensize.getHeight();
	//public static int WIDTH=1366;
	//public static int HEIGHT=768;
	public static int bg_x=0;
	public static int bg_y=0;
	public static int hero_x=WIDTH/2-50;
	public static int hero_y=HEIGHT-100;
	public static int hero_width=100;
	public static int hero_height=100;
	public static int heroMoveSpeed_x=5;
	public static int hero_blood=200;
	public static GameState state=GameState.START;//游戏状态
	public static int enemy_num=5;//屏幕上显示的敌机数量
	public static char keyDownValue='\0';
	public static int score=0;
	public static int custom=1;//关卡
	public static int timer=0;//定时器，用于显示关卡信息
	public static ArrayList<Enemy> enemies=new ArrayList<Enemy>();
	public static ArrayList<Explode> explodes=new ArrayList<Explode>();
	public static GameSound startGameSound=new GameSound("bgmusic.mp3", true);
	public SpaceMain(){
		this.setTitle("太空大战-06.03.19-作者：董玉杭");
		this.setUndecorated(true);//去掉边框
		this.setSize(WIDTH, HEIGHT);
		Toolkit tk=Toolkit.getDefaultToolkit();
		this.setIconImage(tk.getImage(SpaceMain.class.getClassLoader().getResource("image/ICO.png")));
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new CloseWindow());
		this.addKeyListener(new KeyDown());
		new MyThread().start();
		startGameSound.setName("music");
		startGameSound.start();
	}
	//双重缓冲消除闪烁
	Image bufferImage=null; 
	Graphics GraImage = null;
	@Override
	public void update(Graphics g) {
		if(bufferImage==null){
			bufferImage = createImage(this.getWidth(), this.getHeight());   //创建图形缓冲区  
		}
	    GraImage = bufferImage.getGraphics();       //获取图形缓冲区的图形上下文  
	    paint(GraImage);        //用paint方法中编写的绘图过程对图形缓冲区绘图  
	    GraImage.dispose();     //释放图形上下文资源  
	    g.drawImage(bufferImage, 0, 0, this);   //将图形缓冲区绘制到屏幕上 
	}
	//绘图
	@Override
	public void paint(Graphics g) {
		switch (state) {
		case START:
			gameStart(g);
			break;
		case RUN:
			gameRun(g);
			
			break;
		case STOP:
			gameStop(g);
			break;
		case WIN:
			gameWin(g);
			break;
		case END:
			gameEnd(g);
			break;
			
		default:
			break;
		}
	}
	/*
	 * 绘画线程
	 */
	class MyThread extends Thread{
		@Override
		public void run() {
			super.run();
			while (true) {
				try {
					repaint();
					Thread.sleep(41);
					//显示关卡提示信息
					if(isUpCustome==true&&state==GameState.RUN){
						timer++;
						if(timer>=121){
							timer=0;
							isUpCustome=false;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/*
	 * 数据复位
	 */
	public static void resetDate(){
		hero_blood=200;
		score=0;
		enemies.clear();
		custom=1;
		explodes.clear();
		enemy_num=5;
		bg_x=0;
		bg_y=0;
		timer=0;
		hero_x=WIDTH/2-50;
		hero_y=HEIGHT-100;
		index=0;
		bgStart_x=0;
		bgStart_y=0;
		isUpCustome=true;
		tipColorExit=Color.WHITE;
		tipExitSize=50;
		tipColorCon=Color.YELLOW;//继续键的颜色
		tipConSize=60;
		indexChose=0;
		lostNum=0;
		isInput=false;
	}
	public String[] bgStart={"bgStart01.gif","bgStart02.jpg","bgStart03.gif","bgStart04.gif","bgStart05.gif","bgStart06.gif"} ;
	public static int index=0;
	public static int bgStart_x=0,bgStart_y=0;
	/*
	 * 游戏开始gameStart
	 */
	public void gameStart(Graphics g){
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/"+bgStart[4]));
		g.drawImage(img,bg_x,bg_y, WIDTH/2, HEIGHT/2,null);
		img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/"+bgStart[0]));
		g.drawImage(img,bg_x+WIDTH/2,bg_y, WIDTH/2, HEIGHT/2,null);
		img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/"+bgStart[3]));
		g.drawImage(img,bg_x,bg_y+HEIGHT/2, WIDTH/2, HEIGHT/2,null);
		img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/"+bgStart[5]));
		g.drawImage(img,bg_x+WIDTH/2,bg_y+HEIGHT/2, WIDTH/2, HEIGHT/2,null);
		img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/"+bgStart[2]));
		g.drawImage(img,bg_x+WIDTH/4,bg_y+HEIGHT/4, WIDTH/2, HEIGHT/2,null);
		
		//画出提示信息
		g.setFont(new Font("宋体", Font.BOLD, 30));
		g.setColor(Color.YELLOW);
		g.drawString("《太空大战》--打字游戏", WIDTH/2-WIDTH/10,HEIGHT/2-HEIGHT/5);
		g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		g.drawString("按下 Enter/ESC键 开始/退出游戏》》》", WIDTH/2-220, HEIGHT/2);
		g.setColor(Color.RED);
		g.drawString("作者：懂吧，邮箱：Struggle.dyh@qq.com", WIDTH/2-270, HEIGHT/2+HEIGHT/4-20);
	}
	/*
	 * 获取玩家矩形
	 */
	public static Rectangle heroRectang(){
		return new Rectangle(hero_x, hero_y, hero_width, hero_height);
	}
	
	/*
	 * 绘制背景，得分信息
	 */
	public static String[] bgImage={"bg01.jpg","bg02.jpg","bg03.jpg","bg04.jpg","bg05.jpg"};
	public void drawBackground(Graphics g){
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/"+bgImage[custom-1]));
		g.drawImage(img, bg_x, bg_y, WIDTH, HEIGHT,null);
		bg_y+=6;
		g.drawImage(img, bg_x, -HEIGHT+bg_y, WIDTH, HEIGHT,null);
		if(bg_y>HEIGHT){
			bg_y=0;
		}
	}
	/*
	 * 绘制玩家
	 */
	public void drawHero(Graphics g){
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/hero.png"));
		g.drawImage(img, hero_x,hero_y,hero_width,hero_height,null);
		heroMove();
	}
	/*
	 * 玩家移动，自动左右移动
	 */
	public void heroMove(){
		hero_x+=heroMoveSpeed_x;
		if(hero_x>=WIDTH-hero_width){
			heroMoveSpeed_x=-heroMoveSpeed_x;
		}
		if(hero_x<=0){
			heroMoveSpeed_x=-heroMoveSpeed_x;
		}
	}
	Enemy temp;
	Enemy temp01;
	Random random=new Random();
	Enemy enemy;
	Explode explode;
	Explode explodeTemp;
	int ASCCII;
	public static boolean isUpCustome=true;
	char lastKilledKey;//刚打过的字母
	public static int lostNum=0;//按错键的数量
	public static boolean isInput=false;
	/*
	 * 开始游戏
	 */
	public void gameRun(Graphics g){
		//画出背景，英雄
		drawBackground(g);
		drawHero(g);
		 // 将敌机加入集合
		while(enemies.size()<enemy_num){
			ASCCII=random.nextInt(26)+65;
			//控制不能同时显示两个相同的字母,并且不出现上一个打过的字母
			for (int i = 0; i <enemies.size();) {
				temp01=enemies.get(i);
				//控制不出现相同字母
				if((temp01.value+"").equalsIgnoreCase((char)ASCCII+"")){
					ASCCII=random.nextInt(26)+65;
					i=0;
				}else {
					i++;
				}
				//控制不出现上一个消灭的字母
				if((lastKilledKey+"").equalsIgnoreCase((char)ASCCII+"")){
					ASCCII=random.nextInt(26)+65;
				}
			}
			temp=new Enemy(random.nextInt(5)+5,random.nextInt(5)+3,20, true, (char)ASCCII);
			enemies.add(temp);
		}
		/***画出敌机***/
		for (int i = 0; i < enemies.size(); i++) {
			enemy=enemies.get(i);
			if(enemy.isAlive){
				enemy.drawEnemy(g);
			}else {
				enemies.remove(enemy);
			}
		}
		/************/
		//判断是否被打中
		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < enemies.size(); j++) {
				enemy=enemies.get(j);
				if((keyDownValue+"").equalsIgnoreCase(enemy.value+"")){
					new GameSound("Laser Blast.mp3", false).start();//音效,激光
					lastKilledKey=enemy.value;
					explode=new Explode(enemy.enemy_x, enemy.enemy_y, true);
					explodes.add(explode);
					enemy.isAlive=false;
					enemies.remove(enemy);
					score+=10;
					//画出激光枪
					Graphics2D g2=(Graphics2D)g;
					g2.setStroke(new BasicStroke(8f));
					g2.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g2.drawLine(hero_x+hero_width/2-5, hero_y, enemy.enemy_x+enemy.enemy_width/2-5, enemy.enemy_y+enemy.enemy_height);
					lostNum--;
					break;
				}if(!(keyDownValue+"").equalsIgnoreCase(enemy.value+"")&&isInput==true){
					lostNum++;
					isInput=false;
				}
			}
			
		}
		 // 判读是否打中敌机
		/*for (int i = 0; i < enemies.size(); i++) {
			enemy=enemies.get(i);
			
			
			if((keyDownValue+"").equalsIgnoreCase(enemy.value+"")){
				new GameSound("Laser Blast.mp3", false).start();//音效,激光
				lastKilledKey=enemy.value;
				explode=new Explode(enemy.enemy_x, enemy.enemy_y, true);
				explodes.add(explode);
				enemy.isAlive=false;
				score++;
				//画出激光枪
				Graphics2D g2=(Graphics2D)g;
				g2.setStroke(new BasicStroke(8f));
				g2.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
				g2.drawLine(hero_x+hero_width/2-5, hero_y, enemy.enemy_x+enemy.enemy_width/2-5, enemy.enemy_y+enemy.enemy_height);
				System.out.println("打中");
				lostNum--;
			}else {
				if(isInput==true){
					System.out.println("没打中");
					lostNum++;
				}
			}
			if(enemy.isAlive){
				enemy.drawEnemy(g);
			}else {
				enemies.remove(enemy);
			}
		}*/
		//如果按错键就减血，如果按错键enemies集合的敌机不会减少
		if(lostNum>=0){
			hero_blood-=lostNum*10;
		}
		lostNum=0;
		//敌机爆炸
		for (int j = 0; j < explodes.size(); j++) {
			explodeTemp=explodes.get(j);
			if(explodeTemp.isAlive==true){
				explodeTemp.drawExplode(g);
			}else {
				enemies.remove(explodeTemp);
			}
		}
		/*
		 * 判断第几关调节难度
		 * 根据得分情况调节难度
		 */
		switch (score) {
		//第一关默认是5架敌机
		case 0:
			enemy_num=5;
			break;
		//第二关
		case 1000:
			isUpCustome=true;
			enemies.clear();
			score+=10;
			//血量回满
			hero_blood=200;
			custom++;
			enemy_num=8;
			new GameSound("CustomUp.mp3", false).start();
			break;
		//第三关
		case 3000:
			isUpCustome=true;
			enemies.clear();
			score+=20;
			custom++;
			enemy_num=13;
			//血量回满
			hero_blood=200;
			new GameSound("CustomUp.mp3", false).start();
			break;
		//第四关
		case 5000:
			isUpCustome=true;
			enemies.clear();
			score+=30;
			custom++;
			enemy_num=15;
			//血量回满
			hero_blood=200;
			new GameSound("CustomUp.mp3", false).start();
			break;
		//第五关
		case 7000:
			isUpCustome=true;
			enemies.clear();
			score+=10;
			custom++;
			enemy_num=18;
			//血量回满
			hero_blood=200;
			new GameSound("CustomUp.mp3", false).start();
			break;
		case 10000:
			state=GameState.WIN;
			break;
		default:
			break;
		}
		/*
		 *画出得分信息 
		 *判断是否需要升级关卡
		 */
		drawInfo(g);
		customUp(g);
		//判断血量是否为0，游戏是否结束
		if(hero_blood<=0){
			state=GameState.END;
		}
	}
	
	public static Color tipColorExit=Color.WHITE;//退出键的颜色
	public static int tipExitSize=50;
	
	public static Color tipColorCon=Color.YELLOW;//继续键的颜色
	public static int tipConSize=60;
	
	public static int indexChose=0;
	/*
	 * 游戏暂停
	 * 游戏暂停gameStop
	 */
	public void gameStop(Graphics g){
		g.setFont(new Font("宋体", Font.BOLD, 100));
		g.setColor(Color.GRAY);
		g.fillRect(WIDTH*2/5, HEIGHT/3, WIDTH/4, HEIGHT/4);
		
		g.setFont(new Font("宋体", Font.BOLD, 30));
		g.setColor(Color.RED);
		g.drawString("已暂停", WIDTH/2, HEIGHT/2-150);
		
		g.setFont(new Font("宋体", Font.BOLD, tipConSize));
		g.setColor(tipColorCon);
		g.drawString("继续游戏", WIDTH/2-50, HEIGHT/2-50);
		
		g.setFont(new Font("宋体", Font.BOLD, tipExitSize));
		g.setColor(tipColorExit);
		g.drawString("退出游戏", WIDTH/2-50, HEIGHT/2+50);
	}
	/*
	 * 变更关卡
	 * 画出变更关卡提示
	 */
	public void customUp(Graphics g){
		if(isUpCustome==true){
			g.setFont(new Font("宋体", Font.BOLD, 100));
			g.setColor(Color.WHITE);
			g.drawString("第 "+custom+" 关", WIDTH/2-150, HEIGHT/2);
		}
	}
	
	/* 游戏失败
	 * 画出结束状态游戏失败
	 */
	public void gameEnd(Graphics g){	
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/End.jpg"));
		g.drawImage(img, 0,0,WIDTH,HEIGHT,this);
		g.setFont(new Font("宋体", Font.BOLD, 100));
		g.setColor(Color.RED);
		g.drawString("游戏失败", WIDTH/2-150, HEIGHT/2-200);
		g.setFont(new Font("宋体", Font.BOLD, 80));
		g.setColor(Color.GREEN);
		g.drawString("到达关卡：第 "+custom+" 关", WIDTH/2-250, HEIGHT/2-20);
		g.drawString("得分："+score+" 分", WIDTH/2-150, HEIGHT/2+90);
		g.setFont(new Font("楷体", Font.BOLD, 40));
		g.setColor(Color.YELLOW);
		g.drawString("下次再接再厉哟！", WIDTH/2-150, HEIGHT/2+180);
		g.setFont(new Font("宋体", Font.BOLD, 40));
		g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		g.drawString("按下 Enter/ESC键 开始/退出游戏》》》", WIDTH/2-250, HEIGHT-100);
		
	}
	/*
	 * 游戏胜利
	 */
	public void gameWin(Graphics g){
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/Win.jpg"));
		g.drawImage(img, 0,0,WIDTH,HEIGHT,this);
		
		g.setFont(new Font("宋体", Font.BOLD, 100));

		g.setColor(Color.RED);
		g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		g.drawString("胜利！！胜利！！", WIDTH/2-350, HEIGHT/2-200);
		g.setFont(new Font("宋体", Font.BOLD, 60));
		g.setColor(Color.GREEN);
		g.drawString("到达关卡：第 "+custom+" 关", WIDTH/2-250, HEIGHT/2-20);
		g.drawString("得分："+score+" 分", WIDTH/2-150, HEIGHT/2+90);
		g.setFont(new Font("楷体", Font.BOLD, 25));
		g.setColor(Color.YELLOW);
		g.drawString("恭喜您，成功征服了我....", WIDTH/2-150, HEIGHT/2+130);
		g.drawString("这或许就是单身的力量吧....哈哈..", WIDTH/2-160, HEIGHT/2+180);
		g.drawString("您可以发邮箱：Struggle.dyh@qq.com,告诉开发者，这游戏太easy啦！goodlock！", WIDTH/2-300, HEIGHT/2+220);
		g.setFont(new Font("宋体", Font.BOLD, 40));
		g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		g.drawString("按下 Enter/ESC键 开始/退出游戏》》》", WIDTH/2-250, HEIGHT-100);
	}
	/*
	 * 画出得分信息
	 */
	public void drawInfo(Graphics g){
		g.setFont(new Font("宋体", Font.BOLD, 25));
		g.setColor(Color.GREEN);
		g.drawString("玩家信息", 10, 55);
		g.drawString("得分："+score+" 分", 150, 55);
		g.drawString("玩家血量：",350, 55);
		g.drawRect(480, 30, 201, 31);
		g.setColor(Color.RED);
		g.fillRect(481, 31,hero_blood, 30);
		g.setColor(Color.GREEN);
		g.drawString(hero_blood+"", 550, 55);
		g.setFont(new Font("宋体", Font.BOLD, 25));
		g.setColor(Color.GREEN);
		g.drawString("关卡：第 "+custom+" 关", 10, 100);
	}
	public static void main(String[] args) {
		new SpaceMain();
	}
}
