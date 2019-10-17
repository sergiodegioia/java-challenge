package challenge.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import challenge.vo.UrlVO;
import challenge.data.model.Url;
import challenge.data.repository.UrlRepository;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/redirect")
public class RedirectController{
	@Autowired
	private UrlRepository urlRepository;

	@GetMapping("/{shortCode}")
	public void getUrl( HttpServletResponse response, @PathVariable("shortCode") String shortCode){
		Url url = urlRepository.findByShortCode( shortCode);
		if( null == url){
			throw new ResponseStatusException( HttpStatus.NOT_FOUND, "entity not found");
		}
		response.setStatus( HttpServletResponse.SC_MOVED_PERMANENTLY);
	        response.setHeader("Location", url.getUrl());
	}
}

