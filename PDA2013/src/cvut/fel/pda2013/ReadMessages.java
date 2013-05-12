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
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
		lv.setSelection(lv.getCount()-1);
		TextView tv = (TextView) findViewById(R.id.textView);
		u = (User) Helper.positionMessages.get(selectedMsg);
		
		tv.setText("Historie komunikace s "+u.getName());
		
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
		intent.putExtra("messages", msgs);
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
			GridView message = (GridView) convertView.findViewById(R.id.mainScreenMessage);
			//TextView message = (TextView) convertView.findViewById(R.id.mainScreenMessage);
			TextView date = (TextView) convertView.findViewById(R.id.mainScreenDate);
			
			int resId = convertView.getResources().getIdentifier(msgs.get(position).getFrom().getPhoto(), "drawable", context.getPackageName());
						
			img.setImageResource(resId);
			username.setText(msgs.get(position).getFrom().getName());
			//message.setText(msgs.get(position).getMessage());
			ImageAdapter imAd=new ImageAdapter(context,msgs.get(position).getMessage());
			message.setAdapter(imAd);
			int height=145*(imAd.getCount()/5+1);
			if(imAd.getCount()%5==0)height-=145;
			RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(600,height);
			lp.leftMargin=140;
			message.setLayoutParams(lp);
			message.setEnabled(false);
			
			String datum = msgs.get(position).getDatetime().substring(5);
			datum = datum.replace("-", ".");
			date.setText(datum);
			
			

			return convertView;
			
		}
		
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
}
