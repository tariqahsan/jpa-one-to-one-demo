package mil.dtic.ured.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import mil.dtic.ured.model.Customer;
import mil.dtic.ured.model.RequestInfo;
import mil.dtic.ured.repository.CustomerRepository;
import mil.dtic.ured.util.ErrorMessage;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Validated
@RequestMapping("/api")
public class CustomerController {

	@Autowired
	CustomerRepository repository;
	
	@Autowired
	MessageSource messageSource;

	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = new ArrayList<>();
		try {
			repository.findAll().forEach(customers::add);
			
			if (customers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(customers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/customers/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") long id) {
		Optional<Customer> customerData = repository.findById(id);

		if (customerData.isPresent()) {
			return new ResponseEntity<>(customerData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/customer")
	public ResponseEntity<Customer> postCustomer(@RequestBody Customer customer) {
		System.out.println(customer.getAddress());
		try {
			Customer customer2 = repository.save(new Customer(customer.getName(), customer.getAddress(), customer.getAge()));
			System.out.println(customer.getAddress());
			return new ResponseEntity<>(customer, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}
	

	//@PostMapping(value = "/customers", consumes = {"application/xml", "application/json"})	
	//@Consumes({"application/xml", "application/json"})
	@PostMapping("/customers")
	@ResponseBody
	public ResponseEntity<List<Customer>> postCustomers(@RequestBody List<@Valid Customer> customers) {
		List<Customer> customers2 = new ArrayList<>();
		//try {
//			for (Customer customer : customers) {
//				Customer customer2 = repository.save(new Customer(customer.getName(), customer.getAddress(), customer.getAge()));
//				System.out.println(customer.getAddress());
//				customers2.add(customer2);
//			}
			customers
            .stream()
            .forEach(customer -> {            
				Customer customer2 = repository.save(new Customer(customer.getName(), customer.getAddress(), customer.getAge()));
				System.out.println(customer.getAddress());
				customers2.add(customer2);
            });
		//} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		//}
		return new ResponseEntity<List<Customer>>(customers2, HttpStatus.CREATED);
		
	}
	
	@PostMapping(value = "/create")
	public ResponseEntity create(@RequestBody @Valid final List<Customer> customers, BindingResult bindingResult) {
		List<String> errorList = new ArrayList<>();
		List<ErrorMessage> errorMessages = new ArrayList<>();
		if (bindingResult.hasErrors()) {
			 bindingResult.getFieldErrors().forEach(fieldError ->
	            errorList.add(fieldError.getField() + ": " + messageSource.getMessage(fieldError, Locale.US))
	        );
			 bindingResult
	            .getFieldErrors()
	            .stream()
	            .forEach(fieldError -> {            
	            	ErrorMessage errorMessage = new ErrorMessage(messageSource.getMessage(fieldError, Locale.US), fieldError.getField());           	
	            	System.out.println(errorMessage.getMessage());
	            	System.out.println(errorMessage.getFieldName());
	            	errorMessages.add(errorMessage);
	            });
			 return new ResponseEntity<>(errorMessages, HttpStatus.NOT_ACCEPTABLE);
		}

		//System.out.println(requestInfo.toString());
		return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
	
//	@RequestMapping(value = "/customers", method = RequestMethod.POST, consumes = "application/json")
//	@ResponseBody
//	public ResponseEntity<List<Customer>> postCustomers(@RequestBody List<Customer> customers) {
//		try {
//			for (Customer customer : customers) {
//				repository.save(new Customer(customer.getName(), customer.getAddress(), customer.getAge()));
//				System.out.println(customer.getAddress());
//			}
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
//		}
//		return new ResponseEntity<List<Customer>>(customers, HttpStatus.CREATED);
//		
//	}

	@DeleteMapping("/customers/{id}")
	public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") long id) {
		try {
			repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/customers")
	public ResponseEntity<HttpStatus> deleteAllCustomers() {
		try {
			repository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}

	@GetMapping(value = "customers/age/{age}")
	public ResponseEntity<List<Customer>> findByAge(@PathVariable int age) {
		try {
			List<Customer> customers = repository.findByAge(age);

			if (customers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(customers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/customers/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
		Optional<Customer> customerData = repository.findById(id);

		if (customerData.isPresent()) {
			Customer customer1 = customerData.get();
			customer1.setName(customer1.getName());
			customer1.setAge(customer1.getAge());
			customer1.setActive(customer1.isActive());
			return new ResponseEntity<>(repository.save(customer1), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
