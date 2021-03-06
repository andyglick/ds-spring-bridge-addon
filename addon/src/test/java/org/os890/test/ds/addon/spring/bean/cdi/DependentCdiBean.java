/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.os890.test.ds.addon.spring.bean.cdi;

import org.os890.test.ds.addon.spring.bean.spring.NamedSpringBean;
import org.os890.test.ds.addon.spring.bean.spring.PrototypeSpringBean;
import org.os890.test.ds.addon.spring.bean.spring.SingletonSpringBean;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@SuppressWarnings("CdiInjectionPointsInspection")
@Dependent
public class DependentCdiBean
{
    @Inject
    private SingletonSpringBean singletonSpringBean;

    @Inject
    private PrototypeSpringBean prototypeSpringBean;

    @Inject
    private NamedSpringBean namedSpringBean;

    public SingletonSpringBean getSingletonSpringBean()
    {
        return singletonSpringBean;
    }

    public PrototypeSpringBean getPrototypeSpringBean()
    {
        return prototypeSpringBean;
    }

    public NamedSpringBean getNamedSpringBean()
    {
        return namedSpringBean;
    }
}
