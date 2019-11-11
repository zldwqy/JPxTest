### Jetpack
Jetpack是Android软件组件的集合，使您可以更轻松地开发出色的Android应用程序。这些组件可帮助您遵循最佳实践，免于编写样板代码并简化复杂任务，因此您可以专注于您关心的代码。

Jetpack包含 androidx.* 包库，与平台API 分开。这意味着它提供向后兼容性并且比Android平台更频繁地更新，确保您始终可以访问最新和最好版本的Jetpack组件。

##### Android X
AndroidX是Android团队用于在Jetpack中开发，测试，打包，发布和发布库的开源项目 。

AndroidX是对原始Android 支持库的重大改进 。与支持库一样，AndroidX与Android操作系统分开提供，并提供跨Android版本的向后兼容性。AndroidX通过提供功能奇偶校验和新库完全取代了支持库。此外，AndroidX还包括以下功能：
- AndroidX中的所有软件包都以字符串开头，位于一致的命名空间中androidx。
- 与支持库不同，AndroidX软件包是单独维护和更新的。
- 所有新的支持库开发都将在AndroidX库中进行。这包括维护原始支持库工件和引入新的Jetpack组件。
###### 如何使用AndroidX
1. 对于已有工程来说，Android提供了一个转换的工具
![image](https://img-blog.csdnimg.cn/2018121810025799.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzE3NzY2MTk5,size_16,color_FFFFFF,t_70)
2. 创建新工程的时候有一个选项 Use AndroidX artifacts  这样就会默认创建一个AndroidX的工程

如果你是一个新项目，如果使用AndroidX相关依赖，默认会在在gradle.properties文件里生成此配置

```
android.useAndroidX=true
android.enableJetifier=true
```
如果你想使用AndroidX，但是之前的不迁移，可以这样配置：
```
android.useAndroidX=true
android.enableJetifier=false
```

##### 构建原则
- 关注点分离
- 模型驱动U
 

-推荐的应用架构

![image](https://developer.android.google.cn/topic/libraries/architecture/images/final-architecture.png)

请注意，每个组件仅取决于其下一级的组件。例如，活动和片段仅依赖于视图模型。存储库是唯一依赖于多个其他类的类; 在此示例中，存储库依赖于持久数据模型和远程后端数据源。

这种设计创造了一致和愉快的用户体验。无论用户在上次关闭应用程序几分钟后还是几天后再回到应用程序，他们都会立即看到应用程序在本地持续存在的用户信息。如果此数据过时，应用程序的存储库模块将开始在后台更新数据。

##### 总结：可以说Jetpack是为了规范Android开发生态的而提出来的，它把一些规范融入了官方提供的组件中，通过这些组件来规范Android开发。
##### Jetpack 是 Android 组件的集合，使您可以更轻松地开发出色的 Android 应用。这些组件可帮助您遵循最佳实践、减少编写样板代码的工作并简化复杂任务，以便您将精力集中放在开发您应用的核心逻辑。









