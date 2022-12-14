package ru.ulyanaab.lifemates.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
import ru.ulyanaab.lifemates.data.api.ChatsApi
import ru.ulyanaab.lifemates.data.api.InterestsApi
import ru.ulyanaab.lifemates.data.api.MatchApi
import ru.ulyanaab.lifemates.data.api.MeApi
import ru.ulyanaab.lifemates.data.api.ReportsApi
import ru.ulyanaab.lifemates.data.local.TokensCachedStorage
import ru.ulyanaab.lifemates.data.repositoryimpl.AuthRepositoryImpl
import ru.ulyanaab.lifemates.data.repositoryimpl.ChatsRepositoryImpl
import ru.ulyanaab.lifemates.data.repositoryimpl.InterestsRepositoryImpl
import ru.ulyanaab.lifemates.data.repositoryimpl.MatchRepositoryImpl
import ru.ulyanaab.lifemates.data.repositoryimpl.ReportsRepositoryImpl
import ru.ulyanaab.lifemates.data.repositoryimpl.TokensRepositoryImpl
import ru.ulyanaab.lifemates.data.repositoryimpl.UserInfoRepositoryImpl
import ru.ulyanaab.lifemates.data.repositoryimpl.UsersRepositoryImpl
import ru.ulyanaab.lifemates.domain.auth.repository.AuthRepository
import ru.ulyanaab.lifemates.domain.chats.repository.ChatsRepository
import ru.ulyanaab.lifemates.domain.common.repository.InterestsRepository
import ru.ulyanaab.lifemates.domain.common.repository.TokensRepository
import ru.ulyanaab.lifemates.domain.common.repository.TokensStorage
import ru.ulyanaab.lifemates.domain.match.repository.MatchRepository
import ru.ulyanaab.lifemates.domain.report.repository.ReportsRepository
import ru.ulyanaab.lifemates.domain.user_info.repository.UserInfoRepository
import ru.ulyanaab.lifemates.domain.users.repository.UsersRepository
import javax.inject.Named

@Module
interface AppModule {

    @AppScope
    @Binds
    fun bindTokensStorage(impl: TokensCachedStorage): TokensStorage

    @Binds
    fun bindTokensRepository(impl: TokensRepositoryImpl): TokensRepository

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindUserInfoRepository(impl: UserInfoRepositoryImpl): UserInfoRepository

    @Binds
    fun bindUsersRepository(impl: UsersRepositoryImpl): UsersRepository

    @Binds
    fun bindMatchRepository(impl: MatchRepositoryImpl): MatchRepository

    @Binds
    fun bindInterestsRepository(impl: InterestsRepositoryImpl): InterestsRepository

    @Binds
    fun bindChatsRepository(impl: ChatsRepositoryImpl): ChatsRepository

    @Binds
    fun bindReportsRepository(impl: ReportsRepositoryImpl): ReportsRepository

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
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
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

        @AppScope
        @Provides
        fun provideMatchApi(retrofit: Retrofit): MatchApi {
            return retrofit.create(MatchApi::class.java)
        }

        @AppScope
        @Provides
        fun provideInterestsApi(retrofit: Retrofit): InterestsApi {
            return retrofit.create(InterestsApi::class.java)
        }

        @AppScope
        @Provides
        fun provideChatsApi(retrofit: Retrofit): ChatsApi {
            return retrofit.create(ChatsApi::class.java)
        }

        @AppScope
        @Provides
        fun provideReportsApi(retrofit: Retrofit): ReportsApi {
            return retrofit.create(ReportsApi::class.java)
        }
    }
}
