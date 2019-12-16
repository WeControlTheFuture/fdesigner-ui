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
 *     Chris Gross (schtoo@schtoo.com) - initial API and implementation
 *       (bug 49497 [RCP] JFace dependency on org.eclipse.core.runtime enlarges standalone JFace applications)
 *******************************************************************************/

package org.fdesigner.databinding.observable.util;

import org.fdesigner.runtime.common.runtime.IStatus;

/**
 * A mechanism to log errors throughout JFace Data Binding.
 * <p>
 * Clients may provide their own implementation to change how errors are logged
 * from within JFace Data Binding.
 * </p>
 *
 * @see Policy#getLog()
 * @see Policy#setLog(ILogger)
 * @since 1.1
 */
@FunctionalInterface
public interface ILogger {

	/**
	 * Logs the given status.
	 *
	 * @param status
	 *            the status to log
	 */
	public void log(IStatus status);

}
