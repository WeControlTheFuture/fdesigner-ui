/*******************************************************************************
 *  Copyright (c) 2007, 2018 IBM Corporation and others.
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *     Cloudsmith Inc. - converted into expression based query
 *******************************************************************************/
package org.fdesigner.p2.engine.query;

import org.fdesigner.p2.metadata.IInstallableUnit;
import org.fdesigner.p2.metadata.expression.ExpressionUtil;
import org.fdesigner.p2.metadata.expression.IExpression;
import org.fdesigner.p2.metadata.expression.IExpressionFactory;
import org.fdesigner.p2.metadata.expression.IMatchExpression;
import org.fdesigner.p2.metadata.query.ExpressionMatchQuery;

/**
 * A query that searches for {@link IInstallableUnit} instances that have
 * a property associated with the specified profile, whose value matches the provided value.
 * @since 2.0
 */
public class IUProfilePropertyQuery extends ExpressionMatchQuery<IInstallableUnit> {
	/**
	 * A property value constant that will match any defined property value.
	 * @see #IUProfilePropertyQuery(String, String)
	 */
	public static final String ANY = "*"; //$NON-NLS-1$

	private static final IExpression matchValue = ExpressionUtil.parse("profileProperties[$0] == $1"); //$NON-NLS-1$
	private static final IExpression matchAny = ExpressionUtil.parse("profileProperties[$0] != null"); //$NON-NLS-1$

	private static IMatchExpression<IInstallableUnit> createMatch(String propertyName, String propertyValue) {
		IExpressionFactory factory = ExpressionUtil.getFactory();
		return ANY.equals(propertyValue) ? factory.matchExpression(matchAny, propertyName) : factory.<IInstallableUnit> matchExpression(matchValue, propertyName, propertyValue);
	}

	/**
	 * Creates a new query on the given property name and value.
	 * Because the queryable for this query is typically the profile
	 * instance, we use a reference to the profile rather than the
	 * profile id for performance reasons.
	 * @param propertyName The name of the property to match
	 * @param propertyValue The value to compare to. A value of {@link #ANY} will match any value.
	 */
	public IUProfilePropertyQuery(String propertyName, String propertyValue) {
		super(IInstallableUnit.class, createMatch(propertyName, propertyValue));
	}
}
