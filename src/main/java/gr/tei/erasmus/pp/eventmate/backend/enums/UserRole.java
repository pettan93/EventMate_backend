package gr.tei.erasmus.pp.eventmate.backend.enums;

public enum UserRole {
    EVENT_OWNER(1L),
    TASK_OWNER(2L),
    EVENT_GUEST(3L),
    TASK_ASSIGNEE(4L);


    private Long number;

    UserRole(Long number) {
        this.number = number;
    }

    public static UserRole getByNumber(Long num) {
        for (UserRole value : UserRole.values()) {
            if (value.number.equals(num))
                return value;
        }
        return null;
    }


}
