/*******************************************************************************
 * Copyright (c) 2008, 2009 Matthew Hall and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Matthew Hall - initial API and implementation (bug 246782)
 ******************************************************************************/

package org.fdesigner.databinding.observable.internal.databinding.observable.masterdetail;

import java.util.Objects;

import org.fdesigner.databinding.observable.Realm;
import org.fdesigner.databinding.observable.util.Policy;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;

/* package */class DetailObservableHelper {
	/* package */static void warnIfDifferentRealms(Realm detailRealm,
			Realm innerObservableRealm) {
		if (!Objects.equals(detailRealm, innerObservableRealm)) {
			Throwable throwable = new Throwable();
			throwable.fillInStackTrace();
			String message = "Inner observable realm (" + innerObservableRealm //$NON-NLS-1$
					+ ") not equal to detail realm (" //$NON-NLS-1$
					+ detailRealm + ")"; //$NON-NLS-1$
			Policy.getLog().log(
					new Status(IStatus.WARNING, Policy.JFACE_DATABINDING,
							message, throwable));
		}
	}
}
