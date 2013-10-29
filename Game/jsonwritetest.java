import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import java.util.ArrayList;
import java.util.*;
/*

This should inherit from GameObjects.


This file should fully build character objects and have it's own write to method here that could be invoked by the overall saving machine. Characters will also have their own .show() method (will display a png or something basic for now).

*/
public class jsonwritetest {
   	public static void main(String[] args) throws IOException{
   		Random picking = new Random();
   			FileWriter document = new FileWriter(".//"+args[0]);
   			PrintWriter fileWriter = new PrintWriter(document);
	    	JsonWriter writer;
	    	String[] firstnames = {"Jay","George","Samuel","Jim","Jenny","Liam","Dorothy","Julie","Chloe","Daphnee","Deirdre","Rose","Eleanore", "Adam", "Ann", "Art", "Amy", "Andrew", "Asuman","Alfred", "Audrey", "April", "Anthony", "Andrea", "Arash", "Amilia", "Bonny", "Bobby", "Bevin", "Bettina","Chris", "Christie", "Charles", "Carrie", "Cindy","Cheryl", "Craig", "Daniel", "Debbie", "Dustin", "Delia", "Diane", "Edward", "Ellen", "Eric", "Esther", "Frank", "Fay", "Farhad", "Fred", "Gene", "Gary", "Gerry", "Gerald", "Grechen", "Gaston", "Gregg", "Heather", "Henry", "Harry", "Hilary","Ian", "Igor", "Jeff", "John", "Jack", "Luigi","Wario","James", "Joe", "Jerome", "Jorge", "Jennifer", "Joshua", "Jodie", "Jay","June", "Kimberly", "Kersey", "Kathleen", "Karen", "Kay", "Larry", "Lynn", "Lauren", "Lisa", "Micheal", "Michelle", "Minju", "Mario", "Mary", "Miriam", "Mark", "Maria", "Majorie", "Mercedes", "Nancy", "Nellie", "Newton", "Pam", "Peter", "Penny", "Phillip", "Pari", "Patrick", "Paul", "Ron", "Rob", "Rod", "Roger", "Robert", "Richard", "Sally", "Shannah", "Sonny", "Sven", "Shana", "Steven", "Scott", "Sam", "Sandra", "Sarah", "Tom", "Todd", "Timothy", "Tobias", "Ulrich", "Vaughn", "Walter", "Wayne", "William", "Zach","Abir","Acelin","Acton","Adair","Adar","Addison","Aden", "Adir","Aiken","Aimon","Ainsley","Ajani","Akira","Ali","Amadeus","Amal","Amir","Ammon","Amon","Aneurin","Angelo", "Annan","Ansari","Anwar","Archer","Arden","Ari","Ariel","Arion","Arje","Arjuna","Arley","Arlo","Asher","Ashlin","Asim","Aston","Avan","Ayer","Bae","Bakari","Barak","Beck","Benedict","Blaine","Blair","Blaise","Boyce","Brady", "Brae","Brock","Brodie","Bryn","Bryce","Cadell","Cadmus","Caedmon","Caesar","Cahil","Cailean","Cain","Caleb","Camden","Carey","Carlin","Carne","Carrik","Carson","Casey","Caspar","Cassidy","Cathan","Cato","Cavan","Chad","Chaim","Chaney","Chase","Chet","Chiron","Cian","Ciaran","Ciro","Clay","Cleary","Cody","Cole","Conall","Conan","Conn","Corey","Crewe","Crispin","Cristian","Cristo","Cy","Cyrus","Dacey","Dai","Dakota","Daley","Damas","Damon","Dane","Dante","Darby","Darcy","Darien","Darius","Darnell","Delmar","Demas","Dev","Diego","Dimitri","Donato","Drew","Duarte","Dyami","Dyre","Eaton","Edison","Ehren","Elan","Eli","Ellery","Emerson","Emil","Emyr","Ennis","Ephraim","Estes","Etienne","Evander","Ezra","Fabian","Fabrice","Fane","Farquhar","Fariss","Favian","Fenn","Fidel","Fineas","Finian","Finn","Flavien","Frayne","Gabriel","Galen","Garek","Gaston","Geary","Germaine","Garvais","Gianni","Giles","Giordano","Jourdano","Girvan","Grady","Graison","Gye","Haine","Hakim","Hakon","Haley","Hamlin","Hamon","Hanan","Hani","Hannes","Harlan","Harper","Harvae","Hasim","Hassan","Haven","Heath","Hemi","Howell","Hume","Hunter","Hyam","Iago","Iden","Ihaka","Innes","Ira","Issac","Isaiah","Israel","Ives","Jacek","Jael","Jai","Jaleel","Jamal","Jaron","Javed","Jervase","Jesse","Jett","Joachim","Jourdan","Kadir","Kamal","Kane","Karan","Kareem","Kasim","Kavan","Keane","Keenan","Kelan","Kent","Kerne","Khalil","Kieran","Killian","Kim","Kinnard","Kinsey","Kiran","Kito","Kolya","Kyne","Lachlan","Lafayette","Lal","Lamar","Lamech","Lamont","Laine","Lani","Lann","Lars","Leal","Leif","Lennon","Lennox","Levin","Levi","Linton","Lorant","Lorenzo","Lorne","Lucian","Madoc","Mahir","Mahon","Makani","Makis","Maksim","Malachi","Malik","Malin","Malise","Manas","Manaia","Mani","Manu","Marcello","Marius","Marley","Marlon","Mason","Massimo","Matai","Mayan","Mayer","Melor","Mercer","Micah","Midas","Milan","Miles","Milos","Mischa","Myall","Myron","Naaman","Nadir","Nairn","Namir","Narayn","Nasir","Niall","Nico","Nikita","Nils","Niran","Nishan","Nur","Nye","Nyall","Omar","Oran","Orion","Orlando","Orton","Paige","Paine","Paris","Parker","Pascal","Payton","Pearce","Perry","Phelan","Philemon","Phineas","Phoenix","Pearson","Qadir","Qasim","Quillan","Quilliam","Quincy","Quinlan","Quinn","Rafe","Rafferty","Rafi","Rafiq","Rainer","Ramiro","Rasheed","Ravi","Reeve","Reid","Regan","Reilly","Remy","Renato","Renzo","Riordan","Roarke","Rocco","Rohan","Roman","Ronan","Rory","Roshan","Royce","Rune","Ryder","Back","Sabian","Sacha","Sage","Saleem","Sancho","Sandor","Santos","Saul","Saviero","Saville","Saxton","Sayed","Sebastian","Semyon","Seth","Severn","Shae","Shakir","Sharif","Sheehan","Shen","Sheridan","Shiloh","Sloane","Sol","Soren","Steele","Steede","Sumner","Swayne","Tahir","Tai","Tait","Tanner","Tariq","Tarn","Tarquin","Tarun","Tashi","Tehan","Thane","Theon","Thierry","Tierney","Timon","Tobias","Torin","Travis","Tremaine","Tynan","Tyne","Tyson","Ultan","Upton","Uriah","Urian","Vachel","Valentino","Valerian","Valles","Van","Varden","Varian","Varick","Vartan","Venn","Vere","Vidal","Vidas","Vidya","Vijay","Vitale","Vyasa","Wade","Waite","Weston","Willard","Wren","Xavier","Xylon","Yael","Yannis","Yasir","Yeshe","Yves","Zahir","Zaki","Zamir","Zaine","Zed","Zeke","Stan"};
			String[] lastnames = {"Gatz","Gatsby","Smith","Heroku","Kaminari","Chen","Starry","Cherander","Strawberry","Lennon","Portishead","Black","Brown","Pink","Shine","Foo","Dupont","Haddock","Tournesol","Rockefeller","Kennedy","Pie","Eta","Epsilon","Longsword","Smithonian","Getty","Barriere","Decaux","Bidochon","Greene","Geller","Kubrick","Cohen","Goldberg","Getz","Tarsem","Lichtenstein","Rhein","Manchuria"};
	    	String[] cities = {"Tokyo, Japan","New York, NY","Paris, France","Mombasa, Kenya","Johannesburg, South Africa","Capetown, South Africa","Beijing, China","Seoul, Korea","Bordeaux, France","Vienna, Italy","Berlin, Germany","St Petersburg, Russia","Kiev, Ukraine","Oklahoma City, Oklahoma","San Francisco, California","Los Angeles, California","Boston, Massachusetts","London, United Kingdom","Lisbon, Portugal"};
	    	try {
			writer = new JsonWriter(document);
		 	
		 	writer.beginArray(); // [

		 	for (int i=0; i<Integer.parseInt(args[1]);i++){
		 	fileWriter.println();
			writer.beginObject(); // Object i {
			writer.name("Name").value(firstnames[picking.nextInt(firstnames.length)]+" "+lastnames[picking.nextInt(lastnames.length)]); //
			fileWriter.println();
			writer.name("Birthplace").value(cities[picking.nextInt(cities.length)]); //
			fileWriter.println();
			writer.name("Myarray"); //
			writer.beginArray(); // [
			writer.value(picking.nextInt(101)); //
			writer.value(picking.nextInt(11)); //
			writer.value(picking.nextInt(100001)); //
			writer.endArray(); // ]
			fileWriter.println();
			writer.endObject(); // }
			}
			fileWriter.println();
			writer.endArray(); // ]
			writer.close();
		 
			System.out.println("Done");
	 
	    	} catch (IOException e) {
			e.printStackTrace();
	    	}
 
   	}
 
}