package pl.syncraft.giza.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.syncraft.giza.entity.Customer;
import pl.syncraft.giza.exceptions.ValidationException;
import pl.syncraft.giza.repository.CustomerRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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
        List<String> validationErrors = new ArrayList<>();

        if (!customerRepository.findByEmail(customer.getEmail()).isEmpty())
            validationErrors.add(String.format("Customer with email: %s already exist", customer.getEmail()));

        if (!customerRepository.findByLogin(customer.getLogin()).isEmpty())
            validationErrors.add(String.format("Customer with login: %s already exist", customer.getLogin()));

        if (!validationErrors.isEmpty()) {
            String err = StringUtils.collectionToDelimitedString(validationErrors, ". ");
            throw new ValidationException(err);
        }

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
