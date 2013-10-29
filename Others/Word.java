import java.util.ArrayList;
public class Word {
	private String word;
	private int position;
	private ArrayList<Word> neighbors;

	public Word(String name, int place){
		word = name;
		position = place;
	}

	public String Word(){
		return word;
	}

	public int Position(){
		return position;
	}

	public String toString(){
		return word+" ("+position+")";
	}
}