package busjourneyplanner;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class JourneyPlanner {
    private Map<String, Set<BusTrip>> locationMap;
    
    public JourneyPlanner() {
        locationMap = new HashMap<>();
    }
    
    public boolean add(BusTrip bus) {
        boolean edited = false;
        if(!locationMap.containsKey(bus.getDepartLocation())) {
            HashSet<BusTrip> newSet = new HashSet();
            newSet.add(bus);
            edited = true;
            locationMap.put(bus.getDepartLocation(), newSet);
            return edited;
        }
        else {
            HashSet<BusTrip> newSet = (HashSet)locationMap.get(bus.getDepartLocation());

            if(!newSet.contains(bus)) {
                newSet.add(bus);
                edited = true;
            }

            locationMap.replace(bus.getDepartLocation(), newSet);
            return edited;
        }
    }
    
    public LinkedList<BusJourney> getPossibleJourneys(String startLocation, LocalTime startTime, String endLocation, LocalTime endTime) {
        LinkedList<BusJourney> journeyList = new LinkedList();
        BusJourney currentJourney = new BusJourney();
        
        findPaths(startLocation, startTime, endLocation, endTime, currentJourney, journeyList);
        
        return journeyList;
    }
    
    private void findPaths(String currentLocation, LocalTime currentTime, String endLocation, LocalTime endTime, BusJourney currentJourney, LinkedList<BusJourney> journeyList) {
        if(!currentLocation.equals(endLocation)) {
            HashSet<BusTrip> neighbors = (HashSet)locationMap.get(currentLocation);
        
            Iterator iterator = neighbors.iterator();
            while(iterator.hasNext()) {
                BusTrip currentBusTrip = (BusTrip)iterator.next();
                
                if(currentTime.compareTo(currentBusTrip.getDepartTime()) <= 0 && currentBusTrip.getArrivalTime().compareTo(endTime) <= 0) {
                    if(currentBusTrip.getArrivalLocation().equals(endLocation)) {
                        if(currentJourney.addBus(currentBusTrip)) {
                            journeyList.add(currentJourney.cloneJourney());
                            currentJourney.removeLastTrip();
                        }
                    }
                    else {
                        if(currentJourney.addBus(currentBusTrip))
                            findPaths(currentBusTrip.getArrivalLocation(), currentBusTrip.getArrivalTime(), endLocation, endTime, currentJourney, journeyList);
                    }
                }
            }
            if(currentJourney.getDestination().equals(endLocation)) {
                journeyList.add(currentJourney.cloneJourney());
                currentJourney.removeLastTrip();
            }
            else {
                currentJourney.removeLastTrip();
            }
        }
        else {
            if(currentLocation.equals(endLocation) && currentTime.compareTo(endTime) <= 0) {
                journeyList.add(currentJourney.cloneJourney());
            }
        }
    }
}
