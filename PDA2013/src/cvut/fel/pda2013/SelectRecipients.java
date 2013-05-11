package cvut.fel.pda2013;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SelectRecipients extends Activity {

	static Set<Integer> recipients = new HashSet<Integer>(); 
	ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_recipients);
		recipients.clear();
		
		//napln listview userama
		ListView lv = (ListView) findViewById(R.id.listView1);		
		this.lv = lv;		
		
		lv.setAdapter(new UsersAdapter(Users.getAllUsers(), this));
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_recipients, menu);
		return true;
	}

	public void confirm(View view) {
		
		if (recipients.isEmpty()) {
			Toast.makeText(this, "Nejsou vybrání žádní pøíjemci.", Toast.LENGTH_SHORT).show();
		}else {
			WriteMessage.reply = false;			
			Intent intent = new Intent(this, WriteMessage.class);
			startActivity(intent);
		}

	}
	
		
	/**
	 * Adapter, ktery z objektu User naplni user_layout
	 * @author vojta
	 *
	 */
	public class UsersAdapter extends ArrayAdapter<User> {

		private List<User> userList;
		private Context context;

		public UsersAdapter(List<User> users, Context ctx) {
			super(ctx, R.layout.users_layout, users);
			this.userList = users;
			this.context = ctx;
		}

		
		
		public View getView(int position, View convertView, ViewGroup parent) {
		
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.users_layout, parent, false);
		
			// Now we can fill the layout with values
			ImageView image = (ImageView) convertView.findViewById(R.id.userImage);
			TextView nameView = (TextView) convertView.findViewById(R.id.userName);
			CheckBox chckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
			
			User u = userList.get(position);		
			
			
			//podminka pro skupiny
			if (u.getId() == 0 || u.getId() == 4) {

				//odstraneni obrazku u skupiny a zarovnani vlevo
				image.setVisibility(ImageView.INVISIBLE);			
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) nameView.getLayoutParams();
			    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

				nameView.setLayoutParams(params);
			} else {
			
				int resId = convertView.getResources().getIdentifier(u.getPhoto(), "drawable", context.getPackageName());		
				image.setImageResource(resId);
				
				//nastavit odsazeni imageview			
			    MarginLayoutParams marginParams = new MarginLayoutParams(image.getLayoutParams());
			    marginParams.setMargins(80, 1, 1, 1);
			    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
			    image.setLayoutParams(layoutParams);
				
				
			}
			nameView.setText(u.getName());
			
			chckBox.setId(u.getId());
			
			chckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {				
					CheckBox b = (CheckBox) buttonView;
					
					if (b.getId() == 0 || b.getId() == 4) { 
					//pokud vyberu checkbox id=0 || id==4, tak se oznaci prislusne podcheckboxy teto skupiny	
											
						if (b.getId() == 0) 
							for (int i = 1; i<=3; i++) 								
								((CheckBox) (findViewById(i))).setChecked(isChecked);
							

														
						if (b.getId() == 4)							
							for (int i = 5; i<=7; i++)
								((CheckBox) (findViewById(i))).setChecked(isChecked);
							
				
						
						
					} else {
					
						//jednotlivy vyber recipientu (ne pres skupiny)
						if (isChecked)
							SelectRecipients.recipients.add(b.getId());
						if (!isChecked)
							SelectRecipients.recipients.remove(b.getId());
					}
				}
			});		

			return convertView;
		}


	}
	
	
}
