package cvut.fel.pda2013;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class Messages {

	public static Map<User, ArrayList<Message>> messages = new HashMap<User, ArrayList<Message>>();
		
	public static List<Message> getMessagesForUser(User u) {				
		for (Entry<User, ArrayList<Message>> entry : messages.entrySet()) {
			if (entry.getKey() == u) return entry.getValue();
		}
		
		return null;
	}
	
	private static void addMessageForUser(Message msg, User u) {
		if (messages.get(u) != null) {
			messages.get(u).add(msg);
		}else {
			ArrayList<Message> list = new ArrayList<Message>();
			list.add(msg);
			messages.put(u, list);
		}
	}	
	
	public static void ParseMessages(List<Message> msgs) {
		for(Message m : msgs) {
			addMessageForUser(m, m.getFrom());
		}
	}
	
	public static List<Message> receiveMessages() {
		List<Message> list = new ArrayList<Message>();
		
		NetWorkers net = new NetWorkers();
		try {
			String str = net.receiveMessage(Login.loggedUser+"");			
			for(String s : str.split("#")) {
				String[] splt = s.split(";");
				User from = Users.getUserById(Integer.parseInt(splt[0])); 
				User to = Login.getLoggedUser();				
				
				Message msg = new Message(from, to, splt[1]+"", splt[2]+"");
				
				list.add(msg);
			}
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {}
		return list;
		
	}

	
	public static void loadFromMem(Context ctx) {
		String FILENAME = "messages";	
		
        try {
        	FileInputStream fos = ctx.openFileInput(FILENAME);
			ObjectInputStream s = new ObjectInputStream(fos);
			
			Map<User, ArrayList<Message>> map = (Map<User, ArrayList<Message>>) s.readObject();
			
			s.close();
			fos.close();
			
			messages = map;			
			
		} catch (IOException e) {
			Toast.makeText(ctx, "I/O chyba pri cteni \n" + e.getStackTrace(), Toast.LENGTH_SHORT).show();			
		} catch (ClassNotFoundException e) {
			Toast.makeText(ctx, "ClassNotFoundException \n" + e.getStackTrace(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (Exception e) {
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	public static void saveToMem(Context ctx) {
		String FILENAME = "messages";	
		
        try {
        	FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream s = new ObjectOutputStream(fos);
			
			s.writeObject(Messages.messages);
			//Toast.makeText(ctx, ctx.getFilesDir().toString(), Toast.LENGTH_LONG).show();
			s.close();
			fos.close();
		} catch (IOException e) {
			Toast.makeText(ctx, "Chyba pri ukladani \n" + e.getStackTrace(), Toast.LENGTH_SHORT).show();			
		}
        
	}
	
}
