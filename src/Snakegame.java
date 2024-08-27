import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Snakegame extends JPanel implements ActionListener, KeyListener{
     private class Tile{
        int x;
        int y;

        Tile(int x , int y ){
            this.x = x;
            this.y = y;
        }
     }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    //snake
    Tile snakehead;
    ArrayList<Tile>snakebody;
    //food
    Tile food;
    Random random;
    //game logic
    Timer gameLoop;
    boolean gameover = false;
    //velocity
    int velocityX;
    int velocityY;

    Snakegame(int boardWidth,int boardHeight){
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);


        snakehead = new Tile(5, 5);
        snakebody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();
        placeFood();
        velocityX = 0;
        velocityY = 1;
        gameLoop = new Timer(100,this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //grid
        // for (int i=0 ; i<boardWidth/tileSize; i++){
        //     g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
        //     g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        // }
        //food
        g.setColor(Color.white);
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize,true);
        //snake
        g.setColor(Color.red);
        g.fill3DRect(snakehead.x*tileSize, snakehead.y*tileSize, tileSize, tileSize,true);
        //snakebody
        for(int i=0; i < snakebody.size(); i++){
            Tile snakepart = snakebody.get(i);
            g.fill3DRect(snakepart.x*tileSize, snakepart.y*tileSize, tileSize, tileSize,true);
        }
        //score
        g.setFont(new Font("Arial",Font.PLAIN, 16));
        if (gameover) {
            g.setColor(Color.yellow);
            g.drawString("game over: "+ String.valueOf(snakebody.size()), tileSize - 16 , tileSize);
        }
        else{
            g.setColor(Color.yellow);
            g.drawString("Score: "+ String.valueOf(snakebody.size()), tileSize - 16 , tileSize);
        }
    }
    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }
    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void move(){
        //eat food
        if(collision(snakehead, food)){
            snakebody.add(new Tile(food.x,food.y));
            placeFood();
        }
        for (int i = snakebody.size()-1; i>=0; i--){
            Tile snakepart = snakebody.get(i);
            if(i==0){
                snakepart.x = snakehead.x;
                snakepart.y = snakehead.y;
            }
            else{
                Tile  prevsnakepart = snakebody.get(i-1);
                snakepart.x = prevsnakepart.x;
                snakepart.y = prevsnakepart.y;
            }

        }
        snakehead.x += velocityX;
        snakehead.y += velocityY;
        //game over

        for(int i = 0; i<snakebody.size();i++){
            Tile snakepart = snakebody.get(i);
            if(collision(snakehead, snakepart)){
                gameover = true;
            }
        }
        if(snakehead.x*tileSize < 0 || snakehead.x*tileSize > boardWidth || snakehead.y*tileSize < 0 || snakehead.y*tileSize > boardHeight){
            gameover = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       move();
       repaint();
       if(gameover){
        gameLoop.stop();
       }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}