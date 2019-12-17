package org.fdesigner.workbench.internal.databinding;

import org.fdesigner.databinding.observable.value.ValueDiff;
import org.fdesigner.databinding.property.INativePropertyListener;
import org.fdesigner.databinding.property.ISimplePropertyListener;
import org.fdesigner.databinding.property.value.SimpleValueProperty;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.workbench.ISelectionService;

public class SingleSelectionProperty<S extends ISelectionService, T> extends SimpleValueProperty<S, T> {
	private final String partId;
	private final boolean post;
	private final Object elementType;

	public SingleSelectionProperty(String partId, boolean post, Object elementType) {
		this.partId = partId;
		this.post = post;
		this.elementType = elementType;
	}

	@Override
	public INativePropertyListener<S> adaptListener(ISimplePropertyListener<S, ValueDiff<? extends T>> listener) {
		return new SelectionServiceListener<>(this, listener, partId, post);
	}

	@Override
	protected T doGetValue(S source) {
		ISelection selection;
		if (partId != null) {
			selection = ((ISelectionService) source).getSelection(partId);
		} else {
			selection = ((ISelectionService) source).getSelection();
		}
		if (selection instanceof IStructuredSelection) {
			@SuppressWarnings("unchecked")
			T elem = (T) ((IStructuredSelection) selection).getFirstElement();
			return elem;
		}
		return null;
	}

	@Override
	protected void doSetValue(S source, T value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getValueType() {
		return elementType;
	}
}
