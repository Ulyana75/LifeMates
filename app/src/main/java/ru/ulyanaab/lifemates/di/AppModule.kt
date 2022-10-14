package ru.ulyanaab.lifemates.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.ulyanaab.lifemates.data.api.FeedApi
import ru.ulyanaab.lifemates.data.api.TokenApi
import ru.ulyanaab.lifemates.data.api.UserApi
import ru.ulyanaab.lifemates.data.local.TokensCachedStorage
import ru.ulyanaab.lifemates.data.repositoryimpl.AuthRepositoryImpl
import ru.ulyanaab.lifemates.domain.repository.AuthRepository
import ru.ulyanaab.lifemates.domain.repository.TokensRepository
import java.util.logging.Level
import javax.inject.Named

@Module
interface AppModule {

    @AppScope
    @Binds
    fun bindTokensRepository(impl: TokensCachedStorage): TokensRepository

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {

        @IntoSet
        @Provides
        fun provideAuthInterceptor(tokensRepository: TokensRepository): Interceptor {
            return Interceptor { chain ->
                val accessToken = runBlocking { tokensRepository.get().accessToken }
                val request = chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()

                chain.proceed(request)
            }
        }

        @AppScope
        @Provides
        fun provideOkHttpClient(interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor)
                .apply {
                    interceptors.forEach {
                        addInterceptor(it)
                    }
                }
                .build()
        }

        @AppScope
        @Provides
        fun provideRetrofit(
            client: OkHttpClient,
            @Named(BASE_URL) baseUrl: String
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
        }

        @AppScope
        @Provides
        fun provideFeedApi(retrofit: Retrofit): FeedApi {
            return retrofit.create(FeedApi::class.java)
        }

        @AppScope
        @Provides
        fun provideUserApi(retrofit: Retrofit): UserApi {
            return retrofit.create(UserApi::class.java)
        }

        @AppScope
        @Provides
        fun provideTokenApi(retrofit: Retrofit): TokenApi {
            return retrofit.create(TokenApi::class.java)
        }
    }
}
