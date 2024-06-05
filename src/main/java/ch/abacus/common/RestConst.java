/*
 * RestConst.java
 *
 * Creator:
 * 05.06.2024 11:21 josia.schweizer
 *
 * Maintainer:
 * 05.06.2024 11:21 josia.schweizer
 *
 * Last Modification:
 * $Id:$
 *
 * Copyright (c) 2024 ABACUS Research AG, All Rights Reserved
 */
package ch.abacus.common;

public class RestConst {
  public static final int PORT = 8080;
  public static final int SECURE_PORT = 8888;
  public static final int MAX_THREAD_COUNT = 100;
  public static final int TIMEOUT = 100;
  public static final int BUFFER_SIZE = 8192;
  public static final int HEADER_SIZE = 8192;
  public static final String SECURE_SCHEME = "https";
  public static final String SERVLET_NAME = "jersey-servlet";
  public static final String KEYSTORE_PATH = "keystore.jks";
  public static final String WAR_PATH = "webapp";
}