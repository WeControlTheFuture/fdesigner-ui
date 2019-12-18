/*******************************************************************************
 * Copyright (c) 2004, 2015 IBM Corporation and others.
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
 *******************************************************************************/
package org.fdesigner.workbench.internal.registry;

import java.util.HashMap;
import java.util.Map;

import org.fdesigner.runtime.core.Platform;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.runtime.registry.runtime.IExtension;
import org.fdesigner.runtime.registry.runtime.IExtensionPoint;
import org.fdesigner.runtime.registry.runtime.dynamichelpers.ExtensionTracker;
import org.fdesigner.runtime.registry.runtime.dynamichelpers.IExtensionChangeHandler;
import org.fdesigner.runtime.registry.runtime.dynamichelpers.IExtensionTracker;
import org.fdesigner.workbench.PlatformUI;

/**
 * Contains extensions defined on the <code>keywords</code> extension point.
 *
 * @since 3.1
 */
public final class KeywordRegistry implements IExtensionChangeHandler {

	private static final String ATT_ID = "id"; //$NON-NLS-1$

	private static final String ATT_LABEL = "label"; //$NON-NLS-1$

	private static KeywordRegistry instance;

	private static final String TAG_KEYWORD = "keyword"; //$NON-NLS-1$

	/**
	 * Return the singleton instance of the <code>KeywordRegistry</code>.
	 *
	 * @return the singleton registry
	 */
	public static KeywordRegistry getInstance() {
		if (instance == null) {
			instance = new KeywordRegistry();
		}

		return instance;
	}

	/**
	 * Map of id-&gt;labels.
	 */
	private Map internalKeywordMap = new HashMap();

	/**
	 * Private constructor.
	 */
	private KeywordRegistry() {
		IExtensionTracker tracker = PlatformUI.getWorkbench().getExtensionTracker();
		tracker.registerHandler(this, ExtensionTracker.createExtensionPointFilter(getExtensionPointFilter()));
		for (IExtension extension : getExtensionPointFilter().getExtensions()) {
			addExtension(PlatformUI.getWorkbench().getExtensionTracker(), extension);
		}
	}

	@Override
	public void addExtension(IExtensionTracker tracker, IExtension extension) {
		for (IConfigurationElement element : extension.getConfigurationElements()) {
			if (element.getName().equals(TAG_KEYWORD)) {
				String name = element.getAttribute(ATT_LABEL);
				String id = element.getAttribute(ATT_ID);
				internalKeywordMap.put(id, name);
				PlatformUI.getWorkbench().getExtensionTracker().registerObject(extension, id,
						IExtensionTracker.REF_WEAK);
			}
		}
	}

	private IExtensionPoint getExtensionPointFilter() {
		return Platform.getExtensionRegistry().getExtensionPoint(PlatformUI.PLUGIN_ID,
				IWorkbenchRegistryConstants.PL_KEYWORDS);
	}

	/**
	 * Return the label associated with the given keyword.
	 *
	 * @param id the keyword id
	 * @return the label or <code>null</code>
	 */
	public String getKeywordLabel(String id) {
		return (String) internalKeywordMap.get(id);
	}

	@Override
	public void removeExtension(IExtension extension, Object[] objects) {
		for (Object object : objects) {
			if (object instanceof String) {
				internalKeywordMap.remove(object);
			}
		}
	}
}
