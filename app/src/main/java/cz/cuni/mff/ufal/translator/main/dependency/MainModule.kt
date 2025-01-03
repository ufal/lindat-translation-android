package cz.cuni.mff.ufal.translator.main.dependency

import android.app.Application
import android.content.Context
import cz.cuni.mff.ufal.translator.interactors.analytics.Analytics
import cz.cuni.mff.ufal.translator.interactors.analytics.IAnalytics
import cz.cuni.mff.ufal.translator.interactors.api.Api
import cz.cuni.mff.ufal.translator.interactors.api.IApi
import cz.cuni.mff.ufal.translator.interactors.asr.AudioTextRecognizer
import cz.cuni.mff.ufal.translator.interactors.asr.IAudioTextRecognizer
import cz.cuni.mff.ufal.translator.interactors.asr.uk.AudioRecorder
import cz.cuni.mff.ufal.translator.interactors.asr.uk.CharlesAudioTextRecognizer
import cz.cuni.mff.ufal.translator.interactors.asr.uk.IAudioRecorder
import cz.cuni.mff.ufal.translator.interactors.asr.uk.IWebsocketClient
import cz.cuni.mff.ufal.translator.interactors.asr.uk.WebsocketClient
import cz.cuni.mff.ufal.translator.interactors.db.Db
import cz.cuni.mff.ufal.translator.interactors.db.IDb
import cz.cuni.mff.ufal.translator.interactors.languages.ILanguagesManager
import cz.cuni.mff.ufal.translator.interactors.languages.LanguagesManager
import cz.cuni.mff.ufal.translator.interactors.preferences.IUserDataStore
import cz.cuni.mff.ufal.translator.interactors.preferences.UserDataStore
import cz.cuni.mff.ufal.translator.interactors.tts.ITextToSpeechWrapper
import cz.cuni.mff.ufal.translator.interactors.tts.TextToSpeechWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus
import javax.inject.Singleton

/**
 * @author Tomas Krabac
 */
@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideContext(context: Application): Context = context

    @Singleton
    @Provides
    fun provideApi(impl: Api): IApi = impl

    @Singleton
    @Provides
    fun provideDb(context: Application): IDb {
        return Db(context)
    }

    @Singleton
    @Provides
    fun provideUserDataStore(context: Application): IUserDataStore {
        return UserDataStore(context)
    }

    @Provides
    fun provideTextToSpeech(impl: TextToSpeechWrapper): ITextToSpeechWrapper = impl

    @Provides
    fun provideAnalytics(impl: Analytics): IAnalytics = impl

    @Provides
    fun provideAudioTextRecognizer(impl: AudioTextRecognizer): IAudioTextRecognizer = impl

    @Provides
    @Singleton
    @ApplicationCoroutineScope
    fun provideApplicationCoroutineScope(): CoroutineScope = MainScope() + Dispatchers.Main.immediate

    @Singleton
    @Provides
    fun provideLanguagesManager(impl: LanguagesManager): ILanguagesManager = impl

    @Singleton
    @Provides
    fun provideWebsocketClient(impl: WebsocketClient): IWebsocketClient = impl

    @Singleton
    @Provides
    fun provideAudioRecorder(impl: AudioRecorder): IAudioRecorder = impl

    @Singleton
    @Provides
    fun provideScope(): CoroutineScope = MainScope() + Dispatchers.Main.immediate

}