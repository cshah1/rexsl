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
package com.rexsl.core;

import com.ymock.util.Logger;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;
import org.apache.commons.lang.StringUtils;

/**
 * Wrapper around <tt>MANIFEST.MF</tt> files.
 *
 * The class will read all <tt>MANIFEST.MF</tt> files available in classpath
 * and all properties from them.
 *
 * @author Yegor Bugayenko (yegor@rexsl.com)
 * @author Prasath Premkumar (popprem@gmail.com)
 * @version $Id$
 */
public final class WarManifest {

    /**
     * Singleton instance.
     */
    public static final WarManifest INSTANCE = new WarManifest();

    /**
     * Properties retrieved from all existing <tt>MANIFEST.MF</tt> files.
     * @see #load()
     */
    private final Map<String, String> properties;

    /**
     * Private ctor.
     */
    private WarManifest() {
        synchronized (this) {
            this.properties = this.load();
        }
    }

    /**
     * Read one property and convert to the type requested.
     *
     * <p>If such a property doesn't exist {@link IllegalArgumentException}
     * will be thrown.
     *
     * @param name Name of the property
     * @return The value of the property retrieved
     */
    public String read(final String name) {
        final String value = this.properties.get(name);
        if (value == null) {
            throw new IllegalArgumentException(
                String.format(
                    "Property '%s' not found in any MANIFEST.MF file",
                    name
                )
            );
        }
        return value;
    }

    /**
     * Load properties from all files.
     * @return The properties loaded
     */
    private Map<String, String> load() {
        final Map<String, String> props = new HashMap<String, String>();
        Enumeration<URL> resources;
        try {
            resources = this.getClass().getClassLoader()
                .getResources("META-INF/MANIFEST.MF");
        } catch (java.io.IOException ex) {
            throw new IllegalStateException(ex);
        }
        Integer count = 0;
        while (resources.hasMoreElements()) {
            props.putAll(this.loadOneFile(resources.nextElement()));
            count += 1;
        }
        Logger.debug(
            this,
            "#load(): %d properties loaded from %d URL(s): %s",
            props.size(),
            count,
            StringUtils.join(props.keySet(), ", ")
        );
        return props;
    }

    /**
     * Load properties from one file.
     * @param url The URL of it
     * @return The properties loaded
     */
    private Map<String, String> loadOneFile(final URL url) {
        final Map<String, String> props = new HashMap<String, String>();
        InputStream stream;
        try {
            stream = url.openStream();
        } catch (java.io.IOException ex) {
            throw new IllegalStateException(ex);
        }
        try {
            final Manifest manifest = new Manifest(stream);
            final Attributes attrs = manifest.getMainAttributes();
            for (Object key : attrs.keySet()) {
                final String value = attrs.getValue((Name) key);
                props.put(key.toString(), value);
            }
        } catch (java.io.IOException ex) {
            throw new IllegalStateException(ex);
        } finally {
            try {
                stream.close();
            } catch (java.io.IOException ex) {
                Logger.error(
                    this,
                    "#loadOneFile('%s'): %s",
                    url,
                    ex.getMessage()
                );
            }
        }
        Logger.debug(
            this,
            "#loadOneFile('%s'): %d properties loaded",
            url,
            props.size()
        );
        return props;
    }

}
