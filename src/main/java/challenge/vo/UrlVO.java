package challenge.vo;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UrlVO{
	private long id;
	private String url;
	private String shortCode;

	public UrlVO( long id, String url, String shortCode){
		this.id = id;
		this.url = url;
		this.shortCode = shortCode;
	}

	public UrlVO(){
	}

	public long getId(){
		return id;
	}
	public void setId( long id){
		this.id = id;
	}
	public String getUrl(){
		return url;
	}
	public void setUrl( String url){
		this.url = url;
	}
	public void createShortCode( String noise){
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			Charset charset = Charset.forName("UTF-8");
			String urlMadeUnique = url + super.hashCode() + ((null == noise)? "": noise);
			byte[] md5 = md.digest( urlMadeUnique.getBytes( charset));
			Base64.Encoder encoder = Base64.getUrlEncoder();
			byte[] base64EncodedMd5 = encoder.encode( md5);
			//this.shortCode = "ok"; // in order to test shortCode clashing uncomment this line and comment the next one
			this.shortCode = new String( base64EncodedMd5, 0, 8, charset);
		}catch( NoSuchAlgorithmException nsae){
			throw new RuntimeException( nsae);
		}
	}
	public String getShortCode(){
		return shortCode;
	}
	public String toString(){
		return String.format("{\"id\": %d, \"url\": \"%s\", \"shortCode\": \"%s\"}", id, url, shortCode);
	}
}

