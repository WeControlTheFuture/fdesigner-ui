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

package org.fdesigner.ui.jface.util;

import org.fdesigner.runtime.common.runtime.ISafeRunnable;

/**
 * Runs a safe runnables.
 * <p>
 * Clients may provide their own implementation to change
 * how safe runnables are run from within JFace.
 * </p>
 *
 * @see SafeRunnable#getRunner()
 * @see SafeRunnable#setRunner(ISafeRunnableRunner)
 * @see SafeRunnable#run(ISafeRunnable)
 * @since 3.1
 */
public interface ISafeRunnableRunner {

	/**
	 * Runs the runnable.  All <code>ISafeRunnableRunners</code> must catch any exception
	 * thrown by the <code>ISafeRunnable</code> and pass the exception to
	 * <code>ISafeRunnable.handleException()</code>.
	 * @param code the code executed as a save runnable
	 *
	 * @see SafeRunnable#run(ISafeRunnable)
	 */
	public abstract void run(ISafeRunnable code);

}
