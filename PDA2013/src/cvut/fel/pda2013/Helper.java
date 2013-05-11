package cvut.fel.pda2013;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Helper {

	public static boolean isTimerRunning = false;
	
	public static boolean isNetworkConnected(Context ctx) {
		  ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  if (ni == null) {		   
		   return false;
		  } else
		   return true;
		 }
	
}
