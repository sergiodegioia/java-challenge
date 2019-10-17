package challenge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import challenge.rest.UrlController;
import challenge.config.TestConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MvcResult;
import challenge.vo.UrlVO;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith( SpringRunner.class)
//@WebMvcTest( UrlController.class)
@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
public class RedirectControllerTest{

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testRedirection() throws Exception{
		MvcResult r = mockMvc.perform( post("/url")
				.contentType( APPLICATION_JSON)
				.content("{\"url\": \"http://www.dailymail.co.uk\"}")
			       )
			.andExpect( status().isCreated())
			.andExpect( jsonPath( "$.url", is("http://www.dailymail.co.uk")))
			.andExpect( jsonPath( "$.id").exists())
			.andExpect( jsonPath( "$.shortCode").exists())
			.andReturn();
		String json = r.getResponse().getContentAsString();
		UrlVO urlVO = new ObjectMapper().readValue( json, UrlVO.class);
		mockMvc.perform( get("/redirect/" + urlVO.getShortCode()))
			.andExpect( status().isMovedPermanently())
			.andExpect( header().string( "Location", "http://www.dailymail.co.uk"));
	}
}


