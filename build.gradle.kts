plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.deployer) apply false
}

subprojects {
    group = "com.pedropathing"
    version = property("version") as String
}

tasks.register("deployLocal") {
    group = "publishing"
    description = "Publishes all subprojects to Maven Local."
    dependsOn(subprojects.map { it.tasks.named("deployLocal") })
}