import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class Drawing {
    int blockSize;
    JFrame frame;
    GraphicalPanel panel;
    NeuralNetwork nn;
    int compressScale;
    double[] data;
    public Drawing(int compressScale, NeuralNetwork nn){
        this.nn = nn;
        this.compressScale = compressScale;
        blockSize = 280 / (28 / compressScale);
        frame = new JFrame("Drawing");
        panel = new GraphicalPanel(this);
        data = new double[(28 / compressScale) * (28 / compressScale)];
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.addMouseListener(new MyListener(this));
        frame.setVisible(true);
    }
    public void registerMouseDrag(int x, int y){
        data[(y / blockSize * 28 / compressScale) + x] = 1;
        frame.repaint();
    }
}

class GraphicalPanel extends JPanel {
    Drawing drawing;
    public GraphicalPanel(Drawing drawing){
        this.drawing = drawing;
        setPreferredSize(new Dimension(280, 280));
    }
    public void paintComponent(Graphics page){
        for(int i = 0; i < 28 / drawing.compressScale; i++){
            for(int j = 0; j < 28 / drawing.compressScale; j++){
                int amt = (int)(drawing.data[i * (28 / drawing.compressScale) + j] * 255);
                page.setColor(new Color(amt, amt, amt));
                page.fillRect(i * drawing.blockSize, j * drawing.blockSize, drawing.blockSize, drawing.blockSize);
            }
        }
        page.setColor(Color.red);
        page.drawString("" + (int)drawing.nn.runCalculation(drawing.data)[0], 0, 0);
    }
}

class MyListener implements MouseListener {
    boolean pressing = false;
    Drawing drawing;
    public MyListener(Drawing drawing){
        this.drawing = drawing;
    }
    @Override
    public void mouseClicked(MouseEvent e){
        drawing.registerMouseDrag(e.getX(), e.getY() - 30);
    }
    @Override
    public void mousePressed(MouseEvent e){
         pressing = true;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        pressing = false;
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
}