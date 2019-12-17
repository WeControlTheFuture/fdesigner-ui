/*******************************************************************************
 * Copyright (c) 2011, 2015 IBM Corporation and others.
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

package org.fdesigner.e4.ui.workbench.modeling;

import org.fdesigner.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.fdesigner.e4.ui.model.application.ui.basic.MWindow;

/**
 * This service is used to resolve references from MPlaceholders.
 *
 * The issue is that we may be storing a cloned snippet which contains references to 'shared
 * elements' but instantiating the snippet in a new window requires that the shared elements list be
 * updated.
 *
 * @noreference This interface is not intended to be referenced by clients.
 * @since 1.0
 */
public interface EPlaceholderResolver {
	/**
	 * This method is used to re-resolve a placeholder's reference to a 'shared part' within the
	 * context of a particular window. This is necessary because placeholders must be referencing an
	 * element in that window's 'sharedParts' list.
	 * <p>
	 * Implementors may presume that the if the placeholder's reference is already non-null then it
	 * has already been resolved.
	 * </p>
	 *
	 * @param ph
	 *            The placeholder to set the reference for (if necessary)
	 * @param refWin
	 *            The window the whose shared parts are to be referenced
	 */
	public void resolvePlaceholderRef(MPlaceholder ph, MWindow refWin);
}
