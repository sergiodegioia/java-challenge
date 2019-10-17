package challenge.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import challenge.vo.UrlVO;
import challenge.data.model.Url;
import challenge.data.repository.UrlRepository;
import java.util.function.BiConsumer;
import static java.util.concurrent.ThreadLocalRandom.current;

@RestController
@RequestMapping("/url")
public class UrlController{
	@Autowired
	private UrlRepository urlRepository;

	@PostMapping( consumes="application/json")
	@ResponseStatus( HttpStatus.CREATED)
	public UrlVO postUrl( @RequestBody UrlVO urlVO) throws ShortCodeClashingException{
		return storeUrl( urlVO, null);
	}

	private UrlVO storeUrl( UrlVO urlVO, BiConsumer<Url,UrlVO> put) throws ShortCodeClashingException{
		Url url = new Url();
		if( null != put){
			put.accept( url, urlVO);
		}
		url.setUrl( urlVO.getUrl());
		final int MAX_ATTEMPTS = 3;
		int i = 0;
		do{
			String noise = null;
			if( 0 != i){
				noise = Integer.valueOf( current().nextInt()).toString();
			}
			urlVO.createShortCode( noise);
			url.setShortCode( urlVO.getShortCode());
			try{
				url = urlRepository.save( url);
				break;
			}catch( Exception e){
			}
		}while( i++ < MAX_ATTEMPTS);
		if( MAX_ATTEMPTS != i - 1){
			urlVO.setId( url.getId());
			return urlVO;
		}
		throw new ShortCodeClashingException();
	}

	@PutMapping( value = "/{id}", consumes="application/json")
	public UrlVO putUrl( @RequestBody UrlVO newUrlVO) throws ShortCodeClashingException{
		return storeUrl( newUrlVO, ( url, urlVO) -> url.setId( urlVO.getId()));
	}

	@GetMapping("/{id}")
	public UrlVO getUrl( @PathVariable("id") long id){
		Url url = urlRepository.findById( id);
		if( null == url){
			throw new ResponseStatusException( HttpStatus.NOT_FOUND, "entity not found");
		}
		UrlVO urlVO = new UrlVO( url.getId(), url.getUrl(), url.getShortCode());
		return urlVO;
	}
	@DeleteMapping("/{id}")
	@ResponseStatus( HttpStatus.NO_CONTENT)
	public void deleteUrl( @PathVariable("id") long id){
		Url url = new Url();
		url.setId( id);
		urlRepository.delete( url);
	}
}

