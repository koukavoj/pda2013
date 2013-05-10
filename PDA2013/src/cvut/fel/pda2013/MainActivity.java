package cvut.fel.pda2013;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends Activity {

	Activity activity;
	UserMessagesAdapter adapter = new UserMessagesAdapter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = this;

		Users.init();
		
		ListView lay = (ListView) findViewById(R.id.mainScreenListView);
		lay.setAdapter(adapter);

		// checkujeme nove zpravy kazde 3s
		new CountDownTimer(999999999, 30000) {

			@Override
			public void onTick(long millisUntilFinished) {
				List<Message> list = Messages.receiveMessages();
				if (list.isEmpty())
					//zadne nove zpravy
					Toast.makeText(activity, "Zadne nove zpravy...",
							Toast.LENGTH_SHORT).show();
				else {
					//nove zpravy
					Toast.makeText(activity,
							"Nalezeno " + list.size() + " novych zprav",
							Toast.LENGTH_SHORT).show();

					//zpracovani zprav
					Messages.ParseMessages(list);
					
					
					
					
					//----------------- zobrazeni notifikace -----------------
					NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
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

		
		//onclicklistener na prepinani adres pro pristup z domova nebo venku
		CheckBox ch = (CheckBox) findViewById(R.id.domaCheckBox);
		ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					System.out.println("Menim na http://10.0.0.105");
					NetWorkers.RECEIVEURL = "http://10.0.0.105/receive.php";
					NetWorkers.SENDURL = "http://10.0.0.105/send.php";
				}
				else { 					
					System.out.println("Menim na http://88.102.138.83:8080");
					NetWorkers.RECEIVEURL = "http://88.102.138.83:8080/receive.php";
					NetWorkers.SENDURL = "http://88.102.138.83:8080/send.php";
				}
			}
		});
		
		
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

	
	public class UserMessagesAdapter extends BaseAdapter {

		Context context;
		
		public UserMessagesAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			return Messages.messages.size();
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
		
			
			//TODO - tohle asi uplne nebude fungovat :)
			//chce to zobrazit posledni zpravu (podle datumu), v tomhle setu neni zaruceny poradi
			User key = (User) Messages.messages.keySet().toArray()[position];
			ArrayList<Message> msgs = Messages.messages.get(key);
			
			ImageView img = (ImageView) convertView.findViewById(R.id.mainScreenUserImage);
			TextView username = (TextView) convertView.findViewById(R.id.mainScreenUserName);
			TextView message = (TextView) convertView.findViewById(R.id.mainScreenMessage);
			TextView date = (TextView) convertView.findViewById(R.id.mainScreenDate);
			
			int resId = convertView.getResources().getIdentifier(msgs.get(0).getFrom().getPhoto(), "drawable", context.getPackageName());
						
			img.setImageResource(resId);
			username.setText(msgs.get(0).getFrom().getName());
			message.setText(msgs.get(0).getMessage());
			date.setText(msgs.get(0).getDatetime());
			

			return convertView;
			
		}
		
	}
	
}
