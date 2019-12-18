/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.fdesigner.workbench.internal.splash;

import java.util.HashMap;
import java.util.Map;

import org.fdesigner.runtime.common.runtime.SafeRunner;
import org.fdesigner.runtime.core.IProduct;
import org.fdesigner.runtime.core.Platform;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.runtime.registry.runtime.IExtension;
import org.fdesigner.runtime.registry.runtime.IExtensionPoint;
import org.fdesigner.ui.jface.util.SafeRunnable;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.registry.IWorkbenchRegistryConstants;
import org.fdesigner.workbench.splash.AbstractSplashHandler;

/**
 * Simple non-caching access to the splashHandler extension point.
 *
 * @since 3.3
 */
public final class SplashHandlerFactory {

	/**
	 * Find the splash handler for the given product or <code>null</code> if it
	 * cannot be found.
	 *
	 * @param product the product
	 * @return the splash or <code>null</code>
	 */
	public static AbstractSplashHandler findSplashHandlerFor(IProduct product) {
		if (product == null)
			return null;

		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(PlatformUI.PLUGIN_ID,
				IWorkbenchRegistryConstants.PL_SPLASH_HANDLERS);

		if (point == null)
			return null;

		IExtension[] extensions = point.getExtensions();
		Map idToSplash = new HashMap(); // String->ConfigurationElement
		String[] targetId = new String[1];
		for (IExtension extension : extensions) {
			IConfigurationElement[] children = extension.getConfigurationElements();
			for (IConfigurationElement element : children) {
				AbstractSplashHandler handler = processElement(element, idToSplash, targetId, product);
				if (handler != null)
					return handler;

			}
		}
		return null;
	}

	/**
	 * Process a given element.
	 *
	 * @param configurationElement the element to process
	 * @param idToSplash           the map of current splash elements
	 * @param targetId             the target id if known
	 * @param product              the product to search for
	 * @return a splash matching the target id from this element or
	 *         <code>null</code>
	 */
	private static AbstractSplashHandler processElement(IConfigurationElement configurationElement, Map idToSplash,
			String[] targetId, IProduct product) {
		String type = configurationElement.getName();
		if (IWorkbenchRegistryConstants.TAG_SPLASH_HANDLER.equals(type)) {
			String id = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
			if (id == null)
				return null;

			// we know the target and this element is it
			if (targetId[0] != null && id.equals(targetId[0])) {
				return create(configurationElement);
			}
			// store for later examination
			idToSplash.put(id, configurationElement);

		} else if (IWorkbenchRegistryConstants.TAG_SPLASH_HANDLER_PRODUCT_BINDING.equals(type)) {
			String productId = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_PRODUCTID);
			if (product.getId().equals(productId) && targetId[0] == null) { // we
				// found the target ID
				targetId[0] = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_SPLASH_ID);
				// check all currently located splashes
				IConfigurationElement splashElement = (IConfigurationElement) idToSplash.get(targetId[0]);
				if (splashElement != null)
					return create(splashElement);
			}
		}

		return null;
	}

	/**
	 * Create the splash implementation.
	 *
	 * @param splashElement the element to create from
	 * @return the element or <code>null</code> if it couldn't be created
	 */
	private static AbstractSplashHandler create(final IConfigurationElement splashElement) {
		final AbstractSplashHandler[] handler = new AbstractSplashHandler[1];
		SafeRunner.run(new SafeRunnable() {

			@Override
			public void run() throws Exception {
				handler[0] = (AbstractSplashHandler) WorkbenchPlugin.createExtension(splashElement,
						IWorkbenchRegistryConstants.ATT_CLASS);
			}

			@Override
			public void handleException(Throwable e) {
				WorkbenchPlugin.log("Problem creating splash implementation", e); //$NON-NLS-1$
			}
		});

		return handler[0];
	}
}
