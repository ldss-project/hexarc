// ### Project Information #############################################################################################
private class ProjectInfo {
    companion object {
        const val longName: String = "HexArc"
        const val description: String = "Utility module for instantiating services following the best practices of the Hexagonal Architecture Pattern, also known as Clean Architecture."

        const val repositoryOwner: String = "jahrim"
        const val repositoryName: String = "hexarc"

        const val artifactGroup: String = "io.github.jahrim"
        const val artifactId: String = "hexarc"
        const val implementationClass: String = "main.MainClass"

        const val license = "The MIT License"
        const val licenseUrl = "https://opensource.org/licenses/MIT"

        val website = "https://github.com/$repositoryOwner/$repositoryName"
        val tags = listOf("scala3", "service", "hexagonal architecture", "clean architecture")
    }
}

// ### Build Configuration #############################################################################################
plugins {
    with(libs.plugins){
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
    testImplementation(libs.scalatest)
    testImplementation(libs.scalatestplusjunit)
}

application {
    mainClass.set(ProjectInfo.implementationClass)
}

spotless {
    isEnforceCheck = false
    scala { scalafmt(libs.versions.scalafmt.version.get()).configFile(".scalafmt.conf") }
    tasks.compileScala.get().dependsOn(tasks.spotlessApply)
}

// ### Publishing ######################################################################################################
group = ProjectInfo.artifactGroup
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
    projectDescription.set(ProjectInfo.description)
    projectLongName.set(ProjectInfo.longName)
    licenseName.set(ProjectInfo.license)
    licenseUrl.set(ProjectInfo.licenseUrl)
    repoOwner.set(ProjectInfo.repositoryOwner)
    projectUrl.set(ProjectInfo.website)
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