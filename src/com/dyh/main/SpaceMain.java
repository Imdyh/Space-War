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
	public static GameState state=GameState.START;//��Ϸ״̬
	public static int enemy_num=5;//��Ļ����ʾ�ĵл�����
	public static char keyDownValue='\0';
	public static int score=0;
	public static int custom=1;//�ؿ�
	public static int timer=0;//��ʱ����������ʾ�ؿ���Ϣ
	public static ArrayList<Enemy> enemies=new ArrayList<Enemy>();
	public static ArrayList<Explode> explodes=new ArrayList<Explode>();
	public static GameSound startGameSound=new GameSound("bgmusic.mp3", true);
	public SpaceMain(){
		this.setTitle("̫�մ�ս-06.03.19-���ߣ�����");
		this.setUndecorated(true);//ȥ���߿�
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
	//˫�ػ���������˸
	Image bufferImage=null; 
	Graphics GraImage = null;
	@Override
	public void update(Graphics g) {
		if(bufferImage==null){
			bufferImage = createImage(this.getWidth(), this.getHeight());   //����ͼ�λ�����  
		}
	    GraImage = bufferImage.getGraphics();       //��ȡͼ�λ�������ͼ��������  
	    paint(GraImage);        //��paint�����б�д�Ļ�ͼ���̶�ͼ�λ�������ͼ  
	    GraImage.dispose();     //�ͷ�ͼ����������Դ  
	    g.drawImage(bufferImage, 0, 0, this);   //��ͼ�λ��������Ƶ���Ļ�� 
	}
	//��ͼ
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
	 * �滭�߳�
	 */
	class MyThread extends Thread{
		@Override
		public void run() {
			super.run();
			while (true) {
				try {
					repaint();
					Thread.sleep(41);
					//��ʾ�ؿ���ʾ��Ϣ
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
	 * ���ݸ�λ
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
		tipColorCon=Color.YELLOW;//����������ɫ
		tipConSize=60;
		indexChose=0;
		lostNum=0;
		isInput=false;
	}
	public String[] bgStart={"bgStart01.gif","bgStart02.jpg","bgStart03.gif","bgStart04.gif","bgStart05.gif","bgStart06.gif"} ;
	public static int index=0;
	public static int bgStart_x=0,bgStart_y=0;
	/*
	 * ��Ϸ��ʼgameStart
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
		
		//������ʾ��Ϣ
		g.setFont(new Font("����", Font.BOLD, 30));
		g.setColor(Color.YELLOW);
		g.drawString("��̫�մ�ս��--������Ϸ", WIDTH/2-WIDTH/10,HEIGHT/2-HEIGHT/5);
		g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		g.drawString("���� Enter/ESC�� ��ʼ/�˳���Ϸ������", WIDTH/2-220, HEIGHT/2);
		g.setColor(Color.RED);
		g.drawString("���ߣ����ɣ����䣺Struggle.dyh@qq.com", WIDTH/2-270, HEIGHT/2+HEIGHT/4-20);
	}
	/*
	 * ��ȡ��Ҿ���
	 */
	public static Rectangle heroRectang(){
		return new Rectangle(hero_x, hero_y, hero_width, hero_height);
	}
	
	/*
	 * ���Ʊ������÷���Ϣ
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
	 * �������
	 */
	public void drawHero(Graphics g){
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/hero.png"));
		g.drawImage(img, hero_x,hero_y,hero_width,hero_height,null);
		heroMove();
	}
	/*
	 * ����ƶ����Զ������ƶ�
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
	char lastKilledKey;//�մ������ĸ
	public static int lostNum=0;//�����������
	public static boolean isInput=false;
	/*
	 * ��ʼ��Ϸ
	 */
	public void gameRun(Graphics g){
		//����������Ӣ��
		drawBackground(g);
		drawHero(g);
		 // ���л����뼯��
		while(enemies.size()<enemy_num){
			ASCCII=random.nextInt(26)+65;
			//���Ʋ���ͬʱ��ʾ������ͬ����ĸ,���Ҳ�������һ���������ĸ
			for (int i = 0; i <enemies.size();) {
				temp01=enemies.get(i);
				//���Ʋ�������ͬ��ĸ
				if((temp01.value+"").equalsIgnoreCase((char)ASCCII+"")){
					ASCCII=random.nextInt(26)+65;
					i=0;
				}else {
					i++;
				}
				//���Ʋ�������һ���������ĸ
				if((lastKilledKey+"").equalsIgnoreCase((char)ASCCII+"")){
					ASCCII=random.nextInt(26)+65;
				}
			}
			temp=new Enemy(random.nextInt(5)+5,random.nextInt(5)+3,20, true, (char)ASCCII);
			enemies.add(temp);
		}
		/***�����л�***/
		for (int i = 0; i < enemies.size(); i++) {
			enemy=enemies.get(i);
			if(enemy.isAlive){
				enemy.drawEnemy(g);
			}else {
				enemies.remove(enemy);
			}
		}
		/************/
		//�ж��Ƿ񱻴���
		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < enemies.size(); j++) {
				enemy=enemies.get(j);
				if((keyDownValue+"").equalsIgnoreCase(enemy.value+"")){
					new GameSound("Laser Blast.mp3", false).start();//��Ч,����
					lastKilledKey=enemy.value;
					explode=new Explode(enemy.enemy_x, enemy.enemy_y, true);
					explodes.add(explode);
					enemy.isAlive=false;
					enemies.remove(enemy);
					score+=10;
					//��������ǹ
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
		 // �ж��Ƿ���ел�
		/*for (int i = 0; i < enemies.size(); i++) {
			enemy=enemies.get(i);
			
			
			if((keyDownValue+"").equalsIgnoreCase(enemy.value+"")){
				new GameSound("Laser Blast.mp3", false).start();//��Ч,����
				lastKilledKey=enemy.value;
				explode=new Explode(enemy.enemy_x, enemy.enemy_y, true);
				explodes.add(explode);
				enemy.isAlive=false;
				score++;
				//��������ǹ
				Graphics2D g2=(Graphics2D)g;
				g2.setStroke(new BasicStroke(8f));
				g2.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
				g2.drawLine(hero_x+hero_width/2-5, hero_y, enemy.enemy_x+enemy.enemy_width/2-5, enemy.enemy_y+enemy.enemy_height);
				System.out.println("����");
				lostNum--;
			}else {
				if(isInput==true){
					System.out.println("û����");
					lostNum++;
				}
			}
			if(enemy.isAlive){
				enemy.drawEnemy(g);
			}else {
				enemies.remove(enemy);
			}
		}*/
		//���������ͼ�Ѫ����������enemies���ϵĵл��������
		if(lostNum>=0){
			hero_blood-=lostNum*10;
		}
		lostNum=0;
		//�л���ը
		for (int j = 0; j < explodes.size(); j++) {
			explodeTemp=explodes.get(j);
			if(explodeTemp.isAlive==true){
				explodeTemp.drawExplode(g);
			}else {
				enemies.remove(explodeTemp);
			}
		}
		/*
		 * �жϵڼ��ص����Ѷ�
		 * ���ݵ÷���������Ѷ�
		 */
		switch (score) {
		//��һ��Ĭ����5�ܵл�
		case 0:
			enemy_num=5;
			break;
		//�ڶ���
		case 1000:
			isUpCustome=true;
			enemies.clear();
			score+=10;
			//Ѫ������
			hero_blood=200;
			custom++;
			enemy_num=8;
			new GameSound("CustomUp.mp3", false).start();
			break;
		//������
		case 3000:
			isUpCustome=true;
			enemies.clear();
			score+=20;
			custom++;
			enemy_num=13;
			//Ѫ������
			hero_blood=200;
			new GameSound("CustomUp.mp3", false).start();
			break;
		//���Ĺ�
		case 5000:
			isUpCustome=true;
			enemies.clear();
			score+=30;
			custom++;
			enemy_num=15;
			//Ѫ������
			hero_blood=200;
			new GameSound("CustomUp.mp3", false).start();
			break;
		//�����
		case 7000:
			isUpCustome=true;
			enemies.clear();
			score+=10;
			custom++;
			enemy_num=18;
			//Ѫ������
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
		 *�����÷���Ϣ 
		 *�ж��Ƿ���Ҫ�����ؿ�
		 */
		drawInfo(g);
		customUp(g);
		//�ж�Ѫ���Ƿ�Ϊ0����Ϸ�Ƿ����
		if(hero_blood<=0){
			state=GameState.END;
		}
	}
	
	public static Color tipColorExit=Color.WHITE;//�˳�������ɫ
	public static int tipExitSize=50;
	
	public static Color tipColorCon=Color.YELLOW;//����������ɫ
	public static int tipConSize=60;
	
	public static int indexChose=0;
	/*
	 * ��Ϸ��ͣ
	 * ��Ϸ��ͣgameStop
	 */
	public void gameStop(Graphics g){
		g.setFont(new Font("����", Font.BOLD, 100));
		g.setColor(Color.GRAY);
		g.fillRect(WIDTH*2/5, HEIGHT/3, WIDTH/4, HEIGHT/4);
		
		g.setFont(new Font("����", Font.BOLD, 30));
		g.setColor(Color.RED);
		g.drawString("����ͣ", WIDTH/2, HEIGHT/2-150);
		
		g.setFont(new Font("����", Font.BOLD, tipConSize));
		g.setColor(tipColorCon);
		g.drawString("������Ϸ", WIDTH/2-50, HEIGHT/2-50);
		
		g.setFont(new Font("����", Font.BOLD, tipExitSize));
		g.setColor(tipColorExit);
		g.drawString("�˳���Ϸ", WIDTH/2-50, HEIGHT/2+50);
	}
	/*
	 * ����ؿ�
	 * ��������ؿ���ʾ
	 */
	public void customUp(Graphics g){
		if(isUpCustome==true){
			g.setFont(new Font("����", Font.BOLD, 100));
			g.setColor(Color.WHITE);
			g.drawString("�� "+custom+" ��", WIDTH/2-150, HEIGHT/2);
		}
	}
	
	/* ��Ϸʧ��
	 * ��������״̬��Ϸʧ��
	 */
	public void gameEnd(Graphics g){	
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/End.jpg"));
		g.drawImage(img, 0,0,WIDTH,HEIGHT,this);
		g.setFont(new Font("����", Font.BOLD, 100));
		g.setColor(Color.RED);
		g.drawString("��Ϸʧ��", WIDTH/2-150, HEIGHT/2-200);
		g.setFont(new Font("����", Font.BOLD, 80));
		g.setColor(Color.GREEN);
		g.drawString("����ؿ����� "+custom+" ��", WIDTH/2-250, HEIGHT/2-20);
		g.drawString("�÷֣�"+score+" ��", WIDTH/2-150, HEIGHT/2+90);
		g.setFont(new Font("����", Font.BOLD, 40));
		g.setColor(Color.YELLOW);
		g.drawString("�´��ٽ�����Ӵ��", WIDTH/2-150, HEIGHT/2+180);
		g.setFont(new Font("����", Font.BOLD, 40));
		g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		g.drawString("���� Enter/ESC�� ��ʼ/�˳���Ϸ������", WIDTH/2-250, HEIGHT-100);
		
	}
	/*
	 * ��Ϸʤ��
	 */
	public void gameWin(Graphics g){
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.getImage(SpaceMain.class.getClassLoader().getResource("image/Win.jpg"));
		g.drawImage(img, 0,0,WIDTH,HEIGHT,this);
		
		g.setFont(new Font("����", Font.BOLD, 100));

		g.setColor(Color.RED);
		g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		g.drawString("ʤ������ʤ������", WIDTH/2-350, HEIGHT/2-200);
		g.setFont(new Font("����", Font.BOLD, 60));
		g.setColor(Color.GREEN);
		g.drawString("����ؿ����� "+custom+" ��", WIDTH/2-250, HEIGHT/2-20);
		g.drawString("�÷֣�"+score+" ��", WIDTH/2-150, HEIGHT/2+90);
		g.setFont(new Font("����", Font.BOLD, 25));
		g.setColor(Color.YELLOW);
		g.drawString("��ϲ�����ɹ���������....", WIDTH/2-150, HEIGHT/2+130);
		g.drawString("�������ǵ����������....����..", WIDTH/2-160, HEIGHT/2+180);
		g.drawString("�����Է����䣺Struggle.dyh@qq.com,���߿����ߣ�����Ϸ̫easy����goodlock��", WIDTH/2-300, HEIGHT/2+220);
		g.setFont(new Font("����", Font.BOLD, 40));
		g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
		g.drawString("���� Enter/ESC�� ��ʼ/�˳���Ϸ������", WIDTH/2-250, HEIGHT-100);
	}
	/*
	 * �����÷���Ϣ
	 */
	public void drawInfo(Graphics g){
		g.setFont(new Font("����", Font.BOLD, 25));
		g.setColor(Color.GREEN);
		g.drawString("�����Ϣ", 10, 55);
		g.drawString("�÷֣�"+score+" ��", 150, 55);
		g.drawString("���Ѫ����",350, 55);
		g.drawRect(480, 30, 201, 31);
		g.setColor(Color.RED);
		g.fillRect(481, 31,hero_blood, 30);
		g.setColor(Color.GREEN);
		g.drawString(hero_blood+"", 550, 55);
		g.setFont(new Font("����", Font.BOLD, 25));
		g.setColor(Color.GREEN);
		g.drawString("�ؿ����� "+custom+" ��", 10, 100);
	}
	public static void main(String[] args) {
		new SpaceMain();
	}
}
