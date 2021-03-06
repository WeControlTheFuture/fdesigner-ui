/*******************************************************************************
 * Copyright (c) 2009, 2015 IBM Corporation and others.
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
package org.fdesigner.e4.ui.workbench.internal.workbench;

import org.fdesigner.e4.ui.workbench.IExceptionHandler;

public class ExceptionHandler implements IExceptionHandler {

	@Override
	public void handleException(Throwable e) {
		e.printStackTrace();
	}

}
