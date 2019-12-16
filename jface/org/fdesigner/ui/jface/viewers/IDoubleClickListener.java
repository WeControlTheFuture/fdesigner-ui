/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 503316
 *******************************************************************************/
package org.eclipse.jface.viewers;

/**
 * A listener which is notified of double-click events on viewers.
 */
@FunctionalInterface
public interface IDoubleClickListener {
	/**
	 * Notifies of a double click.
	 *
	 * @param event event object describing the double-click
	 */
	public void doubleClick(DoubleClickEvent event);
}
