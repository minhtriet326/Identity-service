package com.devteria.identity_service.configurations;

import java.util.Date;
import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.devteria.identity_service.entities.InvalidatedToken;
import com.devteria.identity_service.entities.User;
import com.devteria.identity_service.repositories.InvalidatedTokenRepository;
import com.devteria.identity_service.repositories.UserRepository;

// @Configuration
// @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// @RequiredArgsConstructor
// public class ApplicationInitConfig {
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Bean
//    ApplicationRunner applicationRunner(UserRepository userRepository) {
//        return args -> {
//          if (!userRepository.existsByUsername("admin")) {
//              Set<String> roles = new HashSet<>();
//              roles.add(Role.ADMIN.name());
//
//              User user = User.builder()
//                      .username("admin")
//                      .password(passwordEncoder.encode("12345"))
//                      .roles(roles)
//                      .build();
//
//              userRepository.save(user);
//          }
//        };
//    }
// }
@Configuration
public class ApplicationInitConfig {
  PasswordEncoder passwordEncoder;

  @Bean
  @ConditionalOnProperty(
      prefix = "spring",
      value = "driverClassName",
      havingValue = "com.mysql.cj.jdbc.Driver") // bean chỉ đc init lên khi className là
  // com.mysql.cj.jdbc.Driver
  public ApplicationRunner applicationRunner1(UserRepository userRepository) {
    return args -> {
      if (!userRepository.existsByUsername("admin")) {
        //                Set<Role> roles = new HashSet<>();
        //                roles.add(roleRepository.findById("ADMIN").get());

        User admin =
            User.builder()
                .username("admin")
                .password(passwordEncoder.encode("12345"))
                //                        .roles(roles)
                .build();

        userRepository.save(admin);
      }
    };
  }

  @Bean
  @ConditionalOnProperty(
      prefix = "spring",
      value = "driverClassName",
      havingValue = "com.mysql.cj.jdbc.Driver")
  public ApplicationRunner applicationRunner2(
      InvalidatedTokenRepository invalidatedTokenRepository) {
    return args -> {
      List<InvalidatedToken> invalidatedTokenSet = invalidatedTokenRepository.findAll();

      invalidatedTokenSet.forEach(
          token -> {
            if (!token.getExpiryTime().after(new Date())) {
              invalidatedTokenRepository.delete(token);
            }
          });
    };
  }
}
