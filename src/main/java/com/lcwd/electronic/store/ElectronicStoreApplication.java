package com.lcwd.electronic.store;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ElectronicStoreApplication.class, args);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Value("${normal.role.id}")
    private String role_normal_id;
    @Value("${admin.role.id}")
    private String role_admin_id;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(passwordEncoder.encode("abcd"));

        try {
            // Check and save roles if they don't exist
            Optional<Role> adminRoleOpt = roleRepository.findById(role_admin_id);
            if (adminRoleOpt.isEmpty()) {
                Role role_admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
                roleRepository.save(role_admin);
            }

            Optional<Role> normalRoleOpt = roleRepository.findById(role_normal_id);
            if (normalRoleOpt.isEmpty()) {
                Role role_normal = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
                roleRepository.save(role_normal);
            }

            // Check and save users if they don't exist
            Optional<User> adminUserOpt = userRepository.findByEmail("admin@gmail.com");
            if (adminUserOpt.isEmpty()) {
                User adminUser = User.builder()
                        .name("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin123"))
                        .gender("Male")
                        .imageName("default.png")
                        .roles(Set.of(adminRoleOpt.orElseThrow(), normalRoleOpt.orElseThrow()))
                        .userId(UUID.randomUUID().toString())
                        .about("I am admin User")
                        .build();
                userRepository.save(adminUser);
            }

            Optional<User> normalUserOpt = userRepository.findByEmail("pranjal@gmail.com");
            if (normalUserOpt.isEmpty()) {
                User normalUser = User.builder()
                        .name("Pranjal")
                        .email("pranjal@gmail.com")
                        .password(passwordEncoder.encode("pranjal123"))
                        .gender("Male")
                        .imageName("default.png")
                        .roles(Set.of(normalRoleOpt.orElseThrow()))
                        .userId(UUID.randomUUID().toString())
                        .about("I am Normal User")
                        .build();
                userRepository.save(normalUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
