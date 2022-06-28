package util.io;

public class TimeConverter {
    public static String splitToComponentTimes(long longVal)
	{
	    int hours = (int) (longVal / 3600000);
	    int remainder = (int) longVal - hours * 3600000;
	    int mins = remainder / 60000;
	    remainder = remainder - mins * 60000;
	    int secs = remainder / 1000;
	    remainder = remainder - secs*1000;
	    
	    String result = "";
	    if(hours<10) {
	    	result += "0" + hours;
	    }
	    else {
	    	result += hours;
	    }
	    
	    result += ":";
	    
	    if(mins<10) {
	    	result += "0" + mins;
	    }
	    else {
	    	result += mins;
	    }
	    result += ":";
	    if(secs<10) {
	    	result += "0" + secs;
	    }
	    else {
	    	result += secs;
	    }
	    result += ".";
	    
	    if(remainder<10) {
	    	result += "00" + secs;
	    }
	    else {
	    	if(remainder<100) {
	    		result += "0" + remainder;
	    	}
	    	else result += remainder;
	    }
	    
	    return result;
	}
}
