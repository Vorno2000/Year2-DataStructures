package busjourneyplanner;

import java.util.Comparator;

public class JourneyComparator implements Comparator<BusJourney>{
    
    @Override
    public int compare(BusJourney o1, BusJourney o2) {
        return o1.getDestinationTime().compareTo(o2.getDestinationTime());
    }
}
