package com.workflowengine.workflowengine.services;

import com.workflowengine.workflowengine.model.SysUser;
import com.workflowengine.workflowengine.security.AuthHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthPermissionService {

    private final Logger log = LoggerFactory.getLogger(AuthPermissionService.class.getName());
    @Autowired
    private ServiceLocator serviceLocator;

    public SysUser getCurrentUser() {
        int userId = getCurrentUserId();

        //TODO: Call the resource to fetch the user detials by the id from the token.
        // returning the sample user "Admin".
        SysUser temp = new SysUser();
        temp.setId(1);
        temp.setFirstName("Admin");
        temp.setLastName("Admin");

        return temp;
    }

    private int getCurrentUserId() {
        return Integer.parseInt(AuthHolder.getCurrentToken().getAdditionalAttributes().get("userId"));
    }


    /**
     * Is the current user an admin?  Fishes the isAdmin flag out of the token.
     *
     * @return true if the current user is an admin.
     */
    public String getRole() {
        String role = AuthHolder.getCurrentToken().getAdditionalAttributes().get("role");
        log.info("username=\"{}\" role=\"{}\"", getCurrentUser().getId(), role);
        return role;
    }

    public boolean isSelf(int userId) {
        return getCurrentUser().getId() == userId;
    }

}