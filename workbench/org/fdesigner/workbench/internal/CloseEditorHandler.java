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

package org.fdesigner.workbench.internal;

import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.ExpressionInfo;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.workbench.IEditorPart;
import org.fdesigner.workbench.ISources;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.handlers.HandlerUtil;

/**
 * Closes the active editor.
 * <p>
 * Replacement for CloseEditorAction
 * </p>
 *
 * @since 3.3
 *
 */
public class CloseEditorHandler extends AbstractEvaluationHandler {

	private Expression enabledWhen;

	public CloseEditorHandler() {
		registerEnablement();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IEditorPart part = HandlerUtil.getActiveEditorChecked(event);
		window.getActivePage().closeEditor(part, true);

		return null;
	}

	@Override
	protected Expression getEnabledWhenExpression() {
		if (enabledWhen == null) {
			enabledWhen = new Expression() {
				@Override
				public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
					IEditorPart part = InternalHandlerUtil.getActiveEditor(context);
					if (part != null) {
						return EvaluationResult.TRUE;

					}
					return EvaluationResult.FALSE;
				}

				@Override
				public void collectExpressionInfo(ExpressionInfo info) {
					info.addVariableNameAccess(ISources.ACTIVE_EDITOR_NAME);
				}
			};
		}
		return enabledWhen;
	}
}
