import java.util.*;
import java.io.*;

class Gateway extends Proc {
    public int totalTransmissions;
    public int successfulTransmissions;
    private double last = -1e100;
    private boolean collision = false;
    private double transmissionTime = 1.0; // FIXME

	public void TreatSignal(Signal x)  {
        switch(x.signalType) {
        case ARRIVAL: {
            if(time > last+transmissionTime){
                collision = false;
                SignalList.SendSignal(
                        TRANSMISSION_DONE,
                        this,
                        time+transmissionTime);
            }
            else {
                collision = true;
            }

            last = time;
            ++totalTransmissions;
        } break;
        case TRANSMISSION_DONE: {
            if(collision) {
                return;
            }

            ++successfulTransmissions;
        } break;
        }
	}
}
