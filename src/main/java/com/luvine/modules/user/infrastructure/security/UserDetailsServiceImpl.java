package com.luvine.modules.user.infrastructure.security;

import com.luvine.modules.user.domain.valueobject.Email;
import com.luvine.modules.user.infrastructure.repository.UserCredentialsRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserCredentialsRepository credentialsRepository;

    public UserDetailsServiceImpl(UserCredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public @NullMarked UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialsRepository.findByEmail(new Email(username))
                .map(UserCredentialsDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas."));
    }
}