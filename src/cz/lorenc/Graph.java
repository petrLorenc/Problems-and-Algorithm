package cz.lorenc;

/**
 * Created by petr.lorenc on 17.12.16.
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class Graph extends JPanel {
    private static int MAX_SCORE = 2500;
    private static final int PREF_W = 800;
    private static final int PREF_H = 650;
    private static final int BORDER_GAP = 10;
    private static final Color GRAPH_POINT_COLOR_BEST = new Color(250, 0, 0, 250);
    private static final Color GRAPH_POINT_COLOR_AVERAGE = new Color(0, 250, 0, 250);
    private static final Color GRAPH_POINT_COLOR_WORST = new Color(0, 0, 250, 250);
    private static final int GRAPH_POINT_WIDTH = 3;
    private static final int Y_HATCH_CNT = 15;
    private static final int X_HATCH_CNT = 15;

    private List<Integer> scoresBest;
    private List<Integer> scoresAverage;
    private List<Integer> scoresWorst;


    public Graph(List<Integer> scoresBest,List<Integer> scoresAverage, List<Integer> scoresWorst, int MAX_SCORE) {
        this.scoresBest = scoresBest;
        this.scoresAverage = scoresAverage;
        this.scoresWorst = scoresWorst;
        this.MAX_SCORE = MAX_SCORE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (scoresBest.size() - 1);
        double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

        // create x and y axes
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

        // create hatch marks for y axis.
        for (int i = 0; i < Y_HATCH_CNT; i++) {
            int x0 = BORDER_GAP;
            int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
            int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
            int y1 = y0;
            g2.drawLine(x0, y0, x1, y1);
            g2.drawString((MAX_SCORE/Y_HATCH_CNT)*(i+1) + "", x1,y1);
        }

        // and for x axis
        for (int i = 0; i < X_HATCH_CNT; i++) {
            int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (Y_HATCH_CNT - 1) + BORDER_GAP;
            int x1 = x0;
            int y0 = getHeight() - BORDER_GAP;
            int y1 = y0 - GRAPH_POINT_WIDTH;
            g2.drawLine(x0, y0, x1, y1);
            g2.drawString((scoresBest.size()/X_HATCH_CNT)*(i+1) + "", x1-10,y1);
        }

        g2.drawString("Fitness", 50,20);
        g2.drawString("th generation", PREF_W - getWidth()/2, PREF_H - 40);


        //creating point
        List<Point> graphPoints = new ArrayList<Point>();
        for (int i = 0; i < scoresBest.size(); i++) {
            int x1 = (int) (i * xScale + BORDER_GAP);
            int y1 = (int) ((MAX_SCORE - scoresBest.get(i)) * yScale + BORDER_GAP);
            graphPoints.add(new Point(x1, y1));
        }
        for (int i = 0; i < scoresAverage.size(); i++) {
            int x1 = (int) (i * xScale + BORDER_GAP);
            int y1 = (int) ((MAX_SCORE - scoresAverage.get(i)) * yScale + BORDER_GAP);
            graphPoints.add(new Point(x1, y1));
        }
        for (int i = 0; i < scoresWorst.size(); i++) {
            int x1 = (int) (i * xScale + BORDER_GAP);
            int y1 = (int) ((MAX_SCORE - scoresWorst.get(i)) * yScale + BORDER_GAP);
            graphPoints.add(new Point(x1, y1));
        }


        // drawing
        g2.setColor(GRAPH_POINT_COLOR_BEST);
        for (int i = 0; i < scoresBest.size(); i++) {
            int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
            int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
            int ovalW = GRAPH_POINT_WIDTH;
            int ovalH = GRAPH_POINT_WIDTH;
            g2.fillOval(x, y, ovalW, ovalH);
        }
        g2.setColor(GRAPH_POINT_COLOR_AVERAGE);
        for (int i = scoresBest.size(); i < scoresBest.size() + scoresAverage.size(); i++) {
            int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
            int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
            int ovalW = GRAPH_POINT_WIDTH;
            int ovalH = GRAPH_POINT_WIDTH;
            g2.fillOval(x, y, ovalW, ovalH);
        }
        g2.setColor(GRAPH_POINT_COLOR_WORST);
        for (int i = scoresBest.size() + scoresAverage.size(); i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
            int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
            int ovalW = GRAPH_POINT_WIDTH;
            int ovalH = GRAPH_POINT_WIDTH;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }

    public static void createAndShowGui(List<Integer> scoresBest,
                                        List<Integer> scoresAverage,
                                        List<Integer> scoresWorst,
                                        int MAX_SCORE,
                                        String nameOfPicture) {
        Graph mainPanel = new Graph(scoresBest,scoresAverage,scoresWorst,MAX_SCORE);

        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        //saving picture
        BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        frame.paint(img.getGraphics());
        File outputfile = new File(nameOfPicture +".png");
        try {
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
