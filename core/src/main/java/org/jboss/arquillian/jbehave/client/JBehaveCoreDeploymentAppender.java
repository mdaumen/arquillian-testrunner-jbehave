/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.jbehave.client;

import java.util.Collection;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.jbehave.container.JBehaveContainerExtension;
import org.jboss.arquillian.jbehave.core.ArquillianInstanceStepsFactory;
import org.jboss.arquillian.jbehave.core.StepEnricherProvider;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * Deployment appender that adds the JBehave-Core distribution to a deployment.
 * Also adds the classes and files necessary for the remote loadable extension.
 * 
 * @author Vineet Reynolds
 *
 */
public class JBehaveCoreDeploymentAppender implements AuxiliaryArchiveAppender
{

   @Override
   public Archive<?> createAuxiliaryArchive()
   {
      Collection<JavaArchive> archives = DependencyResolvers.use(MavenDependencyResolver.class)
            .goOffline()
            .loadMetadataFromPom("pom.xml")
            .artifact("org.jbehave:jbehave-core:jar:3.5.4")
            .resolveAs(JavaArchive.class);
      JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "arquillian-jbehave.jar");
      for (Archive<JavaArchive> element : archives)
      {
         archive.merge(element);
      }
      
      archive.addClasses(JBehaveContainerExtension.class, ArquillianInstanceStepsFactory.class, StepEnricherProvider.class);
      archive.addAsServiceProvider(RemoteLoadableExtension.class, JBehaveContainerExtension.class);
      return archive;
   }

}
