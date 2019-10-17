package challenge.data.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table
public class Url{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	private long id;

	private String url;

	@Column( unique = true)
	private String shortCode;

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
	public String getShortCode(){
		return shortCode;
	}
	public void setShortCode( String shortCode){
		this.shortCode = shortCode;
	}
}

