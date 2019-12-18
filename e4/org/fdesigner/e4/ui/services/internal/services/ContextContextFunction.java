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
package org.fdesigner.e4.ui.services.internal.services;

import org.fdesigner.e4.core.contexts.ContextFunction;
import org.fdesigner.e4.core.contexts.IEclipseContext;


public class ContextContextFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		return new ContextContextService(context);
	}

}
