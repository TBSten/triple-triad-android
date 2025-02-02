# Android Project Template

last updated: 2025/01/26

- [x] build-logic ベースのビルド設定
- [x] [Guide to app architecture](https://developer.android.com/topic/architecture) ベースの MVVM アーキテクチャ
- [x] サンプルコード

- [x] Jetpack Compose
- [x] Dagger Hilt
- [x] Room
- [x] HTTP 通信
    - [x] Retrofit + okhttp
    - [x] OpenAPI Generator
- [x] デバッグメニュー

- [x] Ktlint
- [x] Detekt
- [x] Roborazzi による VRT
- [x] Github Actions ベースの CI

## TODO

### 1. 基本的なアプリの情報を決める

以下を決めて変更作業を実施する。

- applicationId
    - デフォルトでは `your.projectPackage` になっています。
    - **`changeApplicationId` Gradle タスクを実行** してapplicationIdの定義とパッケージディレクトリ・パッケージ文を置換します。

```shell
./gradlew changeApplicationId -PnewApplicationId=your.new.package.name
```

- compileSdk, targetSdk (任意)
    - デフォルトでは 35 になっています。
    - gradle/libs.versions.toml の `app-compileSdk`, `app-compileSdk` を編集します。
- minSdk (任意)
    - デフォルトでは 28 になっています。
    - gradle/libs.versions.toml の `app-minSdk` を編集します。

### 2. サンプルコードを削除する

**`delete-samples` ブランチをマージ** することで サンプルコードを削除できます。

```shell
git merge delete-samples
```

### 3. README を用意する。

**このファイルを削除して、 `README.template.md` を `README.md` にリネーム** します。

リネーム後 `<` `>` で囲まれた箇所などを適宜修正してください。

### 4. コードオーナー の設定

**`.github/CODEOWNERS` ファイルを追加** し、PR の Approve が必須となる Github ユーザをリストします。

プロジェクト用のチームを作成し、そのチーム名を設定すると管理しやすいのでおすすめです。

参照: https://docs.github.com/ja/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-code-owners
