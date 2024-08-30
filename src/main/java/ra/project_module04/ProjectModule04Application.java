package ra.project_module04;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectModule04Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectModule04Application.class, args);
    }

//    @Bean
//    public CommandLineRunner runner(PasswordEncoder passwordEncoder, IUserRepository userRepository){
//        return args -> {
//            Roles admin = new Roles(null, RoleName.ROLE_ADMIN);
//            Roles user = new Roles(null, RoleName.ROLE_USER);
//            Roles manager = new Roles(null, RoleName.ROLE_MANAGER);
//            Set<Roles> set = new HashSet<>();
//            set.add(admin);
//            set.add(user);
//            set.add(manager);
//           Users roleAdmin = new Users(null,"admin123","admin123@gmail.com","admin",passwordEncoder.encode("admin123"), null, "0353090212", null,new Date(), new Date(), true, false, set);
//           userRepository.save(roleAdmin);
//        };
//    }
}
