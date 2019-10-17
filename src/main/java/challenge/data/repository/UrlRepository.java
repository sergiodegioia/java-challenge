package challenge.data.repository;

import challenge.data.model.Url;
import org.springframework.data.repository.CrudRepository;

public interface UrlRepository extends CrudRepository<Url, Long>{
	Url findById( long Id);
	Url findByShortCode( String shortCode);
}

