/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
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
package org.fdesigner.e4.core.services.translation;

import org.fdesigner.e4.core.contexts.ContextInjectionFactory;
import org.fdesigner.e4.core.contexts.IEclipseContext;
import org.fdesigner.e4.core.services.internal.services.BundleTranslationProvider;

/**
 * Factory for translation providers.
 *
 * @since 1.2
 */
final public class TranslationProviderFactory {

	private TranslationProviderFactory() {
		// prevents instantiation
	}

	/**
	 * Returns default bundle-based translation provider.
	 *
	 * @param context
	 *            the context for the translation provider
	 * @return bundle-based translation provider
	 */
	static public TranslationService bundleTranslationService(IEclipseContext context) {
		return ContextInjectionFactory.make(BundleTranslationProvider.class, context);
	}

}
