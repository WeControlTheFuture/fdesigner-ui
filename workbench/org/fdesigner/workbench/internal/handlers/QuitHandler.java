/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
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
 ******************************************************************************/

package org.fdesigner.workbench.internal.handlers;

import org.eclipse.swt.widgets.Display;
import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.workbench.IWorkbench;

/**
 * Exit the workbench. Normal invocation calls {@link IWorkbench#close()}, which
 * typically doesn't prompt the user before exiting.
 * <p>
 * Invocation with parameter mayPrompt="true" calls {@link Display#close()},
 * which may prompt the user (via a hook installed by
 * <code>org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor</code>).
 *
 * @since 3.4
 *
 */
public class QuitHandler extends AbstractHandler {
	private static final String COMMAND_PARAMETER_ID_MAY_PROMPT = "mayPrompt"; //$NON-NLS-1$
	private static final String TRUE = "true"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		IWorkbench workbench = (IWorkbench) context.getVariable(IWorkbench.class.getName());
		if (TRUE.equals(event.getParameter(COMMAND_PARAMETER_ID_MAY_PROMPT))) {
			workbench.getDisplay().close();
		} else {
			workbench.close();
		}
		return null;
	}
}
