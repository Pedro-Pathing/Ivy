plugins {
    id("com.android.library")
    id("io.deepmedia.tools.deployer")
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.pedropathing.ivy"
    compileSdk = 30

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    defaultConfig {
        minSdk = 21
    }
}

dependencies {
    compileOnly(libs.core)
    compileOnly(libs.annotations)
    dokkaPlugin(libs.bundles.docs)
}

val dokkaJar = tasks.register<Jar>("dokkaJar") {
    dependsOn(tasks.named("dokkaGenerate"))
    from(dokka.basePublicationsDirectory.dir("html"))
    archiveClassifier = "html-docs"
}

deployer {
    projectInfo {
        name = "Ivy by Pedro Pathing"
        description = "A path follower designed to revolutionize autonomous pathing in robotics"
        url = "https://github.com/Pedro-Pathing/Ivy"
        scm {
            fromGithub("Pedro-Pathing", "Ivy")
        }
        license("BSD 3-Clause License", "https://opensource.org/licenses/BSD-3-Clause")

        developer("Baron Henderson", "baron@pedropathing.com")
        developer("Havish Sripada", "havish@pedropathing.com")
        developer("Davis Luxenberg", "davis@pedropathing.com")
    }

    signing {
        key = secret("MVN_GPG_KEY")
        password = secret("MVN_GPG_PASSWORD")
    }

    content {
        androidComponents("release") {
            docs(dokkaJar)
        }
    }

    centralPortalSpec {
        auth {
            user = secret("SONATYPE_USERNAME")
            password = secret("SONATYPE_PASSWORD")
        }
        allowMavenCentralSync = false
    }

    nexusSpec("snapshot") {
        repositoryUrl = "https://central.sonatype.com/repository/maven-snapshots/"
        auth {
            user = secret("SONATYPE_USERNAME")
            password = secret("SONATYPE_PASSWORD")
        }
    }

    localSpec()
}