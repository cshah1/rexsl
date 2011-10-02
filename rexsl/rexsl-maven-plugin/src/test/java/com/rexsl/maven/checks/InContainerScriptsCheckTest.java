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
package com.rexsl.maven.checks;

import com.rexsl.maven.Check;
import com.rexsl.maven.Environment;
import java.io.File;
import org.apache.maven.plugin.logging.Log;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

/**
 * InContainerScriptsCheck test case.
 * @author Yegor Bugayenko (yegor@rexsl.com)
 * @version $Id$
 */
public final class InContainerScriptsCheckTest {

    /**
     * Temporary folder.
     * @checkstyle VisibilityModifier (3 lines)
     */
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    /**
     * Initialize SLF4J bridge.
     */
    @BeforeClass
    public static void initSlf4jBridge() {
        org.slf4j.impl.StaticLoggerBinder.getSingleton()
            .setMavenLog(Mockito.mock(Log.class));
    }

    /**
     * Validate correct XML+XSL transformation.
     * @throws Exception If something goes wrong
     */
    @Test
    public void textTruePositiveValidation() throws Exception {
        final File basedir = this.temp.newFolder("basedir");
        Utils.copy(basedir, "src/main/webapp/xsl/layout.xsl");
        Utils.copy(basedir, "src/main/webapp/xsl/Home.xsl");
        Utils.copy(basedir, "src/main/webapp/WEB-INF/web.xml");
        Utils.copy(basedir, "src/test/rexsl/scripts/home.groovy");
        final DummyReporter reporter = new DummyReporter();
        final Environment env = Mockito.mock(Environment.class);
        Mockito.doReturn(basedir).when(env).basedir();
        Mockito.doReturn(new File(basedir, "src/main/webapp"))
            .when(env).webdir();
        Mockito.doReturn(reporter).when(env).reporter();
        Mockito.doReturn(this.getClass().getClassLoader())
            .when(env).classloader();
        final Check check = new InContainerScriptsCheck();
        MatcherAssert.assertThat(
            check.validate(env),
            Matchers.describedAs(reporter.summary(), Matchers.is(true))
        );
    }

}