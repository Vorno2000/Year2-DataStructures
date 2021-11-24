package factory.simulator;

import java.awt.Color;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Machine implements Runnable{
    public static long MIN_CONSUMPTION_TIME = 50;
    public static long MAX_CONSUMPTION_TIME = 2000;
    public static long MIN_PRODUCTION_TIME = 50;
    public static long MAX_PRODUCTION_TIME = 2000;
    
    public boolean isAlive = false;
    private final ConveyorBelt[] conveyorBelts;
    private final Random r;
    
    public Machine(ConveyorBelt[] conveyorBelts) {
        this.conveyorBelts = conveyorBelts;
        r = new Random();
    }
    
    private void postParcels(int index) {
        while(!conveyorBelts[index].isFull()) {
            long sleepTime = MIN_PRODUCTION_TIME + ((long)(r.nextDouble()*((MAX_PRODUCTION_TIME+1)-MIN_PRODUCTION_TIME)));
                
            try {
                Thread.sleep(sleepTime);
                if(!isAlive)
                    break;
                char element = (char)(r.nextInt(26) + 'A');
                Color colour = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
                long consumeTime = MIN_CONSUMPTION_TIME + ((long)(r.nextDouble()*((MAX_CONSUMPTION_TIME+1)-MIN_CONSUMPTION_TIME)));
                int priority = r.nextInt(3);
                
                Parcel newParcel = new Parcel(element, colour, consumeTime, priority+1);
                conveyorBelts[index].postParcel(newParcel, this);
                    
            } catch (InterruptedException ex) {
                Logger.getLogger(Machine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void run() {
        while(isAlive) {
            for(int i = 0; i < conveyorBelts.length; i++) {
                if(!conveyorBelts[i].isFull() && isAlive) 
                    if(conveyorBelts[i].connectMachine(this)) {
                        postParcels(i);
                        conveyorBelts[i].disconnectMachine(this);
                    }
            }
        }
    }
    
    public void stop() {
        isAlive = false;
    }
}
