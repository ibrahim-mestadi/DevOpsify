package com.winchesters.devopsify.service.technologies.docker.dockerfile;

import com.winchesters.devopsify.enums.DockerFileType;
import com.winchesters.devopsify.utils.DockerfileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.winchesters.devopsify.enums.DockerfileDataBaseKeywords.*;


public class DataBaseDockerFile implements DockerFileFactory {
    private String imageName = DEFAULT_IMAGE_NAME;
    private String imageVersion = DEFAULT_IMAGE_VERSION;
    private String imageBaseOS = DEFAULT_IMAGE_BASE_OS;

    DockerFileType dockerFileType = DockerFileType.DATABASE;
    private final String dbInitQueriesFilename;

    public DataBaseDockerFile(String imageName, String imageVersion, String imageBaseOS, String dbInitQueriesFilename) {
        if (imageName != null)
            this.imageName = imageName;
        if (imageVersion != null)
            this.imageVersion = imageVersion;
        if (imageBaseOS != null)
            this.imageBaseOS = imageBaseOS;
        this.dbInitQueriesFilename = dbInitQueriesFilename;
    }

    @Override
    public void writeDockerfile() {
        //TODO
    }

    @Override
    public String getDockerfileContent() throws IOException {
        File DockerfileTemplate = getDockerfileTemplate();
        DockerfileUtils dockerfileUtils = new DockerfileUtils(DockerfileTemplate);
        dockerfileUtils
                .setDockerfileKeywordValue(
                        Map.of(
                                DATABASE_NAME.value(), imageName,
                                DATABASE_VERSION.value(), imageVersion,
                                BASE_OS.value(), imageBaseOS
                        )
                );
        if (dbInitQueriesFilename == null)
            dockerfileUtils.commentLine(2);
        else
            dockerfileUtils
                    .setDockerfileKeywordValue(
                            DATABASE_INIT_QUERIES_FILENAME.value(),
                            dbInitQueriesFilename
                    );
        return dockerfileUtils.getDockerfileContent();
    }

    @Override
    public File getDockerfileTemplate() {
        return getDockerfileTemplate(DockerFileType.DATABASE);
    }
}
