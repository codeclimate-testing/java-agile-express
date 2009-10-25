package com.express.model.request
{
import com.express.model.domain.Project;

[RemoteClass(alias="com.express.service.dto.ProjectAccessRequest")]
public class ProjectAccessRequest
{

   public var newProject : Project;

   public var existingProjects : Array;

}
}