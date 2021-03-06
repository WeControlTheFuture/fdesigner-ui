/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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

package org.fdesigner.ide.views.bookmarkexplorer;

import org.fdesigner.resources.IMarker;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.ui.jface.dialogs.IDialogSettings;
import org.fdesigner.ui.jface.viewers.Viewer;
import org.fdesigner.ui.jface.viewers.ViewerComparator;

/*
*
* Marked for deletion, see Bug 550439
*
* @noreference
* @noinstantiate This class is not intended to be instantiated by clients.
* @noextend This class is not intended to be subclassed by clients.
*/
@Deprecated
class BookmarkSorter extends ViewerComparator {

	private int[] directions;

	private int[] priorities;

	static final int ASCENDING = 1;

	static final int DESCENDING = -1;

	static final int DESCRIPTION = 0;

	static final int RESOURCE = 1;

	static final int FOLDER = 2;

	static final int LOCATION = 3;

	static final int CREATION_TIME = 4;

	static final int[] DEFAULT_PRIORITIES = { FOLDER, RESOURCE, LOCATION,
			DESCRIPTION, CREATION_TIME };

	static final int[] DEFAULT_DIRECTIONS = { ASCENDING, //description
			ASCENDING, //resource
			ASCENDING, //folder
			ASCENDING, //location
			ASCENDING, }; //creation time

	public BookmarkSorter() {
		resetState();
	}

	public void reverseTopPriority() {
		directions[priorities[0]] *= -1;
	}

	public void setTopPriority(int priority) {
		if (priority < 0 || priority >= priorities.length) {
			return;
		}

		int index = -1;
		for (int i = 0; i < priorities.length; i++) {
			if (priorities[i] == priority) {
				index = i;
			}
		}

		if (index == -1) {
			resetState();
			return;
		}

		//shift the array
		for (int i = index; i > 0; i--) {
			priorities[i] = priorities[i - 1];
		}
		priorities[0] = priority;
		directions[priority] = DEFAULT_DIRECTIONS[priority];
	}

	public void setTopPriorityDirection(int direction) {
		if (direction == ASCENDING || direction == DESCENDING) {
			directions[priorities[0]] = direction;
		}
	}

	public int getTopPriorityDirection() {
		return directions[priorities[0]];
	}

	public int getTopPriority() {
		return priorities[0];
	}

	public int[] getPriorities() {
		return priorities;
	}

	public void resetState() {
		priorities = new int[DEFAULT_PRIORITIES.length];
		System.arraycopy(DEFAULT_PRIORITIES, 0, priorities, 0,
				priorities.length);
		directions = new int[DEFAULT_DIRECTIONS.length];
		System.arraycopy(DEFAULT_DIRECTIONS, 0, directions, 0,
				directions.length);
	}

	private int compare(IMarker marker1, IMarker marker2, int depth) {
		if (depth >= priorities.length) {
			return 0;
		}

		int column = priorities[depth];
		switch (column) {
		case DESCRIPTION: {
			String desc1 = marker1.getAttribute(IMarker.MESSAGE, "");//$NON-NLS-1$
			String desc2 = marker2.getAttribute(IMarker.MESSAGE, "");//$NON-NLS-1$
			int result = getComparator().compare(desc1, desc2);
			if (result == 0) {
				return compare(marker1, marker2, depth + 1);
			}
			return result * directions[column];
		}
		case RESOURCE: {
			String res1 = marker1.getResource().getName();
			String res2 = marker2.getResource().getName();
			int result = getComparator().compare(res1, res2);
			if (result == 0) {
				return compare(marker1, marker2, depth + 1);
			}
			return result * directions[column];
		}
		case FOLDER: {
			String folder1 = BookmarkLabelProvider.getContainerName(marker1);
			String folder2 = BookmarkLabelProvider.getContainerName(marker2);
			int result = getComparator().compare(folder1, folder2);
			if (result == 0) {
				return compare(marker1, marker2, depth + 1);
			}
			return result * directions[column];
		}
		case LOCATION: {
			int line1 = marker1.getAttribute(IMarker.LINE_NUMBER, -1);
			int line2 = marker2.getAttribute(IMarker.LINE_NUMBER, -1);
			int result = line1 - line2;
			if (result == 0) {
				return compare(marker1, marker2, depth + 1);
			}
			return result * directions[column];
		}
		case CREATION_TIME: {
			long result;
			try {
				result = marker1.getCreationTime() - marker2.getCreationTime();
			} catch (CoreException e) {
				result = 0;
			}
			if (result == 0) {
				return compare(marker1, marker2, depth + 1);
			}
			return ((int) result) * directions[column];
		}
		}

		return 0;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		IMarker marker1 = (IMarker) e1;
		IMarker marker2 = (IMarker) e2;

		return compare(marker1, marker2, 0);
	}

	public void saveState(IDialogSettings settings) {
		if (settings == null) {
			return;
		}

		for (int i = 0; i < priorities.length; i++) {
			settings.put("priority" + i, priorities[i]);//$NON-NLS-1$
			settings.put("direction" + i, directions[i]);//$NON-NLS-1$
		}
	}

	public void restoreState(IDialogSettings settings) {
		if (settings == null) {
			return;
		}

		try {
			for (int i = 0; i < priorities.length; i++) {
				priorities[i] = settings.getInt("priority" + i);//$NON-NLS-1$
				directions[i] = settings.getInt("direction" + i);//$NON-NLS-1$
			}
		} catch (NumberFormatException e) {
			resetState();
		}
	}
}
