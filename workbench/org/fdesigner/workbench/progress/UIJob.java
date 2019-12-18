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
 *     IBM - Initial API and implementation
 *******************************************************************************/
package org.fdesigner.workbench.progress;

import org.eclipse.swt.widgets.Display;
import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.runtime.jobs.runtime.jobs.Job;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.misc.UIStats;
import org.fdesigner.workbench.internal.progress.ProgressMessages;

/**
 * The UIJob is a Job that runs within the UI Thread via an asyncExec.
 *
 * @since 3.0
 */
public abstract class UIJob extends Job {
	private Display cachedDisplay;

	/**
	 * Create a new instance of the receiver with the supplied name. The display
	 * used will be the one from the workbench if this is available. UIJobs with
	 * this constructor will determine their display at runtime.
	 *
	 * @param name the job name
	 */
	public UIJob(String name) {
		super(name);
	}

	/**
	 * Create a new instance of the receiver with the supplied Display.
	 *
	 * @param jobDisplay the display
	 * @param name       the job name
	 * @see Job
	 */
	public UIJob(Display jobDisplay, String name) {
		this(name);
		setDisplay(jobDisplay);
	}

	/**
	 * Convenience method to return a status for an exception.
	 *
	 * @param exception the thrown exception
	 * @return IStatus an error status built from the exception
	 * @see Job
	 */
	public static IStatus errorStatus(Throwable exception) {
		return WorkbenchPlugin.getStatus(exception);
	}

	/**
	 * Note: this message is marked final. Implementors should use runInUIThread()
	 * instead.
	 *
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public final IStatus run(final IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}
		Display asyncDisplay = getDisplay();
		if (asyncDisplay == null || asyncDisplay.isDisposed()) {
			return Status.CANCEL_STATUS;
		}
		asyncDisplay.asyncExec(() -> {
			IStatus result = null;
			Throwable throwable = null;
			try {
				// As we are in the UI Thread we can
				// always know what to tell the job.
				setThread(Thread.currentThread());
				if (monitor.isCanceled()) {
					result = Status.CANCEL_STATUS;
				} else {
					UIStats.start(UIStats.UI_JOB, getName());
					result = runInUIThread(monitor);
				}

			} catch (Throwable t) {
				throwable = t;
			} finally {
				UIStats.end(UIStats.UI_JOB, UIJob.this, getName());
				if (result == null) {
					result = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.ERROR,
							ProgressMessages.InternalError, throwable);
				}
				done(result);
			}
		});
		return Job.ASYNC_FINISH;
	}

	/**
	 * Run the job in the UI Thread.
	 *
	 * @param monitor the monitor to be used for reporting progress and responding
	 *                to cancellation. The monitor is never <code>null</code>
	 * @return resulting status of the run. The result must not be <code>null</code>
	 */
	public abstract IStatus runInUIThread(IProgressMonitor monitor);

	/**
	 * Sets the display to execute the asyncExec in. Generally this is not' used if
	 * there is a valid display available via PlatformUI.isWorkbenchRunning().
	 *
	 * @param runDisplay Display
	 * @see UIJob#getDisplay()
	 * @see PlatformUI#isWorkbenchRunning()
	 */
	public void setDisplay(Display runDisplay) {
		Assert.isNotNull(runDisplay);
		cachedDisplay = runDisplay;
	}

	/**
	 * Returns the display for use by the receiver when running in an asyncExec. If
	 * it is not set then the display set in the workbench is used. If the display
	 * is null the job will not be run.
	 *
	 * @return Display or <code>null</code>.
	 */
	public Display getDisplay() {
		// If it was not set get it from the workbench
		if (cachedDisplay == null && PlatformUI.isWorkbenchRunning()) {
			return PlatformUI.getWorkbench().getDisplay();
		}
		return cachedDisplay;
	}
}