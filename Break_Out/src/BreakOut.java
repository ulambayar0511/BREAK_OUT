import java.awt.Color;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.event.*;

public class BreakOut extends GraphicsProgram {
    private GOval ball = new GOval(WIDTH / 2, HEIGHT / 2, BALL_RADIUS, BALL_RADIUS);
    private double vx = 0, vy;
    private boolean isPlaying = true;

    private GRect paddle = new GRect(PADDLE_WIDTH, PADLLE_HEIGHT);
    private GRect[] toosgo = new GRect[NROW * NBLOCKS_PER_ROW];
    private int removedBlock = 0;
    private int ndie = 0;

    public void run() {

        addMouseListeners();
        setSize(WIDTH, HEIGHT);
        initGame();
        playGame();
        for (int i = 0; i < NBLOCKS_PER_ROW * NROW; i++) {
            if (toosgo != null)
                remove(toosgo[i]);
        }
        remove(paddle);
        remove(ball);
        GLabel title = new GLabel("Game end");
        title.setColor(Color.red);
        title.setLocation(WIDTH / 2, HEIGHT / 2);
        if (removedBlock >= NROW * NBLOCKS_PER_ROW) {
            title.setLabel("You win");
            title.setColor(Color.GREEN);
        } else if (ndie >= HELL)
            title.setLabel("You lose");
        add(title);
    }

    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        if (x > WIDTH - PADDLE_WIDTH)
            x = WIDTH - PADDLE_WIDTH;

        paddle.setLocation(x, paddle.getY());
    }

    public static void main(String[] args) {

        new BreakOut().start();
    }

    public void initGame() {

        initBlocks();
        initPaddle();
        initBall();
    }

    public void initBall() {

        ball.setFilled(true);
        ball.setLocation(WIDTH / 2, HEIGHT / 2);
        RandomGenerator rgen = RandomGenerator.getInstance();
        vx = vx + rgen.nextDouble(1.0, 2.0);
        vy = 3;
        if (rgen.nextBoolean(0.5))
            vx = -vx;
        add(ball);
    }

    public void initPaddle() {

        paddle.setFilled(true);
        // paddle.setLocation();
        add(paddle, WIDTH / 2 - PADDLE_WIDTH / 2, PADDLE_Y_OFFSET);
    }

    public void initBlocks() {

        double dx, dy;
        for (int i = 0; i < NBLOCKS_PER_ROW * NBLOCKS_PER_ROW; i++) {
            dx = i % NBLOCKS_PER_ROW;
            dy = i / NROW;
            toosgo[i] = new GRect(BLOCK_WIDTH, BLOCK_HEIGTH);
            toosgo[i].setLocation(dx * BLOCK_WIDTH + dx * BLOCK_GAP,
                    dy * BLOCK_HEIGTH + dy * BLOCK_GAP + BLOCK_Y_OFFSET);
            Color[] color = { Color.RED, Color.GREEN, Color.GRAY, Color.YELLOW, Color.CYAN };
            toosgo[i].setFilled(true);
            toosgo[i].setColor(color[i / NBLOCKS_PER_ROW]);
            add(toosgo[i]);
        }
    }

    public void ballMove() {

        ball.move(vx, vy);
    }

    public GObject getCollidingObect() {

        GObject result = null;
        if (getElementAt(ball.getX(), ball.getY()) != null)

        {
            result = getElementAt(ball.getX(), ball.getY());
        }

        return result;

    }

    public void playGame() {
        initBall();
        while (isPlaying) {
            ballMove();
            GObject collider = getCollidingObect();
            for (int i = 0; i < NROW * NBLOCKS_PER_ROW; i++) {
                if (collider == toosgo[i]) {
                    remove(toosgo[i]);
                    removedBlock++;
                    vy = -vy;
                    vx++;
                }
            }
            if (collider == paddle)
                vy = -vy;

            if (ball.getLocation().getY() > HEIGHT) {
                ndie++;
                if (ndie >= HELL)
                    isPlaying = false;
                else {
                    initBall();
                    continue;
                }
            }
            if (ball.getLocation().getY() <= 0)
                vy = -vy;
            if (ball.getLocation().getX() <= 0 || ball.getLocation().getX() + BALL_RADIUS * 2 >= WIDTH)
                vx = -vx;
            pause(20);
            if (removedBlock >= NROW * NBLOCKS_PER_ROW)
                isPlaying = false;
        }
    }

    // Тоглоомын талбарын өргөн
    private static final int WIDTH = 400;
    // Тоглоомын талбарын өндөр
    private static final int HEIGHT = 600;
    // Тавцангын өргөн өндөр
    private static final int PADDLE_WIDTH = 60;
    private static final int PADLLE_HEIGHT = 10;
    // Тавцангын байрлал
    private static final int PADDLE_Y_OFFSET = 500;
    // Нэг мөр дахь тоосго тоо
    private static final int NBLOCKS_PER_ROW = 5;
    // Нийт мөрийн тоо
    private static final int NROW = 5;
    // Блок зай
    private static final int BLOCK_GAP = 4;
    // Блокын өргөн өндөр
    private static final int BLOCK_WIDTH = (WIDTH - (NBLOCKS_PER_ROW - 1) * BLOCK_GAP) / NROW;
    private static final int BLOCK_HEIGTH = 20;
    // Бөмбөг радиус
    private static final int BALL_RADIUS = 8;
    private static final int BLOCK_Y_OFFSET = 50;
    private static final int HELL = 3;

}
