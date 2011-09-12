/**
 * Copyright (C) 2010.
 * Olaf Bergner.
 * Hamburg, Germany. olaf.bergner@gmx.de
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
/**
 * 
 */
package net.camelpe.jboss;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import org.apache.camel.impl.DefaultPackageScanClassResolver;
import org.apache.camel.spi.PackageScanFilter;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.VisitorAttributes;
import org.jboss.vfs.util.AbstractVirtualFileVisitor;

/**
 * <p>
 * TODO: Insert short summary for JBossPackageScanClassResolver
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class JBossPackageScanClassResolver extends
        DefaultPackageScanClassResolver {

    @Override
    protected void find(final PackageScanFilter test, final String packageName,
            final ClassLoader loader, final Set<Class<?>> classes) {
        if (this.log.isTraceEnabled()) {
            this.log.trace("Searching for [" + test + "] in package ["
                    + packageName + "] using classloader ["
                    + loader.getClass().getName() + "]");
        }

        Enumeration<URL> urls;
        try {
            urls = getResources(loader, packageName);
            if (!urls.hasMoreElements()) {
                this.log.trace("No URLs returned by classloader");
            }
        } catch (final IOException ioe) {
            this.log.warn("Could not read package [" + packageName + "]", ioe);
            return;
        }

        while (urls.hasMoreElements()) {
            URL url = null;
            try {
                url = urls.nextElement();
                if (this.log.isTraceEnabled()) {
                    this.log.trace("Searching for [" + test + "] in package ["
                            + packageName + "] using URL [" + url
                            + "] from classloader");
                }
                final VirtualFile root = VFS.getChild(url);
                root.visit(new MatchingClassVisitor(test, classes));
            } catch (final URISyntaxException e) {
                // Cannot happen
            } catch (final IOException ioe) {
                this.log.warn("Could not read entries in URL [" + url + "]",
                        ioe);
            }
        }
    }

    private class MatchingClassVisitor extends AbstractVirtualFileVisitor {
        private final PackageScanFilter filter;
        private final Set<Class<?>> classes;

        private MatchingClassVisitor(final PackageScanFilter filter,
                final Set<Class<?>> classes) {
            super(VisitorAttributes.RECURSE_LEAVES_ONLY);
            this.filter = filter;
            this.classes = classes;
        }

        @Override
        public void visit(final VirtualFile file) {
            if (file.getName().endsWith(".class")) {
                final String fqn = file.getPathName();
                String qn;
                if (fqn.indexOf("jar/") != -1) {
                    qn = fqn.substring(fqn.indexOf("jar/") + 4);
                } else {
                    qn = fqn.substring(fqn.indexOf("/") + 1);
                }

                addIfMatching(this.filter, qn, this.classes);
            }
        }
    }

}
