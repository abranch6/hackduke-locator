package deo.locater;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DirectionAPI extends AsyncTask<String, Void, Document>{
	
	GoogleMap myMap;
	ArrayList <LatLng> directionList;
	PolylineOptions line;
	
	public DirectionAPI(GoogleMap myMap)
	{
		this.myMap = myMap;
	}
	
	@Override
	protected Document doInBackground(String... urls) {
		try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(urls[0]);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);
            return doc;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	protected void onPostExecute(Document doc)
	{
		if(doc != null)
		{
			NodeList nl1 = doc.getElementsByTagName("status");
			Log.i("Test", nl1.item(0).getTextContent());
			
			directionList = getDirection(doc);
			line = new PolylineOptions().width(3).color(Color.RED);
			
			for(int i=0; i < directionList.size(); i++)
			{
				line.add(directionList.get(i));
			}
			myMap.addPolyline(line);
		}
	}

	protected ArrayList<LatLng> getDirection(Document doc)
	{
		NodeList nl1, nl2, nl3;
		ArrayList<LatLng> stepList = new ArrayList<LatLng>();
		double latValue, lngValue;
		Node lat, lng;
		
		nl1 = doc.getElementsByTagName("step");
		
		Log.i("step size", Integer.toString(nl1.getLength()));
		
		for(int i=0; i < nl1.getLength(); i++)
		{
			Node nd1 = nl1.item(i);
			nl2 = nd1.getChildNodes();
			
			//gets the index of start location
			Node start = nl2.item(getNodeIndex(nl2,"start_location"));
			
			//gets child node of start location
			nl3 = start.getChildNodes();
			
			lat = nl3.item(getNodeIndex(nl3,"lat"));
			lng = nl3.item(getNodeIndex(nl3,"lng"));

			latValue = Double.parseDouble(lat.getTextContent());
			lngValue = Double.parseDouble(lng.getTextContent());
			
			
			stepList.add(new LatLng(latValue,lngValue));
			
			Node end = nl2.item(getNodeIndex(nl2,"end_location"));
			
			//gets child node of start location
			nl3 = end.getChildNodes();
			
			lat = nl3.item(getNodeIndex(nl3,"lat"));
			lng = nl3.item(getNodeIndex(nl3,"lng"));

			latValue = Double.parseDouble(lat.getTextContent());
			lngValue = Double.parseDouble(lng.getTextContent());
			
			stepList.add(new LatLng(latValue,lngValue));

		}
		return stepList;
	}
	private int getNodeIndex(NodeList nl, String nodeName)
	{
		for(int i = 0; i < nl.getLength(); i++)
		{
			if(nl.item(i).getNodeName().equals(nodeName))
			{
				return i;
			}
		}
		
		return -1;
	}
}

