package cvut.fel.pda2013;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Helper {

	public static boolean isTimerRunning = false;
	public static List<User> positionMessages = new ArrayList<User>(); 
	
	public static boolean isNetworkConnected(Context ctx) {
		  ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  if (ni == null) {		   
		   return false;
		  } else
		   return true;
		 }
	
}
