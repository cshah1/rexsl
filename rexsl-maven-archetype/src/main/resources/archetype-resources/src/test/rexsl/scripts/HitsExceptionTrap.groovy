/**
 * Copyright (c) 2011-2013, ReXSL.com
 * All rights reserved.
 */
package ${package}.rexsl.scripts

import com.rexsl.test.RestTester
import javax.ws.rs.core.UriBuilder

RestTester.start(UriBuilder.fromUri(rexsl.home).path('/trap'))
    .get('hits exception trap')
    .assertStatus(HttpURLConnection.HTTP_OK)
    .assertXPath('//xhtml:title[.="Internal application error"]')
