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




}
