/*******************************************************************************
 * Copyright (c) 2003, 2015 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.activities.ws;

import org.eclipse.swt.graphics.Image;
import org.fdesigner.ui.jface.viewers.LabelProvider;
import org.fdesigner.workbench.activities.IActivity;
import org.fdesigner.workbench.activities.IActivityManager;
import org.fdesigner.workbench.activities.NotDefinedException;

/**
 * Provides labels for <code>IActivity</code> objects. They may be passed
 * directly or as <code>String</code> identifiers that are matched against the
 * activity manager.
 *
 * @since 3.0
 */
public class ActivityLabelProvider extends LabelProvider {

	private IActivityManager activityManager;

	/**
	 * Create a new instance of the receiver.
	 *
	 * @param activityManager
	 * @since 3.0
	 */
	public ActivityLabelProvider(IActivityManager activityManager) {
		this.activityManager = activityManager;
	}

	/**
	 * @param activity
	 * @return
	 */
	private String getActivityText(IActivity activity) {
		try {
			return activity.getName();
		} catch (NotDefinedException e) {
			return activity.getId();
		}
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof String) {
			return getActivityText(activityManager.getActivity((String) element));
		} else if (element instanceof IActivity) {
			return getActivityText((IActivity) element);
		} else {
			throw new IllegalArgumentException();
		}
	}
}
