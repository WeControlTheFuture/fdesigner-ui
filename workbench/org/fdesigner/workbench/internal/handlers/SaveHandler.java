/*******************************************************************************
 * Copyright (c) 2013, 2015 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.handlers;

import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.workbench.IEditorPart;
import org.fdesigner.workbench.ISaveablePart;
import org.fdesigner.workbench.ISaveablesSource;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.InternalHandlerUtil;
import org.fdesigner.workbench.internal.SaveableHelper;
import org.fdesigner.workbench.internal.WorkbenchPage;

/**
 * <p>
 * Replacement for SaveAction
 * </p>
 *
 * @since 3.7
 *
 */
public class SaveHandler extends AbstractSaveHandler {

	public SaveHandler() {
		registerEnablement();
	}

	@Override
	public Object execute(ExecutionEvent event) {

		ISaveablePart saveablePart = getSaveablePart(event);

		// no saveable
		if (saveablePart == null)
			return null;

		// if editor
		if (saveablePart instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) saveablePart;
			IWorkbenchPage page = editorPart.getSite().getPage();
			page.saveEditor(editorPart, false);
			return null;
		}

		// if view
		IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
		WorkbenchPage page = (WorkbenchPage) activePart.getSite().getPage();
		page.saveSaveable(saveablePart, activePart, false, false);

		return null;

	}

	@Override
	protected EvaluationResult evaluate(IEvaluationContext context) {

		IWorkbenchWindow window = InternalHandlerUtil.getActiveWorkbenchWindow(context);
		// no window? not active
		if (window == null)
			return EvaluationResult.FALSE;
		WorkbenchPage page = (WorkbenchPage) window.getActivePage();

		// no page? not active
		if (page == null)
			return EvaluationResult.FALSE;

		// get saveable part
		ISaveablePart saveablePart = getSaveablePart(context);
		if (saveablePart == null)
			return EvaluationResult.FALSE;

		if (saveablePart instanceof ISaveablesSource) {
			ISaveablesSource modelSource = (ISaveablesSource) saveablePart;
			if (SaveableHelper.needsSave(modelSource))
				return EvaluationResult.TRUE;
			return EvaluationResult.FALSE;
		}

		if (saveablePart.isDirty())
			return EvaluationResult.TRUE;

		return EvaluationResult.FALSE;
	}
}
