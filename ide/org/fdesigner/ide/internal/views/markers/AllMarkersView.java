package org.fdesigner.ide.internal.views.markers;
import org.fdesigner.commands.operations.IUndoContext;
import org.fdesigner.commands.operations.ObjectUndoContext;
import org.fdesigner.ide.undo.WorkspaceUndoUtil;
import org.fdesigner.ide.views.markers.MarkerSupportView;
import org.fdesigner.ide.views.markers.internal.MarkerSupportRegistry;


/**
 * AllMarkersView is the view that shows all markers.
 * @since 3.4
 *
 */
public class AllMarkersView extends MarkerSupportView {

	/**
	 * Create a new instance of the receiver.
	 */
	public AllMarkersView() {
		super(MarkerSupportRegistry.ALL_MARKERS_GENERATOR);
	}

	@Override
	protected IUndoContext getUndoContext() {
		ObjectUndoContext context= new ObjectUndoContext(new Object(), "All Markers Context"); //$NON-NLS-1$
		context.addMatch(WorkspaceUndoUtil.getBookmarksUndoContext());
		context.addMatch(WorkspaceUndoUtil.getTasksUndoContext());
		context.addMatch(WorkspaceUndoUtil.getProblemsUndoContext());
		return context;
	}

}
