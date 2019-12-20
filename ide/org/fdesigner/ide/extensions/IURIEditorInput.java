/*******************************************************************************
 * Copyright (c) 2006, 2007  IBM Corporation and others.
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
package org.fdesigner.ide.extensions;

import java.net.URI;

import org.fdesigner.workbench.IEditorInput;

/**
 * This interface defines an editor input based on a URI.
 * <p>
 * Clients implementing this editor input interface should override
 * <code>Object.equals(Object)</code> to answer true for two inputs
 * that are the same. The <code>IWorkbenchPage.openEditor</code> APIs
 * are dependent on this to find an editor with the same input.
 * </p><p>
 * Path-oriented editors should support this as a valid input type, and
 * can allow full read-write editing of its content.
 * </p><p>
 * All editor inputs must implement the <code>IAdaptable</code> interface;
 * extensions are managed by the platform's adapter manager.
 * </p>
 *
 * @see URI
 * @since 3.3
 */
public interface IURIEditorInput extends IEditorInput {
	/**
	 * Returns the {@link URI} of the file underlying this editor input.
	 *
	 * @return {@link URI}
	 */
	public URI getURI();
}
