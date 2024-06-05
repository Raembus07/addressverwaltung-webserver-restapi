/*
 * AddressDTO.java
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

public record AddressDTO(Long id, String street, String number, int plz, String city,
                         String country) {

  @Override
  public Long id() {
    return id;
  }

  @Override
  public String street() {
    return street;
  }

  @Override
  public String number() {
    return number;
  }

  @Override
  public String city() {
    return city;
  }

  @Override
  public int plz() {
    return plz;
  }

  @Override
  public String country() {
    return country;
  }
}