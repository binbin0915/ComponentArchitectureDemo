plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

dependencies {
    compileOnly(gradleApi())
}

gradlePlugin {
    plugins {
        create("version") {
            //自定义plugin的id，其他module引用要用到
            id = "com.wangkai.version"
            //指向自定义plugin类，因为我直接放在目录下，所以没有多余路径
            implementationClass = "VersionConfigPlugin"
        }
    }
}