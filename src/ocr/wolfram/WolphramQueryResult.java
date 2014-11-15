package ocr.wolfram;

import java.util.ArrayList;

public class WolphramQueryResult {
	public final static String WOLPHRAM_QUERY_RESULT = "queryresult";
	public final static String SUCCES = "success", ERROR = "error", NUMPODS = "numpods";
	private String succes, error;
	private int numpods;
	private ArrayList<WolphramPod> pods;
	
	public ArrayList<WolphramPod> getPods(){
		return pods;
	}
	public void addPod(WolphramPod pod){
		pods.add(pod);
	}
	public String isSucces() {
		return succes;
	}
	public void setSucces(String succes) {
		this.succes = succes;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getNumpods() {
		return numpods;
	}
	public void setNumpods(int numpods) {
		this.numpods = numpods;
		if(numpods > 0){
			pods = new ArrayList<>(numpods);
		}
	}
}
