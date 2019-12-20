/*******************************************************************************
 * Copyright (c) 2010, 2017 Cloudsmith Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Cloudsmith Inc. - initial API and implementation
 *     IBM Corporation - ongoing development and bug fixes
 *******************************************************************************/
package org.fdesigner.p2.metadata.internal.p2.metadata.index;

import org.fdesigner.p2.metadata.index.IIndexProvider;
import org.fdesigner.p2.metadata.index.IQueryWithIndex;
import org.fdesigner.p2.metadata.query.IQuery;
import org.fdesigner.p2.metadata.query.IQueryResult;
import org.fdesigner.p2.metadata.query.IQueryable;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;

public abstract class IndexProvider<T> implements IIndexProvider<T>, IQueryable<T> {
	public static <Q> IQueryResult<Q> query(IIndexProvider<Q> indexProvider, IQuery<Q> query, IProgressMonitor monitor) {
		if (monitor != null)
			monitor.beginTask(null, IProgressMonitor.UNKNOWN);
		IQueryResult<Q> result = (query instanceof IQueryWithIndex<?>) ? ((IQueryWithIndex<Q>) query).perform(indexProvider) : query.perform(indexProvider.everything());
		if (monitor != null) {
			monitor.worked(1);
			monitor.done();
		}
		return result;
	}

	@Override
	public IQueryResult<T> query(IQuery<T> query, IProgressMonitor monitor) {
		return query(this, query, monitor);
	}
}
