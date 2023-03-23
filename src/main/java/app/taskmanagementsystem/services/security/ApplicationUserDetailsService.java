package app.taskmanagementsystem.services.security;

import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.domain.entity.UserRoleEntity;
import app.taskmanagementsystem.repositories.UserRepository;
import app.taskmanagementsystem.security.AppUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository
                .findFirstByEmail(email)
                .map(this::mapFromUserEntityToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found!"));
    }

    private UserDetails mapFromUserEntityToUserDetails(UserEntity userEntity) {
        return new AppUserDetails(
                userEntity.getEmail(),
                userEntity.getPassword(),
                extractAuthorities(userEntity),
                userEntity.getFirstName() + " " + userEntity.getLastName(),
                userEntity.getUsername()
        );
    }


    private List<GrantedAuthority> extractAuthorities(UserEntity userEntity) {
        return userEntity
                .getRoles()
                .stream()
                .map(this::mapFromUserRoleToSimpleGrantedAuthority)
                .toList();
    }

    private GrantedAuthority mapFromUserRoleToSimpleGrantedAuthority(UserRoleEntity userRoleEntity) {
        return new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getRole().name());
    }
}
