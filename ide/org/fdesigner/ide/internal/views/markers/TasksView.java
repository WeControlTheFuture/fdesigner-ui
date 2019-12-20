/*******************************************************************************
 * Copyright (c) 2008, 2015 IBM Corporation and others.
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
package org.fdesigner.ide.internal.views.markers;

import org.fdesigner.commands.operations.IUndoContext;
import org.fdesigner.ide.undo.WorkspaceUndoUtil;
import org.fdesigner.ide.views.markers.MarkerSupportView;
import org.fdesigner.ide.views.markers.internal.MarkerMessages;
import org.fdesigner.ide.views.markers.internal.MarkerSupportRegistry;
import org.fdesigner.resources.IMarker;
import org.fdesigner.runtime.common.runtime.Assert;


/**
 * TasksView is the ide view for showing tasks.
 * @since 3.4
 *
 */
public class TasksView extends MarkerSupportView {

	/**
	 * Create a new instance of the receiver.
	 */
	public TasksView() {
		super(MarkerSupportRegistry.TASKS_GENERATOR);

	}

	@Override
	protected IUndoContext getUndoContext() {
		return WorkspaceUndoUtil.getTasksUndoContext();
	}

	@Override
	protected String getDeleteOperationName(IMarker[] markers) {
		Assert.isLegal(markers.length > 0);
		return markers.length == 1 ? MarkerMessages.deleteTaskMarker_operationName : MarkerMessages.deleteTaskMarkers_operationName;
	}

}
