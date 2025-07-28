package com.alangodoy.studioApp.s.business.dto.ClientDTO;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private String email;
    private String senha;
    private Role role;
}
