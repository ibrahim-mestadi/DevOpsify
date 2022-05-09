package com.winchesters.devopsify.service.github;

import com.winchesters.devopsify.dto.GithubRepositoryDto;
import com.winchesters.devopsify.exception.github.PersonalAccessTokenPermissionException;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GithubRepositoryImpl implements GithubRepository {
    private final GithubServiceImpl githubService;

    @Override
    public GHRepository createRepository(GithubRepositoryDto githubRepositoryDto
    ) throws IOException {
        if (githubService.github == null)
            throw new PersonalAccessTokenPermissionException(); //TODO -- a more specific exception
        return githubService.github.createRepository(githubRepositoryDto.name())
                .autoInit(Optional.ofNullable(githubRepositoryDto.autoInit()).orElse(false))
                .licenseTemplate(githubRepositoryDto.licenseTemplate())
                .gitignoreTemplate(githubRepositoryDto.gitIgnoreTemplate())
                .owner(githubRepositoryDto.owner())
                .description(githubRepositoryDto.description())
                .visibility(githubRepositoryDto.visibility())
                .private_(githubRepositoryDto.private_())
                .create();
    }
}
