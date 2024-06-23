package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int CELL_SIZE = 25;
    private final int GAME_CELLS = (WIDTH*HEIGHT)/CELL_SIZE;
    private final int DELAY = 75;

    private final int x[] = new int[GAME_CELLS];
    private final int y[] = new int[GAME_CELLS];

    int initialSnakeBody = 6;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        if(running){
//            for(int i = 0; i <HEIGHT/CELL_SIZE; i++){
//                // creates grid
//                g.drawLine(i*CELL_SIZE, 0, i*CELL_SIZE, HEIGHT);
//                g.drawLine(0, i*CELL_SIZE, WIDTH, i*CELL_SIZE);
//            }

            g.setColor(new Color(211, 38, 59));
            // fill cell within grid at (X,Y)
            g.fillOval(foodX, foodY, 20, 20);

            for(int i=0; i< initialSnakeBody; i++){
                if(i==0) {
                    g.setColor(new Color(126, 41, 72));
                    if(direction == 'U') g.fillArc(x[i], y[i], CELL_SIZE, CELL_SIZE, 120, 300);
                    if(direction == 'D') g.fillArc(x[i], y[i], CELL_SIZE, CELL_SIZE, 300, 300);

                    if(direction == 'L') g.fillArc(x[i], y[i], CELL_SIZE, CELL_SIZE, 200, 300);
                    if(direction == 'R') g.fillArc(x[i], y[i], CELL_SIZE, CELL_SIZE, 30, 300);
                }
                else {
                    g.setColor(new Color(168, 111, 133));
                    g.fillOval(x[i], y[i], CELL_SIZE, CELL_SIZE);
                }
            }
            score(g);
        }
        else {
            gameOver(g);
        }

    }

    // generates coordinates of food item
    public void newFood(){
        foodX = random.nextInt((int) (WIDTH/CELL_SIZE))*CELL_SIZE;
        foodY = random.nextInt((int) (HEIGHT/CELL_SIZE))*CELL_SIZE;
    }

    public void move(){
        for(int i = initialSnakeBody; i >0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction){
            case 'U':
                y[0] = y[0] - CELL_SIZE;
                break;
            case 'D':
                y[0] = y[0] + CELL_SIZE;
                break;
            case 'L':
                x[0] = x[0] - CELL_SIZE;
                break;
            case 'R':
                x[0] = x[0] + CELL_SIZE;
                break;

        }
    }

    public void checkFood(){
        if((x[0] == foodX) && (y[0] == foodY)) {
            initialSnakeBody++;
            foodEaten++;
            newFood();
        }
    }

    public void checkCollisions(){
        // checks if snake head collides with body
        for (int i = initialSnakeBody; i>0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        // check if head touches left border
        if(x[0] < 0) running = false;
        // check if head touches right border
        if(x[0] > WIDTH) running = false;
        // check if head touches top border
        if(y[0] < 0) running = false;
        // check if head touches bottom border
        if(y[0] > HEIGHT) running = false;

        if(!running) timer.stop();
    }

    public void gameOver(Graphics g){
        score(g);

        //Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());

        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over"))/2, HEIGHT/2);
    }

    public void score(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());

        g.drawString("Score: " + foodEaten, 0, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    /**
     * Inner class: MyKeyAdapter
     */
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') direction = 'D';
                    break;
            }
        }
    }

}
