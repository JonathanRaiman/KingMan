import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.util.ArrayList;
 
public class jsonreadtest {
   	public static void main(String[] args) {
   	JsonArray jumbo = new JsonArray ();
 	// JsonObject myobject = new JsonObject();
	try {
		JsonReader reader = new JsonReader(new FileReader(".//"+args[0]));
	 
		reader.beginArray();
		while (reader.hasNext()){
			JsonObject tempobject = new JsonObject();
	 		reader.beginObject();
			while (reader.hasNext()) {
		 
			  	String name = reader.nextName();
				if (name.equalsIgnoreCase("name")) {
				 	String nextstring = reader.nextString();
				 	tempobject.add(name, new JsonPrimitive(nextstring));
				 
				} else if (name.equalsIgnoreCase("birthplace")) {
				 
					String nextstring2 = reader.nextString();
				 	tempobject.add(name, new JsonPrimitive(nextstring2));
				 
				} else if (name.equalsIgnoreCase("myarray")) {
				 	ArrayList<Integer> myarray = new ArrayList<Integer>(3);
						// read array
					reader.beginArray();
				 
					while (reader.hasNext()) {
						Integer a = reader.nextInt();
						myarray.add(a);
					}
				 
					reader.endArray();
					Gson gson = new Gson();
					tempobject.add(name, new JsonPrimitive(gson.toJson(myarray)));
				 
				} else {
				reader.skipValue(); //avoid some unhandle events
				}
			}
			reader.endObject();
			jumbo.add(tempobject);
		}
		reader.endArray();
		reader.close();
		System.out.println(jumbo);
		 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	 
	}
 
}