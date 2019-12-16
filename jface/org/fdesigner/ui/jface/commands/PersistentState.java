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

package org.fdesigner.ui.jface.commands;

import org.fdesigner.commands.State;
import org.fdesigner.ui.jface.preference.IPreferenceStore;

/**
 * <p>
 * This is a state that can be made persistent. A state is persisted to a
 * preference store.
 * </p>
 * <p>
 * Clients may extend this class.
 * </p>
 *
 * @since 3.2
 */
public abstract class PersistentState extends State {

	/**
	 * Whether this state should be persisted.
	 */
	private boolean persisted;

	/**
	 * Loads this state from the preference store, given the location at which
	 * to look. This method must be symmetric with a call to
	 * {@link #save(IPreferenceStore, String)}.
	 *
	 * @param store
	 *            The store from which to read; must not be <code>null</code>.
	 * @param preferenceKey
	 *            The key at which the state is stored; must not be
	 *            <code>null</code>.
	 */
	public abstract void load(final IPreferenceStore store,
			final String preferenceKey);

	/**
	 * Saves this state to the preference store, given the location at which to
	 * write. This method must be symmetric with a call to
	 * {@link #load(IPreferenceStore, String)}.
	 *
	 * @param store
	 *            The store to which the state should be written; must not be
	 *            <code>null</code>.
	 * @param preferenceKey
	 *            The key at which the state should be stored; must not be
	 *            <code>null</code>.
	 */
	public abstract void save(final IPreferenceStore store,
			final String preferenceKey);

	/**
	 * Sets whether this state should be persisted.
	 *
	 * @param persisted
	 *            Whether this state should be persisted.
	 */
	public void setShouldPersist(final boolean persisted) {
		this.persisted = persisted;
	}

	/**
	 * Whether this state should be persisted. Subclasses should check this
	 * method before loading or saving.
	 *
	 * @return <code>true</code> if this state should be persisted;
	 *         <code>false</code> otherwise.
	 */
	public boolean shouldPersist() {
		return persisted;
	}
}
