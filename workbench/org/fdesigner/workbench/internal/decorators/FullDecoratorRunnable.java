/*******************************************************************************
 * Copyright (c) 2004, 2014 IBM Corporation and others.
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

import org.fdesigner.runtime.common.runtime.ISafeRunnable;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.misc.StatusUtil;

/**
 * The FullDecoratorRunnable is the ISafeRunnable that runs the full decorators
 */
abstract class FullDecoratorRunnable implements ISafeRunnable {
	protected Object element;

	protected FullDecoratorDefinition decorator;

	/**
	 * Set the values for the element and the decorator.
	 *
	 * @param object
	 * @param definition
	 */
	protected void setValues(Object object, FullDecoratorDefinition definition) {
		element = object;
		decorator = definition;

	}

	/*
	 * @see ISafeRunnable.handleException(Throwable).
	 */
	@Override
	public void handleException(Throwable exception) {
		IStatus status = StatusUtil.newStatus(IStatus.ERROR, exception.getMessage(), exception);
		String message = NLS.bind(WorkbenchMessages.DecoratorWillBeDisabled, decorator.getName());
		WorkbenchPlugin.log(message, status);
		decorator.crashDisable();
	}

}
