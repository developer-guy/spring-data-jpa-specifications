package org.specifications.demo.specifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@SpringBootApplication
public class SpecificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpecificationApplication.class, args);
	}

	@Bean
	@Order(1)
	public CommandLineRunner customerLoaderRunner(CustomerRepository repository) {

		return args -> {
			repository.save(new Customer(null, "Batuhan", "Apaydın", FinancialStatus.DEBT));
			repository.save(new Customer(null, "Asena", "Tekin", FinancialStatus.GOOD_STANDING));
		};
	}

	@Bean
	@Order(2)
	public CommandLineRunner customerSearcherRunner(CustomerRepository repository) {

		return args -> {
			repository.findByStatusIn(Collections.singletonList(FinancialStatus.DEBT))
					.forEach(customer -> log.info("Customer by findByStatusIn : " + ToStringBuilder.reflectionToString(customer)));

			repository.findAll(CustomerRepository.Specifications.firstNameIs("Batuhan"))
					.forEach(customer -> log.info("Customer by firstNameIs : " + ToStringBuilder.reflectionToString(customer)));
		};
	}
}

@RestController
@RequestMapping("customers")
class CustomerController {

	private final CustomerRepository customerRepository;

	CustomerController(final CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@GetMapping
	public List<Customer> customers() {
		return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
				.filter(customer -> customer.getStatus().equals(FinancialStatus.GOOD_STANDING))
				.collect(Collectors.toList());
	}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
class Customer {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "first_name")
	private String first;
	@Column(name = "last_name")
	private String last;

	@Enumerated(EnumType.STRING)
	private FinancialStatus status;
}

enum FinancialStatus {
	GOOD_STANDING,
	DEBT
}

interface CustomerRepository extends CrudRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
	List<Customer> findByStatusIn(List<FinancialStatus> statuses);

	class Specifications {

		public static Specification<Customer> firstNameIs(String firstName) {

			return (root, criteriaQuery, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("first"), firstName);
		}

	}
}
