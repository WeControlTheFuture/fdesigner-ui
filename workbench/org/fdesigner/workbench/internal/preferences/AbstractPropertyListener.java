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
package org.fdesigner.workbench.internal.preferences;

/**
 * @since 3.1
 */
public abstract class AbstractPropertyListener implements IPropertyMapListener {

	@Override
	public void propertyChanged(String[] propertyIds) {
		update();
	}

	@Override
	public void listenerAttached() {
		update();
	}

	protected abstract void update();
}
