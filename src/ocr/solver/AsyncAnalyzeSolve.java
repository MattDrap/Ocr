package ocr.solver;

import ocr.CaptureActivity;
import ocr.OcrResult;
import ocr.parser.ParserException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.sfsu.cs.orange.ocr.R;

public class AsyncAnalyzeSolve extends AsyncTask<Void, Void, Boolean> {
	private static final String TAG = AsyncAnalyzeSolve.class.getSimpleName();
	private CaptureActivity activity;
	private TextView textView;
	private TextView solveTypeTextView;
	private View progressView;
	private String resultText;
	private String [] preAdjustedTexts;
	private Solver solver;
	
	public AsyncAnalyzeSolve(CaptureActivity activity, OcrResult ocr_result){
		preAdjustedTexts = ocr_result.getText().split("\\r?\\n");
		this.activity = activity;
		textView = (TextView) activity.findViewById(R.id.translation_text_view);
		solveTypeTextView = (TextView) activity.findViewById(R.id.translation_language_text_view);
	    progressView = (View) activity.findViewById(R.id.indeterminate_progress_indicator_view);
	    solver = new Solver();
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		try{
			resultText = solver.solve(preAdjustedTexts);
			return true;
		}catch(ParserException e){
			Log.e(TAG, e.getMessage());
			resultText = e.getMessage();
			return false;
		}catch(NotImplementedException e){
			Log.e(TAG, e.getMessage());
			resultText = e.getMessage();
			return false;
		}
	}
	@Override
	protected void onPostExecute(Boolean result){
		if (result) {
	      //Log.i(TAG, "SUCCESS");
	      
	      textView.setText(resultText);
	      textView.setVisibility(View.VISIBLE);
	      textView.setTextColor(activity.getResources().getColor(R.color.solved_result_text));

	      solveTypeTextView.setText(solver.getType()); 
	      solveTypeTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
	      solveTypeTextView.setVisibility(View.VISIBLE);
	      
	      // Crudely scale betweeen 22 and 32 -- bigger font for shorter text
	      int scaledSize = Math.max(22, 32 - resultText.length() / 4);
	      textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

		} else {
	      Toast.makeText(activity.getBaseContext(), resultText, Toast.LENGTH_LONG).show();
		}
		    
	    // Turn off the indeterminate progress indicator
	    if (progressView != null) {
	      progressView.setVisibility(View.GONE);
	    }
	}
}
