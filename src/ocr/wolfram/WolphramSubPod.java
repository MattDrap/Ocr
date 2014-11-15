package ocr.wolfram;

public class WolphramSubPod{
	public final static String WOLPHRAM_SUBPOD = "subpod";
	public final static String TITLE = "title",
			PLAINTEXT = "plaintext";		
	private String title;
	private String plaintext;
	private WolphramImage image;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPlaintext() {
		return plaintext;
	}
	public void setPlaintext(String plaintext) {
		this.plaintext = plaintext;
	}
	public WolphramImage getImage() {
		return image;
	}
	public void setImage(WolphramImage image) {
		this.image = image;
	}
}
