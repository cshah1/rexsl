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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a class, to indicate which XSL stylesheet to attach to
 * it during XML-to-XHTML transformation.
 *
 * <p>For example, this is your JAX-RS resource, returning JAXB-annotated
 * object on HTTP <tt>/front</tt> request:
 *
 * <pre>
 * import javax.ws.rs.GET;
 * import javax.ws.rs.Path;
 * import javax.ws.rs.Produces;
 * import javax.ws.rs.core.MediaType;
 * &#64;Path("/front")
 * public final class FrontEndResource {
 *  &#64;GET
 *  &#64;Produces(MediaType.APPLICATION_XML)
 *  public Front front() {
 *   return new Front();
 *  }
 * }
 * </pre>
 *
 * <p>Because of <tt>Produces(MediaType.APPLICATION_XML)</tt> annotation JAX-RS
 * knows that the object returned has to be converted to XML (it's done by
 * {@link javax.xml.bind.Marshaller} instantiated by {@link XslResolver}).
 * Let's say, this is the object:
 *
 * <pre>
 * import javax.xml.bind.annotation.XmlElement;
 * import javax.xml.bind.annotation.XmlRootElement;
 * &#64;XmlRootElement(name = "front")
 * public final class Front {
 *  &#64;XmlElement
 *  public String title() {
 *   return "hello, world!";
 *  }
 * }
 * </pre>
 *
 * <p>The XML generated by JAXB will look like this:
 *
 * <pre>
 * &lt;?xml version="1.0"?>
 * &lt;?xml-stylesheet href="/xsl/Front.xsl" type="text/xsl"?>
 * &lt;front>
 *  &lt;title>hello, world!&lt;/title>
 * &lt;/front>
 * </pre>
 *
 * <p>Then, this XML is sent to the browser and it converts it to XHTML using
 * provided stylesheet: <tt>/xsl/Front.xsl</tt>. This stylesheet should be
 * available in <tt>webapp</tt> directory. The name of stylesheet is constructed
 * from a simple name of the class <tt>Front</tt>.
 *
 * <p>What if we want to use
 * another XSL stylesheet for this class? Then we need <tt>&#64;Stylesheet</tt>
 * annotation:
 *
 * <pre>
 * import javax.xml.bind.annotation.XmlElement;
 * import javax.xml.bind.annotation.XmlRootElement;
 * &#64;XmlRootElement(name = "front")
 * &#64;Stylesheet("http://example.com/custom-stylesheet.xsl")
 * public final class Front {
 *  &#64;XmlElement
 *  public String title() {
 *   return "hello, world!";
 *  }
 * }
 * </pre>
 *
 * <p>Now the output XML will look like:
 *
 * <pre>
 * &lt;?xml version="1.0"?>
 * &lt;?xml-stylesheet href="http://example.com/custom-stylesheet.xsl"
 *   type="text/xsl"?>
 * &lt;front>
 *  &lt;title>hello, world!&lt;/title>
 * &lt;/front>
 * </pre>
 *
 * @author Yegor Bugayenko (yegor@rexsl.com)
 * @version $Id$
 * @see <a href="http://trac.fazend.com/rexsl/ticket/47">Feature was introduced in ticket #47</a>
 * @since 0.3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Stylesheet {

    /**
     * Get it's value.
     */
    String value();

}