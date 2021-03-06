package com.company.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {

	private MailService mailService;

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	// Java version:
	// private List<User> users = new ArrayList<>(List.of( // users:
	// 		new User(1, "bob@example.com", "password", "Bob"), // bob
	// 		new User(2, "alice@example.com", "password", "Alice"), // alice
	// 		new User(3, "tom@example.com", "password", "Tom"))); // tom

	private List<User> users = Arrays.asList(
			new User(1, "bob@example.com", "password", "Bob"), // bob
			new User(2, "alice@example.com", "password", "Alice"), // alice
			new User(3, "tom@example.com", "password", "Tom") // tom
			);

	public User login(String email, String password) {
		for (User user : users) {
			if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
				mailService.sendLoginMail(user);
				return user;
			}
		}
		throw new RuntimeException("login failed.");
	}

	public User getUser(long id) {
		// Java version:
		// return this.users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow();
		return this.users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow(RuntimeException::new);
	}

	public User register(String email, String password, String name) {
		users.forEach((user) -> {
			if (user.getEmail().equalsIgnoreCase(email)) {
				throw new RuntimeException("email exist.");
			}
		});
		User user = new User(users.stream().mapToLong(u -> u.getId()).max().getAsLong(), email, password, name);
		users.add(user);
		mailService.sendRegistrationMail(user);
		return user;
	}
}
