import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateM2X implements MqttCallback {
	MqttClient client;
	int harmfulCounter = 0;
	String MosquittoBrokerUrl = "tcp://54.218.1.206:2880";
	public int pressure1 = 0;
	public int pressure2 = 0;
	public int pressure3 = 0;
	public int pressure4 = 0;
	public int distance = 0;
	public int angle = 0;
	public int temperature = 0;
	int i = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new UpdateM2X().doDemo();
	}
	public void doDemo() {
	    try {
	        client = new MqttClient(MosquittoBrokerUrl, "Sending");
	        client.connect();
	        client.setCallback(this);	
	        String[] topicFilter = {"/smartchair/data","/smartchair/alert"};
	        client.subscribe(topicFilter);
	        System.in.read();
	        client.disconnect();
	        client.close();
	    } catch (MqttException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void messageArrived(String topic, MqttMessage message)
	        throws Exception {
		try{
			String s[] = topic.split("/");
			if(s[2].equals("data")){
				System.out.println("data " + message.toString());
				JSONObject jsonDoc = new JSONObject(message.toString());
				int pressure1 = jsonDoc.getInt("pressure1");
				int pressure2 = jsonDoc.getInt("pressure2");
				int pressure3 = jsonDoc.getInt("pressure3");
				int pressure4 = jsonDoc.getInt("pressure4");
				int temperature = jsonDoc.getInt("temperature");
				int distance = jsonDoc.getInt("distance1");
				int angle = jsonDoc.getInt("angle");
				this.pressure1 = pressure1;
				this.pressure2 = pressure2;
				this.pressure3 = pressure3;
				this.pressure4 = pressure4;
				this.temperature = temperature;
				this.angle = angle;
				this.distance = distance;				
			}
			else if(s[2].equals("alert")){
				System.out.println("alert " + message.toString());
				JSONObject jsonDoc = new JSONObject(message.toString());
				String isHarmful = jsonDoc.getString("isHarmful");
				int isHarm = 0;
				if(isHarmful.equals("true"))
				{
					isHarm = 1;
				}
				else
				{
					isHarm = 0;
				}
				JSONObject ret = postRequest(pressure1, pressure2, pressure3, pressure4, distance, angle, isHarm);
				updateCloudant(message.toString(), ret);
				//cloudant function
			}	
			
		}
		catch(Exception ex){
			System.out.println(ex.toString()); 
		}
		//todo update mongo
	}
	@Override
	public void connectionLost(Throwable cause) {
	    // TODO Auto-generated method stub
		
	}
	
	public void updateCloudant(String msg,JSONObject json)
	{
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		json.put("data", msg);
		RequestBody body = RequestBody.create(mediaType, json.toString());
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    	date.setTimeZone(TimeZone.getTimeZone("PDT"));
    	String timestamp= date.format(new Date());
    	
		Request request = new Request.Builder()
		  .url("https://ea413991-75c3-4a37-9414-6daf9d881cad:FqowPO7BT4Hm@7e28982a-128f-4a93-bf02-b5ae59ba67ac-bluemix.cloudant.com/newdb/"+timestamp)
		  .put(body)
		  .addHeader("content-type", "application/json")
		  .addHeader("cache-control", "no-cache")
		  .build();
		i++;	
		try {
			Response response = client.newCall(request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub
		
	}
    
    public static JSONObject postRequest(int p1, int p2, int p3, int p4, int d , int angle, int isHarmful) throws IOException
    {
    	okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

    	okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
    	//java.util.Date date= new java.util.Date();
    	//Timestamp t = new Timestamp(date.getTime());
    	
    	SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    	date.setTimeZone(TimeZone.getTimeZone("PDT"));
    	String timestamp= date.format(new Date());
    	//String timestamp = date.toString();
    	
    	JSONObject jo = new JSONObject();
    	
    	JSONObject  pressure1 = new JSONObject();
    	pressure1.put("timestamp", timestamp);
    	pressure1.put("value", p1);
    	JSONArray pArr1 = new JSONArray();
    	pArr1.put(pressure1);
    	
    	JSONObject  pressure2 = new JSONObject();
    	pressure2.put("timestamp", timestamp);
    	pressure2.put("value", p1);
    	JSONArray pArr2 = new JSONArray();
    	pArr2.put(pressure2);
    	
    	JSONObject  pressure3 = new JSONObject();
    	pressure3.put("timestamp", timestamp);
    	pressure3.put("value", p1);
    	JSONArray pArr3 = new JSONArray();
    	pArr3.put(pressure3);
    	
    	JSONObject  pressure4 = new JSONObject();
    	pressure4.put("timestamp", timestamp);
    	pressure4.put("value", p1);
    	JSONArray pArr4 = new JSONArray();
    	pArr4.put(pressure4);
    	
    	JSONObject  dist = new JSONObject();
    	dist.put("timestamp", timestamp);
    	dist.put("value", p1);
    	JSONArray distArr = new JSONArray();
    	distArr.put(dist);
    	
    	JSONObject  ang = new JSONObject();
    	ang.put("timestamp", timestamp);
    	ang.put("value", p1);
    	JSONArray angArr = new JSONArray();
    	angArr.put(dist);
    	
    	JSONObject  isHa = new JSONObject();
    	isHa.put("timestamp", timestamp);
    	isHa.put("value", p1);
    	JSONArray isHaArr = new JSONArray();
    	isHaArr.put(dist);

    	JSONObject values = new JSONObject();
    	values.put("p1", pArr1);
    	values.put("p2", pArr2);
    	values.put("p3", pArr3);
    	values.put("p4", pArr4);
    	values.put("d", distArr);
    	values.put("angle", angArr);
    	values.put("isharmful", isHaArr);
    	
    	
    	JSONObject finale = new JSONObject();
    	finale.put("values", values);
    	//System.out.println(finale.toString());
    	
    	RequestBody body = RequestBody.create(mediaType, 
    			finale.toString());
    	Request request = new Request.Builder()
    	  .url("http://api-m2x.att.com/v2/devices/79deabe7c1a7c3df4f40a1400c03db65/updates")
    	  .post(body)
    	  .addHeader("content-type", "application/json")
    	  .addHeader("x-m2x-key", "aad4bd8f827fc647e7b5f675399cd4e2")
    	  .build();

    	Response response = client.newCall(request).execute();
    	
    	return finale;
    }

}
