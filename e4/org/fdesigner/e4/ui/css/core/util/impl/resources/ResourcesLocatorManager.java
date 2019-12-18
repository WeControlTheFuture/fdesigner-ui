/*******************************************************************************
 * Copyright (c) 2008, 2015 Angelo Zerr and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *     IBM Corporation - ongoing development
 *******************************************************************************/
package org.fdesigner.e4.ui.css.core.util.impl.resources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.fdesigner.e4.ui.css.core.util.resources.IResourceLocator;
import org.fdesigner.e4.ui.css.core.util.resources.IResourcesLocatorManager;
import org.fdesigner.e4.ui.css.core.utils.StringUtils;

/**
 * Resources locator manager implementation.
 */
public class ResourcesLocatorManager implements IResourcesLocatorManager {

	/**
	 * ResourcesLocatorManager Singleton
	 */
	public static final IResourcesLocatorManager INSTANCE = new ResourcesLocatorManager();

	/**
	 * List of IResourceLocator instance which was registered.
	 */
	private List<IResourceLocator> uriResolvers;

	public ResourcesLocatorManager() {
		registerResourceLocator(new HttpResourcesLocatorImpl());
	}

	@Override
	public void registerResourceLocator(IResourceLocator resourceLocator) {
		if (uriResolvers == null) {
			uriResolvers = new ArrayList<>();
		}
		if (resourceLocator instanceof OSGiResourceLocator) {
			uriResolvers.add(0, resourceLocator);
		} else {
			uriResolvers.add(resourceLocator);
		}
	}

	@Override
	public void unregisterResourceLocator(IResourceLocator resourceLocator) {
		if (uriResolvers == null) {
			return;
		}
		uriResolvers.remove(resourceLocator);
	}

	@Override
	public String resolve(String uri) {
		if (StringUtils.isEmpty(uri)) {
			return null;
		}
		if (uriResolvers == null) {
			return null;
		}
		// Loop for IResourceLocator registered and return the uri resolved
		// as soon as an IResourceLocator return an uri resolved which is not
		// null.
		for (IResourceLocator resolver : uriResolvers) {
			String s = resolver.resolve(uri);
			if (s != null) {
				return s;
			}
		}
		return null;
	}

	@Override
	public InputStream getInputStream(String uri) throws Exception {
		if (StringUtils.isEmpty(uri)) {
			return null;
		}
		if (uriResolvers == null) {
			return null;
		}

		// Loop for IResourceLocator registered and return the InputStream from
		// the uri resolved
		// as soon as an IResourceLocator return an uri resolved which is not
		// null.
		for (IResourceLocator resolver : uriResolvers) {
			String s = resolver.resolve(uri);
			if (s != null) {
				InputStream inputStream = resolver.getInputStream(uri);
				if (inputStream != null) {
					return inputStream;
				}
			}
		}
		return null;
	}


}
