package ocr.wolfram;

import java.util.ArrayList;

public class WolphramPod {
	public final static String WOLPHRAM_POD = "pod";
	public final static String TITLE = "title",
			SCANNER = "scanner",
			ID = "id",
			POSITION = "position",
			ERROR = "error",
			NUMSUBPODS = "numsubpods";
	private String title, scanner, id;
	private int position;
	private String error;
	private int numsubpods;
	
	private ArrayList<WolphramSubPod> subpods;
	
	
	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getScanner() {
		return scanner;
	}



	public void setScanner(String scanner) {
		this.scanner = scanner;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public int getPosition() {
		return position;
	}



	public void setPosition(int position) {
		this.position = position;
	}



	public String getError() {
		return error;
	}



	public void setError(String error) {
		this.error = error;
	}



	public int getNumsubpods() {
		return numsubpods;
	}



	public void setNumsubpods(int numsubpods) {
		this.numsubpods = numsubpods;
		if(numsubpods > 0){
			subpods = new ArrayList<>(numsubpods);
		}
	}



	public ArrayList<WolphramSubPod> getSubpods() {
		return subpods;
	}



	public void addSubpod(WolphramSubPod subpod) {
		subpods.add(subpod);
	}
}
