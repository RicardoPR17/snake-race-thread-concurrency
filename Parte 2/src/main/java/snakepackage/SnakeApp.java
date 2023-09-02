package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private static Board board;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];

    private boolean start=false;

    int worst = -1;

    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();
        
        
        frame.add(board,BorderLayout.CENTER);
        
        JPanel actionsBPabel=new JPanel();
        actionsBPabel.setLayout(new FlowLayout());
        final JButton startButton = new JButton("Start");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!start){
                    for (Snake sn : snakes) {
                        sn.setPaused(false);
                        sn.start();
                    }
                    start=true;
                }

            }
        });
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Snake sn: snakes){
                    sn.setPaused(true);
                }
                showMessage(new JFrame("Game in pause"));
            }
        });
        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Snake sn: snakes){
                    sn.setPaused(false);
                    sn.start();
                }
            }
        });

        actionsBPabel.add(startButton);
        actionsBPabel.add(pauseButton);
        actionsBPabel.add(resumeButton);
        frame.add(actionsBPabel,BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {
        
        
        
        for (int i = 0; i != MAX_THREADS; i++) {
            
            snakes[i] = new Snake(i + 1, spawn[i], i + 1);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
            thread[i].start();
        }

        frame.setVisible(true);


        while (true) {
            /*
            int size = -1;
            int k = 0;
            for(int i=0; i<snakes.length;i++){
                if (snakes[i].getBody().size() > size && !snakes[i].isSnakeEnd()) {
                    size = snakes[i].getBody().size();
                    k = i;
                }
            }
            System.out.println("más larga: [" + k + "], tamano = " + size);
            */

            int x = 0;
            for (int i = 0; i != MAX_THREADS; i++) {
                if (snakes[i].isSnakeEnd() == true) {
                    x++;
                    setWorst();
                }
            }
            if (x == MAX_THREADS) {
                setWorst();
                break;
            }
        }


        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
        

    }

    public int setLongest(){
        int longest = 0;
        for(Snake snake: snakes){
            if(snake.getBody().size() >= longest && !snake.isSnakeEnd()){
                longest = snake.getIdt();
            }
        }
        return longest;
    }

    public int setWorst(){
        for(Snake snake: snakes){
            if(worst != -1){
                break;
            }
            if(snake.isSnakeEnd()){
                worst = snake.getIdt();
            }
        }
        return worst;
    }
    public static SnakeApp getApp() {
        return app;
    }

    private void showMessage(JFrame message){
        /*
        for (Snake snake: snakes){
            int i = snake.getIdt();
            System.out.println("--");
            System.out.println("["+i+"]");
            System.out.println("size: "+snake.getBody().size());
            System.out.println("dead " + (snake.isSnakeEnd()));
            System.out.println("--------------------------");
        }
         */
        JOptionPane.showMessageDialog(message,"Long snake: ["+ String.valueOf(setLongest())+ "]\n"+
                "Worst snake: ["+  String.valueOf(worst) + "]", "Pause", JOptionPane.INFORMATION_MESSAGE );


    }

}
