package gr.tei.erasmus.pp.eventmate.backend.enums;

public enum ErrorType {
    USER_NOT_REPORT_CREATOR(4001);


    ErrorType(int statusCode) {
        this.statusCode = statusCode;
    }

    public int statusCode;
}
