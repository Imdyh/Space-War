package com.dyh.space;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

import com.dyh.main.SpaceMain;

public class KeyDown extends KeyAdapter{
	
	//����
	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		
		switch (SpaceMain.state) {
		case START:
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				SpaceMain.state=GameState.RUN;
			}
			//��Ϣ��ʾ��
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
				int result =JOptionPane.showConfirmDialog(null, "ȷ���˳���Ϸ��", "��ʾ",
						JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(result==0){
					System.exit(0);
				}
			}
			break;
		case RUN:
			if(e.getKeyCode()>=65&&e.getKeyCode()<=90||e.getKeyCode()>=97&&e.getKeyCode()<=122){
				SpaceMain.keyDownValue=e.getKeyChar();
				SpaceMain.isInput=true;
			}
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
				SpaceMain.state=GameState.STOP;
			}
			break;
		case STOP:
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
				SpaceMain.state=GameState.RUN;
			}
			//ѡ�������Ϸindex=0
			if(e.getKeyCode()==KeyEvent.VK_DOWN){
				if(SpaceMain.indexChose==0){
					SpaceMain.tipColorCon=Color.WHITE;
					SpaceMain.tipColorExit=Color.YELLOW;
					SpaceMain.tipConSize=50;
					SpaceMain.tipExitSize=60;
					SpaceMain.indexChose=1;
				}
			}
			//�˳���Ϸindex=1
			if(e.getKeyCode()==KeyEvent.VK_UP){
				if(SpaceMain.indexChose==1){
					SpaceMain.tipColorExit=Color.WHITE;
					SpaceMain.tipColorCon=Color.YELLOW;
					SpaceMain.tipConSize=60;
					SpaceMain.tipExitSize=50;
					SpaceMain.indexChose=0;
				}
			}
			//����enter��
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				if(SpaceMain.indexChose==0){
					SpaceMain.state=GameState.RUN;

				}else {
					SpaceMain.resetDate();
					SpaceMain.state=GameState.START;
				}
			}
			break;
		case END:
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				SpaceMain.resetDate();
				SpaceMain.state=GameState.START;
			}
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
				int result =JOptionPane.showConfirmDialog(null, "ȷ���˳���Ϸ��", "��ʾ",
						JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(result==0){
					System.exit(0);
				}
			}
			break;
		case WIN:
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				SpaceMain.resetDate();
				SpaceMain.state=GameState.START;
			}
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
				int result =JOptionPane.showConfirmDialog(null, "ȷ���˳���Ϸ��", "��ʾ",
						JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(result==0){
					System.exit(0);
				}
			}
			break;
		default:
			break;
		}
	}
	
	//�ɿ�
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO �Զ����ɵķ������
		super.keyReleased(e);
		if(SpaceMain.state==GameState.RUN){
			//SpaceMain.keyDownValue='\0';
			SpaceMain.isInput=false;
		}
	}
}
