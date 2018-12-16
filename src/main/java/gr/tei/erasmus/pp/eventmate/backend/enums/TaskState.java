package gr.tei.erasmus.pp.eventmate.backend.enums;

import java.sql.Blob;

public enum TaskState {
    EDITABLE,
    READY_TO_START,
    IN_PLAY,
    IN_REVIEW,
    FINISHED;

    private String name;

    private Blob icon;


    public static TaskState next(TaskState taskState) {

        switch (taskState) {
            case EDITABLE:
                return READY_TO_START;
            case READY_TO_START:
                return IN_PLAY;
            case IN_PLAY:
                return IN_REVIEW;
            case IN_REVIEW:
                return FINISHED;
            case FINISHED:
                return FINISHED;
        }
        return null;
    }



}
