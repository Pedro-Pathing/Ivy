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
    compileOnly("com.pedropathing:core:3.0.0.local.alpha44")
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
        developer("Kabir Goyal", "kabirgoyal@icloud.com")
    }

    content {
        androidComponents("release") {
            docs(dokkaJar)
        }
    }

    localSpec()
}
