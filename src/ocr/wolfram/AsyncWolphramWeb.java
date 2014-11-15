package ocr.wolfram;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ocr.CaptureActivity;
import ocr.PreferencesActivity;
import ocr.solver.ProcessText;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import edu.sfsu.cs.orange.ocr.R;

public class AsyncWolphramWeb extends AsyncTask<Void, Void, Boolean> {
	private static final String TAG = AsyncWolphramWeb.class.getSimpleName();
	private HttpURLConnection urlConnection;
	private URL url;
	private XmlParser xmlparser;
	private CaptureActivity activity;
	private TextView textView;
	private View progressView;
	private WolphramQueryResult WQResult;
	private String resultText;
	
	public AsyncWolphramWeb(CaptureActivity activity, String ocr_result_text){
		String processed_text = ProcessText.Process(ocr_result_text);
		String preURL = ProcessText.wolphramquery(processed_text, PreferencesActivity.WOLPHRAM_ALPHA_PID);
		try {
			url = new URL(preURL);
		} catch (MalformedURLException e1) {
			Log.e(TAG, e1.getMessage());
		}
		this.activity = activity;
		textView = (TextView) activity.findViewById(R.id.translation_text_view);
	    progressView = (View) activity.findViewById(R.id.indeterminate_progress_indicator_view);

		try {
			xmlparser = new XmlParser();
		} catch (XmlPullParserException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		try{
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");

			urlConnection.setDoOutput(true);
			urlConnection.connect();
			
			
			if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
				WQResult =  xmlparser.setInput(urlConnection.getInputStream());
				resultText = WQResult.getPods().get(0).getSubpods().get(0).getPlaintext();
				return true;
			}else{
				throw new NullPointerException();
			}
		}catch(Exception e){
			try{
				Log.e(TAG, String.valueOf(urlConnection.getResponseCode()));
				InputStreamReader insreader = new InputStreamReader(urlConnection.getErrorStream());
				StringBuilder sb = new StringBuilder(1024);
				char [] buffer =  new char[1024];
				while(insreader.read(buffer) != -1){
					sb.append(buffer);
				}
				Log.e(TAG, sb.toString());
				sb = null;
				return false;
			}catch(IOException ex){
				Log.e(TAG, ex.getMessage());
				return false;
			}
		}finally{
			urlConnection.disconnect();
		}
	}
	@Override
	protected void onPostExecute(Boolean result){
		if (result) {
	      //Log.i(TAG, "SUCCESS");
	      
	      textView.setText(resultText);
	      textView.setVisibility(View.VISIBLE);
	      textView.setTextColor(activity.getResources().getColor(R.color.solved_result_text));

	      // Crudely scale betweeen 22 and 32 -- bigger font for shorter text
	      int scaledSize = Math.max(22, 32 - resultText.length() / 4);
	      textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

		} else {
	      Log.e(TAG, "FAILURE");
		}
		    
	    // Turn off the indeterminate progress indicator
	    if (progressView != null) {
	      progressView.setVisibility(View.GONE);
	    }
	}
}
