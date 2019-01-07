package jp.co.run.web.rest.errors;

public class SelectException extends InternalServerErrorException {

    private static final long serialVersionUID = 1L;

    public SelectException() {
        this("Select error exception.");
    }

    public SelectException(String message) {
        super(message);
    }

}
