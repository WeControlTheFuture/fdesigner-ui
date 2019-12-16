/*******************************************************************************
 * Copyright (c) 2005, 2015 IBM Corporation and others.
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

import org.fdesigner.commands.common.EventManager;
import org.fdesigner.ui.jface.util.IPropertyChangeListener;
import org.fdesigner.ui.jface.util.PropertyChangeEvent;

/**
 * <p>
 * Some common functionality to share between implementations of
 * <code>IAction</code>. This functionality deals with the property change
 * event mechanism.
 * </p>
 * <p>
 * Clients may neither instantiate nor extend this class.
 * </p>
 *
 * @since 3.2
 */
public abstract class AbstractAction extends EventManager implements IAction {

	@Override
	public void addPropertyChangeListener(final IPropertyChangeListener listener) {
		addListenerObject(listener);
	}

	/**
	 * Notifies any property change listeners that a property has changed. Only
	 * listeners registered at the time this method is called are notified.
	 *
	 * @param event
	 *            the property change event
	 *
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	protected final void firePropertyChange(final PropertyChangeEvent event) {
		final Object[] list = getListeners();
		for (Object element : list) {
			((IPropertyChangeListener) element).propertyChange(event);
		}
	}

	/**
	 * Notifies any property change listeners that a property has changed. Only
	 * listeners registered at the time this method is called are notified. This
	 * method avoids creating an event object if there are no listeners
	 * registered, but calls
	 * <code>firePropertyChange(PropertyChangeEvent)</code> if there are.
	 *
	 * @param propertyName
	 *            the name of the property that has changed
	 * @param oldValue
	 *            the old value of the property, or <code>null</code> if none
	 * @param newValue
	 *            the new value of the property, or <code>null</code> if none
	 *
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	protected final void firePropertyChange(final String propertyName,
			final Object oldValue, final Object newValue) {
		if (isListenerAttached()) {
			firePropertyChange(new PropertyChangeEvent(this, propertyName,
					oldValue, newValue));
		}
	}

	@Override
	public void removePropertyChangeListener(
			final IPropertyChangeListener listener) {
		removeListenerObject(listener);
	}

}
