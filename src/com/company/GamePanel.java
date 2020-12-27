package com.company;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Random;
import java.io.FileReader;
import java.io.*;
import javax.sound.sampled.AudioInputStream;

public class GamePanel extends JPanel implements ActionListener {

//------
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int piesEaten = 0;
    int highScore = 0;
    String highScoreFile = "";
    int pieX;
    int pieY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    static boolean gameOn = false;
	JTextField txtInput = new JTextField("");
	getTextField TF;

	boolean start = true;

	int change = 0;
	int piesTemp = 0;
	String name = "";
	meMusic music;
	String what = "";


// for GUI purposes 
	ImageIcon pie = new ImageIcon("assets/Pie.png");
	ImageIcon gameoverimg = new ImageIcon("assets/gameover.png");
	ImageIcon helpScreen = new ImageIcon("assets/helpScreen.png");
	ImageIcon settings = new ImageIcon("assets/settings.png");
	ImageIcon rainbowe = new ImageIcon("assets/rainbow.jpg");
	ImageIcon mikmik = new ImageIcon("assets/Mascot.png");

	private enum STATE{  //helps differentiate the menu screens from the actual game
		MENU, GAME
	};

	private STATE State = STATE.MENU;

	private enum SCREEN{ //notes what screen one is on as of the moment
		START, HELP, SETTINGS, GAME, END
	};

	private SCREEN Screen = SCREEN.START;

// for customizations purposes

	private enum COLORS{ //for custom colors
		RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, WHITE, BLACK, CRAZY, DEFAULT
	};

	private COLORS color = COLORS.DEFAULT;

	private Color snekbodeh = new Color(214, 57, 2);
	private Color snekhead = new Color(189, 42, 0);

	private enum MODE{ //difficulty in game
		EASY/* 90 */, NORMAL/* 75 */, HARD /* 55 */,NIGHTMARE /*35*/
	}

	private MODE difficulty = MODE.NORMAL;

//buttons

	//in starting screen
	public Rectangle playButton = new Rectangle(SCREEN_WIDTH/4, 330, 100, 50);
	public Rectangle startquitButton = new Rectangle(SCREEN_WIDTH/4 * 2 + 50, 330, 100, 50);
	public Rectangle helpButton = new Rectangle(5, SCREEN_HEIGHT-35, 30, 30);
	public Rectangle customizeButton = new Rectangle(40, SCREEN_HEIGHT-35, 30, 30);
	//in end screen
	public Rectangle replayButton = new Rectangle(SCREEN_WIDTH/4, 430, 100, 50);
	public Rectangle endquitButton = new Rectangle(SCREEN_WIDTH/4 * 2 + 50, 430, 100, 50);
	//in settings screen
	public Rectangle backButton = new Rectangle(5,5, 30, 30);

	//colors
	public Rectangle defColor = new Rectangle(40,120, 30, 30);
	public Rectangle redColor = new Rectangle(120,120, 30, 30);
	public Rectangle oraColor = new Rectangle(200,120, 30, 30);
	public Rectangle yelColor = new Rectangle(280,120, 30, 30);
	public Rectangle greColor = new Rectangle(360,120, 30, 30);
	public Rectangle bluColor = new Rectangle(40,190, 30, 30);
	public Rectangle purColor = new Rectangle(120,190, 30, 30);
	public Rectangle whiColor = new Rectangle(200,190, 30, 30);
	public Rectangle blaColor = new Rectangle(280,190, 30, 30);
	public Rectangle CCBColor = new Rectangle(360,190, 30, 30);

