package gr.tei.erasmus.pp.eventmate.backend.enums;

public enum ErrorType {
    BAD_PASSWORD(3999),
    ENTITY_NOT_FOUND(4000),
    NO_REPORTS_IN_EVENT(4017),
    NO_SUBMISSION(4014),

    USER_NOT_EVENT_OWNER(4000),
    USER_NOT_TASK_OWNER(4020),
    USER_NOT_REPORT_CREATOR(4001),
    USER_NOT_TASK_ASSIGNEE(4012),

    NO_PERMISSION(4002),
    NO_PERMISSION_FOR_EVENT(4003),
    NO_PERMISSION_FOR_TASK(4004),
    NO_PERMISSION_FOR_DELETE_REPORT(4005),
    NO_PERMISSION_FOR_SEND_SUBMISSION(4006),
    NO_PERMISSION_FOR_SUBMISSION_FILE(4007),
    NO_PERMISSION_FOR_EDIT_TASK(4008),
    NO_PERMISSION_FOR_ASSIGN_POINTS(4013),


    EMAIL_ALREADY_USED(4015),
    EVENT_NAME_USED(4009),

    TASK_IS_NOT_IN_PLAYABLE_STATE(4018),
    TASK_IS_NOT_IN_REVIEW_STATE(4019),
    EVENT_NOT_IN_EDITABLE_STATE(4010),
    EVENT_NOT_IN_FINISHED_STATE(4011),

    MORE_POINTS_THAN_ALLOWED(4016);


    ErrorType(int statusCode) {
        this.statusCode = statusCode;
    }

    public int statusCode;
}
