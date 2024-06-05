/*
 * PersonDTO.java
 *
 * Creator:
 * 31.05.2024 08:47 josia.schweizer
 *
 * Maintainer:
 * 31.05.2024 08:47 josia.schweizer
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2024 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus.restapi.dto;

import ch.abacus.personmanagement.Gender;

import java.time.LocalDate;

public record PersonDTO(Long id,  String firstname, String lastname, LocalDate birthday, Gender gender, AddressDTO addressDTO) {

  @Override
  public Long id() {
    return id;
  }

  @Override
  public String firstname() {
    return firstname;
  }

  @Override
  public String lastname() {
    return lastname;
  }

  @Override
  public LocalDate birthday() {
    return birthday;
  }

  @Override
  public Gender gender() {
    return gender;
  }

  @Override
  public AddressDTO addressDTO() {
    return addressDTO;
  }
}