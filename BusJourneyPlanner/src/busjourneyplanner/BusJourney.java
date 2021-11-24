package busjourneyplanner;

import java.time.LocalTime;
import java.util.LinkedList;

public class BusJourney {
    private LinkedList<BusTrip> busList;
    
    public BusJourney() {
        busList = new LinkedList();
    }
    
    public BusJourney(LinkedList<BusTrip> list) {
        busList = new LinkedList();
        
        for(int i = 0; i < list.size(); i++) {
            this.addBus(list.get(i));
        }
    }
    
    public boolean addBus(BusTrip bus) {
        if(!busList.isEmpty()) {
            if(!this.containsLocation(bus.getArrivalLocation()) || !this.containsLocation(bus.getDepartLocation())) {
                if(busList.getLast().getArrivalLocation().equals(bus.getDepartLocation())) {
                    if(busList.getLast().getArrivalTime().compareTo(bus.getDepartTime()) <= 0) {
                        busList.addLast(bus);
                        return true;
                    }
                }
            }
        }
        else {
            busList.addLast(bus);
            return true;
        }
        return false;
    }
    
    public boolean removeLastTrip() {
        if(!busList.isEmpty())
            busList.removeLast();
        else
            return false;
        
        return true;
    }
    
    public boolean containsLocation(String location) {
        if(!busList.isEmpty()) {
            for(int i = 0; i < busList.size(); i++) {
                if(location.equals(busList.get(i).getArrivalLocation()) || location.equals(busList.get(i).getDepartLocation())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public String getOrigin() {
        return busList.getFirst().getDepartLocation();
    }
    
    public String getDestination() {
        if(!busList.isEmpty())
            return busList.getLast().getArrivalLocation();
        else
            return "Empty";
    }
    
    public LocalTime getDestinationTime() {
        return busList.getLast().getArrivalTime();
    }
    
    public BusJourney cloneJourney() {
        BusJourney newBusJourney;
        if(!this.busList.isEmpty())
            newBusJourney = new BusJourney(this.busList);
        else
            return null;
        
        return newBusJourney;
    }
    
    public int getNumberOfBusTrips() {
        return busList.size();
    }
    
    public double getTotalCost() {
        float totalCost = 0;
        
        for(int i = 0; i < busList.size(); i++) {
            totalCost += busList.get(i).getCost();
        }
        return totalCost;
    }
    
    public String toString() {
        String busJourney = "";
        busJourney += "Total Cost: $"+this.getTotalCost()+"\n";
        for(int i = 0; i < busList.size(); i++) {
            busJourney += busList.get(i).toString()+ "\n";
        }
        return busJourney;
    }
}
