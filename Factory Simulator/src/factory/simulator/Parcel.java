package factory.simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parcel<E> implements Comparable<Parcel<?>> {

    private E element;
    private Color colour;
    private long consumeTime;
    private int priority;
    private long timestamp;
    
    public Parcel(E element, Color colour, long consumeTime, int priority) {
        this.element = element;
        this.colour = colour;
        this.consumeTime = consumeTime;
        this.priority = priority;
        this.timestamp = System.nanoTime();
    }
    
    public void consume() { 
        try {
            Thread.sleep(consumeTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(Parcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String toString() {
        return (element+"("+priority+")");
    }
    
    public void drawBox(Graphics g, int x, int y, int width, int height) {
        g.setColor(colour);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(this.toString(), x+(width/2), y+(height/2));
    }

    @Override
    public int compareTo(Parcel<?> o) {
        if(this.priority > o.priority)
            return 1;
        else if (this.priority < o.priority)
            return -1;
        else {
            if(this.timestamp > o.timestamp)
                return 1;
            else if(this.timestamp < o.timestamp)
                return -1;
            return 0;
        }
        
    }
}
