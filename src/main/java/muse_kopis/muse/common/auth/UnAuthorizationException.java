package muse_kopis.muse.common.auth;

public class UnAuthorizationException extends RuntimeException {

    public UnAuthorizationException(String message) {
        super(message);
    }
}
