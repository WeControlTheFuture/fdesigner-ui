/*******************************************************************************
 * Copyright (c) 2000, 2015, 2019 IBM Corporation and others.
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
 *     Stefan Winkler <stefan@winklerweb.net> - bug 417255 - Race Condition in DecorationScheduler
 *******************************************************************************/
package org.fdesigner.workbench.internal.decorators;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.viewers.IDecorationContext;
import org.fdesigner.workbench.internal.WorkbenchMessages;

/**
 * A DecorationReference is a class that holds onto the starting text and image
 * of a decoration. Its main purpose is to act as a data object for decorations
 * scheduled to be calculated asynchonously by the {@link DecorationScheduler}.
 */
class DecorationReference {
	static final IDecorationContext[] EMPTY_DECORATION_CONTEXTS = new IDecorationContext[0];

	final Object element;

	final Object adaptedElement;

	String undecoratedText;

	boolean forceUpdate = false;

	final Set<IDecorationContext> contexts = new HashSet<>();

	DecorationReference(Object object, Object adaptedObject, IDecorationContext context) {
		Assert.isNotNull(object);
		element = object;
		this.adaptedElement = adaptedObject;
		addContext(context);
	}

	/**
	 * Returns the adaptedElement.
	 *
	 * @return Object
	 */
	Object getAdaptedElement() {
		return adaptedElement;
	}

	/**
	 * Returns the element.
	 *
	 * @return Object
	 */
	Object getElement() {
		return element;
	}

	/**
	 * Return true if an update should occur whether or not there is a result.
	 *
	 * @return boolean
	 */
	boolean shouldForceUpdate() {
		return forceUpdate;
	}

	/**
	 * Sets the forceUpdate flag. If true an update occurs whether or not a
	 * decoration has resulted.
	 *
	 * @param forceUpdate The forceUpdate to set
	 */
	void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	/**
	 * Set the text that will be used to label the decoration calculation.
	 *
	 * @param text
	 */
	void setUndecoratedText(String text) {
		undecoratedText = text;
	}

	/**
	 * Return the string for the subtask for this element.
	 *
	 * @return String
	 */
	String getSubTask() {
		if (undecoratedText == null) {
			return WorkbenchMessages.DecorationReference_EmptyReference;
		}
		return NLS.bind(WorkbenchMessages.DecorationScheduler_DecoratingSubtask, undecoratedText);
	}

	/**
	 * Returns the decoration context associated with the element being decorated
	 *
	 * @return the decoration context
	 */
	Collection<IDecorationContext> getContexts() {
		return contexts;
	}

	void addContext(IDecorationContext context) {
		contexts.add(context);
	}
}
