/*
 * JerseyConfig.java
 *
 * Creator:
 * 05.06.2024 14:08 josia.schweizer
 *
 * Maintainer:
 * 05.06.2024 14:08 josia.schweizer
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2024 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus.restapi.jersey;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    packages("ch.abacus.sql.rest.jersey");
  }

}