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
public class UrlControllerTest{

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testCRUDLifecycle() throws Exception{
		MvcResult r = mockMvc.perform( post("/url")
				.contentType( APPLICATION_JSON)
				.content("{\"url\": \"http://www.google.com\"}")
			       )
			.andExpect( status().isCreated())
			.andExpect( jsonPath( "$.url", is("http://www.google.com")))
			.andExpect( jsonPath( "$.id").exists())
			.andExpect( jsonPath( "$.shortCode").exists())
			.andReturn();
		String json = r.getResponse().getContentAsString();
		UrlVO urlVO = new ObjectMapper().readValue( json, UrlVO.class);
		mockMvc.perform( get("/url/" + urlVO.getId()))
			.andExpect( status().isOk())
			.andExpect( jsonPath( "$.url", is("http://www.google.com")))
			.andExpect( jsonPath( "$.id").value( urlVO.getId()))
			.andExpect( jsonPath( "$.shortCode", is( urlVO.getShortCode())));
		MvcResult rPut = mockMvc.perform( put("/url/" + urlVO.getId())
				.contentType( APPLICATION_JSON)
				.content("{\"id\": " + urlVO.getId() + ", \"url\": \"http://www.dailymail.co.uk\"}")
			       )
			.andExpect( status().isOk())
			.andExpect( jsonPath( "$.url", is("http://www.dailymail.co.uk")))
			.andExpect( jsonPath( "$.id", ( urlVO.getId())).value( urlVO.getId()))
			.andExpect( jsonPath( "$.shortCode").exists())
			.andReturn();
		String jsonPut = rPut.getResponse().getContentAsString();
		UrlVO urlVOPut = new ObjectMapper().readValue( jsonPut, UrlVO.class);
		mockMvc.perform( get("/url/" + urlVO.getId()))
			.andExpect( status().isOk())
			.andExpect( jsonPath( "$.url", is("http://www.dailymail.co.uk")))
			.andExpect( jsonPath( "$.id").value( urlVO.getId()))
			.andExpect( jsonPath( "$.id").value( urlVOPut.getId()))
			.andExpect( jsonPath( "$.shortCode", is( urlVOPut.getShortCode())));
		mockMvc.perform( delete("/url/" + urlVO.getId()))
			.andExpect( status().isNoContent());
		mockMvc.perform( get("/url/" + urlVO.getId()))
			.andExpect( status().isNotFound());
	}

}

