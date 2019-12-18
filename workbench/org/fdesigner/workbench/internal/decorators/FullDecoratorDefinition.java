/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.decorators;

import org.eclipse.swt.graphics.Image;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.core.Platform;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.util.SafeRunnable;
import org.fdesigner.ui.jface.viewers.IBaseLabelProvider;
import org.fdesigner.ui.jface.viewers.ILabelDecorator;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.WorkbenchPlugin;

/**
 * The RunnableDecoratorDefinition is the definition for decorators that have an
 * ILabelDecorator class to instantiate.
 */

class FullDecoratorDefinition extends DecoratorDefinition {

	ILabelDecorator decorator;

	/**
	 * Create a new instance of the receiver with the supplied values.
	 */

	FullDecoratorDefinition(String identifier, IConfigurationElement element) {
		super(identifier, element);
	}

	/**
	 * Gets the decorator and creates it if it does not exist yet. Throws a
	 * CoreException if there is a problem creating the decorator. This method
	 * should not be called unless a check for enabled to be true is done first.
	 *
	 * @return Returns a ILabelDecorator
	 */
	protected ILabelDecorator internalGetDecorator() throws CoreException {
		if (labelProviderCreationFailed) {
			return null;
		}

		final CoreException[] exceptions = new CoreException[1];

		if (decorator == null) {
			Platform.run(
					new SafeRunnable(NLS.bind(WorkbenchMessages.DecoratorManager_ErrorActivatingDecorator, getName())) {
						@Override
						public void run() {
							try {
								decorator = (ILabelDecorator) WorkbenchPlugin.createExtension(definingElement,
										DecoratorDefinition.ATT_CLASS);
								decorator.addListener(WorkbenchPlugin.getDefault().getDecoratorManager());
							} catch (CoreException exception) {
								exceptions[0] = exception;
							}
						}
					});
		} else {
			return decorator;
		}

		if (decorator == null) {
			this.labelProviderCreationFailed = true;
			setEnabled(false);
		}

		if (exceptions[0] != null) {
			throw exceptions[0];
		}

		return decorator;
	}

	@Override
	protected void refreshDecorator() {
		// Only do something if disabled so as to prevent
		// gratutitous activation
		if (!this.enabled && decorator != null) {
			IBaseLabelProvider cached = decorator;
			decorator = null;
			disposeCachedDecorator(cached);
		}
	}

	/**
	 * Decorate the image provided for the element type. This method should not be
	 * called unless a check for isEnabled() has been done first. Return null if
	 * there is no image or if an error occurs.
	 */
	Image decorateImage(Image image, Object element) {
		try {
			// Internal decorator might be null so be prepared
			ILabelDecorator currentDecorator = internalGetDecorator();
			if (currentDecorator != null) {
				return currentDecorator.decorateImage(image, element);
			}

		} catch (CoreException exception) {
			handleCoreException(exception);
		}
		return null;
	}

	/**
	 * Decorate the text provided for the element type. This method should not be
	 * called unless a check for isEnabled() has been done first. Return null if
	 * there is no text or if there is an exception.
	 */
	String decorateText(String text, Object element) {
		try {
			// Internal decorator might be null so be prepared
			ILabelDecorator currentDecorator = internalGetDecorator();
			if (currentDecorator != null) {
				return currentDecorator.decorateText(text, element);
			}
		} catch (CoreException exception) {
			handleCoreException(exception);
		}
		return null;
	}

	/**
	 * Returns the decorator, or <code>null</code> if not enabled.
	 *
	 * @return the decorator, or <code>null</code> if not enabled
	 */
	public ILabelDecorator getDecorator() {
		return decorator;
	}

	@Override
	protected IBaseLabelProvider internalGetLabelProvider() throws CoreException {
		return internalGetDecorator();
	}

	@Override
	public boolean isFull() {
		return true;
	}

}