	//difficulty
	public Rectangle easyButton = new Rectangle(40, 350, 110, 30);
	public Rectangle normalButton = new Rectangle(220, 350, 110, 30);
	public Rectangle hardButton = new Rectangle(40, 400, 110, 30);
	public Rectangle nightmareButton = new Rectangle(220, 400, 110, 30);

//-------------------

//constructor
    GamePanel() {
		TF = new getTextField();
    	random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(21, 23, 21));
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());//for the direction of snake
        this.addMouseListener(new MouseInput());//for the buttons
		music = new meMusic();


        startGame();
    }

    public void startGame() {
        newPie();
		running = true;
        timer = new Timer(DELAY, this);

        timer.start();
    }

    public void pause() {
        GamePanel.gameOn = true;
        timer.stop();
    }

    public void resume() {
        GamePanel.gameOn = false;
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

		if(State == STATE.GAME){

			music.introSoundStop();

        	if (running) {
				/*
				//this for loop displays a grid sa background
				
				for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
					g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
		  			g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
	  			}

            	g.setColor(Color.red);
           		g.fillOval(pieX, pieY, UNIT_SIZE, UNIT_SIZE);
				*/
				
            	g.drawImage(pie.getImage(),pieX,pieY,25,25,null); // makes image of pie

            	for (int i = 0; i < bodyParts; i++) {
               		if (i == 0) {
						if(color == COLORS.CRAZY){
							g.setColor(new Color(10,10,10));
						}else{
							g.setColor(snekhead);
						}
                    	g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                	} else {
						if(color == COLORS.CRAZY){
							g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
						}else{
							g.setColor(snekbodeh);
						}
                    	g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                	}
            	}

            	g.setColor(new Color(201, 201, 201));
            	g.setFont(new Font("Arial", Font.BOLD, 23));
            	FontMetrics metrics = getFontMetrics(g.getFont());
            	g.drawString("Score: " + piesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + piesEaten)) / 2, g.getFont().getSize());

        	} else {
				gameOver(g);

				music.introSoundStop();
				music.inGameSoundStop();

				music.gameOverSound();
				music.gameOverPlay();
				
        	}

        }else if(State == STATE.MENU){

			switch(Screen){
				case START:
					if(start == true){
						music.introSound();
						music.menuPlay();
						start = false;
					}
					startMenu(g);
					break;
				case HELP:
					pause();
					g.drawImage(helpScreen.getImage(),0,0,600,600,null);
					break;
				case SETTINGS:
					pause();
					settingsScreen(g);
					break;
			}


		}
    }

    public void newPie() {
        pieX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        pieY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkPie() { // this checks if the Pie is "eaten"
        if ((x[0] == pieX) && (y[0] == pieY)) {
            bodyParts++;
            piesEaten++;
            newPie();
        }
    }

    public void checkCollisions() {
        // checks if head collides body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // check if head touches left border
        if (x[0] < 0) {
            running = false;
        }

        // check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }

        // check if head touches top border
        if (y[0] < 0) {
            running = false;
        }

        // check if head touches bottom
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }

	}
	
