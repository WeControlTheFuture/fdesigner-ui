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
 *******************************************************************************/
package org.fdesigner.p2.metadata.internal.p2.metadata.index;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.fdesigner.p2.metadata.IInstallableUnit;
import org.fdesigner.p2.metadata.expression.IEvaluationContext;
import org.fdesigner.p2.metadata.expression.IExpression;
import org.fdesigner.p2.metadata.internal.p2.metadata.IUMap;
import org.fdesigner.p2.metadata.internal.p2.metadata.InstallableUnit;

public class IdIndex extends Index<IInstallableUnit> {
	private final IUMap iuMap;

	public IdIndex(IUMap iuMap) {
		this.iuMap = iuMap;
	}

	public IdIndex(Iterator<IInstallableUnit> ius) {
		iuMap = new IUMap();
		while (ius.hasNext())
			iuMap.add(ius.next());
	}

	@Override
	public Iterator<IInstallableUnit> getCandidates(IEvaluationContext ctx, IExpression variable, IExpression booleanExpr) {
		Object queriedKeys = getQueriedIDs(ctx, variable, InstallableUnit.MEMBER_ID, booleanExpr, null);
		if (queriedKeys == null)
			return null;

		if (queriedKeys instanceof Collection<?>) {
			HashSet<IInstallableUnit> collector = new HashSet<>();
			for (Object key : (Collection<?>) queriedKeys)
				collector.addAll(iuMap.getUnits((String) key));
			return collector.iterator();
		}
		return iuMap.getUnits((String) queriedKeys).iterator();
	}
}
