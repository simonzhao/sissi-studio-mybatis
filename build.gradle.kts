plugins {
	id("java")
	id("org.jetbrains.intellij") version "1.8.0"
}

group = "com.messycode.scenicspot.beijingolympicpack"
version = "0.1"

repositories {
	mavenCentral()
}

intellij {
	version.set("2021.3.3")
	type.set("IC")

	plugins.set(listOf("com.intellij.java"))
} 

dependencies {
	implementation("mysql:mysql-connector-java:5.1.47")
	implementation("com.google.guava:guava:31.1-jre")
	implementation("commons-io:commons-io:2.11.0")
}

tasks {
	withType<JavaCompile> {
		sourceCompatibility = "11"
		targetCompatibility = "11"
	}

	patchPluginXml {
		sinceBuild.set("213")
		untilbuild.set("223.*")
	}
	
	signPlugin {
		certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
		privateKey.set(System.getenv("PRIVATE_KEY"))
		password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
	}

	publishPlugin {
	}
}
