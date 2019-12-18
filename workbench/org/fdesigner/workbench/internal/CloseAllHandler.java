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
import org.fdesigner.workbench.IEditorReference;
import org.fdesigner.workbench.ISources;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.handlers.HandlerUtil;

/**
 * Closes all active editors
 * <p>
 * Replacement for CloseAllAction
 * </p>
 *
 * @since 3.3
 *
 */
public class CloseAllHandler extends AbstractEvaluationHandler {
	private Expression enabledWhen;

	public CloseAllHandler() {
		registerEnablement();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IWorkbenchPage page = window.getActivePage();
		if (page != null) {
			page.closeAllEditors(true);
		}

		return null;
	}

	@Override
	protected Expression getEnabledWhenExpression() {
		if (enabledWhen == null) {
			enabledWhen = new Expression() {
				@Override
				public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
					IWorkbenchPart part = InternalHandlerUtil.getActivePart(context);
					Object perspective = InternalHandlerUtil.getVariable(context,
							ISources.ACTIVE_WORKBENCH_WINDOW_ACTIVE_PERSPECTIVE_NAME);
					if (part != null && perspective != null && part.getSite() != null) {
						IWorkbenchPage page = part.getSite().getPage();
						if (page != null) {
							IEditorReference[] refArray = page.getEditorReferences();
							if (refArray != null && refArray.length > 0) {
								return EvaluationResult.TRUE;
							}
						}
					}
					return EvaluationResult.FALSE;
				}

				@Override
				public void collectExpressionInfo(ExpressionInfo info) {
					info.addVariableNameAccess(ISources.ACTIVE_PART_NAME);
					info.addVariableNameAccess(ISources.ACTIVE_WORKBENCH_WINDOW_ACTIVE_PERSPECTIVE_NAME);
				}
			};
		}
		return enabledWhen;
	}
}
