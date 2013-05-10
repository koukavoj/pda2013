package cvut.fel.pda2013;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class SelectRecipients extends Activity {

	static Set<Integer> recipients = new HashSet<Integer>(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_recipients);
		recipients.clear();
		
		//napln listview userama
		ListView lv = (ListView) findViewById(R.id.listView1);		
		
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
			Intent intent = new Intent(this, WriteMessage.class);
			startActivity(intent);
		}

	}
	
}
