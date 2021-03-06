package teammates.ui.controller;

import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.StatusMessage;
import teammates.common.util.StatusMessageColor;
import teammates.logic.api.GateKeeper;

public class InstructorFeedbackDeleteAction extends Action {

    @Override
    protected ActionResult execute() {
        
        String courseId = getRequestParamValue(Const.ParamsNames.COURSE_ID);
        String feedbackSessionName = getRequestParamValue(Const.ParamsNames.FEEDBACK_SESSION_NAME);
        String nextUrl = getRequestParamValue(Const.ParamsNames.NEXT_URL);
        
        Assumption.assertNotNull(courseId);
        Assumption.assertNotNull(feedbackSessionName);

        if (nextUrl == null) {
            nextUrl = Const.ActionURIs.INSTRUCTOR_FEEDBACKS_PAGE;
        }
        
        new GateKeeper().verifyAccessible(
                logic.getInstructorForGoogleId(courseId, account.googleId),
                logic.getFeedbackSession(feedbackSessionName, courseId),
                false,
                Const.ParamsNames.INSTRUCTOR_PERMISSION_MODIFY_SESSION);
        
        logic.deleteFeedbackSession(feedbackSessionName, courseId);
        statusToUser.add(new StatusMessage(Const.StatusMessages.FEEDBACK_SESSION_DELETED, StatusMessageColor.SUCCESS));
        statusToAdmin = "Feedback Session <span class=\"bold\">[" + feedbackSessionName + "]</span> "
                        + "from Course: <span class=\"bold\">[" + courseId + " deleted.";
        
        return createRedirectResult(nextUrl);
    }

}
