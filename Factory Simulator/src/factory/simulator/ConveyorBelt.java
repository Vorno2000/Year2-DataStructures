package factory.simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.PriorityQueue;

public class ConveyorBelt {
    private int maxCapacity;
    private Machine connectedMachine;
    private Dispatcher connectedDispatcher;
    private PriorityQueue<Parcel<?>> queue;
    private int numElements;
    private final Object lock = new Object();
    
    public ConveyorBelt(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        connectedMachine = null;
        connectedDispatcher = null;
        queue = new PriorityQueue<>();
        numElements = 0;
    }
    
    public ConveyorBelt() {
        maxCapacity = 10;
        connectedMachine = null;
        connectedDispatcher = null;
        queue = new PriorityQueue<>();
        numElements = 0;
    }
    
    public synchronized boolean connectMachine(Machine machine) {
        if(connectedMachine == null) {
            connectedMachine = machine;
            return true;
        }
        else
            return false;
    }
    public synchronized boolean disconnectMachine(Machine machine) {
        if(connectedMachine == machine) {
            connectedMachine = null;
            return true;
        }
        else
            return false;
    }
    
    public synchronized boolean connectDispatcher(Dispatcher dispatcher) {
        if(connectedDispatcher == null) {
            connectedDispatcher = dispatcher;
            return true;
        }
        else
            return false;
    }
    
    public synchronized boolean disconnectDispatcher(Dispatcher dispatcher) {
        if(connectedDispatcher == dispatcher) {
            connectedDispatcher = null;
            return true;
        }
        else
            return false;
    }
    
    public int size() {
        return numElements;
    }
    
    public boolean isEmpty() {
        return numElements == 0;
    }
    
    public boolean isFull() {
        return numElements == maxCapacity;
    }
    
    public boolean postParcel(Parcel<?> p, Machine machine) {
        if(this.connectedMachine == machine) {
            synchronized(lock) {
                queue.add(p);
                numElements++;
                return true;
            }
        }
        else
            return false;
    }
    
    public Parcel<?> getFirstParcel(Dispatcher dispatcher) {
        if(connectedDispatcher == dispatcher)
            return queue.peek();
        else
            return null;
    }
    
    public Parcel<?> retrieveParcel(Dispatcher dispatcher) {
        if(connectedDispatcher == dispatcher) {
            synchronized(lock) {
                numElements--;
                return queue.poll();
            }
        }
        else
            return null;
    }
    
    public void drawBelt(Graphics g, int x, int y, int width, int height) {
        Iterator<Parcel<?>> iterator = queue.iterator();
        int thisX = x;
        int parcelAmt = 0;
        
        if(connectedDispatcher != null) {
            thisX -= 80;
            g.setColor(Color.BLUE);
            g.fillOval(thisX, y, width, height);
            thisX += 80;
        }
        
        while(iterator.hasNext()) {
            Parcel<?> parcel = iterator.next();
            parcel.drawBox(g, thisX, y, width, height);
            
            parcelAmt++;
            thisX += 80;
        }
        
        for(int i = 0; i < maxCapacity-parcelAmt; i++) {
            g.setColor(Color.BLACK);
            g.drawRect(thisX, y, width, height);
            
            thisX += 80;
        }
        
        if(connectedMachine != null) {
            g.setColor(Color.red);
            g.fillOval(thisX, y, width, height);
            thisX += 80;
        }
    }
}
