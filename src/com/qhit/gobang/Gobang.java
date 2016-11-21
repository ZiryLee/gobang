package com.qhit.gobang;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *  �ڶ���ʵ������
 *  ����ʵ����Ŀ --- ����������������Ϸ��
 *  ����ʵ�֣�
 *  	��1������ʺϵĴ��壬����������ͼƬ��������
 *		��2���ڰ����ӽ�������
 *		��3����������ܹ����Եػ��������̵����߽��㴦
 *		��4���ж�ʤ��
 *  ��ʵ�ֹ��ܣ�
 *		��1������
 *		��2������
 *		��3���Զ�����	
 *  ���ʱ�䣺2012��4��21��
 *  ��һ�θ���V1.0��2012��4��22����ע�ͣ����ı�����,�ж�ʤ���� 	
 *  �ڶ��θ���V1.1��2012�� 4��23����ע�ᰴť���ã�
 */
class Gobang extends Frame {
	private static final long serialVersionUID = 1L;
	
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image imageIco = toolkit.getImage("images/tubiao.png");									//�õ�ͼ��
	Image imageBG = toolkit.getImage("images/gobangBackground.jpg");	  					//�õ�����ͼ
	
	//��ά����ȷ���������꣨ǰ��άȷ��һ���㣬����ά��¼X��Y���꣩
	int[][][] coordXY = new int[17][17][3];		
												
	//��¼��������꣨���ֻ��578�����ӣ�
	int[] getXY = new int[578];	
																			
	//��¼�������������������
	int countPress = 0;				
																				
	//���ƺڰ���ɫ�����ӣ�trueΪ�ڣ�falseΪ�ף�
	boolean isBlackWhite = true;		
																	
	//�����������ʼ��ͼ
	boolean isPaint = false;																				
		
	public Gobang() {	
		super("LZR ������ ������ V1.1");	
		
		//���Ͻǵ�һ������
		int x = 18;						
		int y = 44;
		//��ʼ��ÿ�����꣬������25��
		for(int i=0; i < coordXY.length; i++, y+=25, x=18) {	
			for(int j=0; j < coordXY[i].length; j++, x+=25) {	
					coordXY[i][j][0] = x;
					coordXY[i][j][1] = y;
			}	
		}
		
		//ע�ᴰ����������رմ����¼���
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//ע������������������¼���
		addMouseListener(new myMouseListener());
		
		setBounds(500, 400, 435, 459);
		setIconImage( imageIco );																		//����ͼ��				
		setResizable(false);
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		//��������
		isBlackWhite = true;
		//���Ʊ���
		g.drawImage(imageBG, 2, 29, this);													
		
		if(isPaint) {	
			for(int i=0; i < countPress; i+=2) {
				//���ƺڰ����ӽ������
				if(isBlackWhite) {
						g.setColor(Color.black);
						isBlackWhite = false;
				} else {
						g.setColor(Color.white);
						isBlackWhite = true;
				}
				//�������ӣ�x��y�ֱ����ʵ�ֻ��������߽��㴦
				g.fillOval(coordXY[getXY[i+1]][getXY[i]][0]-7, 
										coordXY[getXY[i+1]][getXY[i]][1]-7, 15, 15);	
			}
		}
	}
	
	//��ֹ��˸����
	public void updata(Graphics g) {
		this.paint(g);
	}
	
