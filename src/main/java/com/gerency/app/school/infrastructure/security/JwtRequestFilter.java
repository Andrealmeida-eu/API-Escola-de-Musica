package com.alangodoy.studioApp.s.infrastructure.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Define a classe JwtRequestFilter, que estende OncePerRequestFilter
public class JwtRequestFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
    // Define propriedades para armazenar instâncias de JwtUtil e UserDetailsService
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;


    // Construtor que inicializa as propriedades com instâncias fornecidas
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Método chamado uma vez por requisição para processar o filtro
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        System.out.println("MétodoII: " + request.getMethod()); // Log do método HTTP
        System.out.println("PathII: " + request.getRequestURI());


        // Obtém o valor do header "Authorization" da requisição
        final String authorizationHeader = request.getHeader("Authorization");


        // Verifica se o cabeçalho existe e começa com "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrai o token JWT do cabeçalho
            final String token = authorizationHeader.substring(7);
            // Extrai o nome de usuário do token JWT
            final String username = jwtUtil.extrairEmailToken(token);

            log.debug("Processando token JWT: {}", token != null ? token.substring(0, 10) + "..." : "null");

            // Se o nome de usuário não for nulo e o usuário não estiver autenticado ainda
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carrega os detalhes do usuário a partir do nome de usuário
                UserDetails userDetails = userDetailsService.carregaDadosUsuario(username, authorizationHeader);
                log.debug("Processando user detaJWT: {}", token != null ? token.substring(0, 10) + "..." : "null");
                // Valida o token JWT
                if (jwtUtil.validateToken(token, username)) {
                    // Cria um objeto de autenticação com as informações do usuário
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    // Define a autenticação no contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Métodoii: " + authentication); // Log do método HTTP
                    System.out.println("Pathii: " + request.getRequestURI());

                }


            }
        }
        System.out.println("MétodoBB: " + request.getMethod());
        // Continua a cadeia de filtros, permitindo que a requisição prossiga
        chain.doFilter(request, response);
    }
}
