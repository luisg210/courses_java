package com.lulu.msvcauth.service;

import com.lulu.msvcauth.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Collections;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private WebClient.Builder client;

    //private Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            Usuario user = client
                    .build()
                    .get()
                    .uri("http://localhost:8081/users/login",
                            uri -> uri.queryParam("email", email).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Usuario.class)
                    .block();

            return new User(email, user.getPassword(), true, true,
                    true, true,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());

            throw new UsernameNotFoundException("¡Usuario no existe!");
        }
    }
}