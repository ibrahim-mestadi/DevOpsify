package com.winchesters.devopsify.service.technologies.jenkins;

import com.winchesters.devopsify.exception.jenkins.JenkinsException;
import com.winchesters.devopsify.model.JenkinsAnalyseResults;
import com.winchesters.devopsify.model.entity.Project;
import com.winchesters.devopsify.model.entity.Server;

import java.io.File;
import java.io.IOException;

public interface JenkinsService {

    void generateJenkinsFile(File directory);

    void pingJenkinsServer() throws JenkinsException;

    void pingJenkinsServer(Server server) throws JenkinsException;

    void setJenkinsClient(Server server);

    void installRequiredPlugins();

    void saveGithubCredentials(String token);

    void pullFromGithub();

    void createGithubTrigger();

    String createApiToken(String name);
    void createPipeline(String repositoryUrl, String pipelineName, String webHookToken);

    JenkinsAnalyseResults analyseJenkins(Project project);

    String createJenkinsPipeline(Server jenkins, String name, String remoteRepoUrl, Server dockerhubCredentials, Server ec2Credentials) throws IOException;
}
