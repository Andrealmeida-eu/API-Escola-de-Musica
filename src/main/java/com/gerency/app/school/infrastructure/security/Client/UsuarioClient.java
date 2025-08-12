package com.alangodoy.studioApp.s.infrastructure.security.Client;


import com.alangodoy.studioApp.s.business.dto.ClientDTO.UsuarioResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "usuario", url = "http://usuario-app:8081")
public interface UsuarioClient {

    @GetMapping("/usuario/buscar-por-email")
    UsuarioResponseDTO buscaUsuarioPorEmail(@RequestParam("email") String email,
                                            @RequestHeader("Authorization") String token);

}
