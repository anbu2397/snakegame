import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("FieldCanBeLocal")


public class Board extends JPanel implements ActionListener {
    //Declare all dimensions related to Board panel
    private final int width = 600;
    private final int height = 600;
    private final int dot_size = 10;
    private final int all_dots = 3600;
    private final int rand_pos = 40;
    private final int delay = 140;

    //Declaring position of each dots of Snake with size of all_dots variable

    private final int[] x= new int[all_dots];
    private final int[] y = new int[all_dots];

    //Declaring the size of snake  and Apple positions
    public int dots;
    private int apple_x;
    private int apple_y;

    //Declaring the direction of the snake

    private boolean rightDirection = true;

    private boolean leftDirection = false;

    private boolean upDirection = false;
    private boolean downDirection = false;


    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
//Declaring constructor of class Board
    public Board() {

        initBoard();
    }

    //Initialize the Panel of Board
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        loadImages();
        initGame();
    }

    //Load images of Apple, head and body
    private void loadImages() {



        ImageIcon appleImage = new ImageIcon("src/resources/apple.png");
        apple = appleImage.getImage();

        ImageIcon headImage = new ImageIcon("src/resources/head.png");
        head = headImage.getImage();

        ImageIcon bodyImage = new ImageIcon("src/resources/dot.png");
        ball = bodyImage.getImage();
    }

    //Initialize Game
    private void initGame() {

        dots = 3;

        for (int i = 0; i < dots; i++) {
            x[i] = 50 - i * 10;
            y[i] = 50;
        }

        locateApple();

        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        int score = (dots-3)*100;
        String scoremsg = "\nScore: "+ score;
        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (width - metr.stringWidth(msg)) / 2, (height / 2)-10);
        g.drawString(scoremsg, (width - metr.stringWidth(scoremsg)) / 2,(height / 2)+10 );
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= dot_size;
        }

        if (rightDirection) {
            x[0] += dot_size;
        }

        if (upDirection) {
            y[0] -= dot_size;
        }

        if (downDirection) {
            y[0] += dot_size;
        }
    }
    //Checks collision of head with any border or body of the snake
    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
                break;
            }
        }

        if (y[0] >= height) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= width) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    //Randomize Apple position
    private void locateApple() {

        int r = (int) (Math.random() * rand_pos);
        apple_x = ((r * dot_size));

        r = (int) (Math.random() * rand_pos);
        apple_y = ((r * dot_size));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent ki) {

            int key = ki.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}