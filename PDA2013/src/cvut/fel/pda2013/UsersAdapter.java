package cvut.fel.pda2013;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

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
		
		int resId = convertView.getResources().getIdentifier(u.getPhoto(), "drawable", context.getPackageName());
		
		image.setImageResource(resId);
		
		nameView.setText(u.getName());
		System.out.println(chckBox == null);
		chckBox.setId(u.getId());
		
		chckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				System.out.println("Checked: ");
				CheckBox b = (CheckBox) buttonView;
				
				if (isChecked)
					SelectRecipients.recipients.add(b.getId());
				if (!isChecked)
					SelectRecipients.recipients.remove(b.getId());
				
			}
		});		

		return convertView;
	}


}
