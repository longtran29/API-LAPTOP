package com.springboot.laptop.security.services;

import com.springboot.laptop.model.Account;
import com.springboot.laptop.model.Customer;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.AccountRepository;
import com.springboot.laptop.repository.CustomerRepository;
import com.springboot.laptop.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final CustomerRepository userRepository;

    private final AccountRepository employeeRepository;

    private final UserRoleRepository userRoleRepository;


//    create custom userdetail Service -  load user from db and validate password
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> existingCustomer =  userRepository.findByUsernameIgnoreCase(username);

        if(existingCustomer.isPresent()) {
            Customer customer = userRepository.findByUsernameIgnoreCase(username).get();
            UserDetails existingUser =  mapToUserDetails(customer);
            return existingUser;
        }

        Account existingAccount =  employeeRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new RuntimeException("This user not found on the server"));

        return mapToUserDetails(existingAccount);
    }

    // note: SimpleGrantedAuthority

    private UserDetails mapToUserDetails(Customer customer) {

        String role = userRoleRepository.findByName(UserRoleEnum.ROLE_CUSTOMER).get().getName().name();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        return new User(
                customer.getUsername(),
                customer.getPassword(),
                authorities
        );
    }

    private UserDetails mapToUserDetails(Account userEntity) {

        List<GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(ur -> new SimpleGrantedAuthority(ur.getName().name())).collect(Collectors.toList());
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                authorities
        );
    }
}
