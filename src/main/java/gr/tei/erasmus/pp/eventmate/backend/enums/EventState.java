package gr.tei.erasmus.pp.eventmate.backend.enums;

import java.sql.Blob;

public enum EventState {
    EDITABLE,
    READY_TO_PLAY,
    UNDER_EVALUATION,
    FINISHED;

    private String name;

    private Blob icon;


}
