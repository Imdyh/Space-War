package com.dyh.space;

import java.io.InputStream;

import javazoom.jl.player.advanced.AdvancedPlayer;

public class GameSound extends Thread{
	private String mp3Url;
	private boolean isloop;
	
	//生成构造函数
	public GameSound(String mp3Url, boolean isloop) {
		super();
		this.mp3Url = mp3Url;
		this.isloop = isloop;
	}
	public void run() {
		super.run();
		try {	
			do{
				//加载音乐
				InputStream mp3= GameSound.class.getClassLoader().getResourceAsStream("music/"+mp3Url);
				//播放文件 调用播放函数
				AdvancedPlayer advancedPlayer  =new AdvancedPlayer(mp3);
				advancedPlayer.play();
			}while (isloop);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
