// Jonathan Raiman 010207881 jonathanraiman@gmail.com
import com.google.gson.*;

public class jsontest {

    public static void main (String[] args) {

    	Object myobject = new JsonObject();
    	((JsonObject)myobject).add("JSON", new JsonPrimitive("Hello, World!"));
    	System.out.println(myobject);
    	JsonParser parser = new JsonParser();
		JsonObject bob = (JsonObject)parser.parse("{'magnolia':'2','tip-top':[5,5,5]}");
    	System.out.println(bob["tip-top"]);
    }

}