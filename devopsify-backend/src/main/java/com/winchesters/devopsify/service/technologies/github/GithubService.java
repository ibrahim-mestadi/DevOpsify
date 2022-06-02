package com.winchesters.devopsify.service.technologies.github;

import javax.validation.constraints.NotNull;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;

public interface GithubService {

    /**
     * all permissions for personal access tokens
     */
    List<String> allPermissions = List.of("admin:enterprise", "admin:gpg_key", "admin:org", "admin:org_hook", "admin:public_key", "admin:repo_hook", "delete:packages", "delete_repo", "gist", "notifications", "repo", "user", "workflow", "write:discussion", "write:packages");

    /**
     * initialize github and verify access token
     * @param personalAccessToken personal access token : (https://github.com/settings/tokens)
     * @throws IOException if a problem occurs while connecting to github with the personalAccessToken
     * @return created Github Object
     */
    GitHub connectToGithub(@NotNull @NotEmpty String personalAccessToken) throws IOException;
}