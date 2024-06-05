/*
 * LocalDateAdapter.java
 *
 * Creator:
 * 03.04.2024 08:04 josia.schweizer
 *
 * Maintainer:
 * 03.04.2024 08:04 josia.schweizer
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2024 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus.fileio;


import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

  @Override
  public LocalDate unmarshal(String v) {
    return LocalDate.parse(v);
  }

  @Override
  public String marshal(LocalDate v) {
    return v.toString();
  }
}