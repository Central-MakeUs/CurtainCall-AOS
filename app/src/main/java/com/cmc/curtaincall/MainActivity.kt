package com.cmc.curtaincall

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.cmc.curtaincall.common.designsystem.theme.CurtainCallTheme
import com.cmc.curtaincall.common.designsystem.theme.NoRippleTheme
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        mainViewModel.fetchRemoteConfig()
        setContent {
            CurtainCallApp(chatClient = createChatClient(this@MainActivity))
        }

        if (::firebaseAnalytics.isInitialized) {
            firebaseAnalytics.logEvent(
                FirebaseAnalytics.Event.SCREEN_VIEW,
                bundleOf(
                    FirebaseAnalytics.Param.SCREEN_NAME to MainActivity::class.simpleName,
                    FirebaseAnalytics.Param.SCREEN_CLASS to MainActivity::class.simpleName
                )
            )
        }
    }
}

@Composable
private fun CurtainCallApp(chatClient: ChatClient) {
    CurtainCallTheme {
        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = CurtainCallTheme.colors.background
            ) {
                AppNavHost(chatClient = chatClient)
            }
        }
    }
}

private fun createChatClient(context: Context): ChatClient {
    val pluginFactory = StreamOfflinePluginFactory(
        config = Config(
            backgroundSyncEnabled = true,
            userPresence = true,
            persistenceEnabled = true,
            uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING
        ),
        appContext = context
    )

    return ChatClient.Builder(BuildConfig.GET_STREAM_API_KEY, context)
        .withPlugin(pluginFactory)
        .logLevel(if (BuildConfig.DEBUG) ChatLogLevel.DEBUG else ChatLogLevel.ERROR)
        .build()
}
