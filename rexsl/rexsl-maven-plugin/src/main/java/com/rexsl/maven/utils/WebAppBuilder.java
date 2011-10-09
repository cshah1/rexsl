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

import com.sun.grizzly.http.webxml.schema.Filter;
import com.sun.grizzly.http.webxml.schema.FilterMapping;
import com.sun.grizzly.http.webxml.schema.WebApp;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder of {@link WebApp}.
 *
 * @author Yegor Bugayenko (yegor@rexsl.com)
 * @version $Id$
 */
final class WebAppBuilder {

    /**
     * Create parent webapp.
     * @return The webapp
     */
    public WebApp build() {
        final WebApp webapp = new WebApp();
        final Filter filter = new Filter();
        filter.setFilterClass(RuntimeFilter.class.getName());
        final String name = "ReXSLRuntimeFilter";
        filter.setFilterName(name);
        final List<Filter> filters = new ArrayList<Filter>();
        filters.add(filter);
        webapp.setFilter(filters);
        webapp.setFilterMapping(this.mappings(name));
        return webapp;
    }

    /**
     * Create mappings.
     * @param name Name of filter
     * @return The mappings
     * @see #build()
     */
    private List<FilterMapping> mappings(final String name) {
        final FilterMapping mapping = new FilterMapping();
        mapping.setFilterName(name);
        final List<String> urls = new ArrayList<String>();
        urls.add("/*");
        mapping.setUrlPattern(urls);
        final List<String> dispatchers = new ArrayList<String>();
        dispatchers.add("REQUEST");
        dispatchers.add("ERROR");
        mapping.setDispatcher(dispatchers);
        final List<FilterMapping> mappings = new ArrayList<FilterMapping>();
        mappings.add(mapping);
        return mappings;
    }

}
