package com.winchesters.devopsify.service.technologies.github;

import com.winchesters.devopsify.dto.request.GithubRepositoryDto;
import com.winchesters.devopsify.enums.RepositoryStatus;
import com.winchesters.devopsify.enums.ReadMeStatus;
import com.winchesters.devopsify.exception.github.GithubRepositoryNotFoundException;
import com.winchesters.devopsify.exception.github.PersonalAccessTokenPermissionException;
import com.winchesters.devopsify.model.GithubAnalyseResults;
import com.winchesters.devopsify.service.technologies.github.branch.Branch;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static com.winchesters.devopsify.service.technologies.github.readme.ReadMe.analyseReadMe;

@Service
@RequiredArgsConstructor
@Transactional
public class GithubRepositoryServiceImpl implements GithubRepositoryService {
    private final GithubServiceImpl githubService;

    private static final Logger LOG = LoggerFactory.getLogger(GithubRepositoryServiceImpl.class);

    @Override
    public GHRepository createRepository(GithubRepositoryDto githubRepositoryDto
    ) throws IOException {
        GitHub gitHub = githubService.getGithub();
        if (gitHub == null)
            throw new PersonalAccessTokenPermissionException(); //TODO -- a more specific exception
        return gitHub.createRepository(githubRepositoryDto.name())
                .autoInit(Optional.ofNullable(githubRepositoryDto.autoInit()).orElse(false))
                .licenseTemplate(githubRepositoryDto.licenseTemplate())
                .gitignoreTemplate(githubRepositoryDto.gitIgnoreTemplate())
                .owner(githubRepositoryDto.owner())
                .description(githubRepositoryDto.description())
                .visibility(githubRepositoryDto.visibility())
                .private_(Optional.ofNullable(githubRepositoryDto.private_()).orElse(false))
                .create();
    }

    private String getReadMe(GHRepository repository) throws IOException {
        if (repository == null)
            throw new GithubRepositoryNotFoundException();
        GHContent inputStream = repository.getReadme();
        if (inputStream.getSize() == 0) {
            return "";
        }
        byte[] bytes = new byte[Math.toIntExact(inputStream.getSize())];
        //noinspection ResultOfMethodCallIgnored
        inputStream.read().read(bytes);
        return new String(bytes);
    }

    public ReadMeStatus getReadMeStatus(GHRepository repository) throws IOException {
        String readMeContent = getReadMe(repository);
        LOG.debug("ReadMe's number of lines : {}", readMeContent.lines().count());
        return analyseReadMe(readMeContent, repository.getName());
    }
    public RepositoryStatus getRepositoryStatus(GHRepository repository) throws IOException {
        if (repository == null)
            throw new GithubRepositoryNotFoundException();
        if (repository.getLicense() == null)
            return RepositoryStatus.LICENSE_MISSING;
        Branch branch = new Branch(repository);
        if (!branch.containsGitIgnore()) return RepositoryStatus.GITIGNORE_MISSING;
        if (!getReadMeStatus(repository).equals(ReadMeStatus.OKAY)) return RepositoryStatus.README_PROBLEM;
        return RepositoryStatus.OKAY;
    }

    public GithubAnalyseResults analyseGithub(GHRepository repository) throws IOException {
        return new GithubAnalyseResults(
                repository.getName(),
                getReadMeStatus(repository),
                getRepositoryStatus(repository),
                repository.listCommits().toList().size(),
                repository.getPushedAt()
        );
    }

    public GithubAnalyseResults analyseGithub() {
        //TODO

        return null;
    }

    // IGNORE
    public GithubAnalyseResults test(String name) throws IOException {
        GitHub github = githubService.getGithub();
        GHRepository repository = github.getRepository("temp-devopsify/"+name);
        if (repository == null)
            throw new GithubRepositoryNotFoundException();
        return analyseGithub(repository);

    }

}
