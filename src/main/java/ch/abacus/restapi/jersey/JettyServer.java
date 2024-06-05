package ch.abacus.restapi.jersey;

import ch.abacus.common.RestConst;
import jakarta.annotation.Nonnull;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


public class JettyServer {

    static {
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
        System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
    }

    public static void main(String[] args) throws Exception {
        final var server = createServer(RestConst.PORT, RestConst.SECURE_PORT);
        // Extra options
        server.setDumpAfterStart(true);
        server.setDumpBeforeStop(false);
        server.setStopAtShutdown(true);

        final var handler = new ServletContextHandler();
        server.setHandler(handler);

        handler.getServletHandler().addServletWithMapping(/*createJavaHolder()*/createJerseyHolder(), "/api/*");

        // Start the server
        server.start();
        server.join();
    }

    @SuppressWarnings({"squid:S1144", "squid:S125"})
//    private static ServletHolder createJavaHolder(){
//        return new ServletHolder(PersonConst.SERVLET_NAME, PersonHttpServlet.class);
//    }

    private static ServletHolder createJerseyHolder(){
        return new ServletHolder(RestConst.SERVLET_NAME, new ServletContainer(new JerseyConfig()));
    }

    @SuppressWarnings("squid:S112")
    public static Server createServer(int port, int securePort) throws Exception {
        // === jetty.xml ===
        // Setup Threadpool
        final var threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(RestConst.MAX_THREAD_COUNT);
        // Server
        final var server = new Server(threadPool);
        // Scheduler
        server.addBean(new ScheduledExecutorScheduler(null, false));

        final var context = new WebAppContext();
        final var war = Paths.get(RestConst.WAR_PATH).toFile();
        if(!war.exists()){
            throw new FileNotFoundException(war.toString());
        }
        context.setWar(war.getAbsolutePath());
        context.setConfigurationDiscovered(true);
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*");
        context.setConfigurationDiscovered(true);
        context.setContextPath("/");
        // compression -> experimental......
        context.insertHandler(new GzipHandler());
        server.setHandler(context);

        // HTTP Configuration
        final var httpConfig = new HttpConfiguration();
        httpConfig.setSecureScheme(RestConst.SECURE_SCHEME);
        httpConfig.setSecurePort(securePort);
        httpConfig.setOutputBufferSize(RestConst.BUFFER_SIZE);
        httpConfig.setRequestHeaderSize(RestConst.HEADER_SIZE);
        httpConfig.setResponseHeaderSize(RestConst.HEADER_SIZE);
        httpConfig.setSendServerVersion(true);
        httpConfig.setSendDateHeader(false);

        // === jetty-http.xml ===
        final var http = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
        http.setPort(port);
        http.setIdleTimeout(RestConst.TIMEOUT);
        server.addConnector(http);

        // === jetty-https.xml ===
        // SSL Context Factory
        final var keystorePath = Path.of(Objects.requireNonNull(JettyServer.class.getResource(RestConst.KEYSTORE_PATH)).toURI());
        if (!Files.exists(keystorePath)) {
            throw new FileNotFoundException(keystorePath.toString());
        }
        var sslConnector = getServerConnector(keystorePath, httpConfig, server);
        sslConnector.setPort(securePort);
        server.addConnector(sslConnector);
        return server;
    }

    private static ServerConnector getServerConnector(@Nonnull final Path keystorePath, @Nonnull final HttpConfiguration httpConfig, @Nonnull final Server server) {
        final char[] sec = {101, 108, 105, 101, 108, 105, 101, 108, 105};
        final var sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(keystorePath.toString());
        final var pw = String.valueOf(sec);
        sslContextFactory.setKeyStorePassword(pw);
        sslContextFactory.setKeyManagerPassword(pw);
        sslContextFactory.setTrustStorePath(keystorePath.toString());
        sslContextFactory.setTrustStorePassword(pw);

        // SSL HTTP Configuration
        var httpsConfig = new HttpConfiguration(httpConfig);
        SecureRequestCustomizer src = new SecureRequestCustomizer();
        // important flag because we use selfsigned certificates with no localhost in it....
        src.setSniHostCheck(false);
        httpsConfig.addCustomizer(src);

        // SSL Connector
        return new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(httpsConfig));
    }
}
