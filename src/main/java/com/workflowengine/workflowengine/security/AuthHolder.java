package com.workflowengine.workflowengine.security;

import com.workflowengine.workflowengine.domain.JWTToken;

public class AuthHolder {
    /**
     * The CURRENT TOKEN.
     */
    private static final ThreadLocal<JWTToken> CURRENT_TOKEN = new ThreadLocal<>();

    /**
     * Initialization prohibited.
     */
    private AuthHolder() {
    }

    /**
     * Cleans up the thread local.
     */
    public static void reset() {
        CURRENT_TOKEN.remove();
    }

    /**
     * Materializes the current tracer out of thin air.
     *
     * @return the Tracer currently being traced to.
     */
    public static JWTToken getCurrentToken() {
        return CURRENT_TOKEN.get();
    }

    /**
     * Install a new Tracer for this thread.
     *
     * @param current a Tracer, presumably new.
     */
    public static void setCurrentToken(final JWTToken current) {
        CURRENT_TOKEN.set(current);

    }
}
