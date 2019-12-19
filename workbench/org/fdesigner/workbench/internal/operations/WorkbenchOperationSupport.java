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
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.fdesigner.workbench.internal.operations;

import org.fdesigner.commands.operations.DefaultOperationHistory;
import org.fdesigner.commands.operations.IOperationApprover;
import org.fdesigner.commands.operations.IOperationHistory;
import org.fdesigner.commands.operations.IUndoContext;
import org.fdesigner.commands.operations.ObjectUndoContext;
import org.fdesigner.commands.operations.OperationHistoryFactory;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.misc.Policy;
import org.fdesigner.workbench.operations.IWorkbenchOperationSupport;

/**
 * <p>
 * Provides undoable operation support for the workbench. This includes
 * providing access to the default operation history and installing a
 * workbench-specific operation approver that enforces a linear undo strategy.
 * </p>
 *
 * @since 3.1
 */
public class WorkbenchOperationSupport implements IWorkbenchOperationSupport {

	private ObjectUndoContext undoContext;
	private IOperationApprover approver;

	// initialize debug options
	static {
		DefaultOperationHistory.DEBUG_OPERATION_HISTORY_UNEXPECTED = Policy.DEBUG_OPERATIONS;
		DefaultOperationHistory.DEBUG_OPERATION_HISTORY_OPENOPERATION = Policy.DEBUG_OPERATIONS;
		DefaultOperationHistory.DEBUG_OPERATION_HISTORY_APPROVAL = Policy.DEBUG_OPERATIONS;
		DefaultOperationHistory.DEBUG_OPERATION_HISTORY_NOTIFICATION = Policy.DEBUG_OPERATIONS
				&& Policy.DEBUG_OPERATIONS_VERBOSE;
		DefaultOperationHistory.DEBUG_OPERATION_HISTORY_DISPOSE = Policy.DEBUG_OPERATIONS
				&& Policy.DEBUG_OPERATIONS_VERBOSE;
	}

	/**
	 * Disposes of anything created by the operation support.
	 */
	public void dispose() {
		/*
		 * uninstall the operation approver that we added to the operation history
		 */
		getOperationHistory().removeOperationApprover(approver);
		/*
		 * dispose of all operations using our context
		 */
		getOperationHistory().dispose(getUndoContext(), true, true, true);
	}

	/**
	 * Returns the undo context for workbench operations. The workbench configures
	 * an undo context with the appropriate policies for the workbench undo model.
	 *
	 * @return the workbench operation context.
	 * @since 3.1
	 */
	@Override
	public IUndoContext getUndoContext() {
		if (undoContext == null) {
			undoContext = new ObjectUndoContext(PlatformUI.getWorkbench(), "Workbench Context"); //$NON-NLS-1$
		}
		return undoContext;
	}

	/**
	 * Returns the workbench operation history.
	 *
	 * @return the operation history for workbench operations.
	 * @since 3.1
	 */
	@Override
	public IOperationHistory getOperationHistory() {
		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		/*
		 * Set up the history if we have not done so before.
		 */
		if (approver == null) {
			/*
			 * install an operation approver that prevents linear undo violations in any
			 * context
			 */
			approver = new AdvancedValidationUserApprover(getUndoContext());
			history.addOperationApprover(approver);
			/*
			 * set a limit for the workbench undo context
			 */
			history.setLimit(getUndoContext(), 25);
		}
		return history;
	}

}
