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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.e4.ui.model.application.ui.basic.MPart;
import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.ExpressionInfo;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.workbench.ISources;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.IWorkbenchPartSite;
import org.fdesigner.workbench.handlers.HandlerUtil;

/**
 * Show the menu on top of the icon in the view or editor label.
 * <p>
 * Replacement for ShowPartPaneMenuAction
 * </p>
 *
 * @since 3.3
 */
public class ShowPartPaneMenuHandler extends AbstractEvaluationHandler {

	private Expression enabledWhen;

	public ShowPartPaneMenuHandler() {
		registerEnablement();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part != null) {
			IWorkbenchPartSite site = part.getSite();
			if (site instanceof PartSite) {
				final MPart model = ((PartSite) site).getModel();
				Composite partContainer = (Composite) model.getWidget();
				if (partContainer != null) {
					Composite parent = partContainer.getParent();
					while (parent != null) {
						if (parent instanceof CTabFolder) {
							CTabFolder ctf = (CTabFolder) parent;
							final CTabItem item = ctf.getSelection();
							if (item != null) {
								final Display disp = item.getDisplay();
								final Rectangle bounds = item.getBounds();
								final Rectangle info = disp.map(ctf, null, bounds);
								Event sevent = new Event();
								sevent.type = SWT.MenuDetect;
								sevent.widget = ctf;
								sevent.x = info.x;
								sevent.y = info.y + info.height - 1;
								sevent.doit = true;
								ctf.notifyListeners(SWT.MenuDetect, sevent);
							}
							return null;
						}
						parent = parent.getParent();
					}
				}
			}
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

					if (part != null) {
						return EvaluationResult.TRUE;
					}
					return EvaluationResult.FALSE;
				}

				@Override
				public void collectExpressionInfo(ExpressionInfo info) {
					info.addVariableNameAccess(ISources.ACTIVE_PART_NAME);
				}
			};
		}
		return enabledWhen;
	}
}
