package com.alangodoy.studioApp.s.infrastructure.security;


import com.alangodoy.studioApp.s.business.dto.ClientDTO.UsuarioResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.security.Client.UsuarioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private UsuarioClient client;
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    public UserDetails carregaDadosUsuario(String email, String token){

        log.info("Tentando carregar usuário para email: {}", email);
        UsuarioResponseDTO usuarioDTO = client.buscaUsuarioPorEmail(email, token);
        return User
                .withUsername(usuarioDTO.getEmail()) // Define o nome de usuário como o e-mail
                .password(usuarioDTO.getSenha())// Define a senha do usuário
                .roles(usuarioDTO.getRole().name())
                .build();

    }


}
