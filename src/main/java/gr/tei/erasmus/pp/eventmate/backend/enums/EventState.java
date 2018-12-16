package gr.tei.erasmus.pp.eventmate.backend.enums;

import java.sql.Blob;

public enum EventState {

    EDITABLE,
    READY_TO_PLAY,
    IN_PLAY,
    UNDER_EVALUATION,
    FINISHED;

    private String name;

    private Blob icon;

    public static EventState next(EventState eventState) {

        switch (eventState) {
            case EDITABLE:
                return READY_TO_PLAY;
            case READY_TO_PLAY:
                return IN_PLAY;
            case IN_PLAY:
                return UNDER_EVALUATION;
            case UNDER_EVALUATION:
                return FINISHED;
            case FINISHED:
                return FINISHED;
        }
        return null;
    }


}
