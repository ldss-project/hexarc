// ### Project Information #############################################################################################
private class ProjectInfo {
    val longName: String = "HexArc"
    val description: String = "Utility module for instantiating services following the best practices of the Hexagonal Architecture Pattern, also known as Clean Architecture."

    val repositoryOwner: String = "jahrim"
    val repositoryName: String = "hexarc"

    val artifactGroup: String = "io.github.jahrim"
    val artifactId: String = project.name
    val implementationClass: String = "main.MainClass"

    val license = "The MIT License"
    val licenseUrl = "https://opensource.org/licenses/MIT"

    val website = "https://github.com/$repositoryOwner/$repositoryName"
    val tags = listOf("scala3", "service", "hexagonal architecture", "clean architecture")
}
private val projectInfo: ProjectInfo = ProjectInfo()

// ### Build Configuration #############################################################################################
plugins {
    with(libs.plugins){
        `java-library`
        scala
        application
        alias(spotless)
        alias(wartremover)
        alias(git.semantic.versioning)
        alias(publish.on.central)
    }
}

repositories { mavenCentral() }

dependencies {
    compileOnly(libs.bundles.scalafmt)
    implementation(libs.scala)
    api(libs.vertx)
    api(libs.log)
    api(libs.mongodb.client)
    testImplementation(libs.scalatest)
    testImplementation(libs.scalatestplusjunit)
}

application {
    mainClass.set(projectInfo.implementationClass)
}

spotless {
    isEnforceCheck = false
    scala { scalafmt(libs.versions.scalafmt.version.get()).configFile(".scalafmt.conf") }
    tasks.compileScala.get().dependsOn(tasks.spotlessApply)
}

// ### Publishing ######################################################################################################
group = projectInfo.artifactGroup
gitSemVer {
    buildMetadataSeparator.set("-")
    assignGitSemanticVersion()
}

tasks.javadocJar {
    dependsOn(tasks.scaladoc)
    from(tasks.scaladoc.get().destinationDir)
}

publishOnCentral {
    configureMavenCentral.set(true)
    projectDescription.set(projectInfo.description)
    projectLongName.set(projectInfo.longName)
    licenseName.set(projectInfo.license)
    licenseUrl.set(projectInfo.licenseUrl)
    repoOwner.set(projectInfo.repositoryOwner)
    projectUrl.set(projectInfo.website)
    scmConnection.set("scm:git:$projectUrl")
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                developers {
                    developer {
                        name.set("Jahrim Gabriele Cesario")
                        email.set("jahrim.cesario2@studio.unibo.it")
                        url.set("https://jahrim.github.io")
                    }
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}