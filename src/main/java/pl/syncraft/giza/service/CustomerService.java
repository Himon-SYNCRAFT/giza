package pl.syncraft.giza.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.syncraft.giza.entity.Customer;
import pl.syncraft.giza.repository.CustomerRepository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author Daniel Zawlocki
 * @date 2019/09/05
 */
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public void save(@NonNull Customer customer) {
        customerRepository.save(customer);
    }

    @Transactional
    public Optional<Customer> get(int customerId) {
        return customerRepository.findById(customerId);
    }

    @Transactional
    public void delete(int customerId) {
        Customer customer = customerRepository.getOne(customerId);

        if (customer != null) {
            customerRepository.delete(customer);
        }
    }
}
