package com.winchesters.devopsify.dto.request.project;

import com.winchesters.devopsify.model.entity.Server;

public record CreateNewProjectNexusDto(
        Server server
) {

}
