package ocr.wolfram;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XmlParser {
	private static final String TAG  = XmlParser.class.getSimpleName();
	private WolphramQueryResult WQResult = null;
    private WolphramPod WPpod = null;
    private WolphramSubPod WSPsubpod = null;
    private WolphramImage WIimage = null;
	private XmlPullParser xmlparser;
	public XmlParser() throws XmlPullParserException{
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		xmlparser = factory.newPullParser();
	}
	public XmlParser(InputStream instream) throws XmlPullParserException, IOException{
		if(instream == null){
			Log.e(TAG, "Null reference on inpustream");
			return;
		}
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			xmlparser = factory.newPullParser();
			setInput(instream);
		}catch(XmlPullParserException e){
			Log.e(TAG,e.getMessage());
			throw new XmlPullParserException(e.getMessage());
		}catch(IOException e){
			Log.e(TAG,e.getMessage());
			throw new IOException(e.getMessage());
		}
		
	}
	public WolphramQueryResult setInput(InputStream instream) throws XmlPullParserException, IOException{
		try{
			xmlparser.setInput(instream, null);
			int eventType = xmlparser.getEventType();
		    while(eventType != XmlPullParser.END_DOCUMENT) {
		        switch(eventType) {
		            case XmlPullParser.START_DOCUMENT:
		                WQResult = new WolphramQueryResult();
		                break;
		            case XmlPullParser.START_TAG:
		                String tagName = xmlparser.getName();
		                // if queryresult
		                if(tagName.equalsIgnoreCase(WolphramQueryResult.WOLPHRAM_QUERY_RESULT)) {
		                    //SUCCES
		                	WQResult.setSucces(xmlparser.getAttributeValue(null, WolphramQueryResult.SUCCES));
		                    //ERROR
		                	WQResult.setError(xmlparser.getAttributeValue(null, WolphramQueryResult.ERROR));
		                	//NUMPODS
		                	String temp = xmlparser.getAttributeValue(null, WolphramQueryResult.NUMPODS);
		                	WQResult.setNumpods(Integer.parseInt(temp));
		                }
		                
		                // if WPpod
		                else if(tagName.equalsIgnoreCase(WolphramPod.WOLPHRAM_POD)) {
		                    WPpod = new WolphramPod();
		                    WPpod.setTitle(xmlparser.getAttributeValue(null, WolphramPod.TITLE));
		                    WPpod.setError(xmlparser.getAttributeValue(null, WolphramPod.ERROR));
		                    WPpod.setId(xmlparser.getAttributeValue(null, WolphramPod.ID));
		                    WPpod.setScanner(xmlparser.getAttributeValue(null, WolphramPod.SCANNER));
		                    WPpod.setPosition(Integer.parseInt(xmlparser.getAttributeValue(null, WolphramPod.POSITION)));
		                    WPpod.setNumsubpods(Integer.parseInt(xmlparser.getAttributeValue(null, WolphramPod.NUMSUBPODS)));
		                }
		                // if WSPsubpod
		                else if(tagName.equalsIgnoreCase(WolphramSubPod.WOLPHRAM_SUBPOD)) {
		                	WSPsubpod = new WolphramSubPod();
		                    WSPsubpod.setTitle(xmlparser.getAttributeValue(null, WolphramSubPod.TITLE));
		                    WSPsubpod.setPlaintext(xmlparser.getText());
		                }
		                else if(tagName.equalsIgnoreCase(WolphramImage.WOLPHRAM_IMAGE)) {
		                	WIimage = new WolphramImage();
		                	WIimage.setImgURL(xmlparser.getAttributeValue(null, WolphramImage.IMAGEURL));
		                	WIimage.setImgwidth(Integer.parseInt(xmlparser.getAttributeValue(null, WolphramImage.IMAGEWIDTHATTR)));
		                	WIimage.setImgheight(Integer.parseInt(xmlparser.getAttributeValue(null, WolphramImage.IMAGEHEIGHTATTR)));
		                }
		                break;
		            case XmlPullParser.END_TAG:
		            	String tagEndName = xmlparser.getName();
		            	if(tagEndName.equalsIgnoreCase(WolphramPod.WOLPHRAM_POD)){
		            		WQResult.addPod(WPpod);
		            	}
		            	else if(tagEndName.equalsIgnoreCase(WolphramSubPod.WOLPHRAM_SUBPOD)){
		            		WPpod.addSubpod(WSPsubpod);
		            	}
		            	else if(tagEndName.equalsIgnoreCase(WolphramImage.WOLPHRAM_IMAGE)){
		            		WSPsubpod.setImage(WIimage);
		            	}
		            	break;
		        }
		        // jump to next event
		        eventType = xmlparser.next();
		    }
		    return WQResult;
		}catch(IOException e){
			Log.e(TAG, e.getMessage());
			throw new IOException(e.getMessage());
		}catch(XmlPullParserException e){
			Log.e(TAG, e.getMessage());
			throw new XmlPullParserException(e.getMessage());
		}finally{
			instream.close();
		}
	}
}
