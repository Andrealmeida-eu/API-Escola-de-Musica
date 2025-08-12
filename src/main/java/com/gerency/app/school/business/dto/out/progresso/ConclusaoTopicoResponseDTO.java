package com.alangodoy.studioApp.s.business.dto.out.progresso;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConclusaoTopicoResponseDTO {
    @NotBlank
private String observacoes;
}

