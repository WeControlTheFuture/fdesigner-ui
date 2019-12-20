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
 * The BookmarksView is the ide view for bookmarks.
 * @since 3.4
 *
 */
public class BookmarksView extends MarkerSupportView {

	/**
	 * Create a new instance of the receiver.
	 */
	public BookmarksView() {
		super(MarkerSupportRegistry.BOOKMARKS_GENERATOR);

	}

	@Override
	protected IUndoContext getUndoContext() {
		return WorkspaceUndoUtil.getBookmarksUndoContext();
	}

	@Override
	protected String getDeleteOperationName(IMarker[] markers) {
		Assert.isLegal(markers.length > 0);
		return markers.length == 1 ? MarkerMessages.deleteBookmarkMarker_operationName : MarkerMessages.deleteBookmarkMarkers_operationName;
	}

}
