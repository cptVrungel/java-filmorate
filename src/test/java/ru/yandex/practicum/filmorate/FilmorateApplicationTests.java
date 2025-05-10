package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.DAL.FilmDbStorage;
import ru.yandex.practicum.filmorate.DAL.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, FilmDbStorage.class})
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;

	private User user;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setLogin("test_login");
		user.setEmail("test_email@example.com");
		user.setName("Test User");
		user.setBirthday(java.time.LocalDate.of(1990, 1, 1));

		userStorage.addUser(user);
	}

	@Test
	public void testAddAndGetFilm() {
		Film film = new Film();
		film.setName("Inception");
		film.setDescription("A dream within a dream");
		film.setReleaseDate(LocalDate.of(2010, 7, 16));
		film.setDuration(148);
		film.setMpa(new Mpa(1, "G"));
		film.setGenres(List.of(new Genre(1, "Комедия")));

		filmStorage.addFilm(film);
		Film saved = filmStorage.getFilmById(film.getId());

		assertThat(saved).isNotNull();
		assertThat(saved.getName()).isEqualTo("Inception");
		assertThat(saved.getGenres()).hasSize(1);
	}

	@Test
	public void testUpdateFilm() {
		Film film = new Film();
		film.setName("Old Name");
		film.setDescription("Old desc");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(100);
		film.setMpa(new Mpa(1, "G"));
		filmStorage.addFilm(film);

		film.setName("New Name");
		film.setDescription("Updated desc");
		film.setDuration(120);
		film.setMpa(new Mpa(2, "PG"));
		film.setGenres(List.of(new Genre(2, "Драма")));
		filmStorage.updateFilm(film);

		Film updated = filmStorage.getFilmById(film.getId());
		assertThat(updated.getName()).isEqualTo("New Name");
		assertThat(updated.getDescription()).isEqualTo("Updated desc");
		assertThat(updated.getGenres()).hasSize(1);
		assertThat(updated.getGenres().get(0).getId()).isEqualTo(2);
	}

	@Test
	public void testGetAllFilms() {
		Film film1 = new Film();
		film1.setName("Film One");
		film1.setDescription("Desc 1");
		film1.setReleaseDate(LocalDate.of(2011, 1, 1));
		film1.setDuration(90);
		film1.setMpa(new Mpa(1, "G"));
		filmStorage.addFilm(film1);

		Film film2 = new Film();
		film2.setName("Film Two");
		film2.setDescription("Desc 2");
		film2.setReleaseDate(LocalDate.of(2012, 2, 2));
		film2.setDuration(95);
		film2.setMpa(new Mpa(1, "G"));
		filmStorage.addFilm(film2);

		assertThat(filmStorage.getAllFilms()).hasSize(2);
	}

	@Test
	public void testLikeAndMostPopular() {
		Film film = new Film();
		film.setName("Popular Film");
		film.setDescription("Gets likes");
		film.setReleaseDate(LocalDate.of(2020, 5, 5));
		film.setDuration(110);
		film.setMpa(new Mpa(1, "G"));
		filmStorage.addFilm(film);

		User user1 = new User();
		user1.setLogin("user1");
		user1.setEmail("user1@example.com");
		user1.setName("User One");
		user1.setBirthday(LocalDate.of(1985, 1, 1));
		userStorage.addUser(user1);

		filmStorage.addLike(film.getId(), user1.getId());

		assertThat(filmStorage.getMostLikedFilms(10)).hasSize(1);
		assertThat(filmStorage.getMostLikedFilms(10).iterator().next().getId()).isEqualTo(film.getId());
	}

	@Test
	public void testFindUserById() {
		User foundUser = userStorage.getUserById(user.getId());
		assertThat(foundUser).isNotNull();
		assertThat(foundUser.getId()).isEqualTo(user.getId());
		assertThat(foundUser.getLogin()).isEqualTo(user.getLogin());
	}

	@Test
	public void testGetAllUsers() {
		Collection<User> users = userStorage.getAllUsers();
		assertThat(users).isNotEmpty();
		assertThat(users).anyMatch(u -> u.getId().equals(user.getId()));
	}

	@Test
	public void testUpdateUser() {
		user.setName("Updated Name");
		userStorage.updateUser(user.getId(), user);

		User updatedUser = userStorage.getUserById(user.getId());
		assertThat(updatedUser.getName()).isEqualTo("Updated Name");
	}

	@Test
	public void testCheckUserExists() {
		boolean exists = userStorage.checkUser(user.getId());
		assertThat(exists).isTrue();

		boolean notExists = userStorage.checkUser(999);
		assertThat(notExists).isFalse();
	}

	@Test
	public void testAddAndDeleteFriend() {
		User friend = new User();
		friend.setLogin("friend_login");
		friend.setEmail("friend_email@example.com");
		friend.setName("Friend User");
		friend.setBirthday(java.time.LocalDate.of(1992, 5, 5));

		userStorage.addUser(friend);
		userStorage.addFriend(user.getId(), friend.getId());

		Collection<User> friends = userStorage.getUserFriendList(user.getId());
		assertThat(friends).hasSize(1);
		assertThat(friends).anyMatch(f -> f.getId().equals(friend.getId()));

		userStorage.deleteFriend(user.getId(), friend.getId());

		friends = userStorage.getUserFriendList(user.getId());
		assertThat(friends).isEmpty();
	}

	@Test
	public void testGetCommonFriends() {
		User friend1 = new User();
		friend1.setLogin("friend1");
		friend1.setEmail("friend1@example.com");
		friend1.setName("Friend 1");
		friend1.setBirthday(java.time.LocalDate.of(1991, 7, 7));
		userStorage.addUser(friend1);

		User friend2 = new User();
		friend2.setLogin("friend2");
		friend2.setEmail("friend2@example.com");
		friend2.setName("Friend 2");
		friend2.setBirthday(java.time.LocalDate.of(1993, 3, 3));
		userStorage.addUser(friend2);

		userStorage.addFriend(user.getId(), friend1.getId());
		userStorage.addFriend(user.getId(), friend2.getId());

		Set<User> commonFriends1 = userStorage.getCommonFriendList(user.getId(), friend1.getId());
		assertThat(commonFriends1).isEmpty();

		userStorage.addFriend(friend1.getId(), friend2.getId());
		Set<User> commonFriends2 = userStorage.getCommonFriendList(user.getId(), friend1.getId());
		assertThat(commonFriends2.size() == 1);
	}
}
