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
  public static final String SERVLET_NAME = "persons";
  public static final int PORT = 8888;
  public static final int SECURE_PORT = 8889;
  public static final int BUFFER_SIZE = 32768;
  public static final int HEADER_SIZE = 8192;
  public static final int TIMEOUT = 30000;
  public static final int MAX_THREAD_COUNT = 500;
  public static final String SECURE_SCHEME = "https";
  public static final String WAR_PATH = "target/sql-1.0-SNAPSHOT.war";
  public static final String KEYSTORE_PATH = "/keystore.jks";
}