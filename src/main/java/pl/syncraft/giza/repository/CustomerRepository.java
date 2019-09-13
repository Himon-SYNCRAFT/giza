package pl.syncraft.giza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.syncraft.giza.entity.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByLogin(String login);
    List<Customer> findByEmail(String email);
}
