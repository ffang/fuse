/**
 * Copyright (C) FuseSource, Inc.
 * http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fusesource.fabric.xjc;

import javax.xml.bind.JAXBContext;

import org.apache.camel.converter.jaxb.JaxbDataFormat;

/**
 * An implementation of {@link JaxbDataFormat} which can deal with compiling XSD files on the fly
 * to create the JAXB context
 */
public class DynamicJaxbDataFormat extends JaxbDataFormat {
    public DynamicJaxbDataFormat() {
    }

    public DynamicJaxbDataFormat(JAXBContext context) {
        super(context);
    }

    public DynamicJaxbDataFormat(String contextPath) {
        super(contextPath);
    }

    /**
     * Whenever any kind of compiler is performed this is the handler method to pass in the results
     */
    public void updateCompileResults(CompileResults compileResults) {
        setContext(compileResults.getJAXBContext());
    }
}
