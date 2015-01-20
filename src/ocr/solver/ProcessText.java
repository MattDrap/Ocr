package ocr.solver;


public class ProcessText {
	
	public static String Process(String mathtext){
		StringBuilder result = new StringBuilder();
		mathtext = mathtext.replace(" ", "");
		for(int i = 0; i < mathtext.length(); i++){
			if(Character.isLetter(mathtext.charAt(i))){
				result.append(mathtext.charAt(i));
				int j = i + 1;
				StringBuilder power = new StringBuilder();
				boolean firstpower = true;
				for(;j < mathtext.length() && Character.isDigit(mathtext.charAt(j)); j++){
					if(firstpower){
						power.append("^(");
						firstpower = false;
					}
					power.append(mathtext.charAt(j));
				}
				if(i != j - 1){
					power.append(")");
					result.append(power.toString());
					i = j - 1;
				}
			}else{
				result.append(mathtext.charAt(i));
			}
			
		}
		return result.toString();
	}
	public static String wolphramquery(String inputText, String PID){	
		return "http://api.wolframalpha.com/v2/query?input="+inputText+"&appid="+PID;
	}
}
