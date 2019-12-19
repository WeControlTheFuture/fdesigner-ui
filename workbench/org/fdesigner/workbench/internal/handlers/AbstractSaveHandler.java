/*******************************************************************************
 * Copyright (c) 2010, 2015 IBM Corporation and others.
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
 *     Andrey Loskutov <loskutov@gmx.de> - Bug 372799
 ******************************************************************************/
package org.fdesigner.workbench.internal.handlers;

import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.ExpressionInfo;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.workbench.ISaveablePart;
import org.fdesigner.workbench.ISources;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.AbstractEvaluationHandler;
import org.fdesigner.workbench.internal.InternalHandlerUtil;
import org.fdesigner.workbench.internal.SaveableHelper;

/**
 * @since 3.7
 *
 */
public abstract class AbstractSaveHandler extends AbstractEvaluationHandler {

	protected static DirtyStateTracker dirtyStateTracker;
	private Expression enabledWhen;

	public AbstractSaveHandler() {
		if (dirtyStateTracker == null) {
			dirtyStateTracker = new DirtyStateTracker();
		}
	}

	@Override
	protected Expression getEnabledWhenExpression() {
		if (enabledWhen == null) {
			enabledWhen = new Expression() {
				@Override
				public EvaluationResult evaluate(IEvaluationContext context) {
					return AbstractSaveHandler.this.evaluate(context);
				}

				@Override
				public void collectExpressionInfo(ExpressionInfo info) {
					info.addVariableNameAccess(ISources.ACTIVE_PART_NAME);
				}
			};
		}
		return enabledWhen;
	}

	protected abstract EvaluationResult evaluate(IEvaluationContext context);

	protected ISaveablePart getSaveablePart(IEvaluationContext context) {
		IWorkbenchPart activePart = InternalHandlerUtil.getActivePart(context);
		ISaveablePart part = SaveableHelper.getSaveable(activePart);
		if (part != null) {
			return part;
		}
		return InternalHandlerUtil.getActiveEditor(context);
	}

	protected ISaveablePart getSaveablePart(ExecutionEvent event) {
		IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
		ISaveablePart part = SaveableHelper.getSaveable(activePart);
		if (part != null) {
			return part;
		}
		return HandlerUtil.getActiveEditor(event);
	}

}