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
package com.rexsl.test;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;

/**
 * JAXB-empowered object to XML converting utility.
 *
 * @author Yegor Bugayenko (yegor@rexsl.com)
 * @version $Id$
 */
public final class JaxbConverter {

    /**
     * Private ctor, to avoid direct instantiation of the class.
     */
    private JaxbConverter() {
        // intentionally empty
    }

    /**
     * Convert it to XML. The object has to be annotated with JAXB annotations
     * in order to be convertable.
     *
     * <p>Let's consider an example JAXB-annotated class:
     *
     * <pre>
     * {@code
     * import javax.xml.bind.annotation.XmlAccessType;
     * import javax.xml.bind.annotation.XmlAccessorType;
     * import javax.xml.bind.annotation.XmlElement;
     * import javax.xml.bind.annotation.XmlRootElement;
     * @XmlRootElement(name = "employee")
     * @XmlAccessorType(XmlAccessType.NONE)
     * private static final class Employee {
     *   @XmlElement(name = "name")
     *   public String getName() {
     *     return "John Doe";
     *   }
     * }
     * }
     * </pre>
     *
     * <p>Now you want to test how it works with real data after convertion
     * to XML (in a unit test):
     *
     * <pre>
     * {@code
     * import com.rexsl.test.JaxbConverter;
     * import org.hamcrest.Matchers;
     * import org.junit.Assert;
     * import org.junit.Test;
     * import org.xmlmatchers.XmlMatchers;
     * public final class EmployeeTest {
     *   @Test
     *   public void testObjectToXmlConversion() throws Exception {
     *     final Object object = new Employee();
     *     Assert.assertThat(
     *       JaxbConverter.the(object),
     *       XmlMatchers.hasXPath("/employee/name[.='John Doe']")
     *     );
     *   }
     * }
     * }
     * </pre>
     *
     * <p>We recommend to use <tt>XmlMatchers</tt> class from this Maven
     * artifact:
     * <pre>
     * {@code
     * &lt;dependency>
     *   &lt;groupId>org.xmlmatchers&lt;/groupId>
     *   &lt;artifactId>xml-matchers&lt;/artifactId>
     *   &lt;version>0.10&lt;/version>
     * &lt;/dependency>
     * }
     * </pre>
     *
     * @param object The object to convert
     * @return DOM source/document
     * @throws Exception If anything goes wrong
     */
    public static Source the(final Object object) throws Exception {
        final JAXBContext ctx = JAXBContext.newInstance(object.getClass());
        final Marshaller mrsh = ctx.createMarshaller();
        mrsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        final StringWriter writer = new StringWriter();
        mrsh.marshal(object, writer);
        final String xml = writer.toString();
        return new StringSource(xml);
    }

}
