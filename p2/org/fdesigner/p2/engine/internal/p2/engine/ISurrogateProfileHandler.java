/*******************************************************************************
 *  Copyright (c) 2008, 2013 IBM Corporation and others.
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 * 
 *  Contributors:
 *      IBM Corporation - initial API and implementation
 *      Ericsson AB - Bug 400011 - [shared] Cleanup the SurrogateProfileHandler code
 *******************************************************************************/
package org.fdesigner.p2.engine.internal.p2.engine;

import org.fdesigner.p2.engine.IProfile;
import org.fdesigner.p2.metadata.IInstallableUnit;
import org.fdesigner.p2.metadata.query.IQuery;
import org.fdesigner.p2.metadata.query.IQueryResult;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;

/**
 * @since 2.0
 */
public interface ISurrogateProfileHandler {

	public abstract IProfile createProfile(String id);

	public abstract boolean isSurrogate(IProfile profile);

	public abstract IQueryResult<IInstallableUnit> queryProfile(IProfile profile, IQuery<IInstallableUnit> query, IProgressMonitor monitor);

}