package cvut.fel.pda2013;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cvut.fel.pda2013.ReadMessages.ImageAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Activity activity;
	UserMessagesAdapter adapter = new UserMessagesAdapter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = this;

		Users.init();
//
		//vybrat uzivatele
		if (Login.loggedUser == -1) {
			chooseUser();
		}
		
		if (Login.loggedUser != -1){ 
			init();
		}
		
		//Toast.makeText(this, Messages.messages + "", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void send(View view) {

		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put("message", "this,is,test,message");
		mapa.put("from", "1");
		mapa.put("to", "2");

		NetWorkers net = new NetWorkers();
		net.sendMessage(mapa);

	}
 
	public void receive(View view) {
		List<Message> list = Messages.receiveMessages();

		Toast.makeText(this,
				"Stazeno " + list.size() + " zprav \n " + list.toString(),
				Toast.LENGTH_LONG).show();

	}

	public void newMessage(View view) {
		Intent intent = new Intent(this, SelectRecipients.class);
		startActivity(intent);
	}

	
	/**
	 * prepsani chovani tlacitka zpet na hlavni obrazovce
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
	       
	    	//opravdu ukoncit dialog
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Opravdu chcete aplikaci odsunout na pozadí?");
			builder.setPositiveButton("Ano", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               moveTaskToBack(true);
			           }
			       });
			builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //nic
			           }
			       });

			// Create the AlertDialog
			AlertDialog dialog = builder.create();
			dialog.show();
	    	
	    	
	    	
	    	
	    	
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * zobrazuje radky s jednotlivymi uzivateli a jejich posledni zpravu
	 * @author vojta
	 *
	 */
	public class UserMessagesAdapter extends BaseAdapter {

		Context context;
		
		public UserMessagesAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {			
			return Messages.messages.keySet().size();
		}

		@Override
		public Object getItem(int arg0) {
			return Messages.messages.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {			
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {


			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.main_screen_users, parent, false);
		
			//nalezeni posledni zpravy			
			User key = (User) Messages.messages.keySet().toArray()[position];
			ArrayList<Message> msgs = Messages.messages.get(key);
			
			//seradit zpravy
			Collections.sort(msgs, new MessageComparator());
			
			//do helperu ulozime zpravy tohoto uzivatele, abysme je v dalsim kroku mohli ziskat podle jejich pozice
			Helper.positionMessages.add(key);

			
			ImageView img = (ImageView) convertView.findViewById(R.id.mainScreenUserImage);
			TextView username = (TextView) convertView.findViewById(R.id.mainScreenUserName);
			//TODO TextView message = (TextView) convertView.findViewById(R.id.mainScreenMessage);
			GridView message = (GridView) convertView.findViewById(R.id.mainScreenMessage);
			//message.setEnabled(false);
			TextView date = (TextView) convertView.findViewById(R.id.mainScreenDate);
			
			int resId = convertView.getResources().getIdentifier(msgs.get(msgs.size()-1).getFrom().getPhoto(), "drawable", context.getPackageName());
						
			img.setImageResource(resId);
			username.setText(msgs.get(msgs.size()-1).getFrom().getName());
			//TODO message.setText(msgs.get(msgs.size()-1).getMessage());
			ImageAdapter imAd=new ImageAdapter(context,msgs.get(msgs.size()-1).getMessage());
			message.setAdapter(imAd);
			int height=145*(imAd.getCount()/5+1);
			if(imAd.getCount()%5==0)height-=145;
			RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(600,height);
			lp.leftMargin=90;
			message.setLayoutParams(lp);
			message.setOnItemClickListener(new MyGridOnItemClickListener(position));

			
			String datum = msgs.get(msgs.size()-1).getDatetime().substring(5);
			datum = datum.replace("-", ".");
			date.setText(datum);

			

			return convertView;
			
		}
		
	}
	
	
	private void init() {
		activity.setTitle(Login.getLoggedUser().getName());
		
		ListView lay = (ListView) findViewById(R.id.mainScreenListView);
		lay.setAdapter(adapter);
		
		if (!Helper.isTimerRunning){ 
			Helper.isTimerRunning = true;
			
			// pri prvnim spusteni naloadujeme ulozene zpravy
			Messages.loadFromMem(this);
			adapter.notifyDataSetChanged();
			
			
			// checkujeme nove zpravy kazdych 10s
			new CountDownTimer(999999999, 10000) {

			@Override
			public void onTick(long millisUntilFinished) {
				List<Message> list = Messages.receiveMessages();
				if (list.isEmpty()){
					//zadne nove zpravy
//					Toast.makeText(activity, "Zadne nove zpravy...",
//							Toast.LENGTH_SHORT).show();
				}
				else {
					//nove zpravy
					Toast.makeText(activity,
							"Nalezeno " + list.size() + " novych zprav",
							Toast.LENGTH_SHORT).show();

					//zpracovani zprav
					Messages.ParseMessages(list);
					
					//ulozime nove zpravy
					Messages.saveToMem(activity);
															
					//----------------- zobrazeni notifikace -----------------
					Notification.Builder mBuilder = new Notification.Builder(
							activity).setSmallIcon(R.drawable.ic_launcher)
							.setContentTitle("PDA2013")
							.setContentText("Prisla nova zprava");

					// Creates an explicit intent for an Activity in your app
					Intent resultIntent = new Intent(activity,MainActivity.class);

					TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
					
					stackBuilder.addParentStack(MainActivity.class);
					
					stackBuilder.addNextIntent(resultIntent);
					PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
					mBuilder.setContentIntent(resultPendingIntent);
					NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					
					mNotificationManager.notify(10, mBuilder.build());
					
					adapter.notifyDataSetChanged();
				}

			}

			@Override
			public void onFinish() {

			}

		}.start();

	}
		
		
		
		
		//Listener na kliknuti na zpravu v ListView
		lay.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {			
				ReadMessages.selectedMsg = position;
				
				Intent intent = new Intent(activity, ReadMessages.class);
				startActivity(intent);
				
			}
		});
		

		
		
	}
	

	private void chooseUser() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Vyberte uživatele");
		builder.setPositiveButton("vojta", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               Login.loggedUser = 1;
		               init();
		           }
		       });
		builder.setNegativeButton("honza", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Login.loggedUser = 2;
		        	   init();
		           }
		       });

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    private String [] mess;

	    public ImageAdapter(Context c, String message) {
	        mContext = c;
	        mess=message.split(",");
	    }

	    public int getCount() {
	        return mess.length;
	    }

	    public Object getItem(int position) {
	        return mess[position];
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.pictograms_layout, parent, false);
				
				ImageView pictImage = (ImageView) convertView.findViewById(R.id.pictImage);
				TextView pictDesc = (TextView) convertView.findViewById(R.id.pictDesc);

				String pic = mess[position];

				int photoId = convertView.getResources().getIdentifier(pic, "drawable",mContext.getPackageName());

				pictDesc.setText(pic);

				pictImage.setImageResource(photoId);
				pictImage.setPadding(5, 5, 5, 5);

				return convertView;
			} else
				return (View) convertView;
		}
	
	
	}
	
	class MyGridOnItemClickListener implements OnItemClickListener {
		int pos;
		
		MyGridOnItemClickListener(int position){
			pos=position;
			
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)  {			
			ReadMessages.selectedMsg = pos;
			
			Intent intent = new Intent(activity, ReadMessages.class);
			startActivity(intent);
			
		}


	}
	
}
