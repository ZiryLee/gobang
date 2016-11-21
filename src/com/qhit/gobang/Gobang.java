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
 *  第二章实践部分
 *  本章实践项目 --- 《单机版五子棋游戏》
 *  程序实现：
 *  	（1）设计适合的窗体，并加载棋盘图片到窗体中
 *		（2）黑白棋子交互绘制
 *		（3）点击棋子能够定性地绘制在棋盘的两线交点处
 *		（4）判断胜负
 *  待实现功能：
 *		（1）悔棋
 *		（2）重置
 *		（3）自动下棋	
 *  完成时间：2012，4，21。
 *  第一次更新V1.0：2012，4，22。（注释，更改变量名,判断胜负） 	
 *  第二次更新V1.1：2012， 4，23。（注册按钮重置）
 */
class Gobang extends Frame {
	private static final long serialVersionUID = 1L;
	
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image imageIco = toolkit.getImage("images/tubiao.png");									//得到图标
	Image imageBG = toolkit.getImage("images/gobangBackground.jpg");	  					//得到背景图
	
	//三维数组确定棋子坐标（前二维确定一个点，第三维记录X，Y坐标）
	int[][][] coordXY = new int[17][17][3];		
												
	//记录点击的坐标（最多只有578个棋子）
	int[] getXY = new int[578];	
																			
	//记录鼠标点击次数（棋子数）
	int countPress = 0;				
																				
	//控制黑白颜色的棋子（true为黑，false为白）
	boolean isBlackWhite = true;		
																	
	//控制鼠标点击后开始绘图
	boolean isPaint = false;																				
		
	public Gobang() {	
		super("LZR 单机版 五子棋 V1.1");	
		
		//左上角第一个坐标
		int x = 18;						
		int y = 44;
		//初始化每格坐标，坐标间隔25。
		for(int i=0; i < coordXY.length; i++, y+=25, x=18) {	
			for(int j=0; j < coordXY[i].length; j++, x+=25) {	
					coordXY[i][j][0] = x;
					coordXY[i][j][1] = y;
			}	
		}
		
		//注册窗体监听器（关闭窗口事件）
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//注册鼠标监听器（鼠标点击事件）
		addMouseListener(new myMouseListener());
		
		setBounds(500, 400, 435, 459);
		setIconImage( imageIco );																		//设置图标				
		setResizable(false);
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		//黑棋先下
		isBlackWhite = true;
		//绘制背景
		g.drawImage(imageBG, 2, 29, this);													
		
		if(isPaint) {	
			for(int i=0; i < countPress; i+=2) {
				//控制黑白棋子交替绘制
				if(isBlackWhite) {
						g.setColor(Color.black);
						isBlackWhite = false;
				} else {
						g.setColor(Color.white);
						isBlackWhite = true;
				}
				//绘制棋子，x，y分别减七实现绘制在两线交点处
				g.fillOval(coordXY[getXY[i+1]][getXY[i]][0]-7, 
										coordXY[getXY[i+1]][getXY[i]][1]-7, 15, 15);	
			}
		}
	}
	
	//防止闪烁现象
	public void updata(Graphics g) {
		this.paint(g);
	}
	
	//内部类鼠标监听器（处理鼠标点击事件）
	class myMouseListener extends MouseAdapter {
		//当鼠标点击时
		public void mousePressed(MouseEvent e) {
			//得到最近的网格坐标
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
			//开始绘制棋子
			isPaint = true;
			repaint();
			}

		}
	}
	//判断输赢
	public boolean isWinLos(int x, int y, int ChessColor) {
		int countChessColor = 1;
		
		//上边
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
		//下边
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
		//左边
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
		//右边
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
		//左上
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
		//左下
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
		//右上
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
		//右下
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
	
	// 内部类，黑棋获胜弹出次窗口
	class blackWin extends Frame {
		private static final long serialVersionUID = 1L;
		Panel center = new Panel();
		Panel south = new Panel();
		Button btEnter = new Button("确定");
		Label lbWin = new Label("恭喜黑方获胜",Label.CENTER);
		blackWin() {
			lbWin.setFont( new Font("宋体",Font.BOLD,25) );
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
			//注册按钮监听器（实现重置）
			btEnter.addActionListener(new Reset(this));
		}
		
	}
	
	//内部类白棋获胜弹出此窗口	  
	class whiteWin extends Frame {

		private static final long serialVersionUID = 1L;
		Panel center = new Panel();
		Panel south = new Panel();
		Button btEnter = new Button("确定");
		Label lbLos = new Label("恭喜白方获胜",Label.CENTER);
		whiteWin() {
			lbLos.setFont( new Font("宋体",Font.BOLD,25) );
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
			//注册按钮监听器（实现重置）
			btEnter.addActionListener(new Reset(this));
		}
	}
	
	//按钮监听器（实现重置）
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