//------barrier-------//



	public void checkHighScore(){
    	// for file handling
		//checks if score is new highscore
		if(highScoreFile.equals("")){
			highScoreFile = this.getHighScore();
		}

		if(piesEaten > highScore){
			highScore = piesEaten;
		}
		if(piesEaten > Integer.parseInt((highScoreFile.split(" : ")[1]))){
			piesTemp = piesEaten;
			change = 0;
//			JOptionPane pane = new JOptionPane();
//			name = pane.showInputDialog( "You set a new Record! What is your Piethon's Name?");
//			name = (name != null && !name.isEmpty()) ? name : "Unknown";

//			System.exit(0);

			TF.niceMe();
//			while (!TF.Clicked()) {
//			}
			name = TF.name;
			pause();

			if (name != null) {
				highScoreFile = name + " : " + piesEaten;

				File scoreFile = new File("highScore.dat");
				if(!scoreFile.exists()){
					try {
						scoreFile.createNewFile();
					}
					catch (IOException e){
						e.printStackTrace();
					}
				}
				FileWriter writeFile = null;
				BufferedWriter writer = null;
				try{
					writeFile = new FileWriter(scoreFile);
					writer = new BufferedWriter(writeFile);
					writer.write(this.highScoreFile);
				}
				catch (Exception e){
					//if error
				}
				finally {
					try{
						if(writer!=null){
							writer.close();
						}

					}
					catch (Exception e) {}
				}
				
//				System.exit(0);
//				startGame();

				/*
				 *
				 * either startGame(); or System.exit(0);
				 *
				 * if startGame() kay mo freeze siya ngadto sa Mainpage, pero at least wala nay glitch.
				 * sa System.out(0) kay auto close ang game after input sa user.
				 */
			}
		} else {
			change++;
			name = TF.name;

			if (name.isEmpty()) {
				highScoreFile = getHighScore();
			} else {
				highScoreFile = name + " : " + highScore;
			}
		}
	}

    public String getHighScore(){ // retreives the highest score

    	if (!TF.name.isEmpty())
			return TF.name;

		FileReader readFile = null;
		BufferedReader reader = null;
			try{
				readFile = new FileReader("highScore.dat");
				reader = new BufferedReader(readFile);
				return reader.readLine();
			}
			catch (Exception e){
				return "None : 0";
			}
			finally {
				try {
					if(reader != null){
					reader.close();
				}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
	}

    public void gameOver(Graphics g){
        // Game Over Text
		State = STATE.MENU;
		Screen = SCREEN.END;

        Graphics2D g2d = (Graphics2D) g;

        Image hulu=gameoverimg.getImage();
        Image modifiedhulu= hulu.getScaledInstance(480,170,java.awt.Image.SCALE_SMOOTH);
		gameoverimg=new ImageIcon(modifiedhulu);

		g.drawImage(gameoverimg.getImage(),120,40,null);

        g.setColor(new Color(201, 201, 201));
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, (SCREEN_HEIGHT / 2)-20);

		getHighScore();
		checkHighScore();

        g.setColor(new Color(201, 201, 201));
        g.setFont(new Font("Arial", Font.BOLD, 23));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Your Score: " + piesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Your Score: " + piesEaten)) / 2, (SCREEN_HEIGHT / 3 * 2)-80);



		if (change != 0)
        	g.drawString("High Score: " + highScoreFile, (SCREEN_WIDTH - metrics1.stringWidth("High Score: " + highScoreFile)) / 2, (SCREEN_HEIGHT / 3 * 2)-40);

        g.setColor(new Color(201, 201, 201));
		g.setFont(new Font("Arial", Font.BOLD, 35));
		g.drawString("Play",replayButton.x + 16,replayButton.y + 33);
		g2d.draw(replayButton);
		g.drawString("Quit",endquitButton.x + 16,endquitButton.y + 33);
		g2d.draw(endquitButton);
		g.drawString("M",helpButton.x+1,helpButton.y+28);
		g2d.draw(helpButton);
    }

    public void startMenu(Graphics g){

		Graphics2D g2d = (Graphics2D) g;

		pause();


		//clip_in.start();

		g.setColor(new Color(201, 201, 201));
		g.setFont(new Font("Arial", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("PIETHON", (SCREEN_WIDTH - metrics.stringWidth("PIETHON")) / 2, SCREEN_HEIGHT / 3);

		g.drawImage(mikmik.getImage(),260,0,500,550,null);
		g.drawImage(pie.getImage(),60,400,200,120,null);

		getHighScore();
		checkHighScore();

        g.setColor(new Color(201, 201, 201));
        g.setFont(new Font("Arial", Font.BOLD, 23));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("High Score: " + highScoreFile, (SCREEN_WIDTH - metrics1.stringWidth("High Score: " + highScoreFile)) / 2, (SCREEN_HEIGHT / 3)+50);

		g.setColor(new Color(201, 201, 201));
		g.setFont(new Font("Arial", Font.BOLD, 35));
		g.drawString("Play",playButton.x + 16,playButton.y + 38);
		g2d.draw(playButton);
		g.fillRect(startquitButton.x+1, startquitButton.y+1, 99, 49);
		
		g.drawString("?",helpButton.x+5,helpButton.y+28);
		g2d.draw(helpButton);
		g.drawImage(settings.getImage(),customizeButton.x+3,customizeButton.y+3,25,25,null);
		g2d.draw(customizeButton);

		g.setColor(new Color(20, 20, 20));
		g.drawString("Quit",startquitButton.x + 16,startquitButton.y + 38);
		g2d.draw(startquitButton);

	}

	public void settingsScreen(Graphics g){
		//button for back
		//button for colors
		Graphics2D g2d = (Graphics2D) g;
		int x,y;

		switch(difficulty){
			case EASY:
			g.drawImage(pie.getImage(),easyButton.x+120,easyButton.y,30,30,null);
			break;

			case NORMAL:
			g.drawImage(pie.getImage(),normalButton.x+120,normalButton.y,30,30,null);
			break;

			case HARD:
			g.drawImage(pie.getImage(),hardButton.x+120,hardButton.y,30,30,null);
			break;

			case NIGHTMARE:
			g.drawImage(pie.getImage(),nightmareButton.x+120,nightmareButton.y,30,30,null);
			break;
		}

		g.setColor(snekhead);
		g.fillRect(470,100,50,50);
		g.setColor(snekbodeh);
		g.fillRect(470,150,50,350);


		g.setColor(new Color(201, 201, 201));
		g.setFont(new Font("Arial", Font.BOLD, 35));
		g.drawString("B",backButton.x + 3,backButton.y + 28);
		g2d.draw(backButton);
		
		g.drawString("Color of Snekk",45,75);
		g.drawString("Difficulty",45,320);

		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("Def",defColor.x,defColor.y-3);
		g2d.draw(defColor);
		g.drawString("Red",redColor.x-2,redColor.y-3);
		g2d.draw(redColor);
		g.drawString("Orange",oraColor.x-20,oraColor.y-3);
		g2d.draw(oraColor);
		g.drawString("Yellow",yelColor.x-15,yelColor.y-3);
		g2d.draw(yelColor);
		g.drawString("Green",greColor.x-15,greColor.y-3);
		g2d.draw(greColor);
		g.drawString("Blue",bluColor.x-6,bluColor.y-3);
		g2d.draw(bluColor);
		g.drawString("Purple",purColor.x-10,purColor.y-3);
		g2d.draw(purColor);
		g.drawString("White",whiColor.x-10,whiColor.y-3);
		g2d.draw(whiColor);
		g.drawString("Black",blaColor.x-10,blaColor.y-3);
		g2d.draw(blaColor);
		g.drawString("Crazy",CCBColor.x-10,CCBColor.y-3);
		g2d.draw(CCBColor);

		g.drawString("Easy",easyButton.x + 5,easyButton.y + 22);
		g2d.draw(easyButton);
		g.drawString("Normal",normalButton.x + 5,normalButton.y + 22);
		g2d.draw(normalButton);
		g.drawString("Hard",hardButton.x + 5,hardButton.y + 22);
		g2d.draw(hardButton);
		g.drawString("Nightmare",nightmareButton.x + 5,nightmareButton.y + 22);
		g2d.draw(nightmareButton);

		g.setColor(new Color(214, 57, 2));
		g.fillRect(defColor.x+1,defColor.y+1,29,29);
		g.setColor(Color.RED);
		g.fillRect(redColor.x+1,redColor.y+1,29,29);
		g.setColor(Color.ORANGE);
		g.fillRect(oraColor.x+1,oraColor.y+1,29,29);
		g.setColor(Color.YELLOW);
		g.fillRect(yelColor.x+1,yelColor.y+1,29,29);
		g.setColor(Color.GREEN);
		g.fillRect(greColor.x+1,greColor.y+1,29,29);
		g.setColor(Color.BLUE);
		g.fillRect(bluColor.x+1,bluColor.y+1,29,29);
		g.setColor(new Color(106, 13, 173));
		g.fillRect(purColor.x+1,purColor.y+1,29,29);
		g.setColor(Color.WHITE);
		g.fillRect(whiColor.x+1,whiColor.y+1,29,29);
		g.setColor(Color.BLACK);
		g.fillRect(blaColor.x+1,blaColor.y+1,29,29);
		g.drawImage(rainbowe.getImage(),CCBColor.x+1,CCBColor.y+1,29,29,null);
		
		//button for mode
			
	}

	public void snekColor(){
		
		switch(color){
			case DEFAULT:
				snekbodeh = new Color(214, 57, 2);
				snekhead = new Color(189, 42, 0);
				break;
			case RED:
				snekbodeh = Color.RED;
				snekhead = new Color(225, 20, 20);
				break;
			case ORANGE:
				snekbodeh = Color.ORANGE;
				snekhead = new Color(240, 50, 0);
				break;
			case YELLOW:
				snekbodeh = Color.YELLOW;
				snekhead = new Color(225, 225, 20);
				break;
			case GREEN:
				snekbodeh = Color.GREEN;
				snekhead = new Color(20, 225, 20);
				break;
			case BLUE:
				snekbodeh = Color.BLUE;
				snekhead = new Color(20, 20, 225);
				break;
			case PURPLE:
				snekbodeh = new Color(106, 13, 173);
				snekhead = new Color(36, 0, 99);
				break;
			case WHITE:
				snekbodeh = Color.WHITE;
				snekhead = new Color(240, 240, 240);
				break;
			case BLACK:
				snekbodeh = Color.BLACK;
				snekhead = new Color(15, 15, 15);
				break;
			case CRAZY:
				snekbodeh = Color.CYAN;
				snekhead = new Color(10, 10, 10);
				break;
		}

	}

	public void setMode(){
		switch(difficulty){
			case EASY:
				DELAY = 90;
				break;
			case NORMAL:
				DELAY = 75;
				break;
			case HARD:
				DELAY = 55;
				break;
			case NIGHTMARE:
				DELAY = 35;
				break;
		}
	}

	public class MouseInput implements MouseListener{

		public void mousePressed(MouseEvent e){
			int mx = e.getX();
			int my = e.getY();

			/*
					public Rectangle playButton = new Rectangle(SCREEN_WIDTH/4, 330, 100, 50);
					public Rectangle startquitButton = new Rectangle(SCREEN_WIDTH/4 * 2 + 50, 330, 100, 50);
					public Rectangle replayButton = new Rectangle(SCREEN_WIDTH/4, 430, 100, 50);
					public Rectangle endquitButton = new Rectangle(SCREEN_WIDTH/4 * 2 + 50, 430, 100, 50);
					public Rectangle helpButton = new Rectangle(5, SCREEN_HEIGHT-35, 30, 30);
					public Rectangle customizeButton = new Rectangle(40, SCREEN_HEIGHT-35, 30, 30);

					the following code basically means "if mouse is clicked in this area of the screen"
			*/
			switch(Screen){


				case START:

				//playButton
					if(mx >= SCREEN_WIDTH/4 && mx <= SCREEN_WIDTH/4 + 100){
						if(my >= 330 && my <= 380){
							//pressed playButton
							State = STATE.GAME;
							Screen = SCREEN.GAME;
							int gig = 6;

							for(int i=0;i<6;i++){
								gig--;
								x[i]=gig*UNIT_SIZE;  y[i]=0;
							}

							bodyParts = 6;
							piesEaten = 0;
							direction = 'R';

							music.introSoundStop();
							music.inGameSound();
							music.inGamePlay();
							music.inGamePlayLoop();



							repaint();
							startGame();
						}
					}

				//quitButton
					if(mx >= (SCREEN_WIDTH/4 * 2 + 50) && mx <= (SCREEN_WIDTH/4 * 2 + 50) + 100){
						if(my >= 330 && my <= 380){
							//pressed quitButton

							if (name.isEmpty() && piesTemp == 0)
								System.exit(0);
							if (!name.isEmpty()) {
								name = TF.name;
								if (piesEaten == 0)
									highScoreFile = name + " : " + piesTemp;

								File scoreFile = new File("highScore.dat");
								if(!scoreFile.exists()){
									try {
										scoreFile.createNewFile();
									}
									catch (IOException ee){
										ee.printStackTrace();
									}
								}
								FileWriter writeFile = null;
								BufferedWriter writer = null;
								try{
									writeFile = new FileWriter(scoreFile);
									writer = new BufferedWriter(writeFile);
									writer.write(highScoreFile);
								}
								catch (Exception ee){
									//if error
								}
								finally {
									try {
										if (writer != null) {
											writer.close();
										}

									} catch (Exception ee) {
									}
								}
							}
							System.exit(0);
						}
					}

				//helpButton
					if(mx >= 5 && mx <= 35){
						if(my >= (SCREEN_HEIGHT-35) && my <= (SCREEN_HEIGHT-35) + 30){
							Screen = SCREEN.HELP;
							startGame();
						}
					}

				//settingsButton
					if(mx >= 40 && mx <= 70){
						if(my >= (SCREEN_HEIGHT-35) && my <= (SCREEN_HEIGHT-35) + 30){
							Screen = SCREEN.SETTINGS;
							startGame();
						}
					}

				break;

				case END:
				//replayButton
					if(mx >= SCREEN_WIDTH/4 && mx <= SCREEN_WIDTH/4 + 100){

						if(my >= 430 && my <= 480){
							//pressed playButton
							State = STATE.GAME;
							Screen = SCREEN.GAME;
							int gig = 6;

							for(int i=0;i<6;i++){
								gig--;
								x[i]=gig*UNIT_SIZE;  y[i]=0;
							}

							bodyParts = 6;
							piesEaten = 0;
							direction = 'R';

							music.introSoundStop();
							music.inGameSound();
							music.inGamePlay();
							music.inGamePlayLoop();

							repaint();
							startGame();
						}
					}

				//endquitButton
					if(mx >= (SCREEN_WIDTH/4* 2 + 50) && mx <= (SCREEN_WIDTH/4* 2 + 50) + 100){

						if(my >= 430 && my <= 480){
							//pressed quitButton
							if (name.isEmpty() && piesTemp == 0)
								System.exit(0);
							if (!TF.name.isEmpty()) {
								name = TF.name;

								highScoreFile = name + " : " + piesTemp;

								File scoreFile = new File("highScore.dat");
								if(!scoreFile.exists()){
									try {
										scoreFile.createNewFile();
									}
									catch (IOException ee){
										ee.printStackTrace();
									}
								}
								FileWriter writeFile = null;
								BufferedWriter writer = null;
								try{
									writeFile = new FileWriter(scoreFile);
									writer = new BufferedWriter(writeFile);
									writer.write(highScoreFile);
								}
								catch (Exception ee){
									//if error
								}
								finally {
									try {
										if (writer != null) {
											writer.close();
										}

									} catch (Exception ee) {
									}
								}
							}
							System.exit(1);
						}
					}
				//MainMenuButton
					if(mx >= 5 && mx <= 35){
						if(my >= (SCREEN_HEIGHT-35) && my <= (SCREEN_HEIGHT-35) + 30){
							Screen = SCREEN.START;
							startGame();
						}
					}
				
				break;

				case HELP:
					
					if(mx >= 20 && mx <= 100){
						if(my >= 20 && my <= 100){
							Screen = SCREEN.START;
							startGame();
						}
					}

				break;


				/*
					//colors
					public Rectangle defColor = new Rectangle(40,120, 30, 30);
					public Rectangle redColor = new Rectangle(120,120, 30, 30);
					public Rectangle oraColor = new Rectangle(200,120, 30, 30);
					public Rectangle yelColor = new Rectangle(280,120, 30, 30);
					public Rectangle greColor = new Rectangle(360,120, 30, 30);
					public Rectangle bluColor = new Rectangle(40,190, 30, 30);
					public Rectangle purColor = new Rectangle(120,190, 30, 30);
					public Rectangle whiColor = new Rectangle(200,190, 30, 30);
					public Rectangle blaColor = new Rectangle(280,190, 30, 30);
					public Rectangle CCBColor = new Rectangle(360,190, 30, 30);
					
					//difficulty
					public Rectangle easyButton = new Rectangle(40, 350, 110, 30);
					public Rectangle normalButton = new Rectangle(220, 350, 110, 30);
					public Rectangle hardButton = new Rectangle(40, 400, 110, 30);
					public Rectangle nightmareButton = new Rectangle(220, 400, 110, 30);
				*/

				case SETTINGS:
					/*
						public Rectangle backButton = new Rectangle(5,5, 30, 30);
					*/
					if(mx >= 5 && mx <= 35){
						if(my >= 5 && my <= 25){
							Screen = SCREEN.START;
							startGame();
						}
					}
					//defaultColor
					if(mx >= 40 && mx <= 70){
						if(my >= 120 && my <= 150){
							color = COLORS.DEFAULT;
							snekColor();
							startGame();
						}
					}
					//red
					if(mx >= 120 && mx <= 150){
						if(my >= 120 && my <= 150){
							color = COLORS.RED;
							snekColor();
							startGame();
						}
					}
					//orange
					if(mx >= 200 && mx <= 230){
						if(my >= 120 && my <= 150){
							color = COLORS.ORANGE;
							snekColor();
							startGame();
						}
					}
					//yellow
					if(mx >= 280 && mx <= 310){
						if(my >= 120 && my <= 150){
							color = COLORS.YELLOW;
							snekColor();
							startGame();
						}
					}
					//green
					if(mx >= 360 && mx <= 390){
						if(my >= 120 && my <= 150){
							color = COLORS.GREEN;
							snekColor();
							startGame();
						}
					}
					//blue
					if(mx >= 40 && mx <= 70){
						if(my >= 190 && my <= 220){
							color = COLORS.BLUE;
							snekColor();
							startGame();
						}
					}
					//purple
					if(mx >= 120 && mx <= 150){
						if(my >= 190 && my <= 220){
							color = COLORS.PURPLE;
							snekColor();
							startGame();
						}
					}
					//white
					if(mx >= 200 && mx <= 230){
						if(my >= 190 && my <= 220){
							color = COLORS.WHITE;
							snekColor();
							startGame();
						}
					}
					//black
					if(mx >= 280 && mx <= 310){
						if(my >= 190 && my <= 220){
							color = COLORS.BLACK;
							snekColor();
							startGame();
						}
					}
					//crazy
					if(mx >= 360 && mx <= 390){
						if(my >= 190 && my <= 220){
							color = COLORS.CRAZY;
							snekColor();
							startGame();
						}
					}
					
					/**
					 public Rectangle easyButton = new Rectangle(40, 350, 110, 30);
					public Rectangle normalButton = new Rectangle(220, 350, 110, 30);
					public Rectangle hardButton = new Rectangle(40, 400, 110, 30);
					public Rectangle nightmareButton = new Rectangle(220, 400, 110, 30);
					 */

					if(mx >= 40 && mx <= 150){
						if(my >= 350 && my <= 380){
							difficulty = MODE.EASY;
							setMode();
							startGame();
						}
					}

					if(mx >= 220 && mx <= 330){
						if(my >= 350 && my <= 380){
							difficulty = MODE.NORMAL;
							setMode();
							startGame();
						}
					}

					if(mx >= 40 && mx <= 150){
						if(my >= 400 && my <= 430){
							difficulty = MODE.HARD;
							setMode();
							startGame();
						}
					}

					if(mx >= 220 && mx <= 330){
						if(my >= 400 && my <= 430){
							difficulty = MODE.NIGHTMARE;
							setMode();
							startGame();
						}
					}
					
				break;


			}//end of switch case

		}//end of method

		public void mouseClicked(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
       	public void mouseReleased(MouseEvent e){}
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkPie();
            checkCollisions();
        }
        repaint();
    }

    public class myKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

			if(State == STATE.GAME){

            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(GamePanel.gameOn) {
                        resume();
                    } else {
                        pause();
                    }
                    break;
            }

			}
        }
    }
}
