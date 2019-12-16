/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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
package org.fdesigner.ui.jface.action;

/**
 * A menu listener that gets informed when a menu is about to hide.
 *
 * @see MenuManager#addMenuListener
 * @since 3.2
 */
public interface IMenuListener2 extends IMenuListener {
	/**
	 * Notifies this listener that the menu is about to be hidden by
	 * the given menu manager.
	 *
	 * @param manager the menu manager
	 */
	public void menuAboutToHide(IMenuManager manager);
}
