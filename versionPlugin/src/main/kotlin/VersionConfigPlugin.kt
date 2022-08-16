import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("version")
    }
}
