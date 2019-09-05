package pl.syncraft.giza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.syncraft.giza.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
