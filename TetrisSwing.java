package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class TetrisSwing extends JComponent implements KeyListener, ActionListener {
    static byte fallTimer;
    static double speed;
    static int rotation;
    static int amount;
    static int score;
    static int aX, aY, bX, bY, cX, cY, dX, dY, nextBX, nextBY, nextCX, nextCY, nextDX, nextDY;
    static final int NEXTAX = 450;
    static final int NEXTAY = 120;
    static byte str;
    static boolean cantFall;
    static boolean restart = true;
    static byte[][] field = new byte[25][12]; //Игровое поле
    static boolean gameOver = true;
    static byte[][][][] arrayOfFigures = {
            {{{-1,0},{0,1},{1,0}}, {{0,-1},{-1,0},{0,1}}, {{1,0},{0,-1},{-1,0}}, {{0,1},{1,0},{0,-1}}},
            {{{0,-1},{0,1},{1,1}}, {{1,0},{-1,0},{-1,1}}, {{0,1},{0,-1},{-1,-1}}, {{-1,0},{1,0},{1,-1}}},
            {{{0,-1},{0,1},{-1,1}}, {{1,0},{-1,0},{-1,-1}}, {{0,1},{0,-1},{1,-1}}, {{-1,0},{1,0},{1,1}}},
            {{{1,0},{0,1},{1,1}}},
            {{{0,-1},{0,1},{0,2}}, {{1,0},{-1,0},{-2,0}}, {{0,1},{0,-1},{0,-2}}, {{-1,0},{1,0},{2,0}}},
            {{{-1,0},{0,-1},{1,-1}}, {{0,-1},{1,0},{1,1}}, {{1,0},{0,1},{-1,1}}, {{0,1},{-1,0},{-1,-1}}},
            {{{1,0},{0,-1},{-1,-1}}, {{0,1},{1,0},{1,-1}}, {{-1,0},{0,1},{1,1}}, {{0,-1},{-1,0},{-1,1}}}
    };
    static byte figure; //Значение, показывающее номер фигуры
    static byte nextFigure;
    static byte fillAmount;
    static boolean finishPaint = false;
    static final String FONTSTYLE = "Arial";

    Timer timer = new Timer(5,this);

    static final int WINDOW_WIDTH = 570;
    static final int WINDOW_HEIGHT = 660;
    static JFrame frame;
    static JLabel currentScoreLabel;
    static JLabel restartLabel;
    static Font fontStart;

    public static void main(String[] args) {
        TetrisSwing main = new TetrisSwing();
        frame = getFrame();
        frame.add(main);
        frame.addKeyListener(main);
    }

    static JFrame getFrame() {
        JFrame frameF = new JFrame();
        frameF.setVisible(true);
        frameF.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frameF.setResizable(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        frameF.setBounds((dimension.width- WINDOW_WIDTH) / 2,(dimension.height - WINDOW_HEIGHT) / 2,WINDOW_WIDTH+15,WINDOW_HEIGHT+38);
        fontStart = new Font(FONTSTYLE, Font.PLAIN, 20);
        JLabel scoreLabel = new JLabel("Score: ");
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        scoreLabel.setBounds(390,330,70,30);
        scoreLabel.setFont(fontStart);
        frameF.add(scoreLabel);
        Font fontNext = new Font(FONTSTYLE, Font.PLAIN, 40);
        restartLabel = new JLabel("Press space to start", SwingConstants.CENTER);
        restartLabel.setForeground(Color.WHITE);
        restartLabel.setBounds(70,260,420,60);
        restartLabel.setFont(fontNext);
        frameF.add(restartLabel);
        currentScoreLabel = new JLabel(String.valueOf(score));
        currentScoreLabel.setForeground(Color.LIGHT_GRAY);
        currentScoreLabel.setBounds(460,330,80,30);
        currentScoreLabel.setFont(fontStart);
        frameF.add(currentScoreLabel);
        JLabel nextLabel = new JLabel("Next figure:");
        nextLabel.setBounds(390,27,110,30);
        nextLabel.setForeground(Color.LIGHT_GRAY);
        nextLabel.setFont(fontStart);
        frameF.add(nextLabel);
        frameF.setTitle("T E T R I S");
        return(frameF);
    }
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,WINDOW_WIDTH+1,WINDOW_HEIGHT+1);
        for (int i = 4; i < 24; i++) {
            for (int j = 1; j < 11; j++) {
                if (finishPaint) {
                    switch (field[i][j]) {
                        case 1 -> g.setColor(Color.RED.darker());
                        case 2 -> g.setColor(Color.ORANGE.darker());
                        case 3 -> g.setColor(Color.YELLOW.darker());
                        case 4 -> g.setColor(Color.GREEN.darker());
                        case 5 -> g.setColor(Color.CYAN.darker());
                        case 6 -> g.setColor(Color.BLUE.darker());
                        case 7 -> g.setColor(Color.MAGENTA.darker());
                        default -> g.setColor(Color.BLACK);
                    }
                } else {
                    switch (field[i][j]) {
                        case 1 -> g.setColor(Color.RED);
                        case 2 -> g.setColor(Color.ORANGE);
                        case 3 -> g.setColor(Color.YELLOW);
                        case 4 -> g.setColor(Color.GREEN);
                        case 5 -> g.setColor(Color.CYAN);
                        case 6 -> g.setColor(Color.BLUE);
                        case 7 -> g.setColor(Color.MAGENTA);
                        default -> g.setColor(Color.BLACK);
                    }
                }
                g.fillRect((j+1)*30,(i-3)*30,30,30);
                g.setColor(Color.GRAY);
                g.drawRect((j+1)*30,(i-3)*30,30,30);
            }
        }
        switch (figure) {
            case 2 -> g.setColor(Color.ORANGE);
            case 3 -> g.setColor(Color.YELLOW);
            case 4 -> g.setColor(Color.GREEN);
            case 5 -> g.setColor(Color.CYAN);
            case 6 -> g.setColor(Color.BLUE);
            case 7 -> g.setColor(Color.MAGENTA);
            default -> g.setColor(Color.RED);
        }
        if (aY >= 5) {
            g.fillRect((aX + 1) * 30, (aY - 4) * 30, 30, 30);
        }
        if (bY >= 5) {
            g.fillRect((bX + 1) * 30, (bY - 4) * 30, 30, 30);
        }
        if (cY >= 5) {
            g.fillRect((cX + 1) * 30, (cY - 4) * 30, 30, 30);
        }
        if (dY >= 5) {
            g.fillRect((dX + 1) * 30, (dY - 4) * 30, 30, 30);
        }
        g.setColor(Color.GRAY);
        if (aY >= 5) {
            g.drawRect((aX + 1) * 30, (aY - 4) * 30, 30, 30);
        }
        if (bY >= 5) {
            g.drawRect((bX + 1) * 30, (bY - 4) * 30, 30, 30);
        }
        if (cY >= 5) {
            g.drawRect((cX + 1) * 30, (cY - 4) * 30, 30, 30);
        }
        if (dY >= 5) {
            g.drawRect((dX + 1) * 30, (dY - 4) * 30, 30, 30);
        }
        g.setColor(Color.DARK_GRAY);
        g.fillRect(390,60,150,180);
        switch (nextFigure) {
            case 2 -> g.setColor(Color.ORANGE);
            case 3 -> g.setColor(Color.YELLOW);
            case 4 -> g.setColor(Color.GREEN);
            case 5 -> g.setColor(Color.CYAN);
            case 6 -> g.setColor(Color.BLUE);
            case 7 -> g.setColor(Color.MAGENTA);
            default -> g.setColor(Color.RED);
        }
        if (!gameOver) {
            g.fillRect(NEXTAX, NEXTAY, 30, 30);
            g.fillRect(nextBX, nextBY, 30, 30);
            g.fillRect(nextCX, nextCY, 30, 30);
            g.fillRect(nextDX, nextDY, 30, 30);
            g.setColor(Color.GRAY);
            g.drawRect(NEXTAX, NEXTAY, 30, 30);
            g.drawRect(nextBX, nextBY, 30, 30);
            g.drawRect(nextCX, nextCY, 30, 30);
            g.drawRect(nextDX, nextDY, 30, 30);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        if(gameOver) {
            showRestartLabel();
        } else {
            if (restart) {
                restartGame();
            } else {
                if (cantFall) {
                    makeNextFigure();
                } else {
                    if (fallTimer == speed) {
                        dropFigure();
                    }
                }
                findCoordinates();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { //Этот метод должен присутствовать, но не используется
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!restart) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                rotateFigure();
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                moveFigureLeft();
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                moveFigureRight();
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                moveFigureDown();
            }
        }
        if (e.getKeyCode()==KeyEvent.VK_SPACE) {
            timer.start();
            pressSpace();
        }
    }
    static void showRestartLabel() {
        finishPaint = true;
        Font fontRestart = new Font(FONTSTYLE, Font.PLAIN, 36);
        restartLabel.setFont(fontRestart);
        restartLabel.setBounds(30,160,470,200);
        restartLabel.setText("<html><center>Game over!<center>" +
                "Your score: " + score +
                "<br>Press space to start again<html>");
        restartLabel.setVisible(true);
    }
    static void restartGame() {
        restart = false;
        finishPaint = false;
        score = 0;
        currentScoreLabel.setText(String.valueOf(score));
        speed = 30;
        nextFigure = (byte) (1 + Math.random() * 7);
        aX = 4;
        aY = 2;
        cantFall = true;
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 12; j++) {
                field[i][j] = 0;
            }
        }
    }
    static void makeNextFigure() {
        cantFall = false;
        rotation = 0;
        figure = nextFigure;
        nextFigure = (byte) (1 + Math.random() * 7);
        nextBX = NEXTAX + 30 * arrayOfFigures[nextFigure - 1][0][0][0];
        nextBY = NEXTAY + 30 * arrayOfFigures[nextFigure - 1][0][0][1];
        nextCX = NEXTAX + 30 * arrayOfFigures[nextFigure - 1][0][1][0];
        nextCY = NEXTAY + 30 * arrayOfFigures[nextFigure - 1][0][1][1];
        nextDX = NEXTAX + 30 * arrayOfFigures[nextFigure - 1][0][2][0];
        nextDY = NEXTAY + 30 * arrayOfFigures[nextFigure - 1][0][2][1];
        if (figure == 4) {
            amount = 1;
        } else {
            amount = 4;
        }
        aX = 5;
        aY = 2;
    }
    static void dropFigure() {
        if ((field[aY][aX] == 0) && (aY + 1 < 25) && (field[bY][bX] == 0) && (bY + 1 < 25) && (field[cY][cX] == 0) && (cY + 1 < 25) && (field[dY][dX] == 0) && (dY + 1 < 25)) {
            aY++;
        } else {
            leaveFigure();
            checkStrings();
            for (int i = 1; i < 11; i++) {
                if (field[3][i] != 0) {
                    aX = 5;
                    aY = 2;
                    restart = true;
                    gameOver = true;
                    break;
                }
            }
        }
        fallTimer = 0;
    }
    static void leaveFigure() {
        cantFall = true;
        field[aY - 1][aX] = figure;
        field[bY - 1][bX] = figure;
        field[cY - 1][cX] = figure;
        field[dY - 1][dX] = figure;
    }
    static void checkStrings() {
        str = 23;
        while (str > 3) {
            fillAmount = 0;
            for (int i = 1; i < 11; i++) {
                if (field[str][i] != 0) {
                    fillAmount++;
                }
            }
            if (fillAmount == 10) {
                removeString();
            }
            str--;
        }
    }
    static void removeString() {
        score += 10;
        currentScoreLabel.setText(String.valueOf(score));
        if ((speed - 5 > 0) && (score % 200 == 0)) {
            speed -= 5;
        }
        for (int j = str - 1; j >= 4; j--) {
            System.arraycopy(field[j], 1, field[j + 1], 1, 10);
        }
        str++;
    }
    static void findCoordinates() {
        fallTimer++;
        bX = aX + arrayOfFigures[figure - 1][rotation][0][0];
        bY = aY + arrayOfFigures[figure - 1][rotation][0][1];
        cX = aX + arrayOfFigures[figure - 1][rotation][1][0];
        cY = aY + arrayOfFigures[figure - 1][rotation][1][1];
        dX = aX + arrayOfFigures[figure - 1][rotation][2][0];
        dY = aY + arrayOfFigures[figure - 1][rotation][2][1];
    }
    static void pressSpace() {
        gameOver = false;
        restartLabel.setVisible(false);
    }
    static void rotateFigure() {
        if ((aX + arrayOfFigures[figure-1][(rotation + 1) % amount][0][0] < 11)&&(aX + arrayOfFigures[figure-1][(rotation + 1) % amount][1][0] < 11)&&(aX + arrayOfFigures[figure-1][(rotation + 1) % amount][2][0] < 11)) {
            if ((aX + arrayOfFigures[figure-1][(rotation + 1) % amount][0][0] > 0)&&(aX + arrayOfFigures[figure-1][(rotation + 1) % amount][1][0] > 0)&&(aX + arrayOfFigures[figure-1][(rotation + 1) % amount][2][0] > 0)) {
                if ((field[aY + arrayOfFigures[figure-1][(rotation + 1) % amount][0][1] - 1][aX + arrayOfFigures[figure-1][(rotation + 1) % amount][0][0]] == 0)&&(field[aY + arrayOfFigures[figure-1][(rotation + 1) % amount][1][1] - 1][aX + arrayOfFigures[figure-1][(rotation + 1) % amount][1][0]] == 0)&&(field[aY + arrayOfFigures[figure-1][(rotation + 1) % amount][2][1] - 1][aX + arrayOfFigures[figure-1][(rotation + 1) % amount][2][0]] == 0)) {
                    if (aY + arrayOfFigures[figure - 1][(rotation + 1) % amount][0][1] < 24 && aY + arrayOfFigures[figure - 1][(rotation + 1) % amount][1][1] < 24 && aY + arrayOfFigures[figure - 1][(rotation + 1) % amount][2][1] < 24) {
                        rotation = (rotation + 1) % amount;
                    }
                }
            }
        }
    }
    static void moveFigureDown() {
        if ((field[aY][aX] == 0) && (aY + 1 < 25) && (field[bY][bX] == 0) && (bY + 1 < 25) && (field[cY][cX] == 0) && (cY + 1 < 25) && (field[dY][dX] == 0) && (dY + 1 < 25)) {
            aY++;
            bY = aY + arrayOfFigures[figure - 1][rotation][0][1];
            cY = aY + arrayOfFigures[figure - 1][rotation][1][1];
            dY = aY + arrayOfFigures[figure - 1][rotation][2][1];
        }
    }
    static void moveFigureLeft() {
        if (!cantFall && (field[aY - 1][aX - 1] == 0) && (aX - 1 > 0) && (field[bY - 1][bX - 1] == 0) && (bX - 1 > 0) && (field[cY - 1][cX - 1] == 0) && (cX - 1 > 0) && (field[dY - 1][dX - 1] == 0) && (dX - 1 > 0)) {
            aX--;
            bX = aX + arrayOfFigures[figure - 1][rotation][0][0];
            cX = aX + arrayOfFigures[figure - 1][rotation][1][0];
            dX = aX + arrayOfFigures[figure - 1][rotation][2][0];
        }
    }
    static void moveFigureRight() {
        if (!cantFall && (field[aY - 1][aX + 1] == 0) && (aX + 1 < 11) && (field[bY - 1][bX + 1] == 0) && (bX + 1 < 11) && (field[cY - 1][cX + 1] == 0) && (cX + 1 < 11) && (field[dY - 1][dX + 1] == 0) && (dX + 1 < 11)) {
            aX++;
            bX = aX + arrayOfFigures[figure - 1][rotation][0][0];
            cX = aX + arrayOfFigures[figure - 1][rotation][1][0];
            dX = aX + arrayOfFigures[figure - 1][rotation][2][0];
        }
    }
    @Override
    public void keyReleased(KeyEvent e) { //Этот метод должен присутствовать, но не используется
    }
}