package com.alangodoy.studioApp.s.infrastructure.exceptions;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.coyote.BadRequestException;

import java.util.HashMap;

public class CustomErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {
    return switch (response.status()) {
      case 400 -> new BadRequestException("Requisição inválida para o serviço de usuários");
      case 404 -> new ResourceNotfoundException("Recurso não encontrado no serviço de usuários");
      case 503 -> new DataInvalidaException("Serviço de usuários indisponível");
      default -> new FeignException.FeignClientException(
              response.status(),
              "Erro na comunicação com o serviço de usuários",
              response.request(),
              response.body() != null ? response.body().toString().getBytes() : null,
              response.headers() != null ? new HashMap<>(response.headers()) : null
      );
    };
  }
}