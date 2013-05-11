package cvut.fel.pda2013;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WriteMessage extends Activity {

	List<User> recipients = new ArrayList<User>();

	// aktualne napsane piktogramy
	List<Pictogram> writtenPicts = new ArrayList<Pictogram>();

	// zobrazene piktogramy pro psani (vyber)
	List<Pictogram> shownPicts = new ArrayList<Pictogram>();

	WriteMessageGridViewAdapter writeAdapter;
	ShowPictogramsGridViewAdapter showAdapter;

	//show picts gridView
	GridView showPictsGridView;
		
	//indikuje jestli odpovidame na zpravu nebo piseme novou
	public static boolean reply = false;
	
	//obsahuje posledni zpravu, pokud odepisujeme
	public static Message messageToReply = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_message);

		TextView t = (TextView) findViewById(R.id.recipients);
		TextView t1 = (TextView) findViewById(R.id.prijemci);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.lastMessage);
		
		//nastaveni prijemcu
		for (int i : SelectRecipients.recipients) {
			User u = Users.getUserById(i);
			recipients.add(u);
			t.setText(t.getText() + " " + u.getName() + ";");
		}
		
		
		//zobrazeni posledni zpravy v reply modu
		//nebo zobrazeni prijemcu
		if (reply) {			
			rl.setVisibility(View.VISIBLE);			
			t.setVisibility(View.INVISIBLE);
			t1.setVisibility(View.INVISIBLE);
			
			GridView gv = (GridView) findViewById(R.id.lastMessageView);
			gv.setAdapter(new LastMessagesAdapter(this));
			
			
		} else {			

			rl.setVisibility(View.INVISIBLE);
			t.setVisibility(View.VISIBLE);
			t1.setVisibility(View.VISIBLE);
			
		}
		
		
		
		
		writeAdapter = new WriteMessageGridViewAdapter(this);
		GridView g = (GridView) findViewById(R.id.gridView1);
		g.setAdapter(writeAdapter);

		// jednotlive piktogramy - dolni cast screenu
		showPictsGridView = (GridView) findViewById(R.id.gridView2);
		shownPicts = Pictograms.getFurniture();		
		showAdapter = new ShowPictogramsGridViewAdapter(this);
		showPictsGridView.setAdapter(showAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.write_message, menu);
		return true;
	}
	
	//natahne a zobrazi piktogramy nabidku
	public void getFurniture(View v) {		
//		clearShownPicts();		
		shownPicts = new ArrayList<Pictogram>(Pictograms.getFurniture());
		showAdapter = null;
		showAdapter = new ShowPictogramsGridViewAdapter(this);
		showPictsGridView.setAdapter(showAdapter);
	}
	
	//natahne a zobrazi piktogramy jidla
	public void getFood(View v) {
//		clearShownPicts();
		shownPicts = new ArrayList<Pictogram>(Pictograms.getFood());		
		showAdapter = null;
		showAdapter = new ShowPictogramsGridViewAdapter(this);
		showPictsGridView.setAdapter(showAdapter);
	}
	
	//natahne a zobrazi piktogramy zajmen
	public void getPronouns(View v) {
//		clearShownPicts();
		shownPicts = new ArrayList<Pictogram>(Pictograms.getPronouns());		
		showAdapter = null;
		showAdapter = new ShowPictogramsGridViewAdapter(this);
		showPictsGridView.setAdapter(showAdapter);
		}
	
	//odeslani zpravy
	public void send(View v) {
		
		//otestujem konektivitu
		if (!Helper.isNetworkConnected(this)) {
			Toast.makeText(this, "Neni internetove pripojeni! Zprava nebyla odeslana.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		List<String> msgs = new ArrayList<String>();
		
		String pictogramsText = "";
		
		for (Pictogram p : writtenPicts) {
			pictogramsText += p.getImageName() + ",";
		}
		
		pictogramsText = pictogramsText.substring(0, pictogramsText.length()-1);
		
		NetWorkers net = new NetWorkers();
		
		for (User u : recipients) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("from", Login.loggedUser+"");
			map.put("to", u.getId()+"");
			map.put("message", pictogramsText);
			
			net.sendMessage(map);
			
			//ulozeni zpravy do historie
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String dat = dateFormat.format(date);
			Message m = new Message(Login.getLoggedUser(), u, pictogramsText, dat);
			Messages.addMessageForUser(m, u);
			Messages.saveToMem(this);
			
			
		}
				
		Toast.makeText(this, "Zprava odeslana!", Toast.LENGTH_SHORT).show();
									
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
	}
	
	
	
	
	
	/**
	 * Adapter pro zobrazovani napsanych piktogramu v hornim gridView
	 * @author vojta
	 *
	 */
	public class WriteMessageGridViewAdapter extends BaseAdapter {

		private Context context;

		public WriteMessageGridViewAdapter(Context ctx) {
			context = ctx;
		}

		public View getView(int position, View convertView, ViewGroup parent) {			
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.pictograms_layout,parent, false);
				
				ImageView pictImage = (ImageView) convertView.findViewById(R.id.pictImage);
				TextView pictDesc = (TextView) convertView.findViewById(R.id.pictDesc);

				Pictogram pic = writtenPicts.get(position);

				int photoId = convertView.getResources().getIdentifier(pic.getImageName(), "drawable",context.getPackageName());

				pictDesc.setText(pic.getDesc());
				
				pictImage.setImageResource(photoId);
				pictImage.setId(position);
				
				//listener na smazani kliknuteho piktogramu z psane zpravy
				pictImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						writtenPicts.remove(v.getId());
						writeAdapter.notifyDataSetChanged();
					}
				});
				

				return convertView;
			
		}

		@Override
		public int getCount() {
			return writtenPicts.size();
		}

		@Override
		public Object getItem(int position) {
			return writtenPicts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return writtenPicts.get(position).getId();
		}

	}

	/**
	 * Adapter pro zobrazeni moznych piktogramu k psani zpravy v dolnim gridView
	 * @author vojta
	 *
	 */
	public class ShowPictogramsGridViewAdapter extends BaseAdapter {

		private Context context;

		public ShowPictogramsGridViewAdapter(Context ctx) {
			context = ctx;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.pictograms_layout, parent, false);
				
				ImageView pictImage = (ImageView) convertView.findViewById(R.id.pictImage);
				TextView pictDesc = (TextView) convertView.findViewById(R.id.pictDesc);

				Pictogram pic = shownPicts.get(position);

				int photoId = convertView.getResources().getIdentifier(pic.getImageName(), "drawable",context.getPackageName());

				pictDesc.setText(pic.getDesc());

				pictImage.setImageResource(photoId);
				pictImage.setClickable(true);
				pictImage.setId(pic.getId());

				// listener na kliknuti na piktogram - zapise ho
				pictImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						writtenPicts.add(Pictograms.getPictById(v.getId()));	
						writeAdapter.notifyDataSetChanged();
					}
				});

				return convertView;
			} else
				return (View) convertView;
		}

		@Override
		public int getCount() {
			return shownPicts.size();
		}

		@Override
		public Object getItem(int position) {
			return shownPicts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return shownPicts.get(position).getId();
		}

	}

	/**
	 * adapter na zobrazeni posledni zpravy pri odpovedi
	 * @author vojta
	 *
	 */
	public class LastMessagesAdapter extends BaseAdapter {

		Context context;
		
		public LastMessagesAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {			
			return 1;
		}

		@Override
		public Object getItem(int arg0) {
			return messageToReply;
		}

		@Override
		public long getItemId(int arg0) {			
			return messageToReply.hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.main_screen_users, parent, false);
					
			ImageView img = (ImageView) convertView.findViewById(R.id.mainScreenUserImage);
			TextView username = (TextView) convertView.findViewById(R.id.mainScreenUserName);
			TextView message = (TextView) convertView.findViewById(R.id.mainScreenMessage);
			TextView date = (TextView) convertView.findViewById(R.id.mainScreenDate);
			
			int resId = convertView.getResources().getIdentifier(messageToReply.getFrom().getPhoto(), "drawable", context.getPackageName());
						
			img.setImageResource(resId);
			username.setText(messageToReply.getFrom().getName());
			message.setText(messageToReply.getMessage());
			
			String datum = messageToReply.getDatetime().substring(5);
			datum = datum.replace("-", ".");
			date.setText(datum);
						

			return convertView;
			
		}
		
	}
	
}
