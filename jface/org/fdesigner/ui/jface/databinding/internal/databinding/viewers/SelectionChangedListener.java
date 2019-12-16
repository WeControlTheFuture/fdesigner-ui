/*******************************************************************************
 * Copyright (c) 2009, 2014 Matthew Hall and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Matthew Hall - initial API and implementation (bug 265561)
 *     Ovidio Mallo - bug 270494
 ******************************************************************************/

package org.fdesigner.ui.jface.databinding.internal.databinding.viewers;

import org.fdesigner.databinding.observable.IDiff;
import org.fdesigner.databinding.property.IProperty;
import org.fdesigner.databinding.property.ISimplePropertyListener;
import org.fdesigner.databinding.property.NativePropertyListener;
import org.fdesigner.ui.jface.viewers.IPostSelectionProvider;
import org.fdesigner.ui.jface.viewers.ISelectionChangedListener;
import org.fdesigner.ui.jface.viewers.ISelectionProvider;
import org.fdesigner.ui.jface.viewers.SelectionChangedEvent;

class SelectionChangedListener<S extends ISelectionProvider, D extends IDiff> extends NativePropertyListener<S, D>
		implements
		ISelectionChangedListener {

	private final boolean isPostSelection;

	SelectionChangedListener(IProperty property,
			ISimplePropertyListener<S, D> listener, boolean isPostSelection) {
		super(property, listener);
		this.isPostSelection = isPostSelection;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		fireChange((S) event.getSource(), null);
	}

	@Override
	public void doAddTo(ISelectionProvider source) {
		if (isPostSelection) {
			((IPostSelectionProvider) source).addPostSelectionChangedListener(this);
		} else {
			source.addSelectionChangedListener(this);
		}
	}

	@Override
	public void doRemoveFrom(ISelectionProvider source) {
		if (isPostSelection) {
			((IPostSelectionProvider) source)
					.removePostSelectionChangedListener(this);
		} else {
			source.removeSelectionChangedListener(this);
		}
	}
}