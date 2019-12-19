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
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.workbench.ISaveablesLifecycleListener;
import org.fdesigner.workbench.ISaveablesSource;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.Saveable;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.InternalHandlerUtil;
import org.fdesigner.workbench.internal.SaveablesList;
import org.fdesigner.workbench.internal.WorkbenchPage;

/**
 * Saves all active editors
 * <p>
 * Replacement for SaveAllAction
 * </p>
 *
 * @since 3.7
 *
 */
public class SaveAllHandler extends AbstractSaveHandler {

	public SaveAllHandler() {
		registerEnablement();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IWorkbenchPage page = window.getActivePage();
		if (page != null) {
			((WorkbenchPage) page).saveAllEditors(false, false, true);
		}

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

		// if at least one dirty part, then we are active
		if (page.getDirtyParts().length > 0)
			return EvaluationResult.TRUE;

		// Since Save All also saves saveables from non-part sources,
		// look if any such saveables exist and are dirty.
		SaveablesList saveablesList = (SaveablesList) window.getWorkbench()
				.getService(ISaveablesLifecycleListener.class);
		if (saveablesList == null) {
			return EvaluationResult.FALSE;
		}

		for (ISaveablesSource nonPartSource : saveablesList.getNonPartSources()) {
			Saveable[] saveables = nonPartSource.getSaveables();
			for (Saveable saveable : saveables) {
				if (saveable.isDirty()) {
					return EvaluationResult.TRUE;
				}
			}
		}

		// if nothing, then we are not active
		return EvaluationResult.FALSE;
	}

}
