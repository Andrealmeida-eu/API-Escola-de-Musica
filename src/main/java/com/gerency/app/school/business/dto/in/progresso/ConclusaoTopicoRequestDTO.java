package com.alangodoy.studioApp.s.business.dto.in.progresso;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConclusaoTopicoRequestDTO {
    @NotBlank
private String observacoes;
}

