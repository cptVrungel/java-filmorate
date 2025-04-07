package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(FilmController.class)
class FilmorateApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Film film1;
	private Film film2;


	@BeforeEach
	void setup() {
		film1 = new Film();
		film2 = new Film();

		film1.setName("X");
		film1.setDescription("XX");
		film1.setReleaseDate(LocalDate.of(2000, 04, 12));
		film1.setDuration(200);

		film2.setName("Y");
		film2.setDescription("YY");
		film2.setReleaseDate(LocalDate.of(2000, 04, 15));
		film2.setDuration(300);
	}


	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	public void testPostFilm() throws Exception {

		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(film1)))
				.andReturn();

		MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(film2)))
				.andReturn();

		Film returnedFilm1 = objectMapper.readValue(result1.getResponse().getContentAsString(), Film.class);
		Film returnedFilm2 = objectMapper.readValue(result2.getResponse().getContentAsString(), Film.class);

		assertEquals(returnedFilm1.getId(), 1);
		assertEquals(returnedFilm2.getId(), 2);
	}

	@Test
	@Order(3)
	public void testPutFilm() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(film1)));

		film1.setId(5);
		film1.setName("XXXX");
		film1.setDescription("YYYY");
		film1.setReleaseDate(LocalDate.of(2000, 04, 12));
		film1.setDuration(200);

		mockMvc.perform(MockMvcRequestBuilders.put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(film1)))
				.andExpect(status().isNotFound());

		film1.setId(3);
		film1.setName("");

		mockMvc.perform(MockMvcRequestBuilders.put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(film1)))
				.andExpect(status().isBadRequest());

		film1.setName("XXXX");
		film1.setDescription("rwMGjiDQThxLDVrqADahjbavvuSQqVhzDWjcJQQtHIOzUZUKwzYzsqDRrNuKtoEMgRHaIWJg"
				+ "QOuItLuKNqFImjkfaFZVVFQVlsgsVvxGcRLHrpJoNoGoPNSxakjexJdwQzvVIezyAiswIpGwfcikaNIfkWlx" +
				"IAIkAprybEXZBvctMwNWzHbNPqvxyycrAddXKLsSEijltFmLYuSivu");

		mockMvc.perform(MockMvcRequestBuilders.put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(film1)))
				.andExpect(status().isBadRequest());

		film1.setDescription("YYYY");
		film1.setReleaseDate(LocalDate.of(1736, 04, 12));

		mockMvc.perform(MockMvcRequestBuilders.put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(film1)))
				.andExpect(status().isBadRequest());

		film1.setReleaseDate(LocalDate.of(2000, 04, 12));
		film1.setDuration(-2);

		mockMvc.perform(MockMvcRequestBuilders.put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(film1)))
				.andExpect(status().isBadRequest());
	}
}
