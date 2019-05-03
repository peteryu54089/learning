package model;

import javax.servlet.http.HttpSession;

public interface Authenticate {
    default boolean authCheck(HttpSession session) {
        return false;
    }
}
