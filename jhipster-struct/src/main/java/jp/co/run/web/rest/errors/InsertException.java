package jp.co.run.web.rest.errors;

public class InsertException extends InternalServerErrorException {

    private static final long serialVersionUID = 1L;

    public InsertException(String message) {
        super(message);
    }

}
