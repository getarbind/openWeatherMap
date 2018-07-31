package com.majesco.openWeatherMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest 
public class OpenWeatherAPITest {
	
	private String appID = "04a566a0cde4b45f95a7128fd9217c0d";
	private String weatherURL1 = "https://api.openweathermap.org/data/2.5/weather?q=";
	private String weatherURL2 = "&APPID=";
	
	
	@Test
	public void testOpenWeatherAPIStatusCode() throws ClientProtocolException, IOException {
		String city1 = "London";
		String url1 = weatherURL1+city1+weatherURL2+appID;
	   // Given
	   //HttpUriRequest request = new HttpGet( "https://api.openweathermap.org/data/2.5/weather?q=London&APPID=04a566a0cde4b45f95a7128fd9217c0d");
		HttpUriRequest request = new HttpGet(url1);
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	   // Then
	   assertEquals(httpResponse.getStatusLine().getStatusCode(),200);
	}
	
	@Test
	public void testOpenWeatherAPIDataFormatJSON()
	  throws ClientProtocolException, IOException {
		String city1 = "London";
		String url1 = weatherURL1+city1+weatherURL2+appID;
	   // Given
	   String jsonMimeType = "application/json";
	   HttpUriRequest request = new HttpGet(url1);
	 
	   // When
	   HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	   // Then
	   String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	   assertEquals( jsonMimeType, mimeType );
	}
	
	@Test
	public void testOpenWeatherAPIDataDataAssert()
	  throws ClientProtocolException, IOException, JSONException {
		String city1 = "London";
		String url1 = weatherURL1+city1+weatherURL2+appID;
	   // Given
	   HttpUriRequest request = new HttpGet(url1);
	   // When
	   HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	   // Then
 	   String jsonFromResponse = EntityUtils.toString(response.getEntity());
	   System.out.println(jsonFromResponse);
	   JSONObject jsonObject = new JSONObject(jsonFromResponse);
	   JSONObject sys = new JSONObject(jsonObject.getString("sys"));
	   JSONObject weather  = new JSONObject(jsonObject.getString("main"));
	   System.out.println(jsonObject.getString("name"));
	   
	   assertEquals( "London", jsonObject.getString("name"));
	   assertEquals( "GB", sys.getString("country"));
	   assertTrue(280 <= Float.parseFloat(weather.getString("temp")) 
			   && Float.parseFloat(weather.getString("temp")) <= 300);
  
	}
	
	@Test
	public void testOpenWeatherAPIDataDataAssertforMultipleCity()
	  throws ClientProtocolException, IOException, JSONException {
	  
	   // Given
		String[] city1 = {"Portsmouth","Dover"};
		for (int i=0;i<city1.length;i++) {
		String url1 = weatherURL1+city1[i]+weatherURL2+appID;
		System.out.println(url1);
	   HttpUriRequest request = new HttpGet( url1);
	 
	   // When
	   HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	   // Then
	   String jsonFromResponse = EntityUtils.toString(response.getEntity());
	   System.out.println(jsonFromResponse);
	   
	   JSONObject jsonObject = new JSONObject(jsonFromResponse);
	   JSONObject sys = new JSONObject(jsonObject.getString("sys"));
	   JSONObject weather  = new JSONObject(jsonObject.getString("main"));
	   System.out.println(jsonObject.getString("name"));
	   
	   assertEquals( city1[i], jsonObject.getString("name"));
	   //assertEquals( "US", sys.getString("country"));
	   assertTrue(280 <= Float.parseFloat(weather.getString("temp")) 
			   && Float.parseFloat(weather.getString("temp")) <= 300);
	   
		}
	   
	}
	
}
