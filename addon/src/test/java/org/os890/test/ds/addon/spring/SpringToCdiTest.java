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
package org.os890.test.ds.addon.spring;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.api.provider.DependentProvider;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.os890.test.ds.addon.spring.bean.cdi.ApplicationScopedCdiBean;
import org.os890.test.ds.addon.spring.bean.cdi.DependentCdiBean;
import org.os890.test.ds.addon.spring.bean.spring.NamedSpringBean;
import org.os890.test.ds.addon.spring.bean.spring.PrototypeSpringBean;
import org.os890.test.ds.addon.spring.bean.spring.SingletonSpringBean;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("CdiInjectionPointsInspection")
@RunWith(CdiTestRunner.class)
public class SpringToCdiTest
{
  @Inject
  private ApplicationScopedCdiBean applicationScopedCdiBean;

  @Test
  public void directSpringBeanLookupTest()
  {
    assertThat(BeanProvider.getContextualReference(SingletonSpringBean.class)).isNotNull();
    assertThat(BeanProvider.getContextualReference(PrototypeSpringBean.class)).isNotNull();
    assertThat(BeanProvider.getContextualReference(NamedSpringBean.class)).isNotNull();
  }

  @Test
  public void applicationScopedCdiBeanTest()
  {
    assertThat(this.applicationScopedCdiBean).isNotNull();
    assertThat(this.applicationScopedCdiBean.getSingletonSpringBean()).isNotNull();
    assertThat(this.applicationScopedCdiBean.getPrototypeSpringBean()).isNotNull();
    assertThat(this.applicationScopedCdiBean.getNamedSpringBean()).isNotNull();

    String singletonSpringBeanValue = this.applicationScopedCdiBean.getSingletonSpringBean().getValue();
    String prototypeSpringBeanValue = this.applicationScopedCdiBean.getPrototypeSpringBean().getValue();
    String namedSpringBeanValue = this.applicationScopedCdiBean.getNamedSpringBean().getValue();

    assertThat(singletonSpringBeanValue).isNotNull();
    assertThat(prototypeSpringBeanValue).isNotNull();
    assertThat(namedSpringBeanValue).isNotNull();

    //values won't change with additional lookups
    assertThat(singletonSpringBeanValue)
      .isEqualTo(BeanProvider.getContextualReference(ApplicationScopedCdiBean.class).getSingletonSpringBean().getValue());
    assertThat(prototypeSpringBeanValue)
      .isEqualTo(BeanProvider.getContextualReference(ApplicationScopedCdiBean.class).getPrototypeSpringBean().getValue());
    assertThat(namedSpringBeanValue)
      .isEqualTo(BeanProvider.getContextualReference(ApplicationScopedCdiBean.class).getNamedSpringBean().getValue());
  }

  @Test
  public void dependentScopedCdiBeanTest()
  {
    DependentProvider<DependentCdiBean> dependentCdiBeanHolder = BeanProvider.getDependent(DependentCdiBean.class);
    DependentCdiBean dependentCdiBean = dependentCdiBeanHolder.get();

    assertThat(dependentCdiBean).isNotNull();
    assertThat(dependentCdiBean.getSingletonSpringBean()).isNotNull();
    assertThat(dependentCdiBean.getPrototypeSpringBean()).isNotNull();
    assertThat(dependentCdiBean.getNamedSpringBean()).isNotNull();

    String singletonSpringBeanValue = dependentCdiBean.getSingletonSpringBean().getValue();
    String prototypeSpringBeanValue = dependentCdiBean.getPrototypeSpringBean().getValue();
    String namedSpringBeanValue = dependentCdiBean.getNamedSpringBean().getValue();

    assertThat(singletonSpringBeanValue).isNotNull();
    assertThat(prototypeSpringBeanValue).isNotNull();
    assertThat(namedSpringBeanValue).isNotNull();

    dependentCdiBeanHolder.destroy(); //owb as well as weld don't handle that correctly for injected "external"-beans

    //values of prototype spring-beans will change

    dependentCdiBeanHolder = BeanProvider.getDependent(DependentCdiBean.class);
    dependentCdiBean = dependentCdiBeanHolder.get();

    assertThat(singletonSpringBeanValue).isEqualTo(dependentCdiBean.getSingletonSpringBean().getValue());
    assertThat(prototypeSpringBeanValue).isNotSameAs(dependentCdiBean.getPrototypeSpringBean().getValue());
    assertThat(namedSpringBeanValue).isEqualTo(dependentCdiBean.getNamedSpringBean().getValue());
  }

  @Test
  public void releasePrototypeSpringBeanTest()
  {
    PrototypeSpringBean.setDestroyed(false);
    BeanProvider.getDependent(PrototypeSpringBean.class).destroy();

    assertThat(PrototypeSpringBean.isDestroyed()).isTrue();
  }
}
