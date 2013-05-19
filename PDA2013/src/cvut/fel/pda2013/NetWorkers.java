package cvut.fel.pda2013;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;

public class NetWorkers {	
	public static String SENDURL = "http://88.102.138.83:8080/send.php";
	public static String RECEIVEURL = "http://88.102.138.83:8080/receive.php";

	/**
	 * Odeslani nove zpravy na server
	 * @param url
	 * @param mapa
	 */
	public void sendMessage(Map<String, String> mapa) {
		new SendMessageTask().execute(SENDURL,mapa);
	}
	
	/**
	 * Prijem novych zprav pr uzivatele user
	 * @param user Pro jakeho uzivatele prijmout zpravy
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public String receiveMessage(String user) throws InterruptedException, ExecutionException {
		ReceiveMessageTask r = new ReceiveMessageTask();		
		AsyncTask a =  r.execute(user,RECEIVEURL);
		return (String) a.get();
	}
		
	/**
	 * Asynchronni trida pro invokaci odeslani zpravy na server - bezi ve vlastnim vlakne
	 * @author vojta
	 *
	 */
	private class SendMessageTask extends AsyncTask {
		@Override
		protected Object doInBackground(Object... args) {
			String url = (String) args[0];
			Map<String, String> mapa = (HashMap<String, String>) args[1];

			
			try {
				send(url, mapa);
			} catch (Exception e) { 
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}

	}

	/**
	 * Odesle zpravu na server, spousti se z vlastniho vlakna SendMessageTask
	 * @param url
	 * @param data
	 * @throws Exception
	 */
	private void send(String url, Map<String, String> data) throws Exception {
		URL siteUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) siteUrl.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);

		DataOutputStream out = new DataOutputStream(conn.getOutputStream());

		Set<String> keys = data.keySet();
		Iterator<String> keyIter = keys.iterator();
		String content = "";
		for (int i = 0; keyIter.hasNext(); i++) {
			Object key = keyIter.next();
			if (i != 0) {
				content += "&";
			}
			content += key + "=" + URLEncoder.encode(data.get(key), "UTF-8");
		}
		System.out.println(content);
		out.writeBytes(content);
		out.flush();
		out.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line = "";
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
		in.close();
	}

	/**
	 * Vlakno, ktere zajisti odeslani pozadavku a prijeti novych zprav
	 * @author vojta
	 *
	 */
	private class ReceiveMessageTask extends AsyncTask {
		@Override
		protected Object doInBackground(Object... args) {
			String user = (String) args[0];	
			String url = (String) args[1];

			String ret = "";
			
			try {
				ret = receive(url, user);
			} catch (Exception e) {
				System.out.println("BUM BUM BUM BUM BUM BUM BUM BUM");
			}

			return ret;
		}

	}
	
	/**
	 * Zepta se serveru na nove zpravy a stahne je. Je spousten z vlakna ReceiveMessageTask
	 * @param url Odkus se stahuji zpravy
	 * @param user Pro jakeho uzivatele chceme nove zpravy
	 * @return String se zpravama
	 * @throws Exception
	 */
	private String receive(String url, String user) throws Exception {
		URL siteUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) siteUrl.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);

		DataOutputStream out = new DataOutputStream(conn.getOutputStream());

		String content = "user" + "=" + URLEncoder.encode(user, "UTF-8");
				
		out.writeBytes(content);
		out.flush();
		out.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
	
		String ret = "";
		
		while ((line = in.readLine()) != null) {			
//			System.out.println(line);
			ret += line;
		}
		in.close();
		
		return ret;
		
	}
	
	
	
}

