# <your project name>

## 開発資料

## 初期設定

```shell
git clone <your project url>
cd <your project name>
```

## 使用技術

- Jetpack Compose
- Dagger Hilt
- Room
- Retrofit + okhttp, OpenAPI
- Ktlint
- Detekt
- Roborazzi
- Github Actions
- composite build

## アプリ全体のアーキテクチャ

- MVVM、クリーンアーキテクチャベースの3層アーキテクチャ。
- アプリ全体を Data, Domain, UI の3つの層 に分割する。
- それぞれの層の外にライブラリ等の依存を露出しないように依存関係を管理する。
- 全モジュールで使用できるユーティリティは common モジュールに配置する。

### Data 層

- データの永続化ロジックを配置する層。主に **Repository の実装クラス**、**ライブラリ関連のコード**
  を配置する。
- :data モジュール配下にサブモジュール、ソースコードを配置する。
- ライブラリの依存関係は data 層内に閉じていれば良いため、 例えば :data:api モジュールで
  `api(API 通信用のライブラリ)` は許容範囲内である。

### Domain 層

- アプリケーション固有のロジックを配置する層。主に **ドメインモデル** と **UseCase の interface**, *
  *UseCase の実装クラス**, **Repository の interface** を配置する。
- :domain モジュール配下にソースコードを配置する。

### UI 層

- 画面表示において複数画面内で共通して使用するユーティリティなどを配置する。
- :ui モジュール配下にサブモジュール、ソースコードを配置する。
- 各画面の UI 実装は :ui:feature モジュール配下に配置する。

### 各機能

- 各機能の UI は :ui:feature 以下にモジュールを切って配置する。
- 各 feature モジュールは `libs.plugins.buildLogicModuleFeature` Gradle Plugin を適用する。
- モジュールは機能グループごとにきり、モジュール内にパッケージを切って各画面を配置する。
- 機能グループモジュールごとに Navigation.kt を配置し、グループ内での画面遷移のロジックを配置する。
    - 実行時例外をあらかじめ防ぐため、極力 Navigation Compose のライブラリの typesafe DSL
      は直接使用せず :ui:navigation で定義された DSL を使用する。
- 各画面を表すパッケージ内には以下の規則でファイル等を配置する。

```
(各画面を表すパッケージ)
+---  <ScreenName>Screen
+---  <ScreenName>ViewModel
+---  <ScreenName>UiState
+---  <ScreenName>UiAction
+---  component
      +--- (Screen内で使用するUIコンポーネント)
```

- Screen には Compose で記述する 画面の UI を配置する。
    - なるべく各 UiState に応じた Preview を用意する。
    - 発生する Ui のイベントのラムダは handleUiEvent で囲うことで UI
      で発生したイベントについてもエラーハンドリングが可能になるため極力こちらを使用する。
    - LaunchedEffect 等の副作用 Composable は使用しないで、代わりにエラーハンドリング考慮された
      Safe**Effect を使用する。
    - Preview は必ず PreviewRoot で囲う。これにより、Theme の適用・エラーハンドラーの考慮がされる。
    - Preview が配置されると、自動的にテストコードが生成され CI で VRT が行われる。
- ViewModel では UI の状態を管理する。
    - ViewModel からは該当画面の Ui の状態を表す UiState を StateFlow で公開する。
    - UI で発生したイベントは全て dispatch メソッド経由でハンドリングする。これにより、UI
      層でネストが深くなってもイベントが追加しやすくなるメリットがある。([参照](https://engineering.teknasyon.com/stop-passing-event-ui-action-callbacks-in-jetpack-compose-a4143621c365))
    - ViewModel は BaseViewModel を継承することでユーティリティを使用できるようになる。これを使用しない場合
      例外処理などを独自に実装する必要があるため、積極的に使用する。
    - UI 側では consumeViewModel を使用して ViewModel を取得する。
- UiState には単一地点での 画面の状態を表す class を定義する。
    - 単一の data class にするのか、 sealed interface を使った複数クラスを定義するのかは各画面で決めるものとする。
- UiAction には UI 層で起きえるイベントを sealed interface を遣って定義する。

<details>
<summary>各ファイルの記述テンプレート</summary>

<details>
<summary>Screen</summary>

```kt
@Composable
internal fun HogeScreen(
    modifier: Modifier = Modifier,
    viewModel: HogeViewModel = hiltViewModel(),
) {
    val (uiState, dispatch) = consumeViewModel(viewModel)

    HogeScreen(
        uiState = uiState,
        dispatch = dispatch,
        modifier = modifier,
    )
}

@Composable
internal fun HogeScreen(
    uiState: HogeUiState,
    dispatch: Dispatch<HogeUiAction>,
    modifier: Modifier = Modifier,
) {
    TODO()
}

private class HogeUiStatePreviewParameters : ValuesPreviewParameterProvider<HogeUiState>(
    HogeUiState.InitialLoading,
    HogeUiState.Success(...
),
    HogeUiState.Error(...),
)

@Preview
@Composable
private fun HogeUiStatePreview(
    @PreviewParameter(HogeUiStatePreviewParameters::class)
    uiState: HogeUiState,
) = PreviewRoot {
    HogeScreen(
        uiState = uiState,
        dispatch = {},
    )
}
```

</details>
<details>
<summary>ViewModel</summary>

```kt
@HiltViewModel
internal class HogeViewModel @Inject constructor(
    appExceptionStateHolder: ApplicationErrorStateHolder,
) : BaseViewModel<HogeUiState, HogeUiAction>(appExceptionStateHolder) {
    private val _uiState = MutableStateFlow<HogeUiState>(HogeUiState.InitialLoading)
    override val uiState = _uiState.asStateFlow()

    override fun dispatch(action: HogeUiAction) = when (action) {
        is HogeUiAction.OnFuga -> onFuga()
        is HogeUiAction.OnPiyo -> onPiyo()
    }

    fun onFuga() {
        TODO()
    }

    fun onPiyo() {
        TODO()
    }
}
```

</details>
<details>
<summary>UiState</summary>

```kt
internal sealed interface HogeUiState {
    data object InitialLoading : HogeUiState
    data class Error(val data: Int) : HogeUiState
    data class Success(val message: String) : HogeUiState
}
```

</details>
<details>
<summary>UiAction</summary>

```kt
internal sealed interface HogeUiAction {
    data object OnFuga : HogeUiAction
    data class OnPiyo(val arg: String) : HogeUiAction
}
```

</details>

</details>

## ビルド設定

本プロジェクトでは、 composite build を用いてビルド設定の共通化を行っている。
ビルド設定の実装は :build-logic:convention 内に Gradle Plugin として実装されている。

Gradle Plugin は以下の2種類に分かれる。

- module plugin
    - 各モジュールに1つ適用される plugin。
    - feature モジュールには `buildLogic.module.feature` を適用する。
    - feature 以外のライブラリモジュールには `buildLogic.module.android.library` を適用する。
    - アプリケーションモジュールには `buildLogic.module.android.application` を適用する。
- primitive plugin
    - 各ライブラリの設定を行う plugin。
    - 1ライブラリにつき 1つ以上になるように作成する。

## OpenApi によるコードの生成

以下のコマンドを実行することで ./data/api/openapi.yaml に配置した OpenAPI Spec に基づいて
`data/api/src/main/kotlin/your/projectPackage/data/api/generated` ディレクトリ内にコードが自動生成される。

```shell
./gradlew buildApi
```
