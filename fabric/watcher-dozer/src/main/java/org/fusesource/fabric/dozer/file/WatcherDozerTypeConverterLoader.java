/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fusesource.fabric.dozer.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.CamelContext;
import org.apache.camel.converter.dozer.DozerTypeConverter;
import org.apache.camel.converter.dozer.DozerTypeConverterLoader;
import org.apache.camel.spi.TypeConverterRegistry;
import org.dozer.DozerBeanMapper;

public class WatcherDozerTypeConverterLoader extends DozerTypeConverterLoader {

    private final CamelContext camelContext;
    private String mappingFile;
    private final Set<ConverterFromTo> converters = new LinkedHashSet<ConverterFromTo>();

    public WatcherDozerTypeConverterLoader(CamelContext camelContext, String mappingFile, DozerBeanMapper mapper) {
        this.camelContext = camelContext;
        this.mappingFile = mappingFile;
        setMapper(mapper);
    }

    public void add() {
        init(camelContext, null);
    }

    public void update() {
        // remove before we update by adding again
        remove();
        add();
    }

    public void remove() {
        // remove converters first
        for (ConverterFromTo fromTo : converters) {
            getCamelContext().getTypeConverterRegistry().removeTypeConverter(fromTo.getTo(), fromTo.getFrom());
        }
        converters.clear();
    }

    @Override
    protected Map<String, DozerBeanMapper> lookupDozerBeanMappers() {
        List<String> mappingFiles = new ArrayList<String>(1);
        mappingFiles.add(mappingFile);

        getMapper().setMappingFiles(mappingFiles);
        Map<String, DozerBeanMapper> answer = new HashMap<String, DozerBeanMapper>(1);
        answer.put("dozer", getMapper());
        return answer;
    }

    @Override
    protected void addDozerTypeConverter(TypeConverterRegistry registry, DozerTypeConverter converter, String dozerId, Class<?> to, Class<?> from) {
        super.addDozerTypeConverter(registry, converter, dozerId, to, from);

        // remember the converters we have added
        ConverterFromTo fromTo = new ConverterFromTo(from, to);
        converters.add(fromTo);
    }

    private static final class ConverterFromTo {
        private final Class<?> from;
        private final Class<?> to;

        private ConverterFromTo(Class<?> from, Class<?> to) {
            this.from = from;
            this.to = to;
        }

        public Class<?> getFrom() {
            return from;
        }

        public Class<?> getTo() {
            return to;
        }
    }


}
