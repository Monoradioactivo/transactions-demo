package mx.simio.transactionsdemo.api.component;

import mx.simio.transactionsdemo.api.model.entity.Transaction;
import mx.simio.transactionsdemo.api.model.entity.User;
import mx.simio.transactionsdemo.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Component
public class AppStartUp implements CommandLineRunner {
    private final UserRepository userRepository;

    @Autowired
    public AppStartUp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        User userAdrian = new User("Adrian");
        User userAlexis = new User("Alexis");
        User userAlan = new User("Alan");
        User userWithNoTransactions = new User("Andres");

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("50"), "Tacos El Compa", LocalDate.parse("2020-01-08"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("50"), "Tacos El Marrano", LocalDate.parse("2020-01-09"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("20"), "La Suavecita", LocalDate.parse("2020-01-15"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("25"), "Don Carbon", LocalDate.parse("2020-01-22"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("25"), "Valentina", LocalDate.parse("2020-01-23"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("25"), "Vicente", LocalDate.parse("2020-01-24"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("25"), "Salerosa", LocalDate.parse("2020-01-28"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("10"), "45 Grados", LocalDate.parse("2020-01-29"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("10"), "Amazonas", LocalDate.parse("2020-01-29"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("30"), "Mariscos Nathan", LocalDate.parse("2020-01-29"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("20"), "Tacos Don Robe", LocalDate.parse("2020-01-01"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("20"), "Cerveceria Jimenez", LocalDate.parse("2020-02-02"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("20"), "Walmart", LocalDate.parse("2020-02-03"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("20"), "Oxxo", LocalDate.parse("2020-02-04"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("20"), "Gasolinera San Marcos", LocalDate.parse("2020-02-05"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("20"), "Cinepolis", LocalDate.parse("2020-02-05"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("20"), "Cinemex", LocalDate.parse("2020-02-06"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("20"), "Cafe Europa", LocalDate.parse("2020-02-09"))
        );

        userAdrian.addTransaction(
                new Transaction(new BigDecimal("60"), "Starbucks", LocalDate.parse("2020-02-12"))
        );

        userAlexis.addTransaction(
                new Transaction(new BigDecimal("40"), "Oxxo",  LocalDate.parse("2020-02-25"))
        );

        userAlan.addTransaction(
                new Transaction(new BigDecimal("20"), "Riot Points", LocalDate.parse("2020-02-25"))
        );

        userRepository.saveAll(Arrays.asList(userAdrian, userAlexis, userAlan, userWithNoTransactions));
    }
}
