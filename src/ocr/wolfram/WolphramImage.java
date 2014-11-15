package ocr.wolfram;

public class WolphramImage{
	public static final String WOLPHRAM_IMAGE = "img";
	public static final String IMAGEURL = "src",
			IMAGEWIDTHATTR = "width",
			IMAGEHEIGHTATTR = "height";
	private String imgURL;
	private int imgwidth, imgheight;
	
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	public int getImgwidth() {
		return imgwidth;
	}
	public void setImgwidth(int imgwidth) {
		this.imgwidth = imgwidth;
	}
	public int getImgheight() {
		return imgheight;
	}
	public void setImgheight(int imgheight) {
		this.imgheight = imgheight;
	}
}
