/*******************************************************************************
 * Copyright (c) 2003, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.fdesigner.navigator;

/**
 *
 * Label providers (as specified by the <i>labelProvider</i> attribute of the
 * <b>org.eclipse.ui.navigator.navigatorContent</b> extension point) may
 * choose to also implement this interface in order to provide text for
 * the status bar at the bottom of the Eclipse window.
 *
 * @since 3.2
 */
public interface IDescriptionProvider {

	/**
	 * <p>
	 * Provide a description for the status bar view, if available. A default
	 * string of the form "(x) items selected" will be used if this method
	 * choose to return null.
	 * </p>
	 *
	 * <p>
	 * The empty string ("") will be respected as a valid value if returned.
	 * Return <b>null </b> if the extension defers to the default method of
	 * supplying status bar descriptions.
	 * </p>
	 *
	 * @param anElement
	 *            The element selected in the Navigator
	 * @return A description for the status bar view, or null if not available.
	 */
	String getDescription(Object anElement);

}
