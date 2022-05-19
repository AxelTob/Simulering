import java.util.*;
import java.io.*;

class TransmissionCounter extends Proc {
    public int totalTransmissions = 0;

	public void TreatSignal(Signal x)  {
        if(x.signalType == ARRIVAL) {
            ++totalTransmissions;
        }
	}
}
