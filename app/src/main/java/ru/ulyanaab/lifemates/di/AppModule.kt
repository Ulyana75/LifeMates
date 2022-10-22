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
import ru.ulyanaab.lifemates.data.api.UserApi
import ru.ulyanaab.lifemates.data.api.TokenApi
import ru.ulyanaab.lifemates.data.api.AuthApi
import ru.ulyanaab.lifemates.data.api.MeApi
import ru.ulyanaab.lifemates.data.local.TokensCachedStorage
import ru.ulyanaab.lifemates.data.repositoryimpl.AuthRepositoryImpl
import ru.ulyanaab.lifemates.data.repositoryimpl.UserInfoRepositoryImpl
import ru.ulyanaab.lifemates.data.repositoryimpl.UsersRepositoryImpl
import ru.ulyanaab.lifemates.domain.auth.repository.AuthRepository
import ru.ulyanaab.lifemates.domain.common.repository.TokensStorage
import ru.ulyanaab.lifemates.domain.user_info.repository.UserInfoRepository
import ru.ulyanaab.lifemates.domain.users.repository.UsersRepository
import javax.inject.Named

@Module
interface AppModule {

    @AppScope
    @Binds
    fun bindTokensRepository(impl: TokensCachedStorage): TokensStorage

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindUserInfoRepository(impl: UserInfoRepositoryImpl): UserInfoRepository

    @Binds
    fun bindUsersRepository(impl: UsersRepositoryImpl): UsersRepository

    companion object {

        @IntoSet
        @Provides
        fun provideAuthInterceptor(tokensStorage: TokensStorage): Interceptor {
            return Interceptor { chain ->
                val accessToken = runBlocking { tokensStorage.get().accessToken }
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
        fun provideUserApi(retrofit: Retrofit): UserApi {
            return retrofit.create(UserApi::class.java)
        }

        @AppScope
        @Provides
        fun provideAuthApi(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }

        @AppScope
        @Provides
        fun provideTokenApi(retrofit: Retrofit): TokenApi {
            return retrofit.create(TokenApi::class.java)
        }

        @AppScope
        @Provides
        fun provideMeApi(retrofit: Retrofit): MeApi {
            return retrofit.create(MeApi::class.java)
        }
    }
}