	//�ڲ�����������������������¼���
	class myMouseListener extends MouseAdapter {
		//�������ʱ
		public void mousePressed(MouseEvent e) {
			//�õ��������������
			getXY[countPress] = (e.getX()-18+12) / 25;
			getXY[countPress+1] = (e.getY()-44+12) / 25;
			if(coordXY[getXY[countPress]][getXY[countPress+1]][2] < 1) {
				if(isBlackWhite) {
						coordXY[getXY[countPress]][getXY[countPress+1]][2] = 1;
						isBlackWhite = false;
						if( isWinLos(getXY[countPress], getXY[countPress+1],
								coordXY[getXY[countPress]][getXY[countPress+1]][2]) ) {
								new blackWin().setVisible(true);
						}
				} else {
						coordXY[getXY[countPress]][getXY[countPress+1]][2] = 2;
						isBlackWhite = true;
						if( isWinLos(getXY[countPress], getXY[countPress+1],
								coordXY[getXY[countPress]][getXY[countPress+1]][2]) ) {
								new whiteWin().setVisible(true);
						}
				}
			
			countPress += 2;
			//��ʼ��������
			isPaint = true;
			repaint();
			}

		}
	}
	//�ж���Ӯ
	public boolean isWinLos(int x, int y, int ChessColor) {
		int countChessColor = 1;
		
		//�ϱ�
		int L = 1, R = 1 ;
		if(y - L >= 0) {
			while(coordXY[x][y-L][2] == ChessColor) {
				if((++countChessColor) == 5) {
					return true;
				};
				L++;
				if(y - L < 0) {
					break;
				}
			}
		}
		//�±�
		if(y + R < 17) {
			while(coordXY[x][y+R][2] == ChessColor) {
				if((++countChessColor) == 5) {
					return true;
				};
				R++;
				if(y + R >= 17) {
					break;
				}
			}
		}
		//���
		countChessColor = 1;
		int U = 1, D = 1;
		if(x - U >= 0) {
			while(coordXY[x-U][y][2] == ChessColor) {
				if((++countChessColor) == 5) {
					return true;
				};
				U++;
				if(x - U < 0) {
					break;
				}
			}
		}
		//�ұ�
		if(x + D < 17) {
			while(coordXY[x+D][y][2] == ChessColor) {
				if((++countChessColor) == 5) {
					return true;
				};
				D++;
				if(x + D >= 17) {
					break;
				}
			}
		}
		//����
		countChessColor = 1;
		int LU = 1, LD = 1;
		if(x - LU >= 0 && y - LU >=0) {
			while(coordXY[x-LU][y-LU][2] == ChessColor) {
				if((++countChessColor) == 5) {
					return true;
				};
				LU++;
				if(x - LU < 0 || y - LU < 0) {
					break;
				}
			}
		}
		//����
		if(x + LD < 17 && y + LD < 17) {
			while(coordXY[x+LD][y+LD][2] == ChessColor) {
				if((++countChessColor) == 5) {
					return true;
				};
				LD++;
				if(x + LD >= 17 || y + LD >= 17) {
					break;
				}
			}
		}
		//����
		countChessColor = 1;
		int RU = 1, RD = 1;
		if(x + RU < 17 && y - RU >= 0) {
			while(coordXY[x+RU][y-RU][2] == ChessColor) {
				if((++countChessColor) == 5) {
					return true;
				};
				RU++;
				if(x + RU >=17 || y - RU < 0) {
					break;
				}
			}
		}
		//����
		if(x - RD >=0 && y + RD < 17) {
			while(coordXY[x-RD][y+RD][2] == ChessColor) {
				if((++countChessColor) == 5) {
					return true;
				};
				RD++;
				if(x - RD < 0 || y + RD >= 17) {
					break;
				}
			}
		}
		return false;
	}
	
	// �ڲ��࣬�����ʤ�����δ���
	class blackWin extends Frame {
		private static final long serialVersionUID = 1L;
		Panel center = new Panel();
		Panel south = new Panel();
		Button btEnter = new Button("ȷ��");
		Label lbWin = new Label("��ϲ�ڷ���ʤ",Label.CENTER);
		blackWin() {
			lbWin.setFont( new Font("����",Font.BOLD,25) );
			center.add(lbWin);
			south.add(btEnter);
			setBounds(542, 450, 350, 120);
			add(center, BorderLayout.CENTER);
			add(south,BorderLayout.SOUTH);
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
			});
			//ע�ᰴť��������ʵ�����ã�
			btEnter.addActionListener(new Reset(this));
		}
		
	}
	
	//�ڲ�������ʤ�����˴���	  
	class whiteWin extends Frame {

		private static final long serialVersionUID = 1L;
		Panel center = new Panel();
		Panel south = new Panel();
		Button btEnter = new Button("ȷ��");
		Label lbLos = new Label("��ϲ�׷���ʤ",Label.CENTER);
		whiteWin() {
			lbLos.setFont( new Font("����",Font.BOLD,25) );
			center.add(lbLos);
			south.add(btEnter);
			setBounds(542, 450, 350, 120);
			add(center, BorderLayout.CENTER);
			add(south,BorderLayout.SOUTH);
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
			});
			//ע�ᰴť��������ʵ�����ã�
			btEnter.addActionListener(new Reset(this));
		}
	}
	
	//��ť��������ʵ�����ã�
	class Reset implements ActionListener {
		Frame f ;
		Reset(Frame f) {
			this.f = f;
		}
		
		public void actionPerformed(ActionEvent e) {
			countPress = 0;
			isPaint = false;
			isBlackWhite = true;
			for(int i=0; i < coordXY.length; i++) {	
				for(int j=0; j < coordXY[i].length; j++) {	
						coordXY[i][j][2] = 0;
				}	
			}
			repaint();
			f.setVisible(false);
		}
	}
	
}