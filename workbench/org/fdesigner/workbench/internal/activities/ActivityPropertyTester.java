/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.activities;

import org.fdesigner.expressions.PropertyTester;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.activities.IActivityManager;
import org.fdesigner.workbench.activities.IWorkbenchActivitySupport;
import org.fdesigner.workbench.activities.WorkbenchActivityHelper;

/**
 * An expressions property tester that tests whether or not an activity or
 * category of activities is enabled. Useful for cross-component links and to
 * control discoverability of functionality.
 *
 * @since 3.3
 */
public class ActivityPropertyTester extends PropertyTester {

	private static final String PROPERTY_IS_ACTIVITY_ENABLED = "isActivityEnabled"; //$NON-NLS-1$
	private static final String PROPERTY_IS_CATEGORY_ENABLED = "isCategoryEnabled"; //$NON-NLS-1$

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (args.length == 1 && receiver instanceof IWorkbench && args[0] instanceof String) {
			if (PROPERTY_IS_ACTIVITY_ENABLED.equals(property)) {
				return isActivityEnabled((String) args[0], (IWorkbench) receiver);
			} else if (PROPERTY_IS_CATEGORY_ENABLED.equals(property)) {
				return isCategoryEnabled((String) args[0], (IWorkbench) receiver);
			}
		}
		return false;
	}

	private static boolean isActivityEnabled(String activityId, IWorkbench workbench) {
		try {
			IWorkbenchActivitySupport workbenchActivitySupport = workbench.getActivitySupport();
			IActivityManager activityManager = workbenchActivitySupport.getActivityManager();
			return activityManager.getActivity(activityId).isEnabled();
		} catch (Exception e) {
			// workbench not yet activated; nothing enabled yet
		}
		return false;
	}

	private static boolean isCategoryEnabled(String categoryId, IWorkbench workbench) {
		try {
			IWorkbenchActivitySupport workbenchActivitySupport = workbench.getActivitySupport();
			IActivityManager activityManager = workbenchActivitySupport.getActivityManager();
			return WorkbenchActivityHelper.isEnabled(activityManager, categoryId);
		} catch (Exception e) {
			// workbench not yet activated; nothing enabled yet
		}
		return false;
	}
}
