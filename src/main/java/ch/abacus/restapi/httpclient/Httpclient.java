/*
 * HttpClientWithoutFramwork.java
 *
 * Creator:
 * 05.06.2024 08:09 josia.schweizer
 *
 * Maintainer:
 * 05.06.2024 08:09 josia.schweizer
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2024 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus.restapi.httpclient;

import ch.abacus.common.RestapiConst;
import ch.abacus.restapi.PersonService;
import ch.abacus.restapi.dto.PersonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public class Httpclient implements PersonService {

  private final HttpClient client;
  private final ObjectMapper objectMapper;

  public Httpclient() {
    client = HttpClient.newHttpClient();
    objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
  }

  @Override
  public List<PersonDTO> findAllPerson() {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(RestapiConst.REST_URL))
        .GET()
        .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        return objectMapper.readValue(response.body(), new TypeReference<>() {
        });
      } else {
        throw new RuntimeException("Failed to get persons: " + response.statusCode());
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to get persons", e);
    }
  }

  @Override
  public Optional<PersonDTO> findById(@Nonnull Long id) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(RestapiConst.REST_URL + "/" + id))
        .GET()
        .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        return Optional.ofNullable(objectMapper.readValue(response.body(), PersonDTO.class));
      } else {
        return Optional.empty();
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to get person", e);
    }
  }

  @Override
  public void deleteById(@Nonnull Long id) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(RestapiConst.REST_URL + "/" + id))
        .DELETE()
        .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        throw new RuntimeException("Failed to delete person: " + response.statusCode());
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to delete person", e);
    }
  }

  @Override
  public PersonDTO save(@Nonnull PersonDTO personDTO) throws JsonProcessingException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(RestapiConst.REST_URL))
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(personDTO)))
        .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        return objectMapper.readValue(response.body(), PersonDTO.class);
      } else {
        return personDTO;
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to save person", e);
    }
  }

  @Override
  public PersonDTO update(@Nonnull Long id, @Nonnull PersonDTO personDTO) throws JsonProcessingException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(RestapiConst.REST_URL + "/" + id))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(personDTO)))
        .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        return objectMapper.readValue(response.body(), PersonDTO.class);
      } else {
        return personDTO;
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to update person", e);
    }
  }
}