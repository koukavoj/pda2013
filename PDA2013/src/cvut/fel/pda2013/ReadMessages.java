package cvut.fel.pda2013;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ReadMessages extends Activity {

	//pozice kliknute zpravy
	public static int selectedMsg = -1;	
	
	private static User u = null; 
	
	@Override
 	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_messages);
		
		ListView lv = (ListView) findViewById(R.id.messageHistory);
		lv.setAdapter(new UserMessagesHistoryAdapter(this));
		
		u = (User) Helper.positionMessages.get(selectedMsg);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_messages, menu);
		return true;
	}

	public void reply(View v) {
		SelectRecipients.recipients.clear();
		SelectRecipients.recipients.add(u.getId());
		WriteMessage.reply = true;
		
		ArrayList<Message> msgs = Messages.messages.get(u);
		Collections.sort(msgs, new MessageComparator());
		WriteMessage.messageToReply = msgs.get(msgs.size()-1);
		
		Intent intent = new Intent(this, WriteMessage.class);
		startActivity(intent);
		
		
	}
	
	public class UserMessagesHistoryAdapter extends BaseAdapter {

		Context context;
		
		public UserMessagesHistoryAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			User key = (User) Helper.positionMessages.get(selectedMsg);
			ArrayList<Message> msgs = Messages.messages.get(key);
			
			return msgs.size();
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
			User key = (User) Helper.positionMessages.get(selectedMsg);
			ArrayList<Message> msgs = Messages.messages.get(key);
			
			//seradit zpravy
			Collections.sort(msgs, new MessageComparator());
			
			//do helperu ulozime zpravy tohoto uzivatele, abysme je v dalsim kroku mohli ziskat podle jejich pozice
			Helper.positionMessages.add(key);

			
			ImageView img = (ImageView) convertView.findViewById(R.id.mainScreenUserImage);
			TextView username = (TextView) convertView.findViewById(R.id.mainScreenUserName);
			TextView message = (TextView) convertView.findViewById(R.id.mainScreenMessage);
			TextView date = (TextView) convertView.findViewById(R.id.mainScreenDate);
			
			int resId = convertView.getResources().getIdentifier(msgs.get(position).getFrom().getPhoto(), "drawable", context.getPackageName());
						
			img.setImageResource(resId);
			username.setText(msgs.get(position).getFrom().getName());
			message.setText(msgs.get(position).getMessage());
			
			String datum = msgs.get(position).getDatetime().substring(5);
			datum = datum.replace("-", ".");
			date.setText(datum);
			
			

			return convertView;
			
		}
		
	}
	
}
