/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corporation and others.
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
package org.fdesigner.p2.engine.internal.p2.engine;

import org.fdesigner.p2.core.IProvisioningAgent;
import org.fdesigner.p2.core.internal.p2.core.helpers.LogHelper;
import org.fdesigner.p2.core.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.fdesigner.p2.engine.IEngine;
import org.fdesigner.p2.engine.IPhaseSet;
import org.fdesigner.p2.engine.IProfile;
import org.fdesigner.p2.engine.IProfileRegistry;
import org.fdesigner.p2.engine.IProvisioningPlan;
import org.fdesigner.p2.engine.PhaseSetFactory;
import org.fdesigner.p2.engine.ProvisioningContext;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.MultiStatus;
import org.fdesigner.runtime.common.runtime.NullProgressMonitor;
import org.fdesigner.runtime.common.runtime.Status;

/**
 * Concrete implementation of the {@link IEngine} API.
 */
public class Engine implements IEngine {

	private static final String ENGINE = "engine"; //$NON-NLS-1$
	private IProvisioningAgent agent;

	public Engine(IProvisioningAgent agent) {
		this.agent = agent;
		agent.registerService(ActionManager.SERVICE_NAME, new ActionManager());
	}

	private void checkArguments(IProfile iprofile, PhaseSet phaseSet, Operand[] operands) {
		if (iprofile == null)
			throw new IllegalArgumentException(Messages.null_profile);

		if (phaseSet == null)
			throw new IllegalArgumentException(Messages.null_phaseset);

		if (operands == null)
			throw new IllegalArgumentException(Messages.null_operands);
	}

	@Override
	public IStatus perform(IProvisioningPlan plan, IPhaseSet phaseSet, IProgressMonitor monitor) {
		return perform(plan.getProfile(), phaseSet, ((ProvisioningPlan) plan).getOperands(), plan.getContext(), monitor);
	}

	@Override
	public IStatus perform(IProvisioningPlan plan, IProgressMonitor monitor) {
		return perform(plan, PhaseSetFactory.createDefaultPhaseSet(), monitor);
	}

	public IStatus perform(IProfile iprofile, IPhaseSet phases, Operand[] operands, ProvisioningContext context, IProgressMonitor monitor) {
		PhaseSet phaseSet = (PhaseSet) phases;
		checkArguments(iprofile, phaseSet, operands);
		if (operands.length == 0)
			return Status.OK_STATUS;
		SimpleProfileRegistry profileRegistry = (SimpleProfileRegistry) agent.getService(IProfileRegistry.class);
		IProvisioningEventBus eventBus = agent.getService(IProvisioningEventBus.class);

		if (context == null)
			context = new ProvisioningContext(agent);

		if (monitor == null)
			monitor = new NullProgressMonitor();

		Profile profile = profileRegistry.validate(iprofile);

		profileRegistry.lockProfile(profile);
		try {
			eventBus.publishEvent(new BeginOperationEvent(profile, phaseSet, operands, this));
			if (DebugHelper.DEBUG_ENGINE)
				DebugHelper.debug(ENGINE, "Beginning engine operation for profile=" + profile.getProfileId() + " [" + profile.getTimestamp() + "]:" + DebugHelper.LINE_SEPARATOR + DebugHelper.formatOperation(phaseSet, operands, context)); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

			EngineSession session = new EngineSession(agent, profile, context);

			MultiStatus result = phaseSet.perform(session, operands, monitor);
			if (result.isOK() || result.matches(IStatus.INFO | IStatus.WARNING)) {
				if (DebugHelper.DEBUG_ENGINE)
					DebugHelper.debug(ENGINE, "Preparing to commit engine operation for profile=" + profile.getProfileId()); //$NON-NLS-1$
				result.merge(session.prepare(monitor));
			}
			if (result.matches(IStatus.ERROR | IStatus.CANCEL)) {
				if (DebugHelper.DEBUG_ENGINE)
					DebugHelper.debug(ENGINE, "Rolling back engine operation for profile=" + profile.getProfileId() + ". Reason was: " + result.toString()); //$NON-NLS-1$ //$NON-NLS-2$
				IStatus status = session.rollback(monitor, result.getSeverity());
				if (status.matches(IStatus.ERROR))
					LogHelper.log(status);
				eventBus.publishEvent(new RollbackOperationEvent(profile, phaseSet, operands, this, result));
			} else {
				if (DebugHelper.DEBUG_ENGINE)
					DebugHelper.debug(ENGINE, "Committing engine operation for profile=" + profile.getProfileId()); //$NON-NLS-1$
				if (profile.isChanged())
					profileRegistry.updateProfile(profile);
				IStatus status = session.commit(monitor);
				if (status.matches(IStatus.ERROR))
					LogHelper.log(status);
				eventBus.publishEvent(new CommitOperationEvent(profile, phaseSet, operands, this));
			}
			//if there is only one child status, return that status instead because it will have more context
			IStatus[] children = result.getChildren();
			return children.length == 1 ? children[0] : result;
		} finally {
			profileRegistry.unlockProfile(profile);
			profile.setChanged(false);
		}
	}

	protected IStatus validate(IProfile iprofile, PhaseSet phaseSet, Operand[] operands, ProvisioningContext context, IProgressMonitor monitor) {
		checkArguments(iprofile, phaseSet, operands);

		if (context == null)
			context = new ProvisioningContext(agent);

		if (monitor == null)
			monitor = new NullProgressMonitor();

		ActionManager actionManager = agent.getService(ActionManager.class);
		return phaseSet.validate(actionManager, iprofile, operands, context, monitor);
	}

	@Override
	public IProvisioningPlan createPlan(IProfile profile, ProvisioningContext context) {
		return new ProvisioningPlan(profile, null, context);
	}
}
