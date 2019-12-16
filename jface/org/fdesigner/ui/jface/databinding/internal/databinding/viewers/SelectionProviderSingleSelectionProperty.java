/*******************************************************************************
 * Copyright (c) 2008, 2015 Matthew Hall and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Matthew Hall - initial API and implementation (bug 194734)
 *     Matthew Hall - bugs 195222, 263413, 265561, 271080
 *     Ovidio Mallo - bug 270494
 ******************************************************************************/

package org.fdesigner.ui.jface.databinding.internal.databinding.viewers;

import org.fdesigner.databinding.observable.value.ValueDiff;
import org.fdesigner.databinding.property.INativePropertyListener;
import org.fdesigner.databinding.property.ISimplePropertyListener;
import org.fdesigner.ui.jface.databinding.viewers.ViewerValueProperty;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.ISelectionProvider;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.ui.jface.viewers.Viewer;

/**
 * @param <S> type of the source object
 * @param <T> type of the value of the property
 *
 * @since 3.3
 *
 */
public class SelectionProviderSingleSelectionProperty<S extends ISelectionProvider, T>
		extends ViewerValueProperty<S, T> {

	private final boolean isPostSelection;
	private final Object valueType;

	/**
	 * Constructor.
	 *
	 * @param isPostSelection Whether the post selection or the normal selection is
	 *                        to be observed.
	 * @param valueType       Value type of the property.
	 */
	public SelectionProviderSingleSelectionProperty(boolean isPostSelection, Object valueType) {
		this.isPostSelection = isPostSelection;
		this.valueType = valueType;
	}

	/**
	 * Creates a property with a null value type.
	 *
	 * @param isPostSelection Whether the post selection or the normal selection is
	 *                        to be observed.
	 */
	public SelectionProviderSingleSelectionProperty(boolean isPostSelection) {
		this(isPostSelection, null);
	}

	@Override
	public Object getValueType() {
		return valueType;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected T doGetValue(ISelectionProvider source) {
		ISelection selection = source.getSelection();
		if (selection instanceof IStructuredSelection) {
			return (T) ((IStructuredSelection) selection).getFirstElement();
		}
		return null;
	}

	@Override
	protected void doSetValue(ISelectionProvider source, Object value) {
		IStructuredSelection selection = value == null ? StructuredSelection.EMPTY
				: new StructuredSelection(value);

		if (source instanceof Viewer) {
			((Viewer) source).setSelection(selection, true);
		} else {
			source.setSelection(selection);
		}
	}

	@Override
	public INativePropertyListener<S> adaptListener(ISimplePropertyListener<S, ValueDiff<? extends T>> listener) {
		return new SelectionChangedListener<>(this, listener, isPostSelection);
	}

	@Override
	public String toString() {
		return isPostSelection ? "IPostSelectionProvider.postSelection" //$NON-NLS-1$
				: "ISelectionProvider.selection"; //$NON-NLS-1$
	}
}
