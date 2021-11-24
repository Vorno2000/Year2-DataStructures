package factory.simulator;

public class Dispatcher implements Runnable{

    public boolean isAlive = false;
    private ConveyorBelt[] conveyorBelts;
    
    public Dispatcher(ConveyorBelt[] conveyorBelts) {
        this.conveyorBelts = conveyorBelts;
    }
    
    private void dispatchParcels(int index) {
        Parcel<?> tempParcel = conveyorBelts[index].getFirstParcel(this);
        if(tempParcel != null) {
            tempParcel.consume();
            conveyorBelts[index].retrieveParcel(this);
        }
    }
    
    @Override
    public void run() {
        while(isAlive) {
            for(int i = 0; i < conveyorBelts.length; i++) {
                if(!conveyorBelts[i].isEmpty() && isAlive)
                    if(conveyorBelts[i].connectDispatcher(this)) {
                        dispatchParcels(i);
                        conveyorBelts[i].disconnectDispatcher(this);
                    }
            }
        }
    }
    
    public void stop() {
        isAlive = false;
    }
}
