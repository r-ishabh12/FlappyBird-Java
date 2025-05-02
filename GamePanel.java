package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    //Bird's position and movement
    int birdx = 100;
    int birdy = 300;
    int birdwidth = 20;
    int birdheight = 20;

    //Bird physics
    int velocity = 0;
    int gravity = 1;
    int jumpStrength = -10;

    //pipe
    int pipex = 800;
    int pipewidth = 60;
    int pipeGap = 150;
    int topPipeHeight;
    int pipeSpeed =4;

    boolean gameOver = false;
    int score = 0;
    boolean scored = false;


    Timer gameloop;
    Random rand = new Random();

    GamePanel(){
        setBackground(Color.cyan);
        setFocusable(true);
        addKeyListener(this);

        //pipe
        topPipeHeight = 100 + rand.nextInt(200);

        gameloop = new Timer(20,this);
        gameloop.start();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // Draw the bird
        g.setColor(Color.red);
        g.fillRect(birdx,birdy,20,20);

        //Draw pipe
        g.setColor(Color.green);
        //Top pipe
        g.fillRect(pipex,0,pipewidth,topPipeHeight);
        //Bottom pipe
        g.fillRect(pipex,topPipeHeight+pipeGap, pipewidth, getHeight() - (topPipeHeight +pipeGap));

        //Score
        g.setColor(Color.black);
        g.setFont(new Font("Arial",Font.BOLD,24));
        g.drawString("Score : "+score,20,30);

        //Game OVer Text
        // Game Over text
        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over", getWidth()/2 - 100, getHeight()/2 - 20);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Press SPACE to restart", getWidth()/2 - 100, getHeight()/2 + 20);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if (!gameOver) {
            velocity += gravity;
            birdy += velocity;

            pipex -= pipeSpeed;

            if (pipex + pipewidth < 0) {
                pipex = getWidth();
                topPipeHeight = 100 + rand.nextInt(200);
                scored = false;
            }
            if (!scored && pipex + pipewidth < birdx) {
                score++;
                scored = true;
            }

            checkCollision();

        }
        repaint();
    }

    public void checkCollision(){
        Rectangle birdrect = new Rectangle(birdx,birdy,birdwidth,birdheight);
        Rectangle topPipe = new Rectangle(pipex,0,pipewidth,topPipeHeight);
        Rectangle BottomPipe = new Rectangle(pipex,topPipeHeight + pipeGap,pipewidth,getWidth() - (topPipeHeight+pipeGap));

        if (birdrect.intersects(topPipe) || birdrect.intersects(BottomPipe)){
            gameloop.stop();
            gameOver = true;
//            JOptionPane.showMessageDialog(this,"Game Over!, You hit a pole");
        }
        if (birdy > getHeight() || birdy < 0){
            gameloop.stop();
            gameOver = true;
//            JOptionPane.showMessageDialog(this,"Game over!, You fell off");
        }
    }

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            if (gameOver){
                birdy = 300;
                velocity = 0;
                pipex = getWidth();
                topPipeHeight = 100+ rand.nextInt(200);
                score = 0;
                gameOver = false;

                gameloop.start();
            }else {
                velocity = jumpStrength;
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e){}
    @Override
    public void keyTyped(KeyEvent e){}

}
