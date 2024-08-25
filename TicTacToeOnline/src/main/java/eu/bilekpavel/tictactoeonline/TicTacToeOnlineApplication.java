package eu.bilekpavel.tictactoeonline;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicTacToeOnlineApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TicTacToeOnlineApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Slim made :)");
	}
}
