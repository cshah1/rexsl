/**
 * Copyright (c) 2011, ReXSL.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the ReXSL.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rexsl.maven.utils;

import com.sun.grizzly.http.servlet.deployer.GrizzlyWebServerDeployer;
import com.sun.grizzly.http.servlet.deployer.conf.DeployerServerConfiguration;
import com.sun.grizzly.http.webxml.schema.WebApp;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 * Start/stop grizzly container.
 *
 * @author Yegor Bugayenko (yegor@rexsl.com)
 * @version $Id$
 */
public final class Grizzly {

    /**
     * Deployer.
     */
    private static final GrizzlyWebServerDeployer DEPLOYER =
        new GrizzlyWebServerDeployer();

    /**
     * Context.
     */
    private final String context;

    /**
     * Private ctor.
     * @param ctx Web context
     */
    private Grizzly(final String ctx) {
        this.context = ctx;
    }

    /**
     * Create and start Grizzly container.
     * @param webdir Location of WEB home
     * @param port The port to mount to
     * @return The container
     */
    public static Grizzly start(final File webdir, final Integer port) {
        Grizzly.julToSlf4j();
        final DeployerServerConfiguration conf =
            new DeployerServerConfiguration();
        conf.port = port;
        Grizzly.DEPLOYER.launch(conf);
        final String context = "/";
        try {
            Grizzly.DEPLOYER.deploy(
                // root folder
                webdir.getPath(),
                // context
                context,
                // path
                new File(webdir, "WEB-INF/web.xml").getPath(),
                // class loader
                Grizzly.classloader(webdir),
                // parent web application
                new WebApp()
            );
        // @checkstyle IllegalCatch (1 line)
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        return new Grizzly(context);
    }

    /**
     * Stop this container.
     */
    public void stop() {
        try {
            this.DEPLOYER.undeployApplication(this.context);
        // @checkstyle IllegalCatch (1 line)
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Initialize JUL-to-SLF4J bridge.
     */
    private static void julToSlf4j() {
        final java.util.logging.Logger rootLogger =
            java.util.logging.LogManager.getLogManager().getLogger("");
        final java.util.logging.Handler[] handlers =
            rootLogger.getHandlers();
        for (int idx = 0; idx < handlers.length; idx += 1) {
            rootLogger.removeHandler(handlers[idx]);
        }
        org.slf4j.bridge.SLF4JBridgeHandler.install();
    }

    /**
     * Create classloader for web application.
     * @param webdir Location of WEB home
     * @return The classloader
     */
    private static URLClassLoader classloader(final File webdir) {
        final List<File> paths = new ArrayList<File>();
        paths.add(new File(webdir, "WEB-INF/classes"));
        paths.add(webdir);
        final File lib = new File(webdir, "WEB-INF/lib");
        if (lib.exists()) {
            for (File jar
                : FileUtils.listFiles(lib, new String[] {"jar"}, true)) {
                paths.add(jar);
            }
        }
        final List<URL> urls = new ArrayList<URL>();
        for (File path : paths) {
            if (!path.exists()) {
                continue;
            }
            try {
                urls.add(path.toURI().toURL());
            } catch (java.net.MalformedURLException ex) {
                throw new IllegalStateException("Failed to build URL", ex);
            }
        }
        final URLClassLoader loader = new URLClassLoader(
            urls.toArray(new URL[] {}),
            Grizzly.class.getClassLoader()
        );
        return loader;
    }

}